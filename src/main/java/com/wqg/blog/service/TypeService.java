package com.wqg.blog.service;

import com.wqg.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
public interface TypeService {
    //新增接口
    Type saveType(Type type);
    //查询接口
    Type getType(Long id);
    //分页查询
    Page<Type> listType(Pageable pageable);
    //修改接口
    Type updateType(Long id,Type type);
    //删除删除
    void deleteType(Long id);
    //通过名称查询分类名
    Type getTypeByName(String name);
    //获取所有分类
    List<Type> listType();
    //获取size条type
    List<Type> listTypeTop(Integer size);
}
