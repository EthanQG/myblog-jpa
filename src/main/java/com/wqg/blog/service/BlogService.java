package com.wqg.blog.service;

import com.wqg.blog.po.Blog;
import com.wqg.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wqg
 * @Description:
 */
public interface BlogService {
    Blog getBlog(Long id);

    //获取并转换文章内容为html格式
    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Pageable pageable,String query);

    Page<Blog> listBlog(Pageable pageable,Long tagid);

    Blog savaBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    List<Blog> listRecommendBlogTop(Integer size);

    void deleteBlog(Long id);

    //对所有blog归档
    Map<String,List<Blog>> archiveBlog();

    Long countBlog();


}
