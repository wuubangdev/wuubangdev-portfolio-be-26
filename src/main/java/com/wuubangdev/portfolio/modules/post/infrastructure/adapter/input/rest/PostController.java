package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.infrastructure.global.api.PageResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostEngagementResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.PostResponse;
import com.wuubangdev.portfolio.modules.post.application.dto.PostTranslationRequest;
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

    @GetMapping("/api/v1/posts/paged")
    public ResponseEntity<PageResponse<PostResponse>> getPublishedPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getPublishedPostsPaged(page, size));
    }

    @GetMapping("/api/v1/posts/{slug}")
    public ResponseEntity<PostResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getBySlug(slug));
    }

    @GetMapping("/api/v1/posts/recent")
    public ResponseEntity<List<PostResponse>> getRecentPosts(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(postService.getRecentPosts(limit));
    }

    @GetMapping("/api/v1/posts/{id}/related")
    public ResponseEntity<List<PostResponse>> getRelatedPosts(@PathVariable Long id, @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(postService.getRelatedPosts(id, limit));
    }

    @PostMapping("/api/v1/posts/{slug}/like")
    public ResponseEntity<PostEngagementResponse> likePost(@PathVariable String slug) {
        return ResponseEntity.ok(postService.likePost(slug));
    }

    @DeleteMapping("/api/v1/posts/{slug}/like")
    public ResponseEntity<PostEngagementResponse> unlikePost(@PathVariable String slug) {
        return ResponseEntity.ok(postService.unlikePost(slug));
    }

    @PostMapping("/api/v1/posts/{slug}/share")
    public ResponseEntity<PostEngagementResponse> sharePost(@PathVariable String slug) {
        return ResponseEntity.ok(postService.sharePost(slug));
    }

    // --- ADMIN ---
    @GetMapping("/api/v1/admin/posts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostResponse>> getAllAdmin() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/api/v1/admin/posts/paged")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<PostResponse>> getAllAdminPaged(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPostsPaged(page, size));
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

    @PutMapping("/api/v1/admin/posts/{id}/translations/{locale}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> upsertTranslation(@PathVariable Long id, @PathVariable String locale, @RequestBody PostTranslationRequest request) {
        return ResponseEntity.ok(postService.upsertTranslation(id, locale, request));
    }

    @DeleteMapping("/api/v1/admin/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/v1/admin/posts/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> changeStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(postService.changeStatus(id, status));
    }
}
