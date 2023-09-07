package com.jegan.blogapplication.restcontroller;

import com.jegan.blogapplication.dto.PostRequestDTO;
import com.jegan.blogapplication.entity.Post;
import com.jegan.blogapplication.entity.Tag;
import com.jegan.blogapplication.entity.User;
import com.jegan.blogapplication.service.PostService;
import com.jegan.blogapplication.service.TagService;
import com.jegan.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PostRestController {

    private PostService postService;

    private TagService tagService;

    private UserService userService;

    @Autowired
    public PostRestController(PostService postService,  TagService tagService, UserService userService) {
        this.postService = postService;
        this.tagService = tagService;
        this.userService = userService;
    }
    @GetMapping("/posts")
    public ResponseEntity<Page<Post>> findAll(Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int limit,
                              @RequestParam(required = false, value = "search") String searchQuery,
                              @RequestParam(defaultValue = "desc") String sort,
                              @RequestParam(value = "selectedAuthors", required = false) List<String> filterByAuthors,
                              @RequestParam(value = "selectedTags", required = false) List<String> filterByTags,
                              @RequestParam(value = "publishedAtFrom", required = false) LocalDateTime filterByPublishedAtFrom,
                              @RequestParam(value = "publishedAtTo", required = false) LocalDateTime filterByPublishedAtTo) {
        Sort sorting = Sort.by(Sort.Order.desc("publishedAt"));
        if (sort.equals("asc")) {
            sorting = Sort.by(Sort.Order.asc("publishedAt"));
        }
        Page<Post> postsPage;
        Pageable pageable = PageRequest.of(page-1 , limit, sorting);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            postsPage = postService.searchPosts(searchQuery.toLowerCase(), pageable);
        } else {
            postsPage = postService.findAll(pageable);
        }

        if (filterByAuthors != null && !filterByAuthors.isEmpty()) {
            postsPage = postService.findPostsByAuthors(filterByAuthors, pageable);
        }

        if (filterByTags != null && !filterByTags.isEmpty()) {
            postsPage = tagService.findPostsByTags(filterByTags, pageable);
        }

        if (filterByPublishedAtFrom != null && filterByPublishedAtTo != null) {
            postsPage = postService.findPostsByPublishedDate(
                    filterByPublishedAtFrom,
                    filterByPublishedAtTo,
                    pageable
            );
        }
        return ResponseEntity.ok(postsPage);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable("postId") int postId) {
        Post post=postService.findById(postId);
        if(post!=null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/posts")
    public ResponseEntity<String> savePost(@RequestBody PostRequestDTO postRequest) {
        Post post = new Post();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
            post.setTitle(postRequest.getTitle());
            post.setExcerpt(postRequest.getExcerpt());
            post.setContent(postRequest.getContent());
            User user = userService.findUserByName(name);
            post.setAuthor(user.getName());
            post.setUser(user);
            if (postRequest.getIsPublished()) {
                post.setIsPublished(true);
                post.setPublishedAt(Timestamp.valueOf(LocalDateTime.now()));
            } else {
                post.setIsPublished(false);
                post.setPublishedAt(Timestamp.valueOf(LocalDateTime.now()));
            }
            post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            post.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            Set<Tag> tags = tagService.findOrCreateTag(postRequest.getTags());
            post.setTags(tags);
            postService.save(post);
            if(post.getIsPublished()) {
                return ResponseEntity.ok("Post saved successfully");
            } else {
                return ResponseEntity.ok("Saved To Drafts successfully");
            }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name="id") int id) {
        Post post=postService.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if(post==null) {
            return new ResponseEntity<>("Given Id Not Found", HttpStatus.NOT_FOUND);
        } else {
            if(name.equals(post.getAuthor())) {
                postService.deleteById(id);
                return new ResponseEntity<>("Post Deleted Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Please Authenticate", HttpStatus.NOT_FOUND);
            }
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") int id, @RequestBody PostRequestDTO postRequest) {
        Post existingPost = postService.findById(id);
        if (existingPost == null) {
            return new ResponseEntity<>("Post Not Found", HttpStatus.NOT_FOUND);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        if(name.equals(existingPost.getAuthor())) {
            existingPost.setTitle(postRequest.getTitle());
            existingPost.setExcerpt(postRequest.getExcerpt());
            existingPost.setContent(postRequest.getContent());
            User user = userService.findUserByName(name);
            existingPost.setAuthor(user.getName());
            existingPost.setUser(user);
            if (postRequest.getIsPublished()) {
                existingPost.setIsPublished(true);
                existingPost.setPublishedAt(Timestamp.valueOf(LocalDateTime.now()));
            } else {
                existingPost.setIsPublished(false);
            }
            existingPost.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            existingPost.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            Set<Tag> tagNames = postRequest.getTags();
            Set<Tag> tags = tagService.findOrCreateTag(tagNames);
            existingPost.setTags(tags);
            postService.save(existingPost);
            if (existingPost.getIsPublished()) {
                return ResponseEntity.ok("Post updated and published successfully");
            } else {
                return ResponseEntity.ok("Post updated and saved to Drafts successfully");
            }
        } else {
            return new ResponseEntity<>("You don't have Authorization to Update", HttpStatus.NOT_FOUND);
        }
    }
}
