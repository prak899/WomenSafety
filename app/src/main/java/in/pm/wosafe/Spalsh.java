package in.pm.wosafe;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import in.pm.wosafe.Activity.Dashboard;
import in.pm.wosafe.Activity.LoginActivity;
import in.pm.wosafe.Activity.RegisterActivity;
import in.pm.wosafe.Class.ScreenOnOffReceiver;

import static in.pm.wosafe.Class.ScreenOnOffReceiver.SCREEN_TOGGLE_TAG;

public class Spalsh extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ScreenOnOffReceiver screenOnOffReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction("android.intent.action.KEYCODE_VOLUME_DOWN");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");

        // Set broadcast receiver priority.
        intentFilter.setPriority(100);

        // Create a network change broadcast receiver.
        screenOnOffReceiver = new ScreenOnOffReceiver();

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(screenOnOffReceiver, intentFilter);

        Log.d(SCREEN_TOGGLE_TAG, "onCreate: screenOnOffReceiver is registered.");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(Spalsh.this, Dashboard.class));
            Spalsh.this.finish();
        } else {
            startActivity(new Intent(this, RegisterActivity.class));
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister screenOnOffReceiver when destroy.
        if(screenOnOffReceiver!=null)
        {
            unregisterReceiver(screenOnOffReceiver);
            Log.d(SCREEN_TOGGLE_TAG, "onDestroy: screenOnOffReceiver is unregistered.");
        }
    }
    public void login(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void getStarted(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}