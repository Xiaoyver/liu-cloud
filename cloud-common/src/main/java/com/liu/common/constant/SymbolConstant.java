package com.liu.common.constant;

/**
 * 符号和特殊符号常用类
 *
 * @author gdLiu
 * @date 2023/4/29 15:30
 */
public class SymbolConstant {

    /**
     * 符号：点
     */
    public static final String SPOT = ".";

    /**
     * 符号：双斜杠
     */
    public static final String DOUBLE_BACKSLASH = "\\";

    /**
     * 符号：冒号
     */
    public static final String COLON = ":";

    /**
     * 符号：双冒号
     */
    public static final String DOUBLE_COLON = "::";

    /**
     * 符号：逗号
     */
    public static final String COMMA = ",";

    /**
     * 符号：左花括号 }
     */
    public static final String LEFT_CURLY_BRACKET = "{";

    /**
     * 符号：右花括号 }
     */
    public static final String RIGHT_CURLY_BRACKET = "}";

    /**
     * 符号：井号 #
     */
    public static final String WELL_NUMBER = "#";

    /**
     * 符号：单斜杠
     */
    public static final String SINGLE_SLASH = "/";

    /**
     * 路径分隔符
     */
    public static final String POINT_SINGLE_SLASH = "../";

    /**
     * 符号：双斜杠
     */
    public static final String DOUBLE_SLASH = "//";

    /**
     * 路径分隔符
     */
    public static final String POINT_DOUBLE_SLASH = "..\\";

    /**
     * 符号：感叹号
     */
    public static final String EXCLAMATORY_MARK = "!";

    /**
     * 符号：下划线
     */
    public static final String UNDERLINE = "_";

    /**
     * 中划线
     */
    public static final String MIDDLE_LINE = "-";

    /**
     * 符号：单引号
     */
    public static final String SINGLE_QUOTATION_MARK = "'";

    /**
     * 符号：星号
     */
    public static final String ASTERISK = "*";

    /**
     * 符号：百分号
     */
    public static final String PERCENT_SIGN = "%";

    /**
     * 符号：美元 $
     */
    public static final String DOLLAR = "$";

    /**
     * 符号：和 &
     */
    public static final String AND = "&";

    /**
     * 分号;
     */
    public static final String SEMICOLON = ";";
    /**
     * null
     */
    public static final String NULL = "null";

    /**
     * HTTP
     */
    public static final String HTTP = "http";

    /**
     * HTTP
     */
    public static final String HTTP_URL = "http://";

    /**
     * HTTPS
     */
    public static final String HTTPS_URL = "https://";

    private SymbolConstant() {
        throw new IllegalArgumentException("SymbolConstant类不允许被实例化");
    }
}
