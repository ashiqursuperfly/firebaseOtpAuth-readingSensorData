package ashiqur.ashiqur_util.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FirebaseAuthUtil {
    FirebaseAuth auth;

    public FirebaseAuthUtil() {
        this.auth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public void sendVerificationCode(String mobile, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        Log.d("SMS being sent to:", mobile);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile.trim(),
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks);
    }

    /**
     * @param verificationId : should be obtained from the :
     *    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
     *         public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
     *             super.onCodeSent(s, forceResendingToken);
     *             //storing the verification id that is sent to the user
     * @param code  verificationId = s;
     *             Log.wtf(TAG,"Inside On CodeSent :"+mVerificationId);
     *         }
     *     };
     *
     *
     *
     * */
    public PhoneAuthCredential verifyVerificationCode(String code, String verificationId) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        //signing the user
        return credential;
    }
    public void signUpWithPhoneAuthCredential(Activity activity, PhoneAuthCredential credential, OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(activity,  onCompleteListener);
    }
}
