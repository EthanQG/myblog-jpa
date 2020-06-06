package com.wqg.blog.service;

import com.wqg.blog.po.Comment;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface CommentService {

    List<Comment> listCommentByBlogId(Long id);

    Comment saveComment(Comment comment);
}
