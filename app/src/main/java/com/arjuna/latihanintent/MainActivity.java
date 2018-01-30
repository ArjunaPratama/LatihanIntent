package com.arjuna.latihanintent;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnSend;
    Spinner spSend;
    EditText etWeb, etEmail, etHp, etSMS, etIsi;
    String Media;
    String[] Send = new String[]{
            "Email", "Telephone", "Web", "SMS",
    };
    String Email;
    String Subject;
    String Telp;
    String Pesan;
    String web;
    //tambahkan permission
    private static final int PERMISION_REQUEST_CODE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSMS = (EditText) findViewById(R.id.etSMS);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etHp = (EditText) findViewById(R.id.etHp);
        etWeb = (EditText) findViewById(R.id.etWeb);
        etIsi = (EditText) findViewById(R.id.etIsi);
        spSend = (Spinner) findViewById(R.id.spSend);
        btnSend= (Button) findViewById(R.id.btnSend);


        spSend = (Spinner) findViewById(R.id.spSend);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Send);
        spSend.setAdapter(adapter);
        spSend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Media = adapterView.getItemAtPosition(i).toString();

                if (Media == "Email"){
                    etHp.setVisibility(etHp.INVISIBLE);
                    etSMS.setVisibility(etSMS.INVISIBLE);
                    etWeb.setVisibility(etWeb.INVISIBLE);
                    etEmail.setVisibility(etEmail.VISIBLE);
                    etIsi.setVisibility(etIsi.VISIBLE);
                }else if (Media == "Telephone"){
                    etHp.setVisibility(etHp.VISIBLE);
                    etSMS.setVisibility(etSMS.INVISIBLE);
                    etWeb.setVisibility(etWeb.INVISIBLE);
                    etEmail.setVisibility(etEmail.INVISIBLE);
                    etIsi.setVisibility(etIsi.INVISIBLE);
                }else if (Media == "SMS"){
                    etHp.setVisibility(etHp.VISIBLE);
                    etSMS.setVisibility(etSMS.VISIBLE);
                    etWeb.setVisibility(etWeb.INVISIBLE);
                    etEmail.setVisibility(etEmail.INVISIBLE);
                    etIsi.setVisibility(etIsi.INVISIBLE);
                }else if (Media == "Web"){
                    etHp.setVisibility(etHp.INVISIBLE);
                    etSMS.setVisibility(etSMS.INVISIBLE);
                    etWeb.setVisibility(etWeb.VISIBLE);
                    etEmail.setVisibility(etEmail.INVISIBLE);
                    etIsi.setVisibility(etIsi.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Media == "Email"){
                    String Email = etEmail.getText().toString();
                    String Subject = etEmail.getText().toString();

                    ///intent email
                    Intent nEmail = new Intent(Intent.ACTION_SEND);
                    nEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{Email});
                    nEmail.putExtra(Intent.EXTRA_SUBJECT, Subject);
                    nEmail.putExtra(Intent.EXTRA_TEXT, "Selamat Mencoba");

                    //Format kode u perngiriman emial
                    nEmail.setType("message/rfc822");
                    startActivity(Intent.createChooser(nEmail, "Select your email"));
                }else if (Media == "Telephone"){
                    String Telp = etHp.getText().toString();

                    //aksi ketika btnPhone diklik

                    //intent Implicit ke no Telp
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + Telp));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }else if (Media == "SMS"){
                    String Pesan = etSMS.getText().toString();

                    //android permission SDK 6.0
                    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){

                        if (checkSelfPermission(Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_DENIED) {
                            Log.d("Permision", "permision denided to SEND SMS - requesting it ");
                            String[] permisions = {Manifest.permission.SEND_SMS};

                            requestPermissions(permisions, PERMISION_REQUEST_CODE);
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    //memanggil liblary sms ke SMSManager dan memanggil string dataIsiSMS
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(Telp, null, Pesan, pi, null);

                    Toast.makeText(getApplicationContext(), "SMS Berhasil di KIRIM", Toast.LENGTH_LONG).show();

                }else if (Media == "Web"){

                    String web = etWeb.getText().toString();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                    startActivity(intent);
                }
            }
        });
    }
}
