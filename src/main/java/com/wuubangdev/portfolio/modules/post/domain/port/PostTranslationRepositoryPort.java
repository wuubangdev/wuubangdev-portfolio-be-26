package com.wuubangdev.portfolio.modules.post.domain.port;

import com.wuubangdev.portfolio.modules.post.domain.model.PostTranslation;

import java.util.List;
import java.util.Optional;

public interface PostTranslationRepositoryPort {
    PostTranslation save(PostTranslation translation);
    List<PostTranslation> saveAll(List<PostTranslation> translations);
    Optional<PostTranslation> findByPostIdAndLocale(Long postId, String locale);
    List<PostTranslation> findByPostIdInAndLocale(List<Long> postIds, String locale);
    void deleteByPostId(Long postId);
}
