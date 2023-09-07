package com.jegan.blogapplication.service;

import com.jegan.blogapplication.dao.PostRepository;
import com.jegan.blogapplication.entity.Post;
import com.jegan.blogapplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public Post findById(int id) {
        Optional<Post> result=postRepository.findById(id);
        Post post=null;
        if (result.isPresent()) {
            post=result.get();
        }
        return post;
    }

    @Override
    public void deleteById(int deleteId) {
        postRepository.deleteById(deleteId);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> searchPosts(String searchQuery,Pageable pageable) {
        return postRepository.searchPosts(searchQuery,pageable);
    }

    @Override
    public Page<Post> findPostsByAuthors(List<String> filterByAuthors, Pageable pageable) {
        return postRepository.findPostsByAuthors(filterByAuthors,pageable);
    }

    @Override
    public Page<Post> findPostsByPublishedDate(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return postRepository.findPostsByPublishedDate(from,to,pageable);
    }

}
