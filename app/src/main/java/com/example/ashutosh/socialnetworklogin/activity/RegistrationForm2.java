package com.example.ashutosh.socialnetworklogin.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.ashutosh.socialnetworklogin.R;
import com.example.ashutosh.socialnetworklogin.dbhandler.DatabaseHandler;
import com.example.ashutosh.socialnetworklogin.util.CommonData;
import com.example.ashutosh.socialnetworklogin.util.UserInformation;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

/**
 * Created by ashutosh on 3/10/2016.
 */
public class RegistrationForm2 extends AppCompatActivity {

    EditText etGender,etPhone,etAddress;
    String gender,phone,address;
    ImageView ivProfilePic;
    UserInformation userInformation;
    DatabaseHandler databaseHandler;
    Button btnImageChooser;
    private final int SELECT_PHOTO = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form2);
        init();

         userInformation= CommonData.getUserInfo(RegistrationForm2.this);
        checkForProfile(userInformation);



    }

    private void checkForProfile(UserInformation userInformation) {
        if(userInformation!=null){

            Picasso.with(this)
                    .load(userInformation.getImageUri())
                    .into(ivProfilePic);
            etGender.setText(userInformation.getGender());
        }
    }

    private void getValue() {


        gender=etGender.getText().toString();
        phone=etPhone.getText().toString();
        address=etAddress.getText().toString();
        Log.d("Tag Image",ivProfilePic.toString());
        userInformation.setGender(gender);
        userInformation.setPhoneNo(phone);
        userInformation.setAddress(address);
    }

    private boolean isValid() {



        if (gender.isEmpty())

        {
            etGender.setError("enter gender field");

            return false;
        }
        if (phone.isEmpty())

        {
            etPhone.setError("enter valid mobile no");


            return false;
        }

        if (address.isEmpty())
        {
            etAddress.setError("enter valid address");
            return false;
        }




        return true;
    }


    private void init() {

        etGender=(EditText)findViewById(R.id.sign_up_gender);
        etPhone=(EditText)findViewById(R.id.sign_up_phone);
        ivProfilePic=(ImageView)findViewById(R.id.sign_up_image);
        etAddress=(EditText)findViewById(R.id.sign_up_address);
        btnImageChooser=(Button)findViewById(R.id.button_image_chooser);
        btnImageChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImagePicker=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentImagePicker.setType("image/*");
                startActivityForResult(intentImagePicker, SELECT_PHOTO);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();

                        Log.d("Image uri string",imageUri.toString());


                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                       ivProfilePic.setImageBitmap(selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }


    }



    public void onClickSubmit(View view) {

        getValue();

        if(isValid()) {

            Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Submitted"+userInformation.getfName()+"\n"+userInformation.getlName()
                    +"\n"+userInformation.getGender() +"\n"+userInformation.getEmail() +"\n"+userInformation.getAddress()
                    , Toast.LENGTH_LONG).show();

            databaseHandler=new DatabaseHandler(this);
           databaseHandler.addUser(userInformation);
            Log.d("Insert: ", "Inserting ..");
            Log.d("Count: ", databaseHandler.getUserCount() + "");
           Toast.makeText(this, "added to db"+databaseHandler.getUserCount(), Toast.LENGTH_SHORT).show();

            CommonData.removeData(this);
            startActivity(new Intent(this,MainActivity.class));






        }
    }
}
