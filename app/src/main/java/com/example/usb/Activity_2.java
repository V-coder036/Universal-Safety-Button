package com.example.usb;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

public class Activity_2 extends AppCompatActivity {

    Databasehandler db = new Databasehandler(this);
    SQLiteDatabase dab;
    private final int REQUEST_CODE = 99;
    ArrayList<String> Cons=new ArrayList<>();
    private TextView tv1,tv2,tv3,tv4,tv5;
    private Button ad1,ad2,ad3,ad4,ad5,v1,v2,v3,v4,v5,save;
    String num = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        ad1=findViewById(R.id.ad1);
        ad2=findViewById(R.id.ad2);
        ad3=findViewById(R.id.ad3);
        ad4=findViewById(R.id.ad4);
        ad5=findViewById(R.id.ad5);
        v1=findViewById(R.id.v1);
        v2=findViewById(R.id.v2);
        v3=findViewById(R.id.v3);
        v4=findViewById(R.id.v4);
        v5=findViewById(R.id.v5);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);
        save=findViewById(R.id.save);

        ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                ad1.setVisibility(View.INVISIBLE);
                v1.setVisibility(View.VISIBLE);
            }
        });
        ad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                ad2.setVisibility(View.INVISIBLE);
                v2.setVisibility(View.VISIBLE);
            }
        });
        ad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                ad3.setVisibility(View.INVISIBLE);
                v3.setVisibility(View.VISIBLE);
            }
        });
        ad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                ad4.setVisibility(View.INVISIBLE);
                v4.setVisibility(View.VISIBLE);
            }
        });
        ad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                ad5.setVisibility(View.INVISIBLE);
                v5.setVisibility(View.VISIBLE);
            }
        });
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1.setText(Cons.get(0));
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv2.setText(Cons.get(1));
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv3.setText(Cons.get(2));
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv4.setText(Cons.get(3));
            }
        });
        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv5.setText(Cons.get(4));
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_2.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        dab= db.getWritableDatabase();
        switch (reqCode) {
            case (REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (numbers.moveToNext()) {
                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                        }
                    }
                    db.add(num,dab);
                    Cons.add(num);
                    break;
                }
        }
    }
}
