package com.example.healthyme;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class bluetoothcon extends AppCompatActivity {

    Button bthon,bthpaired,bthdiscover;
    ListView listView_display;

    public DeviceList mListadapter;
    ArrayList<BluetoothDevice> bth_search_arrlist;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_BLUETOOTH = 1001;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetoothcon);

        bthon=findViewById(R.id.bth_on);
        bthpaired=findViewById(R.id.bth_paired);
        bthdiscover=findViewById(R.id.bth_discover);
        listView_display=findViewById(R.id.listview_def);
        bth_search_arrlist=new ArrayList<>();

        bluetoothManager= (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
       bluetoothAdapter=bluetoothManager.getAdapter();

        bthon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    Toast.makeText(getApplicationContext(),"Enabling bluetooth",Toast.LENGTH_LONG).show();
                    startActivityForResult(enableBtIntent,REQUEST_BLUETOOTH);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Bluetooth Already enabled",Toast.LENGTH_LONG).show();
                }
            }
        });

        listView_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = listView_display.getItemAtPosition(i).toString();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void discover(View view) {

        if(bluetoothAdapter.isDiscovering())
        {
            bluetoothAdapter.cancelDiscovery();
        }
        //check bt permissions
        checkBtpermissions();        
        bluetoothAdapter.startDiscovery();
        IntentFilter discoverdevices= new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mbroadcastreceiver,discoverdevices);
        if(!bluetoothAdapter.isDiscovering())
        {
            Toast.makeText(getApplicationContext(),"Not Discovering",Toast.LENGTH_LONG).show();

            checkBtpermissions();
            bluetoothAdapter.startDiscovery();
            discoverdevices= new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mbroadcastreceiver,discoverdevices);
        }
    }

    public BroadcastReceiver mbroadcastreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action=intent.getAction();
            Toast.makeText(getApplicationContext(),"Getting action",Toast.LENGTH_LONG).show();
            if(action.equals(BluetoothDevice.ACTION_FOUND))
            {
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bth_search_arrlist.add(device);
                mListadapter= new DeviceList(context,R.layout.device_adapter_view,bth_search_arrlist);
                listView_display.setAdapter(mListadapter);

            }
        }
    };


    public void pair(View view)
    {

        Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
        String[] strings=new String[bt.size()];
        int index=0;
        if(bt.size()>0) {
            for (BluetoothDevice device : bt) {
                strings[index] = device.getName();
                index++;
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,strings);
           listView_display.setAdapter(arrayAdapter);
        }

    }

    private void checkBtpermissions() {

        Toast.makeText(getApplicationContext(),"IN chech bluetooth permissions",Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        int permissioncheck=this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissioncheck+=this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if(permissioncheck!=0)
        {
         this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1001);
            Toast.makeText(getApplicationContext(),"Setting permissions",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Permissions already there",Toast.LENGTH_LONG).show();
        }
        }
    }







}
