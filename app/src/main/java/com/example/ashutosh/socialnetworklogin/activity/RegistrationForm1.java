package com.example.ashutosh.socialnetworklogin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ashutosh.socialnetworklogin.R;
import com.example.ashutosh.socialnetworklogin.util.CommonData;
import com.example.ashutosh.socialnetworklogin.util.UserInformation;

/**
 * Created by ashutosh on 2/27/2016.
 */
public class RegistrationForm1 extends AppCompatActivity {

    EditText etFirstName,etLastName,etEmail;
    String email,firstName,lastName;
    UserInformation userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form1);
        init();
         userInformation= CommonData.getUserInfo(RegistrationForm1.this);
//        Log.d("Tag", userInformatiion.getfName());
        checkForProfile(userInformation);

    }

    private void checkForProfile(UserInformation userInformation) {

        if(userInformation!=null){
            etFirstName.setText(userInformation.getfName());
            etLastName.setText(userInformation.getlName());
            etEmail.setText(userInformation.getEmail());
        }
    }

    private void getValue() {


        firstName=etFirstName.getText().toString();
        lastName=etLastName.getText().toString();
        email=etEmail.getText().toString();

        userInformation.setfName(firstName);
        userInformation.setlName(lastName);
        userInformation.setEmail(email);
    }

    private boolean isValid() {



        if (firstName.isEmpty())

        {
            etFirstName.setError("enter valid first name");

            return false;
        }
        if (lastName.isEmpty())

        {
            etLastName.setError("enter valid mobile no");


            return false;
        }

        if(TextUtils.isEmpty(email) ||!(email.contains("@"))){
            etEmail.setError("enter valid email");
            return false;
        }






        return true;
    }


    private void init() {

        etFirstName=(EditText)findViewById(R.id.sign_up_first_name);
       etLastName=(EditText)findViewById(R.id.sign_up_last_name);
        etEmail=(EditText)findViewById(R.id.sign_up_email);


    }

    public void onClickNextForm(View view) {

        getValue();



        if(isValid()) {

            Intent intent=new Intent(RegistrationForm1.this,RegistrationForm2.class);
            startActivity(intent);

        }


    }
}
