package org.maziarz;

public class LocationService extends Service {

    private static final long UPDATE_INTERVAL = 60000; // 60 seconds
    private static final float MIN_DISTANCE = 10; // 10 meters

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    sendLocationToServer(location);
                }
            }
        };

        startLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, "LocationServiceChannel")
                .setContentTitle("Location Service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_location)
                .build();

        startForeground(1, notification);
        return START_STICKY;
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(UPDATE_INTERVAL / 2);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(MIN_DISTANCE);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

private void sendLocationToServer(Location location) {
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();

    String serverUrl = "http://org.maziarz.org/location";
    String requestData = "latitude=" + latitude + "&longitude=" + longitude;

    new Thread(() -> {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream os = urlConnection.getOutputStream();
            os.write(requestData.getBytes());
            os.flush();
            os.close();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Successfully sent location data
            } else {
                // Failed to send location data
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }).start();
}



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "LocationServiceChannel",
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

