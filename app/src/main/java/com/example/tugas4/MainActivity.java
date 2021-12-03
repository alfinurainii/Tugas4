package com.example.tugas4;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.SmsManager;

public class MainActivity extends AppCompatActivity {

    Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendSMS = findViewById(R.id.btnSendSMS);
        txtPhoneNo = findViewById(R.id.txtPhoneNo);
        txtMessage = findViewById(R.id.txtMessage);

        txtPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("com.example.tugas4"));
            }
        });

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();

                if (phoneNo.length() > 0 && message.length() > 0){
                    sendSMS(phoneNo,message);
                }else {
                    Toast.makeText(getBaseContext(),
                            "Silahkan entry No Handphone DAN Pesan",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

private void sendSMS(String phoneNumber, String message) {
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";

    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
    PendingIntent deliveredPI = PendingIntent.getBroadcast(this,0, Intent(DELIVERED), 0);

    registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()){
                case MainActivity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS Terkirim", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "Tidak ada Layanan !", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), " Null PDU", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio Off", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }, new IntentFilter(SENT));

    registerReceiver(new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()){
                case MainActivity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS Terkirim!", Toast.LENGTH_SHORT).show();
                    break;
                case MainActivity.RESULT_CANCELED:
                    Toast.makeText(getBaseContext(), "SMS TIDAK Terkirim!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }, new IntentFilter(DELIVERED));

    SmsManager sms = SmsManager.getDefault();
    sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
}
