package com.example.privacyanalyzer.controller;

import com.example.privacyanalyzer.dto.AnalysisResponse;
import com.example.privacyanalyzer.entity.PrivacyPolicy;
import com.example.privacyanalyzer.security.UserPrincipal;
import com.example.privacyanalyzer.service.PrivacyAnalyzerService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class FileUploadController {

    @Autowired
    private PrivacyAnalyzerService analyzerService;

    @PostMapping("/upload")
    public ResponseEntity<AnalysisResponse> uploadPolicyFile(
            @RequestParam("appName") String appName,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (appName == null || appName.trim().isEmpty()) {
            AnalysisResponse errorResponse = new AnalysisResponse();
            errorResponse.setStatus("error");
            errorResponse.setMessage("App name is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        if (file == null || file.isEmpty()) {
            AnalysisResponse errorResponse = new AnalysisResponse();
            errorResponse.setStatus("error");
            errorResponse.setMessage("File is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            String text = extractText(file);
            PrivacyPolicy policy = analyzerService.analyzePolicy(appName, text, userPrincipal.getUser());
            return ResponseEntity.ok(new AnalysisResponse(policy));
        } catch (Exception e) {
            AnalysisResponse errorResponse = new AnalysisResponse();
            errorResponse.setStatus("error");
            errorResponse.setMessage("Failed to process file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    private String extractText(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        } else if (fileName != null && fileName.endsWith(".txt")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString();
        } else {
            throw new IllegalArgumentException("Unsupported file type. Use .txt or .pdf");
        }
    }
}