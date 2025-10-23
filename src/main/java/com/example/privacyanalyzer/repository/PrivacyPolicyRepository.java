package com.example.privacyanalyzer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.entity.User;

@Repository
public interface PrivacyPolicyRepository extends JpaRepository<PrivacyPolicy, Long> {
    Optional<PrivacyPolicy> findByAppNameIgnoreCase(String appName);
    List<PrivacyPolicy> findAllByUser(User user);
}