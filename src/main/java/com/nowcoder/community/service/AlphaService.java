package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
}
