package com.wqg.blog.service;

import com.wqg.blog.NotFondException;
import com.wqg.blog.Util.MarkdownUtils;
import com.wqg.blog.Util.MyBeanUtils;
import com.wqg.blog.dao.BlogRepository;
import com.wqg.blog.po.Blog;
import com.wqg.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Auther: wqg
 * @Description:
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.getOne(id);
        if (blog==null)
            throw new NotFondException("博客不存在");
        //创建一个副本进行内容转换，保证数据库中原来的文本数据不改变以便于修改文章
        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content=blog.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blog.setContent(content);
        //更新views
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            //root获取表的字段 cq查询条件容器 cb设置查询表达式
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates=new ArrayList<>();
                //title不为空
                if (!"".equals(blog.getTitle())&&blog.getTitle()!=null)
                {
                    //模糊查询          like
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId()!=null)
                {
                    //获取到type再获取type的id              equal =
                    predicates.add(cb.equal(root.<String>get("type").get("id"),blog.getTypeId()));
                }
                //是否点了推荐
                if (blog.isRecommend())
                {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                //从当前blog关联tags
                Join join=root.join("tags");
                //关联查询 返回blog_tags的id和当前传入的tagId相等的blogs
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog savaBlog(Blog blog) {
        //新增的blogid为空 修改的id不为空
        if (blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b=blogRepository.getOne(id);
        if (b==null)
            throw new NotFondException("该博客不存在");
        //过滤掉属性值为空的属性 只复制有值的；blog数据源 b是目标对象
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogRepository.findGroupYear();
        Map<String,List<Blog>> map=new HashMap<>();
        //根据年份拿到该年份的博客列表
        for (String year:years)
        {
            //把blog放入map集合中
            map.put(year,blogRepository.findByYear(year));

        }
        return map;
    }

    @Override
    public Long countBlog() {
        //blog表中数据个数
        return blogRepository.count();
    }
}
