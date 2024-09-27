package com.jc.sign;
/* *
 *类名：SignConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：1.0
 *日期：2017-12-21
 */

public class SignConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 商户的私钥
    public static final String APP_SECRET = "123";

    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
//    //签名参数
    public static final String PARAM_SIGN = "sign";
//    //签名方法参数
    public static final String PARAM_SIGN_METHOD = "sign_method";
//
//    //密钥参数
    public static final String PARAM_KEY = "key";

//    // 字符编码格式 目前支持 gbk 或 utf-8
    public static final String INPUT_CHARSET_TYPE = "UTF-8";
//
//    // 签名方式 不需修改
    public static final String SIGN_METHOD = "MD5";

}
