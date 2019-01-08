package ashiqur.ashiqur_util.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static ashiqur.ashiqur_util.UiUtil.showToast;


/** This Class Provides useful Methods For Using Google Firebase.
 *
 * **/
public class FireBaseDatabaseUtil {
    private static final String TAG = "FIREBASEUTIL";
    private DatabaseReference root;
    private Context context;

    public DatabaseReference getRoot() {
        return root;
    }


    /**Constructor That Takes Only the Application's Context
     * Note: This Class Initializes the firebase root to the outermost node in the database hierarchy that the application is using,
     * However you can always change the root to your necessity using setFireBaseDatabaseRoot() method.
     * e.g-setFireBaseDatabaseRoot(FirebaseDatabase.getInstance().getReference().child("Some key string"))
     * or, more easily-setFireBaseDatabaseRoot(getReferenceToKey("Some Key String"));
     * **/
    public FireBaseDatabaseUtil(Context context) {
        root = FirebaseDatabase.getInstance().getReference();
        this.context = context;
    }


    public void setFireBaseDatabaseRoot(DatabaseReference fireBaseDatabase) {
        this.root = fireBaseDatabase;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void createChild(String child)
    {
        root.child(child);
    }
    public void createChild(String child,Object value)
    {
        root.child(child).setValue(value);
    }
    public static DatabaseReference getReferenceToKey(DatabaseReference root, String key)
    {
        return root.child(key);
    }
    // Methods For Writing Data
    // METHOD 1:Checks for the unique primary key,adds the new value only if the primary key doesnt already exist
    public void checkIfPrimaryKeyExistsAndAddToFirebase(DatabaseReference searchStartNode, final String primaryKey, final Object valueToBeStored, final boolean [] isTaskDone)
    {

        DatabaseReference userNameRef = searchStartNode.child(primaryKey);

        ValueEventListener eventListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new user
                    isTaskDone[0]=true;
                    DatabaseReference newUserRef= root.child(primaryKey.trim());
                    addDataToFirebase(newUserRef,valueToBeStored,new boolean[1]);

                }
                else
                {
                    isTaskDone[0]=true;
                    showToast(context,"Primary Key Already Exists",Toast.LENGTH_SHORT);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }

        };

        userNameRef.addListenerForSingleValueEvent(eventListener);


    }
    // METHOD 2: Sets the value of the DatabaseReference root to Object 'value'
    public void addDataToFirebase(DatabaseReference ref, Object value, final boolean [] isSuccessful)
    {
        ref.setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    isSuccessful[0]=true;
                     //   Button btnContinue = (Button) ((Activity) context).findViewById(R.id.btn_continue);
                       // if (btnContinue != null) btnContinue.setText(R.string.Login);
                        showToast(context, "User Info Added Successfully", Toast.LENGTH_SHORT);
                }
                else
                {
                    showToast(context,"Could not Add User Info,please Check your Connection!",Toast.LENGTH_SHORT);
                }
            }
        });

    }
    // METHOD 3: Creates a Child named String 'key' and sets it's value to Object 'value'
    public void addDataToFirebaseWithAUniqueRandomKey(DatabaseReference ref, Object value, final boolean [] isSuccessful)
    {
        ref.push().setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    isSuccessful[0] = true;
                    showToast(context,"User Info Added Successfully",Toast.LENGTH_SHORT);
                }
                else
                {
                    showToast(context,"Could not Add User Info,please Check your Connection!",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void addDataToFirebase(DatabaseReference ref, String key, Object value, final boolean [] isSuccessful){
        //TODO: root.push(value); creates a child with a random key with the value

        ref.child(key).setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    isSuccessful[0]=true;
                    showToast(context,"User Info Added Successfully",Toast.LENGTH_SHORT);
                }
                else
                {
                    showToast(context,"Could not Add User Info,please Check your Connection!",Toast.LENGTH_SHORT);
                }
            }
        });
    }
    // Methods For Reading Data
    public void retrieveDataFromFirebase(DatabaseReference ref, final String[] data)
    {

        Log.wtf(TAG,"Reading Data from Ref :"+ref);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data[0]=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public <T> void retrieveAllDataFromAkey(DatabaseReference ref, final  Class<T> T, final ArrayList<T> data) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            System.out.println("VALUE FOUND :"+snapshot.getValue(T));
                             data.add(snapshot.getValue(T));
                        }
                        System.out.println("DATA SIZE"+data.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public <T> void retrieveDataFromFirebase(DatabaseReference ref, final T data[])
    {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data[0]= (T) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static String encodeForFirebaseKey(String s) {
        return s
                .replace("_", "__")
                .replace(".", "_P")
                .replace("$", "_D")
                .replace("#", "_H")
                .replace("[", "_O")
                .replace("]", "_C")
                .replace("/", "_S")
                ;
    }
    public static String decodeFromFirebaseKey(String s) {
        int i = 0;
        int ni;
        String res = "";
        while ((ni = s.indexOf("_", i)) != -1) {
            res += s.substring(i, ni);
            if (ni + 1 < s.length()) {
                char nc = s.charAt(ni + 1);
                if (nc == '_') {
                    res += '_';
                } else if (nc == 'P') {
                    res += '.';
                } else if (nc == 'D') {
                    res += '$';
                } else if (nc == 'H') {
                    res += '#';
                } else if (nc == 'O') {
                    res += '[';
                } else if (nc == 'C') {
                    res += ']';
                } else if (nc == 'S') {
                    res += '/';
                } else {
                    // this case is due to bad encoding
                }
                i = ni + 2;
            } else {
                // this case is due to bad encoding
                break;
            }
        }
        res += s.substring(i);
        return res;
    }


}
