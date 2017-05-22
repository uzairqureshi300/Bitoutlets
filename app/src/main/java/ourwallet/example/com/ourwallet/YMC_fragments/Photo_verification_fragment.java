package ourwallet.example.com.ourwallet.YMC_fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ourwallet.example.com.ourwallet.Activities.MySingleton;
import ourwallet.example.com.ourwallet.Constants;
import ourwallet.example.com.ourwallet.R;

import static android.app.Activity.RESULT_OK;


public class Photo_verification_fragment extends Fragment implements com.android.volley.Response.Listener<JSONObject>,
        com.android.volley.Response.ErrorListener,View.OnClickListener {
    private EditText photo_id_type, photo_id_number, expiration_date;
    private Button btn_verify_photo;
    private String profile_image;
    private View snack_view;
    private Bitmap bitmap,resize;
    private ProgressDialog mProgressDialog;
    private String expire_date,send_date;
    private ImageView upload_photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.photo_verify_fragment, container, false);
        photo_id_type = (EditText) v.findViewById(R.id.input_national_id);
        photo_id_number = (EditText) v.findViewById(R.id.input_photo_id_number);
        expiration_date = (EditText) v.findViewById(R.id.input_epiry_date);
        snack_view=(View)v.findViewById(R.id.snack_view_photo);
        btn_verify_photo = (Button) v.findViewById(R.id.verify_photo);
        upload_photo = (ImageView) v.findViewById(R.id.upload_image);
        Click_Listeners();
        return v;
    }

    private void Click_Listeners() {
        btn_verify_photo.setOnClickListener(this);
        upload_photo.setOnClickListener(this);
    }
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setMessage("Please Wait..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload_image:
                selectImage();
                break;
            case R.id.verify_photo:
                verify_photo();
        }
    }
    private void DateToString(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // Make sure user insert date into edittext in this format.

        Date dateObject;

        String date;
        String time;
        try{

            expire_date = (expiration_date.getText().toString());

            dateObject = formatter.parse(expire_date);

            date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
             time = new SimpleDateFormat("h:mmaa").format(dateObject);
            send_date=date+" "+time;
            send_date.replace("/","-");
        }

        catch (java.text.ParseException e)
        {
            e.printStackTrace();

        }
    }

    private void verify_photo() {
        try {
            showProgressDialog();
            DateToString();
            JSONObject json = new JSONObject();
            json.put("images", profile_image);
            json.put("photo_type", photo_id_type.getText().toString());
            json.put("photo_id", photo_id_number.getText().toString());
            json.put("user_id", Constants.user_id);
            json.put("token", Constants.token);
            json.put("photo_expiration", send_date);

            JSONObject json2 = new JSONObject();
            json2.put("to", "orupartners");
            json2.put("methods", "photo_request");
            json2.accumulate("complex", json);
            String url = "http://orupartners.com/cp/redirect_to.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, json2, this, this) {

            };
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);

        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
    mProgressDialog.dismiss();
        Snackbar.make(snack_view, "Slow internet connectivity", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onResponse(JSONObject response) {
     Log.e("response",response.toString());
        try {
            if (response.getString("error").equals("0")) {
                Snackbar.make(snack_view, "verified successfully", Snackbar.LENGTH_LONG).show();
            }
            mProgressDialog.dismiss();
        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Take From Camera", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take From Camera")) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 100);
                    } else {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                        } else {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 1);
                            }
                        }
                    }
                }
                if (options[item].equals("Choose from Gallery")) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
               bitmap =(Bitmap) data.getExtras().get("data");
                int height = bitmap.getHeight() / 2;
                    int width = bitmap.getWidth() / 2;
                     resize = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    upload_photo.setImageBitmap(resize);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));
                int height = bitmap.getHeight() / 2;
                int width = bitmap.getWidth() / 2;
                resize = Bitmap.createScaledBitmap(bitmap, width, height, true);


                upload_photo.setImageBitmap(resize);
                Log.w("path from gallery..", picturePath + "");


            }
            String encodedImageData = getEncoded64ImageStringFromBitmap(resize);
            Log.e("64", encodedImageData);
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byteFormat = stream.toByteArray();
            profile_image = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        } catch (Exception ex) {
            upload_photo.setImageResource(R.drawable.upload_image);
        }
        return profile_image;
    }
}
