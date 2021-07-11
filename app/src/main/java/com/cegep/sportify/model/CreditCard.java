package com.cegep.sportify.model;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        if (cardNumber.length() < 8 || cardNumber.length() > 19) {
            Toast.makeText(context, "Invalid card number length", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(expiry)) {
            Toast.makeText(context, "Please enter card expiry information", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (expiry.length() != 4) {
            Toast.makeText(context, "Invalid expiry input", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMyy");
            simpleDateFormat.setLenient(false);
            Date expiryDate = simpleDateFormat.parse(expiry);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expiryDate);

            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);

            if (!validateExpiryDate(month, year)) {
                Toast.makeText(context, "Invalid expiry input", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "Invalid expiry input", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(cvv)) {
            Toast.makeText(context, "Please enter card cvv number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cvv.length() != 3) {
            Toast.makeText(context, "Invalid cvv length", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean validateExpiryDate(int month, int year) {
        if (month < 1 || year < 1) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curYear = cal.get(Calendar.YEAR);
        if (year < 100) {
            curYear -= 2000;
        }
        return (curYear == year) ? curMonth <= month : curYear < year;
    }
}
