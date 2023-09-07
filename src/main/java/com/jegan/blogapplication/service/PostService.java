package com.jegan.blogapplication.service;

import com.jegan.blogapplication.entity.Post;
import com.jegan.blogapplication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    void save(Post post);

    Post findById(int id);

    void deleteById(int deleteId);

    Page<Post> findAll(Pageable pageable);

    Page<Post> searchPosts(String searchQuery,Pageable pageable);

    Page<Post> findPostsByAuthors(List<String> filterByAuthors, Pageable pageable);

    Page<Post> findPostsByPublishedDate(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
