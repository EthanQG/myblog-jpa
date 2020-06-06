package com.wqg.blog.dao;

import com.wqg.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

     List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

}
