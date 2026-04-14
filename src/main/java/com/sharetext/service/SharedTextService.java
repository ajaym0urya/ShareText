package com.sharetext.service;

import com.sharetext.dto.AccessRequest;
import com.sharetext.dto.ContentResponse;
import com.sharetext.dto.MetadataResponse;
import com.sharetext.dto.ShareRequest;
import com.sharetext.model.SharedText;
import com.sharetext.repository.SharedTextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SharedTextService {

    private final SharedTextRepository repository;
    private final PasswordEncoder passwordEncoder;



	public String createSharedText(ShareRequest request) {
        SharedText text = new SharedText();
        
        String id;
        if (request.getCustomAlias() != null && !request.getCustomAlias().trim().isEmpty()) {
            id = request.getCustomAlias().trim();
            // Optional: validate ID format here (e.g., alphanumeric and dashes)
            if (repository.existsById(id)) {
                throw new RuntimeException("Custom link alias is already taken");
            }
        } else {
            id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            while(repository.existsById(id)) {
                id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
            }
        }
        text.setId(id);
        text.setContent(request.getContent());
        text.setExpirationDate(request.getExpirationDate());
        text.setCreatedAt(Instant.now());

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            text.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        repository.save(text);
        return id;
    }

    public MetadataResponse getMetadata(String id) {
        Optional<SharedText> optionalText = repository.findById(id);
        if (optionalText.isEmpty()) {
            throw new RuntimeException("Text not found");
        }

        SharedText text = optionalText.get();
        MetadataResponse response = new MetadataResponse();
        
        boolean isExpired = text.getExpirationDate() != null && text.getExpirationDate().isBefore(Instant.now());
        response.setExpired(isExpired);
        response.setRequiresPassword(text.getPasswordHash() != null && !text.getPasswordHash().isEmpty());
        
        return response;
    }

    public ContentResponse accessContent(String id, AccessRequest request) {
        Optional<SharedText> optionalText = repository.findById(id);
        if (optionalText.isEmpty()) {
            throw new RuntimeException("Text not found");
        }
        
        SharedText text = optionalText.get();
        
        if (text.getExpirationDate() != null && text.getExpirationDate().isBefore(Instant.now())) {
            throw new RuntimeException("Link has expired");
        }

        if (text.getPasswordHash() != null && !text.getPasswordHash().isEmpty()) {
            if (request == null || request.getPassword() == null || !passwordEncoder.matches(request.getPassword(), text.getPasswordHash())) {
                throw new RuntimeException("Invalid password");
            }
        }
        
        ContentResponse response = new ContentResponse();
        response.setContent(text.getContent());
        return response;
    }
}
