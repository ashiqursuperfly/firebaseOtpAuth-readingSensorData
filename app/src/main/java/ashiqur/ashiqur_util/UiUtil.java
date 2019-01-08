package ashiqur.ashiqur_util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.design.widget.Snackbar; //TODO : add in build.gradle(app) implementation 'com.android.support:design:28.0.0'
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ashiqur.androidsensors.R; //TODO: Import your_package_hierarchy.R


public class UiUtil
{

    public static void initSimpleSpinner(Spinner sp,Context context,int stringArrayRes)
    {
        ArrayAdapter<CharSequence> spinnerRideAdapter = ArrayAdapter.createFromResource(context, stringArrayRes, R.layout.support_simple_spinner_dropdown_item);
        spinnerRideAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp.setAdapter(spinnerRideAdapter);

    }
    public static void showToast(Context context,String message,int duration)
    {
        Toast.makeText(context,message,duration).show();
    }
    public static void showSnackBar(View view,String message,int duration)
    {
        if (   Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        Snackbar.make(view,message, Snackbar.LENGTH_LONG).show();

        else showToast(view.getContext(),message,duration);
    }
    public static void showSnackBar(View view,String message,int duration,String btnText,View.OnClickListener task)
    {
        if (   Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        Snackbar.make(view,message, Snackbar.LENGTH_LONG).setAction(btnText,task).show();

        else showToast(view.getContext(),message,duration);
    }
    public static View.OnClickListener DO_NOTHING=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    public static void hideSoftKeyboardFromActivity(Activity activity){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public static void hideSoftKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (NullPointerException nptr){showToast(context,"CANT HIDE KEYBOARD",Toast.LENGTH_SHORT);}
    }

    public static void showAlertDialog(final Activity activity,final String title,final String msg,final String btnText, final int btnTextColor,final int btnBgColor)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, btnText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(btnTextColor));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(activity.getResources().getColor(btnBgColor));
            }
        });
        alertDialog.show();

    }
    /**@param task -Task performed on Button Dialog Click**/
    public static void showAlertDialog(final Activity activity,final String title,final String msg,final String btnText, final int btnTextColor,final int btnBgColor,DialogInterface.OnClickListener task)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, btnText, task);
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(activity.getResources().getColor(btnTextColor));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(activity.getResources().getColor(btnBgColor));
            }
        });
        alertDialog.show();

    }

    public static boolean setSensor(int TYPE, SensorEventListener sensorEventListener, Activity activity)
    {
        Sensor sensor = null;
        SensorManager sensorManager = (SensorManager)activity.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager != null)sensor = sensorManager.getDefaultSensor(TYPE);
        if(sensor == null)
        {
            String str = "Device Doesn't Support This Sensor";
            showToast(activity, str, Toast.LENGTH_LONG);
            return false;
        }
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return true;
    }

    public static void switchFragment(int containerResId, AppCompatActivity activity, Fragment fragment, boolean addToBackStack)
    {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

            ft.add(containerResId, fragment);
            if(addToBackStack)ft.addToBackStack(null);
            ft.commit();

    }
    public static boolean passwordValidation(String password)
    {

        if(password.length()>=8)
        {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            //Pattern eight = Pattern.compile (".{8}");


            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        }
        else
            return false;

    }
}
