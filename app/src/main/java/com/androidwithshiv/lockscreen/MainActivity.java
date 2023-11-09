package com.androidwithshiv.lockscreen;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private View lockButton;
    private SwitchCompat deviceAdminSwitch;
    private ComponentName componentName;
    private DevicePolicyManager devicePolicyManager;
    private static final int RESULT_ENABLE = 123;
    private boolean isAdminOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        lockButton = findViewById(R.id.lock);
        deviceAdminSwitch = findViewById(R.id.admin_switch);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName =  new ComponentName(this,  MyDeviceAdminReceiver.class);

        deviceAdminSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "For lock screen");
                    startActivityForResult(intent, RESULT_ENABLE);
                }
                else{
                    devicePolicyManager.removeActiveAdmin(componentName);
                }
            }
        });

        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(devicePolicyManager.isAdminActive(componentName)){
                    devicePolicyManager.lockNow();
                }else{
                    Toast.makeText(MainActivity.this,"You need to enable device admin..!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if(resultCode== Activity.RESULT_OK){
                    Toast.makeText(this, "You have enable device admin features", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Problem to enable device admin features", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAdminOn = devicePolicyManager.isAdminActive(componentName);
        deviceAdminSwitch.setChecked(isAdminOn);
    }
}