package com.wuubangdev.portfolio.modules.post.application.service;

import com.wuubangdev.portfolio.modules.post.application.dto.CommentRequest;
import com.wuubangdev.portfolio.modules.post.application.dto.CommentResponse;
import com.wuubangdev.portfolio.modules.post.domain.model.Comment;
import com.wuubangdev.portfolio.modules.post.domain.port.CommentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepositoryPort commentRepositoryPort;

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepositoryPort.findByPostId(postId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponse addComment(CommentRequest request) {
        Comment comment = Comment.builder()
                .postId(request.postId())
                .author(request.author())
                .content(request.content())
                .parentId(request.parentId())
                .build();
        return toResponse(commentRepositoryPort.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentRepositoryPort.deleteById(id);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthor(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getParentId()
        );
    }
}
