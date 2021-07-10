package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class CreditCard {

    private String nameOnCard;

    private String cardNumber;

    private String expiry;

    private String cvv;

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public boolean isValid(Context context) {
        if (TextUtils.isEmpty(nameOnCard)) {
            Toast.makeText(context, "Please enter name on the card", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(cardNumber)) {
            Toast.makeText(context, "Please enter card number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(expiry)) {
            Toast.makeText(context, "Please enter card expiry information", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(cvv)) {
            Toast.makeText(context, "Please enter card cvv number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
