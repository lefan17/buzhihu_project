package com.example.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * HTML 消毒工具：富文本入库前清洗，防止存储型 XSS
 */
public class HtmlUtils {

    /**
     * 允许的标签/属性（relaxed 白名单 + wangeditor 常用样式）
     */
    private static final Safelist SAFELIST = Safelist.relaxed()
            .addAttributes(":all", "style", "class")
            .addAttributes("img", "src", "alt", "title", "width", "height")
            .addProtocols("img", "src", "http", "https", "data");

    private static final Document.OutputSettings OUTPUT = new Document.OutputSettings().prettyPrint(false);

    private HtmlUtils() {
    }

    /**
     * 清洗富文本，返回安全的 HTML；null 原样返回
     */
    public static String sanitize(String html) {
        if (html == null) {
            return null;
        }
        return Jsoup.clean(html, "", SAFELIST, OUTPUT);
    }
}
