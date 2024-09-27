package com.jc.sign;

import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: SignSubmit
 * @Description:生成请求参数
 * @author: jason
 * @date: 2017年12月21日 下午5:45:51
 */
public class SignUtilTest {


    public static void main(String[] args) {
        Map<String, String> sParaTemp = new HashMap<>();
        /**
         * 1、注意参数中不能有key和sign名称；
         * 2、参数中加入时间戳的话，可以考虑当前时间相差半小时的请求是合理的，但要求两服务器时间基本上要同步。
         * 3、如果是点对点的加密，只要密钥就可以了，不要通过key查密钥了——使用singUtil2方法即可。
         */
        sParaTemp.put("couponId", "310100534516263");
        sParaTemp.put("time",String.valueOf(System.currentTimeMillis()));
        //签名加密结果
        Map<String, String> signMap = SignUtil.buildRequestPara(sParaTemp);
        System.out.println("sign:"+signMap.get("sign"));
//        String sign = "73dba5b3af97e7e09125c2623e680847";
//        String sign = sParaTemp.get("sign");
//        sParaTemp.put("sign",sign);
        boolean b = SignUtil.verifySign2(signMap);
        System.out.println(b);
    }



}
