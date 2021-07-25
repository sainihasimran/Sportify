package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.firebase.database.Exclude;

public class Address {

    private String id;

    private String name;

    private String suiteNumber;

    private String streetAddress;

    private String city;

    private String province;

    private String postalCode;

    private String phoneNumber;

    @Exclude
    private boolean selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isValid(Context context) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Please enter receivers name", Toast.LENGTH_SHORT).show();
            return false;
        }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        if (suiteNumber != null ? !suiteNumber.equals(address.suiteNumber) : address.suiteNumber != null) {
            return false;
        }
        if (streetAddress != null ? !streetAddress.equals(address.streetAddress) : address.streetAddress != null) {
            return false;
        }
        if (city != null ? !city.equals(address.city) : address.city != null) {
            return false;
        }
        if (province != null ? !province.equals(address.province) : address.province != null) {
            return false;
        }
        return postalCode != null ? postalCode.equals(address.postalCode) : address.postalCode == null;
    }

    @Override
    public int hashCode() {
        int result = suiteNumber != null ? suiteNumber.hashCode() : 0;
        result = 31 * result + (streetAddress != null ? streetAddress.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }
}
