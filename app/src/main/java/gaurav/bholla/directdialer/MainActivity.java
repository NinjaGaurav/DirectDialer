package gaurav.bholla.directdialer;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gauravbholla.dd.AutoCompleteContactTextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //textView=editText


    private final int MY_PERMISSIONS = 124;
    AutoCompleteContactTextView textView;
    EditText editText2;
    EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beginPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.coolStuff);
        editText3 = findViewById(R.id.enter_message);
        editText2 = findViewById(R.id.enter_name);
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
        switch (id) {
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
                if (item.isChecked()) {
                    item.setChecked(false);
                    textView.changeStyle(AutoCompleteContactTextView.STYLE.NONE);
                } else {
                    item.setChecked(true);
                    textView.changeStyle(AutoCompleteContactTextView.STYLE.BOLD);
                }
        }
        return true;
    }

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
    public void clear(View view) {
        textView.setText("");
        editText2.setText("");
        editText3.setText("");

    }

    public void phoneCall(View view) {

        String number = textView.getText().toString();
        if (number.length() < 1)
            Toast.makeText(this, "Enter Phone Number!", Toast.LENGTH_LONG).show();
        else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);

            callIntent.setData(Uri.parse("tel:" + number));

            // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            startActivity(callIntent);
                return;
           // }

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

    public void beginPermission(){
        if ((ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED)&& (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)) {

            Log.i("1", "Permission is not granted");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_CONTACTS) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) && (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE))&& (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS))) {
                Log.i("REQUEST","Requesting permission....");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS },
                        MY_PERMISSIONS);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CONTACTS },
                        MY_PERMISSIONS);

            }
        } else {
            Log.i("1","Permission Granted");
            //TODO:Call Method which requires permission

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("1", "Permission is granted");
                    //TODO: Imlement Permission Method

                } else {
                    Log.i("1", "Permission is again not granted");

                    Snackbar mySnackbar = Snackbar.make(findViewById(android.R.id.content),
                            "Please ennable the permissions", Snackbar.LENGTH_SHORT);
                    mySnackbar.setAction("ENABLE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));

                        }
                    });
                    mySnackbar.show();

                }
                return;
            }
        }
    }

}
