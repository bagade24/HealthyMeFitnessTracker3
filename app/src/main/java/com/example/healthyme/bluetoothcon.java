package com.example.healthyme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
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

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.BleApi;
import com.google.android.gms.fitness.BleApi.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.google.android.gms.fitness.result.BleDevicesResult;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class bluetoothcon extends AppCompatActivity{

    Button bthon, bthpaired, bthdiscover;
    ListView listView_display;



    private GoogleApiClient mApiClient;
    private boolean authInProgress = false;
    private static final String AUTH_PENDING = "auth_state_pending";

   // GoogleSignInAccount gsa;

    public DeviceList mListadapter;
    ArrayList<BluetoothDevice> bth_search_arrlist;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    BluetoothDevice bluetoothDevice;
    BluetoothGattCallback bluetoothGattCallback;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private static final int REQUEST_BLUETOOTH = 1001;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetoothcon);

        bthon = findViewById(R.id.bth_on);
        bthpaired = findViewById(R.id.bth_paired);
        bthdiscover = findViewById(R.id.bth_discover);
        listView_display = findViewById(R.id.listview_def);
        bth_search_arrlist = new ArrayList<>();

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }


        bthon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    Toast.makeText(getApplicationContext(), "Enabling bluetooth", Toast.LENGTH_LONG).show();
                    startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH);
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth Already enabled", Toast.LENGTH_LONG).show();
                }
            }
        });


        listView_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = listView_display.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                bluetoothDevice=bluetoothAdapter.getRemoteDevice(s);
                Toast.makeText(getApplicationContext(), s+"Getting connected", Toast.LENGTH_LONG).show();
                int k=bluetoothDevice.getBondState();
               // bluetoothDevice.connectGatt()

                  Toast.makeText(getApplicationContext(), s+"Bond Creation "+k, Toast.LENGTH_LONG).show();



            }
        });
//bluetoothGattCallback.onCharacteristicRead(ca);


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
