package com.wqg.blog.service;

import com.wqg.blog.dao.CommentRepository;
import com.wqg.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long id) {
        //查询所有父结点为空的评论
        List<Comment> comments=commentRepository.findByBlogIdAndParentCommentNull(id,Sort.by(Sort.Direction.ASC,"createTime"));
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId=comment.getParentComment().getId();
        //父级有对象 说明这是一条回复
        if (parentCommentId!=-1)
        {
            //拿到父评论id 给本条id设置父评论id 建立父子关系
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        }
        //说明这是单独的一条评论 没有父级
        else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments)
    {
        List<Comment> commentsView=new ArrayList<>();
        for (Comment comment:comments)
        {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }
    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        //遍历根节点获取根节点的replycomments 然后再循环遍历replylist的子类 把子评论加入recursively
        for (Comment comment:comments) {
            List<Comment> replylist=comment.getReplyComments();
            for (Comment reply:replylist) {
                //循环找出
                recursively(reply);
            }
            //修改当前评论的子类结点为循环遍历出的list
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }
    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    //不断循环取出所有replylist中的元素 都存入tempReplys中
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶结点放入子代集合
        //如果这个顶节点还有子结点
        if (comment.getReplyComments().size()>0)
        {
            //循环取出 放入子代集合
            List<Comment> replys=comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}
