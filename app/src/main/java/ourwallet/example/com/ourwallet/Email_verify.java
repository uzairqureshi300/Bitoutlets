package ourwallet.example.com.ourwallet;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by uzair on 10/05/2017.
 */
public class Email_verify extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener{
   private EditText email_verify_text,photo_id_type,photo_id_number,expiration_date;
    private ImageView upload_photo;
    private String type="sent mail";
    private Button btn_verify,btn_verify_photo,btn_verify_address;
    private String profile_image;
    private Bitmap bitmap , resized;
    public  static final int RequestPermissionCode  = 1 ;
    String code;
    private View snack_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        EnableRuntimePermission();
        email_verify_text=(EditText)findViewById(R.id.input_verify_email);
        btn_verify=(Button)findViewById(R.id.btn_verify_email);
        photo_id_type=(EditText)findViewById(R.id.input_national_id);
        photo_id_number=(EditText)findViewById(R.id.input_photo_id_number);
        expiration_date=(EditText)findViewById(R.id.input_epiry_date);
        btn_verify_photo=(Button)findViewById(R.id.verify_photo);
        upload_photo=(ImageView)findViewById(R.id.upload_image);
        btn_verify_address=(Button)findViewById(R.id.verify_address);
        snack_view=(View)findViewById(R.id.snack);
        btn_verify.setOnClickListener(this);
        btn_verify_photo.setOnClickListener(this);
        upload_photo.setOnClickListener(this);
        btn_verify_address.setOnClickListener(this);



    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                Email_verify.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(Email_verify.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(Email_verify.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);

        }
    }
    public void EnableRuntimePermission_Camera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                Email_verify.this,
                Manifest.permission.CAMERA))
        {
            Toast.makeText(Email_verify.this,"Camera", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(Email_verify.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        }
    }
    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Email_verify.this);
        builder.setTitle("Add Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take From Camera")) {
//                    if(ContextCompat.checkSelfPermission(Email_verify.this, Manifest.permission.CAMERA) !=  PackageManager.PERMISSION_GRANTED)
//                    {
//                        ActivityCompat.requestPermissions(Email_verify.this, new String[] {Manifest.permission.CAMERA}, 100);
//                    } else {
//                        if (ContextCompat.checkSelfPermission(Email_verify.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(Email_verify.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
//                        } else {
//                            if (ContextCompat.checkSelfPermission(Email_verify.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                                ActivityCompat.requestPermissions(Email_verify.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
//                            } else {
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                                File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//                                startActivityForResult(intent, 1);
//                            }
//                        }
//                    }
//                }
              //  else
                if (options[item].equals("Choose from Gallery")) {
                    if(ContextCompat.checkSelfPermission(Email_verify.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Email_verify.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                        ActivityCompat.requestPermissions(Email_verify.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                    }
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void Verify_Email() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("email", email_verify_text.getText().toString());
        json.put("user_id", Constants.user_id);
        JSONObject json2 = new JSONObject();
        json2.put("to","orupartners");
        json2.put("methods","email_verified");
        json2.accumulate("complex",json);
        String url = "http://orupartners.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,json2, this ,this){

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Log.e("response-verify",response.toString());

        try {
            switch (type){
                case "sent mail":
                    code=response.getString("code");
                    type="compare_code";
                    email_verify_text.setText("");
                    email_verify_text.setHint("Enter Code");
                    btn_verify.setText("Enter code");
                    break;
                case "compare_code":
                   String  error =response.getString("error");
                    if(error.equals("0")){
                        Snackbar.make(snack_view,"Email verified",Snackbar.LENGTH_LONG).show();
                        finish();
                    }

                    break;
                case "photo_verify":
                    Log.e("photo_response",response.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
}
    private void verify_address() throws JSONException {
        type="address_verify";
        JSONObject json = new JSONObject();
        Toast.makeText(Email_verify.this, profile_image, Toast.LENGTH_SHORT).show();
        json.put("images", profile_image);
        json.put("photo_type", "used");
        json.put("address_1","Karachi");
        json.put("address_2","Islamabad");
        json.put("country","Pakistan");
        json.put("state","Punjab");
        json.put("city","Rawaloindi");
        json.put("issuance","1987-03-23 02:34:34");
        json.put("user_id",Constants.user_id);


        JSONObject json2 = new JSONObject();
        json2.put("to","orupartners");
        json2.put("methods","address_request");
        json2.accumulate("complex",json);
        String url = "http://orupartners.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,json2, this ,this){

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }


    private void verify_photo() throws JSONException {
        type="photo_verify";
        JSONObject json = new JSONObject();
        Toast.makeText(Email_verify.this, profile_image, Toast.LENGTH_SHORT).show();
        json.put("images", profile_image);
        json.put("photo_type", "used");
        json.put("photo_id","23333");
        json.put("user_id",Constants.user_id);
        json.put("photo_expiration", "1987-03-23 02:34:34");

        JSONObject json2 = new JSONObject();
        json2.put("to","orupartners");
        json2.put("methods","photo_request");
        json2.accumulate("complex",json);
        String url = "http://orupartners.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,json2, this ,this){

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }






    private void verify(String code) throws JSONException {
        Log.e("code",code);
        JSONObject json = new JSONObject();
        json.put("user_id", Constants.user_id);
        json.put("code", code);


        JSONObject json2 = new JSONObject();
        json2.put("to","orupartners");
        json2.put("methods","email_verification");
        json2.accumulate("complex",json);
        String url = "http://orupartners.com/cp/redirect_to.php";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,json2, this ,this){

        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }


    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.btn_verify_email :
               switch (type){
                   case "sent mail":
                       try {
                           if(email_verify_text.getText().toString().equals("")){
                               Snackbar.make(snack_view,"field cannot be empty",Snackbar.LENGTH_LONG).show();
                           }
                           else {
                               Verify_Email();
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       break;
                   case "compare_code":
                       try {
                           if(email_verify_text.getText().toString().equals("")){
                               Snackbar.make(snack_view,"field cannot be empty",Snackbar.LENGTH_LONG).show();
                           }
                           else {
                               verify(email_verify_text.getText().toString());
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                       break;
               }
               break;

           case R.id.upload_image:
               selectImage();
               break;
           case R.id.verify_photo:

               try {
                   verify_photo();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               break;
           case R.id.verify_address:
               try {
                   verify_address();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
       }

    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] grantResults) {

        switch (RC) {

            case RequestPermissionCode:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    ActivityCompat.requestPermissions(Email_verify.this,new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, RequestPermissionCode);
                }
                case 100:


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);


                    upload_photo.setImageBitmap(bitmap);
                    String path = Environment.getExternalStorageDirectory()+File.separator + "FastAuto_images";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);

                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));




                upload_photo.setImageBitmap(bitmap);
                Log.w("path from gallery..", picturePath + "");


            }
            String encodedImageData = getEncoded64ImageStringFromBitmap(bitmap);
            Log.e("64", encodedImageData);
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();
            profile_image = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        }
        catch(Exception ex){
           upload_photo.setImageResource(R.drawable.upload_image);
            Toast.makeText(Email_verify.this, "Image not supported", Toast.LENGTH_SHORT).show();
        }
        return profile_image;
    }
}
