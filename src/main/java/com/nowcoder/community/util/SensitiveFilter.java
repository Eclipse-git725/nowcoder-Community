package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 文件名称: SensitiveFilter.java
 * 作者: gxy
 * 创建日期: 2025/3/2
 * 描述: 敏感词过滤工具类
 */
@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 替换符
    private static final String REPLACEMENT = "***";

    // 初始化根节点
    private TrieNode rootNode = new TrieNode();

    // 初始化构造前缀树
    @PostConstruct
    public void init() {
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ) {
            String keyword;
            while((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyWord(keyword);
            }

        } catch (Exception e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    // 添加一个敏感词到前缀树中，私有方法，不给外部调用
    private void addKeyWord(String keyword) {
        TrieNode tempNode = rootNode;
        for(int i = 0; i < keyword.length(); i ++ ) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null) {
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            tempNode = subNode;
        }
        tempNode.setEnd(true);
    }

    // 过滤敏感词方法，公有方法，要给外部调用
    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
//    自己写的，没考虑到敏感词中间有符号的情况
//    public String filter(String text) {
//        if(StringUtils.isBlank(text)) {
//            return text;
//        }
//
//        // 结果存放在res中
//        StringBuilder res = new StringBuilder();
//        for(int l = 0; l < text.length(); l ++ ) {
//            // 枚举字符串的左节点和右节点
//            int r = l;
//            TrieNode tempNode = rootNode;
//            while(r < text.length()) {
//                char c = text.charAt(r);
//                TrieNode subNode = tempNode.getSubNode(c);
//                if(subNode == null) {
//                    break;
//                }
//
//                r ++;
//                tempNode = subNode;
//                if(tempNode.isEnd()) {
//                    res.append(REPLACEMENT);
//                    l = r - 1;
//                    break;
//                }
//            }
//            if(r == l) {
//                res.append(text.charAt(r));
//            }
//        }
//
//        return res.toString();
//    }
    // 跟着课写的
    public String filter(String text) {
        TrieNode tempNode = rootNode; // 指针1
        int begin = 0; // 指针2
        int position = 0; // 指针3
        // 结果
        StringBuilder sb = new StringBuilder();
        while(position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if(isSymbol(c)) {
                // 若指针1处于根节点，将此符号计入结果，指针2向下走一步
                if(tempNode == rootNode) {
                    sb.append(c);
                    begin ++;
                }
                // 无论符号在开头还是中间，指针3都向下走一步
                position ++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null) {
                // 已begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if(tempNode.isEnd()) {
                // 发现敏感词，将begin-position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                tempNode = rootNode;
            } else {
                // 继续检查下一个字符
                position ++;
            }
        }

        // 将最后一批字符计入结果，指针2疑似敏感词，指针3已经到结尾，不是敏感词
        sb.append(text.substring(begin));
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80 ~ 0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode {
        // 当前节点的子节点
        // key 是下级节点的字符，value是下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();
        // 关键词结束标识
        boolean isEnd = false;

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        // 添加子节点的方法
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点的方法
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
