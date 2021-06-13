package in.pm.wosafe.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.pm.wosafe.Model.ContactModel;
import in.pm.wosafe.Model.Profile;
import in.pm.wosafe.R;

import static in.pm.wosafe.Class.Constants.UserNumber;

public class EmergencyContactNumber extends AppCompatActivity {
    private ImageButton completeButton;
    Spinner spinner;
    String[] categorydrop = {"Select Category", "Demo1", "Demo2", "Demo3"};
    private TextInputEditText Name, Number;
    String text, number;


    List<ContactModel> contactModels;
    DatabaseReference dbContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_number);
        init();

        SharedPreferences prfs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        number = prfs.getString("nameKey", "");

        contactModels = new ArrayList<>();
        dbContact = FirebaseDatabase.getInstance().getReference("Contacts");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categorydrop);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        completeButton.setOnClickListener(v-> {

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
                            ContactModel contactModel = new ContactModel(Name.getText().toString(), text, Number.getText().toString(), date, id);


                            dbContact.child(number).child(Number.getText().toString()).setValue(contactModel);

                        }
                    });
        } else {
            Toast.makeText(this, "Server error!", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        spinner= findViewById(R.id.spinner);
        completeButton= findViewById(R.id.completeButton);

        //TextInputEditText Bindings
        Name= findViewById(R.id.address);
        Number= findViewById(R.id.pin);

    }
}