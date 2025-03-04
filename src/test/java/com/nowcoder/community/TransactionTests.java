package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * 文件名称: TransactionTests.java
 * 作者: gxy
 * 创建日期: 2025/3/4
 * 描述: 事务管理测试
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTests {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1() {
        Object obj = alphaService.save1();
        // user 和 post 都回滚了，没有插入成功
        System.out.println(obj);
    }

    @Test
    public void testSave2() {
        Object obj = alphaService.save2();
        // user 和 post 都回滚了，没有插入成功
        System.out.println(obj);
    }
}
