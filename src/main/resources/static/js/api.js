/* KaamSaathi - connects index.html to the Spring Boot backend (with Hindi / English support).
   Scripts at the end of index.html, in this order:
       <script src="/js/i18n.js"></script>
       <script src="/js/api.js"></script>                                                   */
(function () {
    'use strict';

    const KS = window.KS;
    const t = KS.t;
    const isEn = () => KS.lang() === 'en';

    // Same origin when index.html is served by Spring Boot. If you open the HTML from another
    // origin (e.g. Live Server on :5500) change to 'http://localhost:8080/api' (CORS is already allowed).
    const API = '/api';
    const TOKEN_KEY = 'ks_token';
    const getToken = () => sessionStorage.getItem(TOKEN_KEY);
    const $ = id => document.getElementById(id);
    let currentJobId = null;
    let lastList = null;
    let lastProfile = null;

    const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
    const safeImg = u => /^https:\/\//i.test(u || '') ? u : '';
    const jobTitle = j => isEn() ? j.title : j.hi;

    async function api(path, opts = {}) {
        const headers = Object.assign({}, opts.headers || {});
        headers['Accept-Language'] = KS.lang();                 // backend answers in the same language
        const token = getToken();
        if (token) headers['Authorization'] = 'Bearer ' + token;
        const init = Object.assign({}, opts, { headers });
        if (opts.json !== undefined) {
            headers['Content-Type'] = 'application/json';
            init.body = JSON.stringify(opts.json);
            delete init.json;
        }
        let res;
        try { res = await fetch(API + path, init); }
        catch (e) { throw new Error(t('सर्वर से कनेक्ट नहीं हो पा रहा है। कृपया इंटरनेट जांचें।')); }
        let data = null;
        try { data = await res.json(); } catch (e) { /* empty body */ }
        if (!res.ok) {
            if (res.status === 401 && token) { sessionStorage.removeItem(TOKEN_KEY); updateLoginButton(); }
            const err = new Error((data && data.message) || t('कुछ गलत हो गया। कृपया दोबारा कोशिश करें।'));
            err.status = res.status;
            throw err;
        }
        return data;
    }

    /* ---------- categories & cities ---------- */
    window.renderCategories = function () {
        $('categoryGrid').innerHTML = categories.map(c =>
            `<button class="category" onclick="categorySearch('${esc(c[2])}')"><div class="cat-icon">${c[0]}</div><b>${esc(t(c[1]))}</b></button>`).join('');
    };
    window.renderCities = function () {
        $('cityGrid').innerHTML = cities.map(c =>
            `<button class="city" onclick="citySearch('${esc(c)}')">📍 ${esc(t(c))}</button>`).join('');
    };

    /* ---------- jobs ---------- */
    async function loadJobs(params) {
        const q = new URLSearchParams();
        if (params && params.keyword) q.set('keyword', params.keyword);
        if (params && params.city) q.set('city', params.city);
        const list = await api('/jobs?' + q.toString());
        list.forEach(j => { if (!jobs.some(x => x.id === j.id)) jobs.push(j); });   // lookup array for viewJob/openApply
        return list;
    }

    window.renderJobs = function (list = jobs) {
        lastList = list;
        const n = list.length;
        $('jobCount').textContent = isEn() ? `${n} ${n === 1 ? 'job' : 'jobs'} found` : `${n} ${t('नौकरियां मिलीं')}`;
        $('empty').style.display = n ? 'none' : 'block';
        $('jobGrid').innerHTML = list.map(j => `<article class="job">
      <img src="${esc(safeImg(j.img))}" alt="${esc(jobTitle(j))}" loading="lazy"><div class="job-body"><h3>${esc(jobTitle(j))}</h3>
      <div class="company">${esc(j.company)} • 🟢 Verified Employer</div>
      <div class="salary">${esc(j.salary)} / ${esc(t('महीना'))}</div>
      <div class="tags"><span class="tag">📍 ${esc(t(j.city))}</span><span class="tag">${esc(j.type)}</span></div>
      <div class="job-meta"><span>🕐 ${esc(KS.val(j.hours))}</span><span>⭐ ${esc(KS.val(j.exp))}</span><span>🍛 ${esc(t('खाना:'))} ${esc(KS.val(j.food))}</span><span>🛏️ ${esc(t('रहना:'))} ${esc(KS.val(j.stay))}</span></div>
      <div class="job-actions"><button class="outline" onclick="viewJob(${Number(j.id)})">${esc(t('विवरण'))}</button><button class="primary" onclick="openApply(${Number(j.id)})">${esc(t('अभी आवेदन करें'))}</button></div>
      </div></article>`).join('');
    };

    window.searchJobs = async function () {
        const keyword = $('keyword').value.trim();
        const city = $('city').value;                       // city option values are always the Hindi names stored in the DB
        try { renderJobs(await loadJobs({ keyword, city })); } catch (e) { showToast(e.message); }
        $('jobs').scrollIntoView({ behavior: 'smooth' });
    };

    window.resetJobs = async function () {
        $('keyword').value = '';
        $('city').value = '';
        try { renderJobs(await loadJobs({})); } catch (e) { showToast(e.message); }
        $('jobs').scrollIntoView({ behavior: 'smooth' });
    };

    function showApplyModal(j) {
        currentJobId = j.id;
        $('applyFormWrap').style.display = 'block';
        $('success').classList.remove('show');
        openModal('applyModal');
    }

    window.viewJob = function (id) {
        const j = jobs.find(x => x.id === id); if (!j) return;
        $('applyTitle').textContent = jobTitle(j) + ' — ' + t('नौकरी विवरण');
        $('applyCompany').innerHTML =
            `<b>${esc(j.company)}</b> • ${esc(t(j.city))}, ${esc(j.location)}<br>💰 ${esc(j.salary)} • 🕐 ${esc(KS.val(j.hours))} • ⭐ ${esc(KS.val(j.exp))}<br>🍛 ${esc(t('खाना:'))} ${esc(KS.val(j.food))} • 🛏️ ${esc(t('रहना:'))} ${esc(KS.val(j.stay))}`;
        showApplyModal(j);
    };

    window.openApply = function (id) {
        const j = jobs.find(x => x.id === id); if (!j) return;
        $('applyTitle').textContent = isEn() ? 'Apply for ' + j.title : j.hi + ' के लिए आवेदन';
        $('applyCompany').textContent = `${j.company} • ${t(j.city)} • ${j.salary}/${t('महीना')}`;
        showApplyModal(j);
    };

    /* ---------- application ---------- */
    window.submitApplication = async function (e) {
        e.preventDefault();
        const v = id => $(id).value;
        const pick = id => { const x = v(id); return x === 'चुनें' ? null : x; };   // option values stay Hindi in both languages
        const ageRaw = v('age');
        const data = {
            jobId: currentJobId,
            name: v('name').trim(),
            phone: v('phone').trim(),
            age: ageRaw ? parseInt(ageRaw, 10) : null,
            gender: pick('gender'),
            homeCity: v('homeCity').trim() || null,
            workCity: pick('workCity'),
            experience: v('experience'),
            salary: v('salary').trim() || null,
            note: v('note').trim() || null
        };
        const fd = new FormData();
        fd.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
        const files = document.querySelectorAll('#applyForm input[type=file]');
        if (files[0] && files[0].files[0]) fd.append('photo', files[0].files[0]);
        if (files[1] && files[1].files[0]) fd.append('resume', files[1].files[0]);

        const form = e.target, btn = form.querySelector('button.primary');
        btn.disabled = true;
        try {
            const r = await api('/applications', { method: 'POST', body: fd });
            $('ref').textContent = r.applicationId;
            $('applyFormWrap').style.display = 'none';
            $('success').classList.add('show');
            form.reset();
        } catch (err) { showToast(err.message); }
        finally { btn.disabled = false; }
    };

    /* ---------- email + password login / register ---------- */
    let authMode = 'login';

    function applyAuthMode() {
        const reg = authMode === 'register';
        $('registerFields').style.display = reg ? 'block' : 'none';
        $('authEyebrow').textContent = reg ? 'Register' : 'Login';
        $('authTitle').textContent = t(reg ? 'नया खाता बनाएं' : 'लॉगिन करें');
        $('authSub').textContent = t(reg ? 'नाम, ईमेल और पासवर्ड से खाता बनाएं।' : 'अपना ईमेल और पासवर्ड डालें।');
        $('authSubmit').textContent = t(reg ? 'खाता बनाएं' : 'लॉगिन');
        $('authSwitch').textContent = t(reg ? 'पहले से खाता है? लॉगिन करें' : 'नया खाता बनाएं');
        $('authPassword').autocomplete = reg ? 'new-password' : 'current-password';
        $('authName').required = reg;
    }

    window.switchAuth = function () { authMode = authMode === 'login' ? 'register' : 'login'; applyAuthMode(); };

    window.submitAuth = async function (e) {
        e.preventDefault();
        const email = $('authEmail').value.trim();
        const password = $('authPassword').value;
        const btn = $('authSubmit');
        btn.disabled = true;
        try {
            let r;
            if (authMode === 'register') {
                const phone = $('authPhone').value.trim();
                if (phone && !/^[6-9]\d{9}$/.test(phone)) { showToast(t('कृपया सही 10 अंकों का मोबाइल नंबर डालें।')); return; }
                if (password.length < 8 || !/[A-Za-z]/.test(password) || !/\d/.test(password)) {
                    showToast(t('पासवर्ड कम से कम 8 अक्षर का हो, जिसमें अक्षर और अंक दोनों हों।')); return;
                }
                r = await api('/auth/register', { method: 'POST', json: { name: $('authName').value.trim(), email, phone: phone || null, password } });
            } else {
                r = await api('/auth/login', { method: 'POST', json: { email, password } });
            }
            sessionStorage.setItem(TOKEN_KEY, r.token);
            $('authPassword').value = '';
            closeModal('loginModal');
            updateLoginButton();
            showToast(t('स्वागत है,') + ' ' + (r.name || '') + '!');
        } catch (err) { showToast(err.message); }
        finally { btn.disabled = false; }
    };

    function updateLoginButton() {
        const b = document.querySelector('nav .login');
        if (b) b.textContent = getToken() ? t('👤 प्रोफाइल') : t('लॉगिन');
    }

    const origOpenLogin = window.openLogin;
    window.openLogin = function () {
        if (getToken()) { window.openProfile(); }
        else { authMode = 'login'; applyAuthMode(); origOpenLogin(); }
    };

    /* ---------- profile ---------- */
    const STATUS = {
        SUBMITTED: ['आवेदन भेजा गया', 'yellow'], VIEWED: ['कंपनी ने देखा', 'blue'],
        SHORTLISTED: ['चयनित (Shortlisted)', 'green'], REJECTED: ['इस बार चयन नहीं हुआ', 'yellow']
    };

    function logout() {
        sessionStorage.removeItem(TOKEN_KEY);
        lastProfile = null;
        updateLoginButton();
        closeModal('profileModal');
        showToast(t('आप लॉगआउट हो गए।'));
    }

    function renderProfile(p) {
        lastProfile = p;
        const box = document.querySelector('#profileModal .profile-box > div:first-child p');
        if (box) box.textContent = isEn() ? `Your profile is ${p.completion}% complete` : `आपका प्रोफाइल ${p.completion}% पूरा है`;
        const bar = document.querySelector('#profileModal .progress span');
        if (bar) bar.style.width = p.completion + '%';

        const wrap = document.querySelector('#profileModal .applications');
        wrap.textContent = '';
        if (!p.applications.length) {
            const d = document.createElement('div'); d.className = 'muted';
            d.textContent = t('अभी तक कोई आवेदन नहीं। लॉगिन रहते हुए आवेदन करेंगे तो यहां दिखेगा।');
            wrap.appendChild(d);
        }
        p.applications.forEach(a => {
            const row = document.createElement('div'); row.className = 'app-row';
            const left = document.createElement('div');
            const b = document.createElement('b'); b.textContent = isEn() ? a.jobTitleEn : a.jobTitle;
            const sp = document.createElement('span'); sp.className = 'muted';
            sp.textContent = `${t(a.city)} • ${a.company} • ID: ${a.applicationId}`;
            left.append(b, document.createElement('br'), sp);
            const st = STATUS[a.status] || STATUS.SUBMITTED;
            const badge = document.createElement('span'); badge.className = 'status ' + st[1]; badge.textContent = t(st[0]);
            row.append(left, badge); wrap.appendChild(row);
        });

        let lb = $('logoutBtn');
        if (!lb) {
            lb = document.createElement('button');
            lb.id = 'logoutBtn'; lb.className = 'outline'; lb.style.marginTop = '16px';
            lb.onclick = logout;
            wrap.after(lb);
        }
        lb.textContent = t('लॉगआउट');
    }

    window.openProfile = async function () {
        if (!getToken()) { origOpenLogin(); showToast(t('प्रोफाइल देखने के लिए पहले लॉगिन करें।')); return; }
        try { renderProfile(await api('/profile/me')); openModal('profileModal'); }
        catch (e) { showToast(e.message); if (e.status === 401) origOpenLogin(); }
    };

    /* ---------- employer + report ---------- */
    const empBtn = document.querySelector('#employerModal .primary');
    if (empBtn) empBtn.onclick = async function () {
        const f = [...document.querySelectorAll('#employerModal input, #employerModal select, #employerModal textarea')];
        const [company, phone, title, city, salary, exp, desc] = f.map(x => x.value.trim());
        if (!company || !title) { showToast(t('कंपनी का नाम और नौकरी का नाम जरूरी है।')); return; }
        if (!/^[6-9]\d{9}$/.test(phone)) { showToast(t('कृपया सही 10 अंकों का मोबाइल नंबर डालें।')); return; }
        try {
            const r = await api('/employer/job-requests', { method: 'POST',
                json: { companyName: company, phone, jobTitle: title, city, salary: salary || null, experience: exp || null, description: desc || null } });
            f.forEach(x => { if (x.tagName !== 'SELECT') x.value = ''; });
            closeModal('employerModal'); showToast(r.message);
        } catch (e) { showToast(e.message); }
    };

    window.reportIssue = async function () {
        const message = window.prompt(t('संदिग्ध नौकरी / कंपनी की जानकारी लिखें:'));
        if (!message || !message.trim()) return;
        try { const r = await api('/reports', { method: 'POST', json: { message: message.trim() } }); showToast(r.message); }
        catch (e) { showToast(e.message); }
    };

    /* ---------- voice search (Hindi or English) ---------- */
    const ALIASES = [
        ['Security Guard', ['सिक्योरिटी', 'सुरक्षा', 'security', 'guard']], ['Driver', ['ड्राइवर', 'driver']],
        ['Delivery', ['डिलीवरी', 'delivery']], ['Housekeeping', ['हाउसकीपिंग', 'housekeeping']],
        ['Warehouse', ['वेयरहाउस', 'warehouse']], ['Factory', ['फैक्ट्री', 'factory']], ['Hotel', ['होटल', 'hotel']],
        ['Office Boy', ['ऑफिस', 'office']], ['Gardener', ['माली', 'gardener']],
        ['Electrician', ['इलेक्ट्रिशियन', 'electrician']], ['Plumber', ['प्लंबर', 'plumber']],
        ['Construction', ['निर्माण', 'मजदूर', 'construction']]
    ];

    window.startVoice = function () {
        const SR = window.SpeechRecognition || window.webkitSpeechRecognition;
        if (!SR) { showToast(t('आपके browser में voice search उपलब्ध नहीं है।')); return; }
        const r = new SR();
        r.lang = isEn() ? 'en-IN' : 'hi-IN';
        r.interimResults = false;
        showToast(isEn() ? '🎤 Speak: “I need a security guard job in Noida”' : '🎤 बोलिए: “मुझे नोएडा में सिक्योरिटी गार्ड की नौकरी चाहिए”');
        r.onresult = e => {
            const text = e.results[0][0].transcript.toLowerCase();
            let bestCity = '';
            for (const c of cities) {                              // longest match wins (Greater Noida vs Noida)
                const names = [c.toLowerCase(), KS.en(c).toLowerCase()];
                if (names.some(n => text.includes(n)) && c.length > bestCity.length) bestCity = c;
            }
            const hit = ALIASES.find(a => a[1].some(w => text.includes(w)));
            $('keyword').value = hit ? hit[0] : '';
            $('city').value = bestCity;
            searchJobs();
        };
        try { r.start(); } catch (err) { /* already started */ }
    };

    /* ---------- language button ---------- */
    function refreshDynamic() {
        renderCategories();
        renderCities();
        renderJobs(lastList || jobs);
        applyAuthMode();
        updateLoginButton();
        $('langBtn').textContent = isEn() ? 'हिंदी' : 'English';       // shows the language you can switch TO
        if ($('profileModal').classList.contains('open') && lastProfile) renderProfile(lastProfile);
    }
    $('langBtn').onclick = () => KS.setLang(isEn() ? 'hi' : 'en');
    KS.onChange(refreshDynamic);

    /* ---------- start ---------- */
    refreshDynamic();
    loadJobs({}).then(list => { jobs.splice(0, jobs.length, ...list); renderJobs(jobs); })
        .catch(e => showToast(e.message));
})();