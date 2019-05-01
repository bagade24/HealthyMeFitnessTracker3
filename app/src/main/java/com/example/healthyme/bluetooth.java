package com.example.healthyme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.BleApi;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.google.android.gms.fitness.result.BleDevicesResult;

import java.util.ArrayList;
import java.util.Set;

import static com.google.android.gms.fitness.FitnessStatusCodes.*;

public class bluetooth extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH = 1001;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice>bluetoothDevice;
    //BluetoothDevice bthdevice_search;
    String[] strings;
    //ListView listView;
    int index;
    TextView t1;
    Button b1,b2;

    ArrayList<String> stringArrayList;
    ArrayAdapter<String> arrayAdapter_search;
    ListView listView1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        t1=findViewById(R.id.textViewbth);
        b1=findViewById(R.id.discover);
        b2=findViewById(R.id.paired);
        listView1 = findViewById(R.id.bth_listview);

        arrayAdapter_search=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,stringArrayList);
        //listView1.setAdapter(arrayAdapter_search);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    t1.setText("Enabling bth");
                    startActivityForResult(enableBtIntent,REQUEST_BLUETOOTH);
                }
                else
                {
                    t1.setText("Bth there");
                }
               // bthpaired();
              //  bthdiscovery();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    t1.setText("Enabling bth");
                    startActivityForResult(enableBtIntent,REQUEST_BLUETOOTH);
                }
                else
                {
                    t1.setText("Bth there");
                }


                 bthpaired();
             //   bthdiscovery();
            }
        });

    }


    public void bthpaired() {

        bluetoothDevice=bluetoothAdapter.getBondedDevices();
        strings= new String[bluetoothDevice.size()];
        index=0;
        if(bluetoothDevice.size()>0)
        {
            for(BluetoothDevice bthDevice:bluetoothDevice) {
                strings[index] = bthDevice.getName();
                index++;
            }

            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,strings);
            listView1.setAdapter(arrayAdapter);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

            t1.setText("bth connected");
            //toast for bth
    }

    @Override
    protected void onStart() {
        super.onStart();
        t1.setText("Onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


}