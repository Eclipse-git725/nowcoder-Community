package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jws.soap.SOAPBinding;
import java.util.Date;

/**
 * 文件名称: AlphaService.java
 * 作者: gxy
 * 创建日期: 2025/2/20
 * 描述: 业务组件，处理业务例子
 *
 * 输出可以看出，被Springboot管理的Bean，只实例化了一次，初始化一次，销毁一次
 * @Scope("prototype")表示每次调用getBean()方法时，都会创建一个新的实例，一般都用单例模式
 */
@Service
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private TransactionTemplate transactionTemplate;

//    public AlphaService() {
//        System.out.println("实例化AlphaService");
//    }
//
//    @PostConstruct
//    public void init() {
//        System.out.println("初始化AlphaService");
//    }
//
//    @PreDestroy
//    public void destroy() {
//        System.out.println("销毁AlphaService");
//    }

    public String find() {
        return alphaDao.select();
    }

    // 事务管理
    // REQUIRED：支持当前事务（或者说外部事务，A调用B，A是当前事务），如果不存在则创建新事务
    // REQUIRES_NEW：创建一个新事物，并且暂停当前事务
    // NESTED：如果当前存在事务（外部事务），则嵌套在该事务中执行（但有独立的提交和回滚），否则和REQUIRED一致
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle("hello");
        discussPost.setContent("新人报道！");
        discussPost.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(discussPost);

        // 报错是否会会滚
        Integer.valueOf("abc");
        return "ok";
    }

    // 编程式事务
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/89t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setUserId(user.getId());
                discussPost.setTitle("你好");
                discussPost.setContent("我是新人！");
                discussPost.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(discussPost);

                // 报错是否会会滚
                Integer.valueOf("abc");
                return "ok";
            }
        });
    }

}
