package com.nowcoder.community;

import com.nowcoder.community.dao.*;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;


/**
 * 文件名称: MapperTests.java
 * 作者: gxy
 * 创建日期: 2025/2/20
 * 描述: 对于Mapper的测试类
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectById() {
        User user = userMapper.selectById(0);
        System.out.println(userMapper);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("https://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser() {
        int rows = userMapper.updateStatus(1924665351, 1);
        System.out.println(rows);
        rows = userMapper.updateHeader(1924665351,"http://images.nowcoder.com/head/120t.png");
        System.out.println(rows);
        rows = userMapper.updatePassword(1924665351,"123456");
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post : discussPostList) {
            System.out.println(post);
            System.out.println(post.getUserId());
        }
        int rows = discussPostMapper.selectDiscussPostsRows(149);
        System.out.println(rows);

    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectByTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testInsertDiscussPost() {
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(101);
        discussPost.setTitle("test");
        discussPost.setContent("test");
        discussPost.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(discussPost);
    }

    @Test
    public void testSelectDiscussPostById() {
        DiscussPost discussPost = discussPostMapper.selectDiscussPostById(282);
        System.out.println(discussPost.getContent());
    }


    @Test
    public void testSelectCommentByEntity() {
        List<Comment> list = commentMapper.selectCommentByEntity(2, 12, 0, 10);
        System.out.println(list.size());
        int rows = commentMapper.selectCommentsRows(2, 12);
        System.out.println(rows);
    }

    @Test
    public void testInsertComment() {
        Comment comment = new Comment();
        comment.setUserId(101);
        comment.setEntityType(2);
        comment.setEntityId(12);
        comment.setTargetId(0);
        comment.setContent("test");
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentMapper.insertComment(comment);
    }

    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for(Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for(Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);

        Message message = new Message();
        message.setFromId(111);
        message.setToId(112);
        if(message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent("test");
        message.setCreateTime(new Date());
        messageMapper.insertMessage(message);
    }
}
