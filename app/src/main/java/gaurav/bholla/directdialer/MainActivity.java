package gaurav.bholla.directdialer;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Menu;
import android.telephony.SmsManager;


import com.gauravbholla.dd.AutoCompleteContactTextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //textView=editText

    String[] Permissions = {Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
    int Permission_All = 1;
    AutoCompleteContactTextView textView;
    EditText editText2;
    EditText editText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(!hasPermissions(this, Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);

        }
        else    //has all
        {
            setContentView(R.layout.activity_main);
            textView = findViewById(R.id.coolStuff);
            editText3 = findViewById(R.id.enter_message);
            editText2 = findViewById(R.id.enter_name);
        }

    }

    @Override
    protected void onStart() {

        super.onStart();

        if(!hasPermissions(this, Permissions)){
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }

    }


@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == Permission_All) {

        if (grantResults.length > 0) {

            boolean A = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean B = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean C = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            boolean D = grantResults[3] == PackageManager.PERMISSION_GRANTED;
            boolean E = grantResults[4] == PackageManager.PERMISSION_GRANTED;
            if (A&&B&&C&&D&&E)
            {
                setContentView(R.layout.activity_main);
                textView = findViewById(R.id.coolStuff);
                editText3 = findViewById(R.id.enter_message);
                editText2 = findViewById(R.id.enter_name);
            }
            else
            {
                Toast.makeText(MainActivity.this,"Please Grant All Permissions!",Toast.LENGTH_LONG).show();
            }
        }

    }

}


    public boolean hasPermissions(Context context, String... permissions){

        if(context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }

    public void clear(View view)
    {
        textView.setText("");
        editText2.setText("");
        editText3.setText("");

    }
    public void phoneCall(View view) {

        String number = textView.getText().toString();
        if(number.length()<1)
            Toast.makeText(this, "Enter Phone Number!", Toast.LENGTH_LONG).show();
        else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);

            callIntent.setData(Uri.parse("tel:" + number));

            startActivity(callIntent);
        }
    }

    public void autoSMS(View view)
    {
        SmsManager sm = SmsManager.getDefault();
        String number = textView.getText().toString();
        String msg = editText3.getText().toString();

        if(msg.length()<1 && number.length()<1)
            Toast.makeText(this, "Enter Phone Number & Message!", Toast.LENGTH_LONG).show();
        else if(msg.length()<1)
            Toast.makeText(this, "Enter Message!", Toast.LENGTH_LONG).show();
        else if(number.length()<1)
            Toast.makeText(this, "Enter Phone Number!", Toast.LENGTH_LONG).show();
        else{
            sm.sendTextMessage(number, null, msg, null, null);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_LONG).show();
        }
    }
    public void addAsContactAutomatic(View view) {
        String displayName = editText2.getText().toString();
        String mobileNumber = textView.getText().toString();
        Context context = this;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        // Names
        if(displayName.length()<1 || mobileNumber.length()<1)
        {
            if(displayName.length()<1 && mobileNumber.length()<1)
                Toast.makeText(context, "Enter Name & Phone Number!", Toast.LENGTH_LONG).show();
            else if(displayName.length()<1)
                Toast.makeText(context, "Enter Name!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "Enter Phone Number!", Toast.LENGTH_LONG).show();

        }
        else {
            if (displayName != null) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                displayName).build());
            }

            // Mobile Number
            if (mobileNumber != null) {
                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
            }


            // Asking the Contact provider to create a new contact
            try {
                context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(context, "Contact " + displayName + " added.", Toast.LENGTH_LONG).show();
        }
    }

}
