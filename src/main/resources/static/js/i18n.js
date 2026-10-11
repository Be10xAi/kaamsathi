/* KaamSaathi - Hindi / English switcher.
   Static page text is swapped by looking up the original Hindi text in this dictionary.
   Dynamic parts (jobs, categories, messages) are written by api.js through KS.t().
   Load this file BEFORE api.js. */
(function () {
    'use strict';
    const EN = {
        "कामसाथी — आसान नौकरी, बेहतर भविष्य": "KaamSaathi — Easy Jobs, Better Future",
        "🇮🇳 भारत का आसान नौकरी प्लेटफॉर्म": "🇮🇳 India's easy job platform",
        "नौकरी के लिए किसी को पैसे न दें • OTP साझा न करें": "Never pay anyone for a job • Do not share your OTP",
        "कामसाथी": "KaamSaathi",
        "नौकरी खोजें": "Find Jobs",
        "काम की श्रेणियां": "Job Categories",
        "कंपनियों के लिए": "For Companies",
        "सहायता": "Help",
        "लॉगिन": "Login",
        "✓ आसान आवेदन • ✓ मोबाइल से • ✓ हिंदी में": "✓ Easy apply • ✓ From mobile • ✓ In Hindi & English",
        "अपने शहर में पाएं": "Find the job you like",
        "अपनी पसंद की नौकरी": "in your own city",
        "हजारों नौकरियां • आसान आवेदन • कोई जटिल प्रक्रिया नहीं": "Thousands of jobs • Easy apply • No complicated process",
        "किस शहर में नौकरी चाहिए?": "Which city do you want a job in?",
        "दिल्ली": "Delhi",
        "नोएडा": "Noida",
        "ग्रेटर नोएडा": "Greater Noida",
        "गाजियाबाद": "Ghaziabad",
        "गुरुग्राम": "Gurugram",
        "फरीदाबाद": "Faridabad",
        "मुंबई": "Mumbai",
        "पुणे": "Pune",
        "बेंगलुरु": "Bengaluru",
        "हैदराबाद": "Hyderabad",
        "चेन्नई": "Chennai",
        "अहमदाबाद": "Ahmedabad",
        "जयपुर": "Jaipur",
        "लखनऊ": "Lucknow",
        "चंडीगढ़": "Chandigarh",
        "कोलकाता": "Kolkata",
        "इंदौर": "Indore",
        "🔍 नौकरी खोजें": "🔍 Find Jobs",
        "🎤 बोलकर नौकरी खोजें": "🎤 Search jobs by voice",
        "आज": "Today",
        "नौकरियां उपलब्ध": "jobs available",
        "नौकरी ढूंढना अब आसान है": "Finding a job is now easy",
        "सिर्फ तीन आसान कदम": "Just three easy steps",
        "काम चुनें": "Choose work",
        "Security Guard, Driver, Helper और अपनी पसंद का काम चुनें।": "Choose Security Guard, Driver, Helper or any work you like.",
        "शहर चुनें": "Choose city",
        "जिस शहर में काम करना है, वह शहर चुनें।": "Choose the city where you want to work.",
        "आवेदन करें": "Apply",
        "अपनी जानकारी भरें और नौकरी के लिए आवेदन करें।": "Fill in your details and apply for the job.",
        "काम की श्रेणी चुनें": "Choose a job category",
        "सभी नौकरियां →": "All jobs →",
        "आपके लिए उपलब्ध नौकरियां": "Jobs available for you",
        "6 नौकरियां मिलीं": "6 jobs found",
        "सभी देखें ↻": "View all ↻",
        "😕 इस खोज से कोई नौकरी नहीं मिली।": "😕 No jobs found for this search.",
        "दूसरा काम या शहर चुनकर देखें।": "Try a different job or city.",
        "लोकप्रिय शहर": "Popular cities",
        "बोलकर नौकरी खोजें": "Search jobs by voice",
        "टाइप करने में परेशानी हो तो बोलकर कहें — “मुझे नोएडा में सिक्योरिटी गार्ड की नौकरी चाहिए।”": "If typing is difficult, just say it — “I need a security guard job in Noida.”",
        "आसान आवेदन": "Easy application",
        "अपना ईमेल और पासवर्ड से आसानी से प्रोफाइल बनाएं।": "Create your profile easily with your email and password.",
        "नई नौकरी का अलर्ट": "New job alerts",
        "अपना शहर, काम और सैलरी चुनें और नई नौकरी की जानकारी पाएं।": "Choose your city, work and salary and get new job updates.",
        "जहां उपलब्ध हो, नौकरी पर कंपनी की verification status साफ दिखाई जाएगी।": "Where available, the company’s verification status will be clearly shown on the job.",
        "नौकरी के नाम पर धोखाधड़ी से बचें": "Beware of job fraud",
        "नौकरी की शिकायत करें": "Report a job",
        "नौकरी पाने के लिए किसी को पैसे न दें।": "Do not pay anyone to get a job.",
        "अपना OTP किसी के साथ साझा न करें।": "Do not share your OTP with anyone.",
        "बैंक PIN या Password कभी न बताएं।": "Never reveal your bank PIN or password.",
        "संदिग्ध नौकरी की जानकारी हमें रिपोर्ट करें।": "Report suspicious jobs to us.",
        "अपने काम के लिए सही कर्मचारी खोजें": "Find the right employees for your work",
        "कंपनी रजिस्टर करें, नौकरी पोस्ट करें, उम्मीदवारों की जानकारी देखें और सही व्यक्ति को shortlist करें।": "Register your company, post jobs, view candidate details and shortlist the right person.",
        "नौकरी पोस्ट करें": "Post a Job",
        "आज ही नौकरी खोजें": "Find a job today",
        "आपका अगला काम सिर्फ कुछ क्लिक दूर है।": "Your next job is just a few clicks away.",
        "👤 अपना प्रोफाइल बनाएं": "👤 Create your profile",
        "नौकरी": "Jobs",
        "मेरे आवेदन": "My applications",
        "कंपनियां": "Companies",
        "सुरक्षा": "Safety",
        "शिकायत करें": "Report a problem",
        "📞 कॉल करें": "📞 Call us",
        "© 2026 कामसाथी • गोपनीयता नीति • Terms & Conditions • Fraud Awareness": "© 2026 KaamSaathi • Privacy Policy • Terms & Conditions • Fraud Awareness",
        "नौकरी के लिए आवेदन करें": "Apply for a job",
        "आपका नाम *": "Your name *",
        "मोबाइल नंबर *": "Mobile number *",
        "उम्र": "Age",
        "लिंग": "Gender",
        "चुनें": "Select",
        "पुरुष": "Male",
        "महिला": "Female",
        "अन्य": "Other",
        "आप किस शहर में रहते हैं?": "Which city do you live in?",
        "किस शहर में काम चाहिए?": "Which city do you want to work in?",
        "काम का अनुभव": "Work experience",
        "कोई अनुभव नहीं": "No experience",
        "1–2 साल": "1–2 years",
        "3–5 साल": "3–5 years",
        "5+ साल": "5+ years",
        "उम्मीद की सैलरी": "Expected salary",
        "काम का अनुभव / अतिरिक्त जानकारी": "Work experience / additional information",
        "फोटो (वैकल्पिक)": "Photo (optional)",
        "ID / Resume (वैकल्पिक)": "ID / Resume (optional)",
        "आपका आवेदन सफलतापूर्वक भेज दिया गया है।": "Your application has been submitted successfully.",
        "कंपनी आपके मोबाइल नंबर पर आपसे संपर्क कर सकती है।": "The company may contact you on your mobile number.",
        "ठीक है": "OK",
        "लॉगिन करें": "Log in",
        "अपना ईमेल और पासवर्ड डालें।": "Enter your email and password.",
        "मोबाइल नंबर (वैकल्पिक)": "Mobile number (optional)",
        "ईमेल *": "Email *",
        "पासवर्ड *": "Password *",
        "नया खाता बनाएं": "Create a new account",
        "मेरा प्रोफाइल": "My profile",
        "प्रोफाइल पूरा करें": "Complete your profile",
        "आपका प्रोफाइल 80% पूरा है": "Your profile is 80% complete",
        "🟢 आपकी प्रोफाइल नौकरी के लिए तैयार है": "🟢 Your profile is ready for jobs",
        "काम और शहर की जानकारी भरने से बेहतर नौकरी मिल सकती है।": "Adding your work and city details can help you get a better job.",
        "आवेदन भेजा गया": "Application sent",
        "कंपनी ने देखा": "Viewed by company",
        "कंपनी का नाम *": "Company name *",
        "नौकरी का नाम *": "Job title *",
        "शहर *": "City *",
        "सैलरी": "Salary",
        "आवश्यक अनुभव": "Required experience",
        "0–2 वर्ष": "0–2 years",
        "3–5 वर्ष": "3–5 years",
        "5+ वर्ष": "5+ years",
        "काम की जानकारी": "Job details",
        "आप कौन सा काम चाहते हैं?": "What work do you want?",
        "पूरा नाम": "Full name",
        "10 अंकों का मोबाइल नंबर": "10-digit mobile number",
        "जैसे गाजियाबाद": "e.g. Ghaziabad",
        "जैसे ₹18,000": "e.g. ₹18,000",
        "आपने पहले कहां काम किया है?": "Where have you worked before?",
        "कम से कम 8 अक्षर (अक्षर + अंक)": "At least 8 characters (letters + digits)",
        "10 अंकों का नंबर": "10-digit number",
        "जैसे Security Guard": "e.g. Security Guard",
        "काम क्या करना होगा?": "What will the work be?",
        "हिंदी / English": "English",
        "उपलब्ध": "Available",
        "नहीं": "No",
        "सुरक्षा गार्ड": "Security Guard",
        "हाउसकीपिंग": "Housekeeping",
        "ड्राइवर": "Driver",
        "वेयरहाउस हेल्पर": "Warehouse Helper",
        "निर्माण मजदूर": "Construction Worker",
        "फैक्ट्री वर्कर": "Factory Worker",
        "होटल/रेस्टोरेंट": "Hotel / Restaurant",
        "ऑफिस बॉय": "Office Boy",
        "डिलीवरी बॉय": "Delivery Boy",
        "माली": "Gardener",
        "इलेक्ट्रिशियन हेल्पर": "Electrician Helper",
        "प्लंबर हेल्पर": "Plumber Helper",
        "सर्वर से कनेक्ट नहीं हो पा रहा है। कृपया इंटरनेट जांचें।": "Cannot connect to the server. Please check your internet.",
        "कुछ गलत हो गया। कृपया दोबारा कोशिश करें।": "Something went wrong. Please try again.",
        "नौकरियां मिलीं": "jobs found",
        "खाना:": "Food:",
        "रहना:": "Stay:",
        "विवरण": "Details",
        "अभी आवेदन करें": "Apply now",
        "महीना": "month",
        "नौकरी विवरण": "Job details",
        "कृपया सही 10 अंकों का मोबाइल नंबर डालें।": "Please enter a valid 10-digit mobile number.",
        "पासवर्ड कम से कम 8 अक्षर का हो, जिसमें अक्षर और अंक दोनों हों।": "Password must be at least 8 characters with both letters and digits.",
        "स्वागत है,": "Welcome,",
        "👤 प्रोफाइल": "👤 Profile",
        "लॉगआउट": "Logout",
        "आप लॉगआउट हो गए।": "You have been logged out.",
        "अभी तक कोई आवेदन नहीं। लॉगिन रहते हुए आवेदन करेंगे तो यहां दिखेगा।": "No applications yet. Applications you submit while logged in will appear here.",
        "चयनित (Shortlisted)": "Shortlisted",
        "इस बार चयन नहीं हुआ": "Not selected this time",
        "प्रोफाइल देखने के लिए पहले लॉगिन करें।": "Please log in first to view your profile.",
        "पहले से खाता है? लॉगिन करें": "Already have an account? Log in",
        "नाम, ईमेल और पासवर्ड से खाता बनाएं।": "Create an account with your name, email and password.",
        "खाता बनाएं": "Create account",
        "कंपनी का नाम और नौकरी का नाम जरूरी है।": "Company name and job title are required.",
        "संदिग्ध नौकरी / कंपनी की जानकारी लिखें:": "Describe the suspicious job / company:",
        "आपके browser में voice search उपलब्ध नहीं है।": "Voice search is not available in your browser."
    };

    const STORE = 'ks_lang';
    let lang = 'hi';
    try { const s = localStorage.getItem(STORE); if (s === 'en' || s === 'hi') lang = s; } catch (e) { /* storage blocked */ }

    const DEV = /[\u0900-\u097F]/;
    const hooks = [];
    const has = s => Object.prototype.hasOwnProperty.call(EN, s);
    const t = s => (lang === 'en' && has(s)) ? EN[s] : s;
    const en = s => has(s) ? EN[s] : s;                       // English text regardless of current language

    /** Values stored in the database (hours, experience, food, stay ...). */
    function val(s) {
        if (s == null) return '';
        s = String(s);
        if (lang !== 'en') return s;
        if (has(s)) return EN[s];
        return s.replace(/घंटे/g, 'hrs').replace(/वर्ष/g, 'yrs').replace(/साल/g, 'years');
    }

    // Elements that are written by api.js (they are re-rendered on language change) are skipped here.
    const SKIP = 'script,style,#langBtn,#jobGrid,#categoryGrid,#cityGrid,#jobCount,#applyTitle,#applyCompany,#ref,#toast,'
        + '#profileModal .applications,#profileModal .profile-box > div:first-child p,'
        + '#authEyebrow,#authTitle,#authSub,#authSubmit,#authSwitch';
    const ORIG_TITLE = document.title;

    function fixOptions() {   // <option> without value: keep the Hindi text as the value that is sent to the server
        document.querySelectorAll('option:not([value])').forEach(o => { o.value = o.textContent.trim(); });
    }

    function translateStatic() {
        const w = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
        const nodes = [];
        let n;
        while ((n = w.nextNode())) nodes.push(n);
        nodes.forEach(node => {
            const p = node.parentElement;
            if (!p || p.closest(SKIP)) return;
            if (node.__o === undefined) node.__o = DEV.test(node.nodeValue) ? node.nodeValue : null;
            if (node.__o === null) return;
            const m = node.__o.match(/^(\s*)([\s\S]*?)(\s*)$/);
            node.nodeValue = (lang === 'en' && has(m[2])) ? m[1] + EN[m[2]] + m[3] : node.__o;
        });
        document.querySelectorAll('[placeholder],[alt],[title]').forEach(el => {
            el.__a = el.__a || {};
            ['placeholder', 'alt', 'title'].forEach(a => {
                if (!el.hasAttribute(a)) return;
                if (!(a in el.__a)) el.__a[a] = el.getAttribute(a);
                const o = el.__a[a];
                el.setAttribute(a, (lang === 'en' && has(o)) ? EN[o] : o);
            });
        });
    }

    function apply() {
        document.documentElement.lang = lang;
        document.title = (lang === 'en' && has(ORIG_TITLE)) ? EN[ORIG_TITLE] : ORIG_TITLE;
        fixOptions();
        translateStatic();
    }

    function setLang(l) {
        lang = (l === 'en') ? 'en' : 'hi';
        try { localStorage.setItem(STORE, lang); } catch (e) { /* ignore */ }
        apply();
        hooks.forEach(f => f());
    }

    window.KS = { t, en, val, EN, lang: () => lang, setLang, onChange: f => hooks.push(f) };
    apply();
})();