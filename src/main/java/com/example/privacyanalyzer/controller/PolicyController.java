package com.example.privacyanalyzer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.privacyanalyzer.dto.AnalysisResponse;
import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.entity.User;
import com.example.privacyanalyzer.repository.PrivacyPolicyRepository;
import com.example.privacyanalyzer.security.UserPrincipal;
import com.example.privacyanalyzer.service.PolicyService;
import com.example.privacyanalyzer.service.PrivacyAnalyzerService;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class PolicyController {

    @Autowired
    private PrivacyPolicyRepository repository;
    
    @Autowired
    private PolicyService service;
    
    @Autowired
    private PrivacyAnalyzerService analyzerService;

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyzePolicy(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        String appName = request.get("appName");
        String policyText = request.get("policyText");

        if (appName == null || appName.trim().isEmpty()) {
            AnalysisResponse errorResponse = new AnalysisResponse();
            errorResponse.setStatus("error");
            errorResponse.setMessage("App name is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (policyText == null || policyText.trim().isEmpty()) {
            AnalysisResponse errorResponse = new AnalysisResponse();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Policy text is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        PrivacyPolicy policy = analyzerService.analyzePolicy(appName, policyText, userPrincipal.getUser());
        return ResponseEntity.ok(new AnalysisResponse(policy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivacyPolicy> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-app")
    public ResponseEntity<PrivacyPolicy> getByAppName(@RequestParam String appName) {
        return service.findByAppName(appName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history")
    public ResponseEntity<List<PrivacyPolicy>> getUserHistory(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            if (userPrincipal == null) {
                System.out.println("UserPrincipal is null");
                return ResponseEntity.status(401).build();
            }
            
            User user = userPrincipal.getUser();
            if (user == null) {
                System.out.println("User is null");
                return ResponseEntity.status(401).build();
            }
            
            System.out.println("Getting history for user: " + user.getUsername());
            List<PrivacyPolicy> policies = repository.findAllByUser(user);
            System.out.println("Found " + policies.size() + " policies");
            return ResponseEntity.ok(policies);
        } catch (Exception e) {
            System.out.println("Error in getUserHistory: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PrivacyPolicy>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }
}