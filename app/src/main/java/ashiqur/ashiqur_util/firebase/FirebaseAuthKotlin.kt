package ashiqur.ashiqur_util.firebase

import android.app.Activity
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.auth.AuthResult
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

object FirebaseAuthUtil {
    var auth: FirebaseAuth

    fun sendVerificationCode(
            mobile: String,
            callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        Log.d("SMS being sent to:", mobile)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile.trim { it <= ' ' },
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                callbacks
        )
    }

    /**
     * @param verificationId : should be obtained from the :
     * new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
     * public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
     * super.onCodeSent(s, forceResendingToken);
     * //storing the verification id that is sent to the user
     * @param code  verificationId = s;
     * Log.wtf(TAG,"Inside On CodeSent :"+mVerificationId);
     * }
     * };
     *
     *
     *
     */
    fun verifyVerificationCode(
            code: String,
            verificationId: String
    ): PhoneAuthCredential { //creating the credential
        //signing the user
        return PhoneAuthProvider.getCredential(verificationId, code)
    }

    fun signUpWithPhoneAuthCredential(
            activity: Activity,
            credential: PhoneAuthCredential,
            onCompleteListener: OnCompleteListener<AuthResult?>
    ) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(activity, onCompleteListener)
    }

    init {
        auth = FirebaseAuth.getInstance()
    }
}