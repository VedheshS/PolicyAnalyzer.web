package com.example.privacyanalyzer.dto;

import java.time.LocalDateTime;

import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.entity.RiskLevel;

public class AnalysisResponse {
    private Long id;
    private String appName;
    private String policyText;
    private RiskLevel riskLevel;
    private Double riskScore;
    private LocalDateTime createdAt;
    private String status;
    private String message;

    public AnalysisResponse() {}

    public AnalysisResponse(PrivacyPolicy policy) {
        this.id = policy.getId();
        this.appName = policy.getAppName();
        this.policyText = policy.getPolicyText();
        this.riskLevel = policy.getRiskLevel();
        this.riskScore = policy.getRiskScore();
        this.createdAt = policy.getCreatedAt();
        this.status = "success";
        this.message = "Policy analyzed successfully";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public String getPolicyText() { return policyText; }
    public void setPolicyText(String policyText) { this.policyText = policyText; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}