package com.wuubangdev.portfolio.modules.post.infrastructure.adapter.input.rest;

import com.wuubangdev.portfolio.modules.post.application.dto.CommentRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.CommentResponse;
import com.wuubangdev.portfolio.modules.post.application.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
