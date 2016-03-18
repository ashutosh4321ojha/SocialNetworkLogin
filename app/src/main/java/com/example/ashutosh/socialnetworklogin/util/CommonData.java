package com.example.ashutosh.socialnetworklogin.util;

import android.content.Context;


import com.example.ashutosh.socialnetworklogin.sharedpreferences.Prefs;
import com.example.ashutosh.socialnetworklogin.sharedpreferences.SharedPreferencesName;


public class CommonData {


    private static UserInformation userInformation=null;


    public static UserInformation getUserInfo(Context context) {
        if (userInformation == null) {
            userInformation = Prefs.with(context).getObject(SharedPreferencesName.USER_INFO, UserInformation.class);
        }


        return userInformation;

    }


    public static void saveUserInfo(Context context,UserInformation myUserInformation){
        userInformation = myUserInformation;
        Prefs.with(context).save(SharedPreferencesName.USER_INFO,myUserInformation );
    }

    public static void removeData(Context context){

        userInformation=null;
        Prefs.with(context).remove(SharedPreferencesName.USER_INFO);
    }

}
