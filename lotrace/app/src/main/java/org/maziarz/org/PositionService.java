package org.maziarz.org;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PositionService extends Service {

    public static final String ACTION_LIST_UPDATED = "org.maziarz.org.ACTION_LIST_UPDATED";

    private final IBinder binder = new LocalBinder();

    private int counter = 0;
    private final LinkedList<String> itemList = new LinkedList<>();

    public PositionService() {
        super();
        Log.d("PositionService", "Constructing..");
    }

    public void addItem(String item) {
        this.itemList.add("[" + this.counter++ + "] " + item);
        if (this.itemList.size() > 20) {
            this.itemList.poll();
        }
    }

    public class LocalBinder extends Binder {
        PositionService getService() {
            return PositionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyService", "Service onBind called");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        itemList.add("This is the first item");
        itemList.add("This is the second item");

        Log.d("MyService", "Service onCreate called");
    }

    public List<String> getItemList() {
        return itemList;
    }

    private void broadcastListUpdate() {
        Intent intent = new Intent(ACTION_LIST_UPDATED);
        sendBroadcast(intent);
    }
}
