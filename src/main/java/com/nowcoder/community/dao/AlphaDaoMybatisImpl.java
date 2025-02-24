package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * 文件名称: AlphaDaoMybatisImpl.java
 * 作者: gxy
 * 创建日期: 2025/2/20
 * 描述: 假设想换用Mybatis实现AlphaDao
 *
 * @Primary注解表示优先使用这个实现
 */

@Repository("alphaDaoMybatis")
@Primary
public class AlphaDaoMybatisImpl implements AlphaDao{

    @Override
    public String select() {
        return "Mybatis";
    }

}
