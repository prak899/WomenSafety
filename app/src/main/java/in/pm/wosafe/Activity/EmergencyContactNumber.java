package in.pm.wosafe.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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


    FloatingActionButton FabHome, FabDone;
    List<ContactModel> contactModels;
    DatabaseReference dbContact;

    CheckBox ImporantCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_number);
        init();

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

            addUser();
        });


    }

    private void addUser() {
        String a="0";
        Date date = new Date();
        if (a.equals("0")) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("XTAGX", "getInstanceId failed", task.getException());
                                return;
                            }


                            String id = dbContact.push().getKey();
                            ContactModel contactModel = null;

                            if(ImporantCon.isChecked()){
                                contactModel = new ContactModel(Name.getText().toString(), spinner.getSelectedItem().toString(), Number.getText().toString(), date, id, true);
                            } else {
                                contactModel = new ContactModel(Name.getText().toString(), spinner.getSelectedItem().toString(), Number.getText().toString(), date, id, false);

                            }

                            dbContact.child(number).child(Number.getText().toString()).setValue(contactModel);

                            Toast.makeText(EmergencyContactNumber.this, "Contact added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EmergencyContactNumber.this, Dashboard.class));

                        }
                    });
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

    }
}