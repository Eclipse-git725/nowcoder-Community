package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 文件名称: SensitiveTest.java
 * 作者: gxy
 * 创建日期: 2025/3/2
 * 描述: 敏感词过滤器测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testFilter() {
        String text = "开票是什么";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
        text = "不能☆赌☆博☆";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
