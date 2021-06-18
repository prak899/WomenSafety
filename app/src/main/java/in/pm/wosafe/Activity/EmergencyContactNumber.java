package in.pm.wosafe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.pm.wosafe.Model.ContactModel;

import in.pm.wosafe.R;

public class EmergencyContactNumber extends AppCompatActivity {
    Spinner spinner;
    String[] categorydrop = {"Select Category", "Spouses", "Parents", "Grandparents", "Brothers", "Sisters", "Daughters", "Sons"};
    private TextInputEditText Name, Number;
    String number;
    SharedPreferences sharedpreferences;

    FloatingActionButton FabHome, FabDone;
    List<ContactModel> contactModels;
    DatabaseReference dbContact;

    CheckBox ImporantCon;

    public static final String MyPREFERENCES = "NumberStorePreef" ;
    public static final String NumberContacts = "number";

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_number);
        init();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        SharedPreferences prfs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        number = prfs.getString("nameKey", "");

        contactModels = new ArrayList<>();
        dbContact = FirebaseDatabase.getInstance().getReference("Contacts");

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, categorydrop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        FabHome.setOnClickListener(v-> {
            startActivity(new Intent(this, Dashboard.class));
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FabDone.setOnClickListener(v-> {

            String name = Name.getText().toString();
            String numberContact = Number.getText().toString();

            if (name.isEmpty()){
                Toast.makeText(this, "Check name", Toast.LENGTH_SHORT).show();
            }else if (numberContact.isEmpty()){
                Toast.makeText(this, "Check number", Toast.LENGTH_SHORT).show();
            }else {
                addUser(name, numberContact);
            }
        });


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);
            }
        }

    }

    private void addUser(String name, String numberContact) {

        Date date = new Date();


        if (!name.isEmpty() || numberContact.isEmpty()) {

            String id = dbContact.push().getKey();
            ContactModel contactModel = null;

            if (ImporantCon.isChecked()) {
                contactModel = new ContactModel(name, spinner.getSelectedItem().toString(), numberContact, date, id, true);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(NumberContacts, numberContact);
                editor.commit();
            } else {
                contactModel = new ContactModel(name, spinner.getSelectedItem().toString(), numberContact, date, id, false);

            }

            dbContact.child(number).child(Number.getText().toString()).setValue(contactModel);
            Toast.makeText(EmergencyContactNumber.this, "Contact added", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EmergencyContactNumber.this, Dashboard.class));

            Name.setText(null);
            Number.setText(null);

        } else {
            Toast.makeText(this, "Server error!", Toast.LENGTH_LONG).show();

        }
    }

    private void init() {
        spinner= findViewById(R.id.spinner);
        FabHome= findViewById(R.id.fab1);

        //TextInputEditText Bindings
        Name= findViewById(R.id.address);
        Number= findViewById(R.id.pin);

        FabDone= findViewById(R.id.fab);

        ImporantCon = (CheckBox)findViewById(R.id.important);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }

        }
    }
}