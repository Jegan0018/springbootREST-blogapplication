package com.jegan.blogapplication.service;

import com.jegan.blogapplication.entity.Post;
import com.jegan.blogapplication.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TagService {

    Set<Tag> findOrCreateTag(Set<Tag> tagName);

    Page<Post> findPostsByTags(List<String> filterByTags, Pageable pageable);
}
