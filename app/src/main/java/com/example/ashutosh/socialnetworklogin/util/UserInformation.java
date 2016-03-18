package com.example.ashutosh.socialnetworklogin.util;

/**
 * Created by ashutosh on 3/11/2016.
 */
public class UserInformation {
    String fName;
    String lName;
    String email;
    String gender;
    String imageUri;
    String address;
    String phoneNo;

    public UserInformation() {
    }

    public UserInformation(String fName, String lName, String gender, String email, String imageUri) {

        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.email = email;
        this.imageUri = imageUri;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }





    public String getfName() {
        return fName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getlName() {
        return lName;
    }


    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}
