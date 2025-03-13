package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件名称: CommentMapper.java
 * 作者: gxy
 * 创建日期: 2025/3/4
 * 描述: 评论类的Mapper
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);
    int selectCommentsRows(int entityType, int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);

}
