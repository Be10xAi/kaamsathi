package com.kaamsaathi.config;

import com.kaamsaathi.model.Job;
import com.kaamsaathi.repository.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Inserts the 6 demo jobs from the HTML page on first start (only if the jobs table is empty). */
@Component
public class DataSeeder implements CommandLineRunner {

    private final JobRepository jobs;

    public DataSeeder(JobRepository jobs) { this.jobs = jobs; }

    @Override
    public void run(String... args) {
        if (jobs.count() > 0) return;
        jobs.save(job("Security Guard", "सिक्योरिटी गार्ड", "ABC Facility Services", "नोएडा", "Sector 62",
                "₹15,000 – ₹18,000", "8–12 घंटे", "0–2 वर्ष", "उपलब्ध", "उपलब्ध", "Security Guard",
                "https://images.unsplash.com/photo-1557804506-669a67965ba0?auto=format&fit=crop&w=800&q=80"));
        jobs.save(job("Housekeeping Staff", "हाउसकीपिंग स्टाफ", "Prime Hospitality", "गुरुग्राम", "Sector 44",
                "₹14,000 – ₹17,000", "9 घंटे", "कोई अनुभव नहीं", "उपलब्ध", "नहीं", "Housekeeping",
                "https://images.unsplash.com/photo-1581578731548-c64695cc6952?auto=format&fit=crop&w=800&q=80"));
        jobs.save(job("Warehouse Helper", "वेयरहाउस हेल्पर", "XYZ Logistics", "ग्रेटर नोएडा", "Ecotech 3",
                "₹16,000 – ₹20,000", "9 घंटे", "0–2 वर्ष", "नहीं", "उपलब्ध", "Warehouse",
                "https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80"));
        jobs.save(job("Delivery Boy", "डिलीवरी बॉय", "QuickDrop Services", "दिल्ली", "Dwarka",
                "₹18,000 – ₹25,000", "8–10 घंटे", "कोई अनुभव नहीं", "नहीं", "नहीं", "Delivery",
                "https://images.unsplash.com/photo-1616401784845-180882ba9ba8?auto=format&fit=crop&w=800&q=80"));
        jobs.save(job("Factory Worker", "फैक्ट्री वर्कर", "Industrial Works Pvt Ltd", "फरीदाबाद", "Sector 24",
                "₹15,500 – ₹19,000", "8 घंटे", "1–2 साल", "उपलब्ध", "उपलब्ध", "Factory",
                "https://images.unsplash.com/photo-1581092160562-40aa08e78837?auto=format&fit=crop&w=800&q=80"));
        jobs.save(job("Driver", "ड्राइवर", "City Transport Services", "गाजियाबाद", "Indirapuram",
                "₹17,000 – ₹23,000", "10 घंटे", "2+ वर्ष", "नहीं", "नहीं", "Driver",
                "https://images.unsplash.com/photo-1502877338535-766e1452684a?auto=format&fit=crop&w=800&q=80"));
    }

    private static Job job(String title, String hi, String company, String city, String location, String salary,
                           String hours, String exp, String food, String stay, String category, String img) {
        Job j = new Job();
        j.setTitle(title);
        j.setTitleHi(hi);
        j.setCompany(company);
        j.setCity(city);
        j.setLocation(location);
        j.setSalary(salary);
        j.setHours(hours);
        j.setExperience(exp);
        j.setFood(food);
        j.setStay(stay);
        j.setJobType("Full Time");
        j.setCategory(category);
        j.setImageUrl(img);
        j.setVerified(true);
        return j;
    }
}
