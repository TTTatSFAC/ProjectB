package com.example.projecta;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecta.database.UserDB;
import com.example.projecta.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    boolean testing = true;

    TextView textView;
    Button btnLogin, btnOTP;
    EditText editTel, editValid;
    ProgressBar progressBar;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    SharedPreferences sharedPreferences;

    private void initView() {
        editTel = findViewById(R.id.editTextPhone);
        editValid = findViewById(R.id.editTextNumber);
        textView = findViewById(R.id.tv_app);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setEnabled(false);
        btnOTP = findViewById(R.id.btn_otp);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("登入/註冊");
        setSupportActionBar(toolbar);
    }

    private void setOnClick() {
        btnLogin.setOnClickListener(v->{
            String otpCode = editValid.getText().toString();
            if (TextUtils.isEmpty(otpCode)) {
                Toast.makeText(this, "請輸入驗證碼", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOTP(otpCode);
        });

        btnOTP.setOnClickListener(v->{
            String phoneNumber = editTel.getText().toString();
            if (TextUtils.isEmpty(phoneNumber)) {
                Toast.makeText(this, "請輸入電話號碼", Toast.LENGTH_SHORT).show();
                return;
            }
            sendOTP(phoneNumber);
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+852" + phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(MainActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String otpCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
        signInByCredential(credential);
    }

    private void signInByCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "成功登入", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "getUserInfo: authid = " + mAuth.getCurrentUser().getUid());
                            goNextPage(editTel.getText().toString());
                        }
                    }
                });
    }

    private void initCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);
                final String otpCode = credential.getSmsCode();
                if (otpCode != null) {
                    verifyOTP(otpCode);
                }

//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e);
                Toast.makeText(MainActivity.this, "登入失敗", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }


            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                super.onCodeSent(verificationId, token);
                Log.d("TAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                Toast.makeText(MainActivity.this, "已發送驗證碼", Toast.LENGTH_SHORT).show();
                btnLogin.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        if (testing)
            mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);

        initView();
        setOnClick();
        initCallbacks();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
//            startActivity(new Intent(MainActivity.this, SexActivity.class));
            finish();
        }
    }

    private void goNextPage(String phoneNumber) {
        Intent intent = new Intent();
        User user = new User();
        UserDB userDB = new UserDB();
        DatabaseReference userRef = userDB.getDatabase().child("users").getRef();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exist = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User mUser = dataSnapshot.getValue(User.class);
                    if (mUser == null)
                        return;
                    if (mUser.getPhoneNumber().equals(phoneNumber)) {
                        sharedPreferences.edit().putString("UUID", mUser.getUserId()).apply();
                        intent.putExtra("UUID", mUser.getUserId());
                        intent.setClass(MainActivity.this, MenuActivity.class);
                        Log.e(TAG, "goNextPage: USER EXIST, userId = " + mUser.getUserId());
                        exist = true;
                    }
                }
                if (!exist) {
                    user.generateUUID();
                    userDB.writeNewUserPhoneNumber(user.getUserId(), phoneNumber);
                    sharedPreferences.edit().putString("UUID", user.getUserId()).apply();
                    intent.putExtra("UUID", user.getUserId());
                    intent.setClass(MainActivity.this, SexActivity.class);
                    Log.e(TAG, "goNextPage: NEW USER, userId = " + user.getUserId());
                }
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


