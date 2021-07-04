package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class Address {

    private String suiteNumber;

    private String streetAddress;

    private String city;

    private String province;

    private String postalCode;

    private String phoneNumber;

    public String getSuiteNumber() {
        return suiteNumber;
    }

    public void setSuiteNumber(String suiteNumber) {
        this.suiteNumber = suiteNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isValid(Context context) {
        if (TextUtils.isEmpty(suiteNumber)) {
            Toast.makeText(context, "Please enter suite number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(streetAddress)) {
            Toast.makeText(context, "Please enter street address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(city)) {
            Toast.makeText(context, "Please enter city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(postalCode)) {
            Toast.makeText(context, "Please enter postal code", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
