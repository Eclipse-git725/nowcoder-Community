package com.nowcoder.community;

import com.nowcoder.community.util.MailClilent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * 文件名称: MailTests.java
 * 作者: gxy
 * 创建日期: 2025/2/25
 * 描述: 邮箱工具测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    @Autowired
    private MailClilent mailClilent;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClilent.sendMail("nowcodergxy@sina.com", "Test", "Welcome.");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "gxy");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClilent.sendMail("nowcodergxy@sina.com", "HTML", content);
    }
}
