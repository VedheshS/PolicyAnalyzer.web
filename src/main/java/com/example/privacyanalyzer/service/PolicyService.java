package com.example.privacyanalyzer.service;

import java.util.List;
import java.util.Optional;

import com.example.privacyanalyzer.entity.PrivacyPolicy;

public interface PolicyService {
    PrivacyPolicy savePolicy(PrivacyPolicy policy);
    Optional<PrivacyPolicy> findById(Long id);
    Optional<PrivacyPolicy> findByAppName(String appName);
    List<PrivacyPolicy> listAll();
    PrivacyPolicy analyzeAndSave(PrivacyPolicy policy);
}