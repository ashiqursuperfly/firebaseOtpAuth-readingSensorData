package ashiqur.androidsensors.module1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import ashiqur.androidsensors.MainActivity;
import ashiqur.androidsensors.R;
import ashiqur.ashiqur_util.firebase.FireBaseDatabaseUtil;
import ashiqur.ashiqur_util.UiUtil;
import ashiqur.ashiqur_util.firebase.FirebaseAuthUtil;

import static ashiqur.ashiqur_util.UiUtil.showSnackBar;
import static ashiqur.ashiqur_util.UiUtil.showToast;

public class LoginActivity extends AppCompatActivity {

    //XML variables
    private EditText editTextCellPhone,editTextEnterVerificationCode,editTextCountryCode;
    //JAVA Variables
    private Button buttonGetVerCode,buttonVerify;
    private ProgressBar progressBarVerification;
    private View.OnClickListener handleClick;
    private String mVerificationId;
    private FireBaseDatabaseUtil firebaseUtil;
    private FirebaseAuthUtil firebaseAuthUtil;

    private String TAG = "Login-Activity";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.wtf(TAG, currentUser+"");
        if(currentUser != null)
        {
         Intent i = new Intent(LoginActivity.this, MainActivity.class);
         startActivity(i);
         finish();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        initializeXmlVariables();
        initializeJavaVariables();

    }

    private void initializeJavaVariables() {

        firebaseUtil = new FireBaseDatabaseUtil(getApplicationContext());
        firebaseAuthUtil = new FirebaseAuthUtil();
        handleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.btn_verify: {
                        String code = editTextEnterVerificationCode.getText().toString().trim();
                        if (code.isEmpty() || code.length() < 6) {
                            editTextEnterVerificationCode.setError("Enter valid code");
                            editTextEnterVerificationCode.requestFocus();
                            return;
                        }
                        progressBarVerification.setVisibility(View.VISIBLE);

                        firebaseAuthUtil.signUpWithPhoneAuthCredential(LoginActivity.this, firebaseAuthUtil.verifyVerificationCode(code, mVerificationId), onCompleteListener);
                        break;
                    }
                    case R.id.btn_getverification_code:
                    {
                        final String phoneNo = "+"+editTextCountryCode.getText().toString().trim() + editTextCellPhone.getText().toString().trim();
                        UiUtil.showAlertDialog(LoginActivity.this,
                                "Please Confirm this is your Phone Number", "You'll Receive a 6 digit Code via sms at :\n" + phoneNo, "CONFIRM",
                                R.color.colorAccent, R.color.colorPrimaryDark, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //sendVerificationCode
                                        firebaseAuthUtil.sendVerificationCode(phoneNo, mCallbacks);
                                    }
                                });
                        break;
                    }
                }
            }
        };
        buttonGetVerCode.setOnClickListener(handleClick);
        buttonVerify.setOnClickListener(handleClick);
    }

    private void initializeXmlVariables() {
        editTextCountryCode = findViewById(R.id.et_countrycode_signup);
        editTextCellPhone = findViewById(R.id.et_phone_signup);
        editTextEnterVerificationCode = findViewById(R.id.et_verification_code);

        buttonGetVerCode = findViewById(R.id.btn_getverification_code);
        buttonVerify = findViewById(R.id.btn_verify);

        progressBarVerification = findViewById(R.id.progressbar_fragment_signup);

    }
    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            progressBarVerification.setVisibility(View.INVISIBLE);
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextEnterVerificationCode.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            showSnackBar(findViewById(android.R.id.content), e.getMessage(), Toast.LENGTH_LONG);
            progressBarVerification.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s) {
            showSnackBar(findViewById(android.R.id.content), "Timeout ! Please Check Your Connection", Snackbar.LENGTH_LONG,"DISMISS",UiUtil.DO_NOTHING);
            progressBarVerification.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //storing the verification id that is sent to the user
            progressBarVerification.setVisibility(View.VISIBLE);
            mVerificationId = s;
            Log.wtf(TAG,"Inside On CodeSent :"+mVerificationId);
        }
    };

    OnCompleteListener<AuthResult> onCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                //verification successful we will start the profile activity
                progressBarVerification.setVisibility(View.INVISIBLE);
                showToast(getApplicationContext(),"Verification Successful",Toast.LENGTH_LONG);
                buttonVerify.setEnabled(false);
                //TODO: Switch To Signup Fragment 2 for More Data in Signup
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                //verification unsuccessful.. display an error message
                progressBarVerification.setVisibility(View.INVISIBLE);
                String message = "Something is wrong, we will fix it soon...";
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    message = "Invalid code entered...";
                }
                showToast(getApplicationContext(), message, Toast.LENGTH_LONG);

            }
        }
    };


}
