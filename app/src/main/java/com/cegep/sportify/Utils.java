package com.cegep.sportify;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class Utils {

    public static DatabaseReference getUserReference() {
        return FirebaseDatabase.getInstance().getReference("Users").child(SportifyApp.user.userId);
    }

    public static DatabaseReference getAddressReference() {
        return getUserReference().child("Address");
    }

    public static DatabaseReference getPaymentReference() {
        return getUserReference().child("Payment");
    }

    public static DatabaseReference getOrdersRefernece() {
        return FirebaseDatabase.getInstance().getReference("Orders");
    }

    public static DatabaseReference getShoppingCartReference() {
        return FirebaseDatabase.getInstance().getReference("Users").child(SportifyApp.user.userId).child("ShoppingCart");
    }

    public static DatabaseReference getProductReference(String productId) {
        return getAdminDatabase().getReference("Products").child(productId);
    }

    public static DatabaseReference getEquipmentReference(String equipmentId) {
        return getAdminDatabase().getReference("Equipments").child(equipmentId);
    }

    public static FirebaseDatabase getAdminDatabase() {
        return FirebaseDatabase.getInstance("https://sportify-admin-default-rtdb.firebaseio.com/");
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
