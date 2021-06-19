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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import in.pm.wosafe.Activity.Dashboard;
import in.pm.wosafe.Activity.RegisterActivity;

import static java.lang.Thread.sleep;
//Here the pull request

public class Spalsh extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView textView= findViewById(R.id.tv_splash_app_title);
        TextView textView1= findViewById(R.id.tv_splash_app_version);

        textView1.setText("V 1.0.0");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        firebaseAuth = FirebaseAuth.getInstance();

/*
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
*/

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            int count = 0;

            @Override
            public void run() {
                count++;

                if (count == 1)
                {
                    textView.setText("Loading.");
                }
                else if (count == 2)
                {
                    textView.setText("Loading..");
                }
                else if (count == 3)
                {
                    textView.setText("Loading...");
                }

                if (count == 3)
                    Load();

                handler.postDelayed(this, 2 * 1000);
            }
        };
        handler.postDelayed(runnable, 1 * 1000);

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

    public void Load(){
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(Spalsh.this, Dashboard.class));
            Spalsh.this.finish();
        } else {
            startActivity(new Intent(Spalsh.this, RegisterActivity.class));
        }
    }

}