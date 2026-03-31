package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.application.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // --- PUBLIC ---
    @GetMapping("/api/v1/posts")
    public ResponseEntity<List<PostResponse>> getPublished() {
        return ResponseEntity.ok(postService.getPublishedPosts());
    }

    @GetMapping("/api/v1/posts/{slug}")
    public ResponseEntity<PostResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getBySlug(slug));
    }

    // --- ADMIN ---
    @GetMapping("/api/v1/admin/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostResponse>> getAllAdmin() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/api/v1/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> getByIdAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping("/api/v1/admin/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(request));
    }

    @PutMapping("/api/v1/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @Valid @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.update(id, request));
    }

    @DeleteMapping("/api/v1/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
