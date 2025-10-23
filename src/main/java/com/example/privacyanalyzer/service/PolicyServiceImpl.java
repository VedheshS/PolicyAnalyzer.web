package com.example.privacyanalyzer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.entity.RiskLevel;
import com.example.privacyanalyzer.repository.PrivacyPolicyRepository;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

    private final PrivacyPolicyRepository repo;

    public PolicyServiceImpl(PrivacyPolicyRepository repo) {
        this.repo = repo;
    }

    @Override
    public PrivacyPolicy savePolicy(PrivacyPolicy policy) {
        return repo.save(policy);
    }

    @Override
    public Optional<PrivacyPolicy> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<PrivacyPolicy> findByAppName(String appName) {
        return repo.findByAppNameIgnoreCase(appName);
    }

    @Override
    public List<PrivacyPolicy> listAll() {
        return repo.findAll();
    }

    @Override
    public PrivacyPolicy analyzeAndSave(PrivacyPolicy policy) {
        AnalysisResult result = analyze(policy.getPolicyText());
        policy.setRiskLevel(result.level);
        policy.setRiskScore(result.score);
        return repo.save(policy);
    }

    // Simple analyzer - keyword counting and heuristics
    private AnalysisResult analyze(String text) {
        if (text == null) text = "";

        String lower = text.toLowerCase();

        // sensitive keywords that increase risk
        String[] highRiskKeywords = {"sell", "third party", "share with", "advertising id", "track", "track user", "tracking", "advertiser", "ads", "data broker", "permission to access contacts", "transfer", "transfer to", "analytics"};
        String[] mediumRiskKeywords = {"collect", "store", "retain", "location", "gps", "device id", "imei", "personal information", "email", "phone number"};
        String[] lowRiskKeywords = {"aggregate", "anonymize", "use for improvement", "improve service", "security", "retain for troubleshooting"};

        double score = 0.0;

        for (String k : highRiskKeywords) {
            if (lower.contains(k)) score += 0.25;
        }
        for (String k : mediumRiskKeywords) {
            if (lower.contains(k)) score += 0.10;
        }
        for (String k : lowRiskKeywords) {
            if (lower.contains(k)) score += 0.02;
        }

        // clamp
        if (score > 1.0) score = 1.0;

        RiskLevel level;
        if (score >= 0.5) level = RiskLevel.HIGH;
        else if (score >= 0.15) level = RiskLevel.MEDIUM;
        else level = RiskLevel.LOW;

        return new AnalysisResult(level, score);
    }

    private static class AnalysisResult {
        RiskLevel level;
        Double score;
        AnalysisResult(RiskLevel level, Double score) {
            this.level = level;
            this.score = score;
        }
    }
}