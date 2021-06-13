package in.pm.wosafe.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.pm.wosafe.Model.Profile;
import in.pm.wosafe.R;

public class OTP extends AppCompatActivity {

    private EditText mEt1;
    private Context mContext;
    Button verify_btn;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    String phoneNo;
    List<Profile> profiles;
    DatabaseReference databaseArtists;

    TextView NumberFourOnly, ResendOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        mAuth = FirebaseAuth.getInstance();

        mEt1 = findViewById(R.id.otp);
        profiles = new ArrayList<>();
        databaseArtists = FirebaseDatabase.getInstance().getReference("Users");


        phoneNo = getIntent().getStringExtra("phoneNo");

        sendVerificationCode(phoneNo);
    }

    public void verify(View view){
        String code = mEt1.getText().toString();
        Log.d("XotpX", code);
        if (code.isEmpty() || code.length() < 6) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.otp_lay), "Please Enter OTP", Snackbar.LENGTH_LONG);
            snackbar.setAction("OK", v1 -> {

            });
            snackbar.show();
            return;
        }

        //verifying the code entered manually
        verifyVerificationCode(code);
    }



    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                mEt1.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTP.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
            addUser();
        }catch (Exception e){
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("XlogX", e.getMessage());
            Snackbar snackbar = Snackbar.make(findViewById(R.id.otp_lay), e.getMessage(), Snackbar.LENGTH_LONG);
            snackbar.setAction("Resend", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendVerificationCode(phoneNo);
                }
            });
            snackbar.show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTP.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(OTP.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.otp_lay), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Call me
                                }
                            });
                            snackbar.show();
                        }

                    }
                });

    }


    private void addUser() {
        String a="0";
        if (a.equals("0")) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("XTAGX", "getInstanceId failed", task.getException());
                                return;
                            }
                            String newPass =  sha256("123456");
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            String id = databaseArtists.push().getKey();
                            Profile profile = new Profile(id, phoneNo, newPass);
                            Profile profile1 = new Profile(token);

                            databaseArtists.child(phoneNo).setValue(profile);
                            databaseArtists.child(phoneNo).child("Token").setValue(profile1);
                            Log.w("XTokenX", token);
                        }
                    });
        } else {
            Toast.makeText(this, "Server error!", Toast.LENGTH_LONG).show();
        }
    }
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}