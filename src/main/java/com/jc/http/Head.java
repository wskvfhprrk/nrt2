package com.jc.http;

import lombok.Data;

@Data
public class Head {
    //Url
    private String url;
    //sign
    private String sign;
    //appInstanceId
    private String appInstanceId="7p92yl9q";
    //apiVersion
    private String apiVersion = "V2";
    //nonce
    private String nonce;
    //timestamp
    private String timestamp;
    //dataJson
    private String dataJson;
}
