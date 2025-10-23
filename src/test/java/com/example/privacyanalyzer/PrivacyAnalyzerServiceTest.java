package com.example.privacyanalyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.entity.RiskLevel;
import com.example.privacyanalyzer.entity.User;
import com.example.privacyanalyzer.repository.PrivacyPolicyRepository;
import com.example.privacyanalyzer.service.PrivacyAnalyzerService;

class PrivacyAnalyzerServiceTest {

    @Mock
    private PrivacyPolicyRepository repository;

    @InjectMocks
    private PrivacyAnalyzerService analyzerService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testuser", "password");
        testUser.setId(1L);
    }

    @Test
    void testAnalyzePolicyHighRisk() {
        // Given
        String highRiskText = "We sell your personal data to third parties for advertising purposes and track your location.";
        PrivacyPolicy mockPolicy = new PrivacyPolicy();
        mockPolicy.setId(1L);
        mockPolicy.setAppName("TestApp");
        mockPolicy.setPolicyText(highRiskText);
        mockPolicy.setRiskLevel(RiskLevel.HIGH);
        mockPolicy.setRiskScore(0.75);
        mockPolicy.setUser(testUser);

        when(repository.save(any(PrivacyPolicy.class))).thenReturn(mockPolicy);

        // When
        PrivacyPolicy result = analyzerService.analyzePolicy("TestApp", highRiskText, testUser);

        // Then
        assertNotNull(result);
        assertEquals("TestApp", result.getAppName());
        assertEquals(RiskLevel.HIGH, result.getRiskLevel());
        assertTrue(result.getRiskScore() >= 0.4);
        assertEquals(testUser, result.getUser());
    }

    @Test
    void testAnalyzePolicyMediumRisk() {
        // Given
        String mediumRiskText = "We collect your email and location data to improve our service.";
        PrivacyPolicy mockPolicy = new PrivacyPolicy();
        mockPolicy.setId(2L);
        mockPolicy.setAppName("TestApp2");
        mockPolicy.setPolicyText(mediumRiskText);
        mockPolicy.setRiskLevel(RiskLevel.MEDIUM);
        mockPolicy.setRiskScore(0.3);
        mockPolicy.setUser(testUser);

        when(repository.save(any(PrivacyPolicy.class))).thenReturn(mockPolicy);

        // When
        PrivacyPolicy result = analyzerService.analyzePolicy("TestApp2", mediumRiskText, testUser);

        // Then
        assertNotNull(result);
        assertEquals("TestApp2", result.getAppName());
        assertEquals(RiskLevel.MEDIUM, result.getRiskLevel());
        assertTrue(result.getRiskScore() >= 0.4);
    }

    @Test
    void testAnalyzePolicyLowRisk() {
        // Given
        String lowRiskText = "We use anonymized data for security purposes to improve our service.";
        PrivacyPolicy mockPolicy = new PrivacyPolicy();
        mockPolicy.setId(3L);
        mockPolicy.setAppName("TestApp3");
        mockPolicy.setPolicyText(lowRiskText);
        mockPolicy.setRiskLevel(RiskLevel.LOW);
        mockPolicy.setRiskScore(0.06);
        mockPolicy.setUser(testUser);

        when(repository.save(any(PrivacyPolicy.class))).thenReturn(mockPolicy);

        // When
        PrivacyPolicy result = analyzerService.analyzePolicy("TestApp3", lowRiskText, testUser);

        // Then
        assertNotNull(result);
        assertEquals("TestApp3", result.getAppName());
        assertEquals(RiskLevel.LOW, result.getRiskLevel());
        assertTrue(result.getRiskScore() < 0.4);
    }

    @Test
    void testAnalyzePolicyEmptyText() {
        // Given
        String emptyText = "";
        PrivacyPolicy mockPolicy = new PrivacyPolicy();
        mockPolicy.setId(4L);
        mockPolicy.setAppName("EmptyApp");
        mockPolicy.setPolicyText(emptyText);
        mockPolicy.setRiskLevel(RiskLevel.LOW);
        mockPolicy.setRiskScore(0.0);
        mockPolicy.setUser(testUser);

        when(repository.save(any(PrivacyPolicy.class))).thenReturn(mockPolicy);

        // When
        PrivacyPolicy result = analyzerService.analyzePolicy("EmptyApp", emptyText, testUser);

        // Then
        assertNotNull(result);
        assertEquals("EmptyApp", result.getAppName());
        assertEquals(RiskLevel.LOW, result.getRiskLevel());
        assertEquals(0.0, result.getRiskScore());
    }
}