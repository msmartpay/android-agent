package in.msmartpayagent.helpAndSupport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import in.msmartpayagent.R;
import in.msmartpayagent.utility.BaseActivity;
import in.msmartpayagent.utility.Keys;
import in.msmartpayagent.utility.Util;

public class HelpSupportActivity extends BaseActivity {
    private TextView support,confirm_contact;
    private ImageView call;
    private TextView website_link;
    private String supportNumber, correctno,email,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_support_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Support");

        supportNumber = Util.LoadPrefData(getApplicationContext(), Keys.SUPPORT_1);
        time = Util.LoadPrefData(getApplicationContext(), Keys.TIME);
        email =Util.LoadPrefData(getApplicationContext(), Keys.SUPPORT_EMAIL);

        call = (ImageView) findViewById(R.id.id_support_callbtn);
        website_link=(TextView)findViewById(R.id.website_link);
        support = (TextView) findViewById(R.id.help_contact);
        confirm_contact = (TextView) findViewById(R.id.confirm_contact);
        confirm_contact.setText(email);
        support.setText(supportNumber + "\nTiming ("+time+")");

        website_link.setOnClickListener(v -> {
            Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url)));
            Intent chooser = Intent.createChooser(in,"Select Browser");
            startActivity(chooser);
        });

        call.setOnClickListener(v -> {
           /* if (null != supportNumber && supportNumber.length() >= 10) {
                Service sc = new Service();
                Log.d("Help  supportnumber1", "" + supportNumber);
                correctno = sc.validateMobileNumber(supportNumber);
                Log.d("Help correct number", "" + correctno);
                if (null != correctno) {
                    if (isPermissionGranted()) {
                        Log.d("Call Permission", "Granted");
                        call_action(correctno);
                    }else {
                        Log.d("Call Permission", "Not Granted");
                    }
                } else {

                    Toast.makeText(HelpSupportActivity.this, "Dialed Number is Busy,\n Please Try Later", Toast.LENGTH_LONG).show();
                }
            }*/
        });
    }

    /* public  boolean isPermissionGranted() {
         if (Build.VERSION.SDK_INT >= 23) {
             if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                     == PackageManager.PERMISSION_GRANTED) {
                 Log.v("TAG","Permission is granted");
                 return true;
             } else {

                 Log.v("TAG","Permission is revoked");
                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                 return false;
             }
         }
         else { //permission is automatically granted on sdk<23 upon installation
             Log.v("TAG","Permission is granted");
             return true;
         }
     }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                /*if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action(correctno);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }*/
                return;
            }
        }
    }
    private void call_action(String correctno){
        /*Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + correctno));
        if (ActivityCompat.checkSelfPermission(HelpSupportActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
