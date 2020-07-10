package com.smartnight.antiepidemicsupport;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Content {
    private String text1,text2,text3;
    private int MODE;
    private String TIME;//请求生成的时间
    public Content(int mode,String father,String son,String need,int sum,int num){
        this.MODE = mode;
        if(mode == 1){
            //give模式
            text1 = "收到来自"+father+"的请求：";
            text2 = son+"需要"+need+"共"+sum+"件";
            text3 = "请尽快将本地该物"+num+"件送达";
        }else if(mode == 2){
            //receive模式
            text1 = "收到来自"+father+"的援助：";
            text2 = "援助物资"+need+"共"+sum+"件到达";
            text3 = "请尽快联系需要的居民领取";
        }else{
            //错误请求
        }

    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }
}
