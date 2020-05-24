package com.whf.messagerelayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whf.messagerelayer.R;
import com.whf.messagerelayer.utils.NativeDataManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mSmsLayout, mEmailLayout, mRuleLayout;
    private NativeDataManager mNativeDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();
        mNativeDataManager = new NativeDataManager(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Boolean isReceiver = mNativeDataManager.getReceiver();
        final MenuItem menuItem = menu.add("开关");
        if (isReceiver) {
            menuItem.setIcon(R.mipmap.ic_send_on);
        } else {
            menuItem.setIcon(R.mipmap.ic_send_off);
        }

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Boolean receiver = mNativeDataManager.getReceiver();
                if(receiver){
                    mNativeDataManager.setReceiver(false);
                    menuItem.setIcon(R.mipmap.ic_send_off);
                    Toast.makeText(MainActivity.this,"总闸已关闭",Toast.LENGTH_SHORT).show();
                }else{
                    mNativeDataManager.setReceiver(true);
                    menuItem.setIcon(R.mipmap.ic_send_on);
                    Toast.makeText(MainActivity.this,"总闸已开启",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("关于").setIcon(R.mipmap.ic_about)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        mSmsLayout = (RelativeLayout) findViewById(R.id.sms_relay_layout);
        mEmailLayout = (RelativeLayout) findViewById(R.id.email_relay_layout);
        mRuleLayout = (RelativeLayout) findViewById(R.id.rule_layout);

        mSmsLayout.setOnClickListener(this);
        mEmailLayout.setOnClickListener(this);
        mRuleLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sms_relay_layout:
                startActivity(new Intent(this, SmsRelayerActivity.class));
                break;
            case R.id.email_relay_layout:
                startActivity(new Intent(this, EmailRelayerActivity.class));
                break;
            case R.id.rule_layout:
                startActivity(new Intent(this, RuleActivity.class));
        }
    }

    //权限动态申请
    private void requestPermissions(){

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }

        if (!permissionList.isEmpty()){  //申请的集合不为空时，表示有需要申请的权限
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else { //所有的权限都已经授权过了

        }
    }

    //权限申请回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){ //安全写法，如果小于0，肯定会出错了
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){ //这个是权限拒绝
                            //String s = permissions[i];
                            //Toast.makeText(this,s+"权限被拒绝了",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{ //授权成功了
                            //do Something
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
