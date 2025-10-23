package com.example.privacyanalyzer.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.entity.RiskLevel;
import com.example.privacyanalyzer.entity.User;
import com.example.privacyanalyzer.repository.PrivacyPolicyRepository;

@Service
public class PrivacyAnalyzerService {

    @Autowired
    private PrivacyPolicyRepository repository;

    public PrivacyPolicy analyzePolicy(String appName, String policyText, User user) {
        double score = calculateRiskScore(policyText);
        String riskLevel = getRiskLevel(score);

        PrivacyPolicy policy = new PrivacyPolicy();
        policy.setAppName(appName);
        policy.setPolicyText(policyText);
        policy.setRiskScore(score);
        policy.setRiskLevel(RiskLevel.valueOf(riskLevel));
        policy.setCreatedAt(LocalDateTime.now());
        policy.setUser(user);

        return repository.save(policy);
    }

    public PrivacyPolicy analyzePolicy(String appName, String policyText) {
        double score = calculateRiskScore(policyText);
        String riskLevel = getRiskLevel(score);

        PrivacyPolicy policy = new PrivacyPolicy();
        policy.setAppName(appName);
        policy.setPolicyText(policyText);
        policy.setRiskScore(score);
        policy.setRiskLevel(RiskLevel.valueOf(riskLevel));
        policy.setCreatedAt(LocalDateTime.now());

        return repository.save(policy);
    }

    public List<PrivacyPolicy> getAllPolicies() {
        return repository.findAll();
    }

    private static double calculateRiskScore(String text) {
        String lower = text.toLowerCase();
        String[] riskyWords = {
                "collect", "share", "third party", "advertising", "location",
                "device id", "personal data", "sell", "analytics", "tracking"
        };

        int hits = 0;
        for (String word : riskyWords) {
            if (lower.contains(word)) hits++;
        }
        return Math.min(1.0, hits / 10.0); // normalized score
    }

    private static String getRiskLevel(double score) {
        if (score >= 0.7) return "HIGH";
        else if (score >= 0.4) return "MEDIUM";
        else return "LOW";
    }
}