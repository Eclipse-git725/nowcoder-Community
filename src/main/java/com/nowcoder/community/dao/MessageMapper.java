package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文件名称: MessageMapper.java
 * 作者: gxy
 * 创建日期: 2025/3/5
 * 描述: 消息的数据层
 */
@Mapper
public interface MessageMapper {
    // 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信的数量（用户的未读会话，会话的未读私信）
    int selectLetterUnreadCount(int userId, String conversationId);

    // 发送私信
    // 增加一条私信
    int insertMessage(Message message);

    // 更改私信的状态
    int updateStatus(List<Integer> ids, int status);

    // 查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题所包含的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知的数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 查询某个主题所包含的通知列表(分页)
    List<Message> selectNotices(int userId, String topic, int offset, int limit);
}
