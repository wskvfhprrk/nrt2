package com.jc.sign;

import java.util.*;


/**
 * 类名:	SignCore
 * 功能：接口公用函数类
 * 详细：该类是请求、通知返回两个文件所调用的公用函数核心处理文件，不需要修改
 * 版本：1.0
 * 日期：2017年12月21日 上午11:19:28
 */
public class SignCore {

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<>(16);

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        Iterator iterator= sArray.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry=(Map.Entry)iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (value == null || "".equals(value) || key.equals(SignConfig.PARAM_SIGN)
                    || key.equals(SignConfig.PARAM_SIGN_METHOD) || key.equals(SignConfig.PARAM_KEY)) {
                continue;
            }
            result.put(key.toString(), value.toString());
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuffer stringBuffer=new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            //拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                stringBuffer.append(key + "=" + value);
            } else {
                stringBuffer.append(key + "=" + value + "&");
            }
        }

        return stringBuffer.toString();
    }
}
