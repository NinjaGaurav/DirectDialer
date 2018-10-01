package gaurav.bholla.directdialer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

    private final int PICK_CONTACT = 1;
    AutoCompleteContactTextView textView;
    EditText editText2;
    EditText editText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView =  findViewById(R.id.coolStuff);
        editText3 = findViewById(R.id.enter_message);
        editText2 =  findViewById(R.id.enter_name);
        int Permission_All = 1;
//        String[] Permissions = {Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
//        if(!hasPermissions(this, Permissions)){
//            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
//        }
        checkSMSPermission();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_phone:
                textView.setType(AutoCompleteContactTextView.TYPE_OF_DATA.PHONE);
                item.setChecked(true);
                break;
            case R.id.menu_mail:
                item.setChecked(true);
                textView.setType(AutoCompleteContactTextView.TYPE_OF_DATA.EMAIL);
                break;
            case R.id.menu_both:
                item.setChecked(true);
                textView.setType(AutoCompleteContactTextView.TYPE_OF_DATA.BOTH);
                break;
            case R.id.bold:
                if (item.isChecked()){
                    item.setChecked(false);
                    textView.changeStyle(AutoCompleteContactTextView.STYLE.NONE);
                }
                else{
                    item.setChecked(true);
                    textView.changeStyle(AutoCompleteContactTextView.STYLE.BOLD);
                }
        }
        return true;
    }
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkSMSPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) MainActivity.this, Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("SMS Permission is required to send message");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                break;
        }
    }
//    public boolean hasPermissions(Context context, String... permissions){
//
//        if(context!=null && permissions!=null){
//            for(String permission: permissions){
//                if(ActivityCompat.checkSelfPermission(context, permission)!=PackageManager.PERMISSION_GRANTED){
//                    return  false;
//                }
//            }
//        }
//        return true;
//    }
 /*   public void callContacts(View v)
    {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        switch (reqCode) {
            case (PICK_CONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {


                        String id =c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                                    null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex("data1"));

                            Intent callIntent = new Intent(Intent.ACTION_CALL);

                            callIntent.setData(Uri.parse("tel:"+cNumber));

                            startActivity(callIntent);

                        }

                    }
                }
                break;
        }
    }
    */
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
