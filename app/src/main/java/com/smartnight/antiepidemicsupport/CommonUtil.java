package com.smartnight.antiepidemicsupport;

import android.app.Activity;
import android.content.Intent;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

public class CommonUtil {
    public static void uploadPicture(Activity activity,int number,int requestCode){
        //通过Matisse选择图片
        Matisse.from(activity)
                .choose(MimeType.ofAll())
                .countable(true)
                .maxSelectable(9)
                .thumbnailScale(0.8f)
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(new GlideEngine())
                .forResult(requestCode);
    }
}
