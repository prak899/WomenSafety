/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package in.pm.wosafe.Activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jb.dev.progress_indicator.fadeProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.pm.wosafe.Adapter.MasterAdapter;
import in.pm.wosafe.Model.MasterModel;
import in.pm.wosafe.R;

public class Dashboard extends AppCompatActivity {
    DatabaseReference databaseReference;
    List<MasterModel> list = new ArrayList<>();
    private static final int PERMISSIONS_REQUEST = 1;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    com.jb.dev.progress_indicator.fadeProgressBar dotBounceProgressBar;
    TextView EmptyView, HeaderName;

    RadioButton contactsAdd, ProfileSection;

    String number;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        init();
        statusCheck();

        SharedPreferences prfs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        number = prfs.getString("nameKey", "");


        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        seoM();

        final LocationManager manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
            Toast.makeText(this, "GPS is disable!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "GPS is Enable!", Toast.LENGTH_LONG).show();

        contactsAdd.setOnClickListener(v-> startActivity(new Intent(this, EmergencyContactNumber.class)));
        ProfileSection.setOnClickListener(v-> {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLay), "Progress", Snackbar.LENGTH_LONG);snackbar.show();
        });


        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.promoter_recycler);
        dotBounceProgressBar = (fadeProgressBar) findViewById(R.id.dotBounce);
        EmptyView = (TextView) findViewById(R.id.empty_view);


        HeaderName= findViewById(R.id.header_name);

        contactsAdd = findViewById(R.id.imageButton2);
        ProfileSection= findViewById(R.id.imageButton5);


    }
    public void seoM(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts").child(number);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.d("XtrueX", "onDataChange: "+snapshot.getChildrenCount());

                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MasterModel grocery = dataSnapshot.getValue(MasterModel.class);
                    list.add(grocery);
                }
                adapter = new MasterAdapter(list, Dashboard.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    EmptyView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    EmptyView.setVisibility(View.GONE);
                }
                dotBounceProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dotBounceProgressBar.setVisibility(View.GONE);

            }
        });
    }

    int i=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){

            SharedPreferences prfs = getSharedPreferences(EmergencyContactNumber.MyPREFERENCES, Context.MODE_PRIVATE);
            String number = prfs.getString(EmergencyContactNumber.NumberContacts, "");

            i++;
            if(i==1){
                //do something
                requestLocationUpdates(number);
                EmergencyCall(number);

            }
        }
        return true;
    }

    private void EmergencyCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        startActivity(callIntent);
    }
    private void requestLocationUpdates(String number) {
        LocationRequest request = new LocationRequest();
        /*request.setInterval(1000);
        request.setFastestInterval(1000);*/
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    latitude= locationResult.getLastLocation().getLatitude();
                    longitude= locationResult.getLastLocation().getLongitude();

                    if (location != null) {
                        try {
                            sendSMS(number, latitude, longitude);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }, null);
        }
    }


    public void sendSMS(String phoneNo, double lat, double lon) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        String AlertMessage= "AM in danger and am here :-"+address+" right now.\n"+
                state+", "+country+", "+city+", "+postalCode;

        String AlertMessage1= "https://www.google.com/maps/?q="+lat+","+lon;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, AlertMessage1, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}