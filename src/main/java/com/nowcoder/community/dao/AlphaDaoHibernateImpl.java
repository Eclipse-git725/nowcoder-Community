package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * 文件名称: AlphaDaoHibernateImpl.java
 * 作者: gxy
 * 创建日期: 2025/2/20
 * 描述: 使用Hibernate实现AlphaDao接口
 *
 * @Repository("alphaDaoHibernate")，在使用时可以用名字注入
 */
@Repository("alphaDaoHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{

    @Override
    public String select() {
        return "Hibernate";
    }

}
