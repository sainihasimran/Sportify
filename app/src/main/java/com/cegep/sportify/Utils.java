package com.cegep.sportify;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class Utils {

    public static DatabaseReference getUserReference() {
        return getClientDatabase().getReference("Users").child(SportifyApp.user.userId);
    }

    public static DatabaseReference getAddressReference() {
        return getUserReference().child("Address");
    }

    public static DatabaseReference getPaymentReference() {
        return getUserReference().child("Payment");
    }

    public static DatabaseReference getOrdersReference() {
        return getClientDatabase().getReference("Orders");
    }

    public static DatabaseReference getShoppingCartReference() {
        return getClientDatabase().getReference("Users").child(SportifyApp.user.userId).child("ShoppingCart");
    }

    public static DatabaseReference getProductReference(String productId) {
        return getAdminDatabase().getReference("Products").child(productId);
    }

    public static DatabaseReference getEquipmentReference(String equipmentId) {
        return getAdminDatabase().getReference("Equipments").child(equipmentId);
    }

    public static FirebaseDatabase getAdminDatabase() {
        return FirebaseDatabase.getInstance(FirebaseApp.getInstance(SportifyApp.ADMIN_FIREBASE));
    }

    public static FirebaseDatabase getClientDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
