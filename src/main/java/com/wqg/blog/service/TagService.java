package com.wqg.blog.service;

import com.wqg.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface TagService {
    //新增标签
    Tag saveTag(Tag tag);
    //查询标签
    Tag getTag(Long id);
    //分页查询
    Page<Tag> listTag(Pageable pageable);
    //修改标签
    Tag updateTag(Long id, Tag tag);
    //删除标签
    void deleteTag(Long id);
    //通过名称查询标签名
    Tag getTagByName(String name);
    //获取所有tags
    List<Tag> listTag();
    //通过多个id查询tag
    List<Tag> listTag(String ids);
    //查询size个tag
    List<Tag> listTagTop(Integer size);
}
