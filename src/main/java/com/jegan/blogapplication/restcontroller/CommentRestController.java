package com.jegan.blogapplication.restcontroller;

import com.jegan.blogapplication.dto.CommentRequestDTO;
import com.jegan.blogapplication.entity.Comment;
import com.jegan.blogapplication.entity.Post;
import com.jegan.blogapplication.service.CommentService;
import com.jegan.blogapplication.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {
    private CommentService commentService;
    private PostService postService;

    @Autowired
    public CommentRestController(CommentService commentService , PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> savePost(@RequestBody CommentRequestDTO commentRequestDTO,@PathVariable("postId") int postId) {
        Post post=postService.findById(postId);
        if(post!=null) {
            commentRequestDTO.setPost(post);
            Comment comment=new Comment();
            comment.setName(commentRequestDTO.getName());
            comment.setEmail(commentRequestDTO.getEmail());
            comment.setTheComment(commentRequestDTO.getTheComment());
            comment.setPost(commentRequestDTO.getPost());
            commentService.save(comment);
            return ResponseEntity.ok(" Comment Added successfully to the Given Post");
        } else {
            return new ResponseEntity<>("Given Id For Post is Not Found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> findAll(@PathVariable("postId") int postId) {
        List<Comment> comments=commentService.findAllByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> updateComments(@PathVariable("postId") int postId,@PathVariable("id") int id,@RequestBody CommentRequestDTO commentRequestDTO) {
        Post post=postService.findById(postId);
        Comment comment=commentService.findById(id);
        comment.setName(commentRequestDTO.getName());
        comment.setEmail(commentRequestDTO.getEmail());
        comment.setTheComment(commentRequestDTO.getTheComment());
        commentService.updateComment(comment);
        return ResponseEntity.ok(" Comment Updated successfully to the Given Post");
    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") int postId,@PathVariable("id") int id) {
        Post post=postService.findById(postId);
        Comment comment=commentService.findById(id);
        if(comment==null) {
            return new ResponseEntity<>("Given Id For Comment is Not Found", HttpStatus.NOT_FOUND);
        } else {
            commentService.deleteById(id);
            return new ResponseEntity<>("Comment Deleted Successfully", HttpStatus.OK);
        }
    }
}
