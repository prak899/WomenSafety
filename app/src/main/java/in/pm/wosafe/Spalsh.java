package in.pm.wosafe;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import in.pm.wosafe.Activity.RegisterActivity;
//Here the pull request

public class Spalsh extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(Spalsh.this, Dashboard.class));
                    Spalsh.this.finish();
                } else {
                    startActivity(new Intent(Spalsh.this, RegisterActivity.class));
                }
            }
        }, SPLASH_DISPLAY_LENGTH);


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