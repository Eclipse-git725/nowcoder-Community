package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文件名称: MessageService.java
 * 作者: gxy
 * 创建日期: 2025/3/5
 * 描述: 消息业务层
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    // 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    // 查询当前用户的会话数量
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    // 查询某个会话所包含的私信列表
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    // 查询某个会话所包含的私信数量
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    // 查询未读私信的数量（用户的未读会话，会话的未读私信）
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

}
