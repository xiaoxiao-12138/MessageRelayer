package com.whf.messagerelayer.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.whf.messagerelayer.activity.SmsRelayerActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WHF on 2017/3/26.
 */

public class SmsRelayerManager {
    /**
     * 发送短信至目标手机号
     * @param dataManager
     * @param content      短信内容
     */
    public static void relaySms(NativeDataManager dataManager, String content) {
        //发信人
        String sender = dataManager.getSender();
        //默认收信人
        String objectMobile = dataManager.getObjectMobile();
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 手机号验证规则
        String regEx = "^收信人1[3|4|5|7|8][0-9]\\d{8}$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        //若短信内容超过14个字(收信人+电话号码)
        if (content.length() > 14){
            //将短信内容与(收信人+电话号码)进行匹配
            Matcher matcher = pattern.matcher(content.substring(0,14));
            boolean isMatch = matcher.matches();
            //如果匹配到了并且是默认的收信人号码（安全起见）
            if (isMatch && sender.equals(objectMobile)) {
                //将短信内容发送到指定号码，并将短信内容中的(收信人+电话号码)去掉
                smsManager.sendTextMessage(content.substring(3,14), null, content.substring(15), null, null);
            }else {
                //将短信内容发送到APP中设置好的号码中
                smsManager.sendTextMessage(objectMobile, null, "发信人"+sender+"\r"+content, null, null);
            }
        } else {    //若短信内容不超过14个字
            smsManager.sendTextMessage(objectMobile, null, "发信人"+sender+"\r"+content, null, null);
        }
    }
}
