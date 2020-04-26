package com.example.usb;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.*;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.*;
import java.util.*;

import pub.devrel.easypermissions.*;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public static final String TextF = "persmsg.txt";
    private EditText pmsg;
    private FusedLocationProviderClient fusedLocationClient;
    String text, msg="Help";
    private final int REQUEST_CODE = 99;
    public Button getcon, getloc;
    Databasehandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pmsg = findViewById(R.id.ed1);
        getcon = findViewById(R.id.getcontact);
        db = new Databasehandler(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            String[] perms = {
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_CONTACTS
            };
            if (!EasyPermissions.hasPermissions(this, perms)) {
                EasyPermissions.requestPermissions(this, "All permissions are required in oder to run this application", REQUEST_CODE, perms);
            }
            getcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, Activity_2.class);
                    startActivity(i);
                }
            });
        }

    }

    public void save(View v) {
        msg = pmsg.getText().toString();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(TextF, MODE_PRIVATE);
            fos.write((msg).getBytes());
            pmsg.getText().clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void load(View v) {
        FileInputStream fis = null;
        try {
            fis = openFileInput(TextF);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            pmsg.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(View v) {
        SQLiteDatabase dab = db.getWritableDatabase();
        Cursor res = dab.rawQuery("select * from " + db.tbn1, null);
        if (res.getCount() == 0) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            text = "\nLatitude:-" + location.getLatitude() + "\nLongitude:-" + location.getLongitude();
                        }
                    }
                });
        FileInputStream fis = null;
        try {
            fis = openFileInput(TextF);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        while (res.moveToNext()) {
            String phone = res.getString(1);
            SmsManager sms = SmsManager.getDefault();

            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
            sms.sendTextMessage(phone, null, msg + text, sentIntent, deliveredIntent);
            Intent intent = new Intent(android.content.Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.requestPermissions(this, "All permissions are required to run this application", requestCode, permissions);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}