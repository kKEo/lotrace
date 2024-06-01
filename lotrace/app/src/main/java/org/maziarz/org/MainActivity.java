package org.maziarz.org;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private TextView textView;

    private Button button;

    private PositionService myService;
    private boolean isBound = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("MainActivity", "Service connected");
            PositionService.LocalBinder binder = (PositionService.LocalBinder) service;
            myService = binder.getService();
            isBound = true;
            updateTextView();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("MainActivity", "Service disconnected");
            isBound = false;
        }
    };

    private final BroadcastReceiver listUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTextView();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        unregisterReceiver(listUpdateReceiver);
    }

    private void updateTextView() {
        if (isBound && myService != null) {
            List<String> itemList = myService.getItemList();
            StringBuilder displayText = new StringBuilder();
            for (String item : itemList) {
                displayText.append(item).append("\n");
            }
            textView.setText(displayText.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Button clicked");

                if (isBound && myService != null) {
                    myService.addItem("This is a new item added via button");
                    updateTextView();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationService() {
        Log.d("MainActivity", "Service stating..");

        Intent intent = new Intent(this, PositionService.class);
        ComponentName cname = startService(intent);

        Log.d("MainActivity", "Component: " + cname);

//        startForegroundService(intent);

        boolean res = bindService(intent, connection, BIND_AUTO_CREATE);

        Log.d("MainActivity","Result: " + res);

        IntentFilter filter = new IntentFilter(PositionService.ACTION_LIST_UPDATED);
        registerReceiver(listUpdateReceiver, filter);
    }
}


