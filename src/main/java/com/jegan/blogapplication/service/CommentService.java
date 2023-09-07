package com.jegan.blogapplication.service;

import com.jegan.blogapplication.dto.CommentRequestDTO;
import com.jegan.blogapplication.entity.Comment;

import java.util.List;

public interface CommentService {

    void save(Comment comment);

    List<Comment> findAllByPostId(int postId);

    Comment findById(int commentId);

    void deleteById(int deleteId);

    void updateComment(Comment originalComment);
}
