package in.pm.wosafe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jb.dev.progress_indicator.dotBounceProgressBar;
import com.jb.dev.progress_indicator.fadeProgressBar;

import java.util.ArrayList;
import java.util.List;

import in.pm.wosafe.Adapter.MasterAdapter;
import in.pm.wosafe.Model.MasterModel;
import in.pm.wosafe.R;

import static in.pm.wosafe.Class.Constants.UserNumber;

public class Dashboard extends AppCompatActivity {
    DatabaseReference databaseReference;
    List<MasterModel> list = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    com.jb.dev.progress_indicator.fadeProgressBar dotBounceProgressBar;
    TextView EmptyView, HeaderName;

    RadioButton contactsAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        init();

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        seoM();

        contactsAdd.setOnClickListener(v-> {
            startActivity(new Intent(this, EmergencyContactNumber.class));
        });
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.promoter_recycler);
        dotBounceProgressBar = (fadeProgressBar) findViewById(R.id.dotBounce);
        EmptyView = (TextView) findViewById(R.id.empty_view);


        HeaderName= findViewById(R.id.header_name);
        contactsAdd = findViewById(R.id.imageButton2);
    }
    public void seoM(){
        SharedPreferences prfs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String number = prfs.getString("nameKey", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts").child(number);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

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
}