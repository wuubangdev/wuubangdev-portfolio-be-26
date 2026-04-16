package com.wuubangdev.portfolio.modules.post.domain.port;

import com.wuubangdev.portfolio.modules.post.domain.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryPort {
    Post save(Post post);
    List<Post> findAllPublished();
    List<Post> findAll();
    Optional<Post> findById(Long id);
    Optional<Post> findBySlug(String slug);
    void deleteById(Long id);
    boolean existsBySlug(String slug);
    List<Post> findRecentPosts(int limit);
    List<Post> findRelatedPosts(String category, Long excludeId, int limit);
    // Pagination
    List<Post> findAllPublishedPaged(int page, int size);
    long countAllPublished();
    List<Post> findAllPaged(int page, int size);
    long countAll();
}
