package in.pm.wosafe.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.textfield.TextInputEditText;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import in.pm.wosafe.R;

import static in.pm.wosafe.Class.Constants.MyPREFERENCES;
import static in.pm.wosafe.Class.Constants.UserNumber;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText User_Number;
    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String NUm = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void init() {
        User_Number= findViewById(R.id.number);

    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
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

    public void signup(View v){
        String phoneNo = User_Number.getText().toString();

        Intent intent = new Intent(getApplicationContext(),OTP.class);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(NUm, phoneNo);
        editor.putString(Phone, "ph");
        editor.putString(Email, "e");
        editor.commit();

        intent.putExtra("phoneNo",phoneNo);

        startActivity(intent);
    }
}