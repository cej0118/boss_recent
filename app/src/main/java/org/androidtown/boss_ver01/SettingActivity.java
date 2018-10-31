package org.androidtown.boss_ver01;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
//라이브러리 부분
//app 하단 gradle에 라이브러리 컴파일
import java.sql.Time;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class SettingActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    private Button FB_on;       //플래시 on/off 버튼 id
    private Button FB_off;
    private Switch FS;
    private Button CB_on;
    private Button CB_off;
    private Switch CS;
    private Switch Bar_Switch;
    private Switch DS;
    BluetoothSocket mSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        bt = new BluetoothSPP(this); //Initializing

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(SettingActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {
        //블루투스 데이터 처리
        //
        //
        //


        //안전바 이벤트
        Bar_Switch = (Switch)findViewById(R.id.bar_switch);
        Bar_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override                                              //안전바 스위치 이벤트 처리
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChedked) {
                if(isChedked){
                    //bar switch on
                    bt.send("bar_on",true);
                }
                else{
                    //bar switch off
                    bt.send("bar_off",true);
                }
            }
        });
        
        //플래시 이벤트
        FS = (Switch)findViewById(R.id.flash_switch);
        FB_on = (Button)findViewById(R.id.flash_button_on);
        FB_off = (Button)findViewById(R.id.flash_button_off);

        FS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override                                              //플래시 스위치 이벤트 처리
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChedked) {
                if(isChedked){
                    //flash switch on
                    bt.send("fs_on",true);
                }
                else{
                    //flash switch off
                    bt.send("fs_off",true);
                }
            }
        });
        FB_on.setOnClickListener(new View.OnClickListener()                 //플래시 on버튼 이벤트 처리
        {
            public void onClick(View v) { bt.send("f_on", true);
            }
        });
        FB_off.setOnClickListener(new View.OnClickListener()                //플래시 off버튼 이벤트 처리
        {
            public void onClick(View v){
                bt.send("f_off",true);
            }
        });


        //덮개 이벤트
        CS = (Switch)findViewById(R.id.cover_switch);
        CB_on = (Button)findViewById(R.id.cover_button_on);
        CB_off = (Button)findViewById(R.id.cover_button_off);

        CS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override                                              //덮개 스위치 이벤트 처리
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChedked) {
                if(isChedked){
                    //cover switch on
                    bt.send("cs_on",true);
                }
                else{
                    //cover switch off
                    bt.send("cs_off",true);
                }
            }
        });
        CB_on.setOnClickListener(new View.OnClickListener()                 //덮개 on버튼 이벤트 처리
        {
            public void onClick(View v) { bt.send("c_on", true);
            }
        });
        CB_off.setOnClickListener(new View.OnClickListener()                //덮개 off버튼 이벤트 처리
        {
            public void onClick(View v){
                bt.send("c_off",true);
            }
        });

        DS=(Switch)findViewById(R.id.drive_switch);
        DS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override                                              //플래시 스위치 이벤트 처리
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChedked) {
                if(isChedked){
                    //flash switch on
                    bt.send("ds_on",true);
                    bt.send("AT+RSSI?",true);
                }
                else{
                    //flash switch off
                    bt.send("ds_off",true);
                }
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}