package com.sharetext.controller;

import com.sharetext.dto.AccessRequest;
import com.sharetext.dto.ContentResponse;
import com.sharetext.dto.MetadataResponse;
import com.sharetext.dto.ShareRequest;
import com.sharetext.service.SharedTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/texts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SharedTextController {

    private final SharedTextService service ;

    public SharedTextController(SharedTextService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<?> createSharedText(@RequestBody ShareRequest request) {
        try {
            String id = service.createSharedText(request);
            return ResponseEntity.ok(Map.of("id", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/metadata")
    public ResponseEntity<?> getMetadata(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.getMetadata(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/access")
    public ResponseEntity<?> accessContent(@PathVariable String id, @RequestBody(required = false) AccessRequest request) {
        try {
            return ResponseEntity.ok(service.accessContent(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
