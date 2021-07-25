package com.cegep.sportify;

import android.text.TextUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.search.result.SearchAddress;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;
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

    public static DatabaseReference getFavoriteProductsReference() {
        return getUserReference().child("favoriteProducts");
    }

    public static DatabaseReference getFavoriteEquipmentsReference() {
        return getUserReference().child("favoriteEquipments");
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

    public static DatabaseReference getSportWithTeamsReference() {
        return FirebaseDatabase.getInstance(FirebaseApp.getInstance(SportifyApp.ADMIN_FIREBASE)).getReference("SportWithTeams");
    }

    public static DatabaseReference getbrandReference() {
        return FirebaseDatabase.getInstance(FirebaseApp.getInstance(SportifyApp.ADMIN_FIREBASE)).getReference("Admin");
    }

    public static FirebaseDatabase getClientDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static String getUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getAddressLine1(SearchSuggestion searchSuggestion) {
        if (searchSuggestion == null) {
            return "Unknown";
        }

        SearchAddress searchAddress = searchSuggestion.getAddress();
        if (searchAddress == null) {
            return "Unknown";
        }

        String houseNumber = searchAddress.getHouseNumber();
        String street = searchAddress.getStreet();

        if (TextUtils.isEmpty(houseNumber) && TextUtils.isEmpty(street)) {
            return "Unknown";
        } else if (TextUtils.isEmpty(houseNumber)) {
            return street;
        } else {
            return houseNumber + ", " + street;
        }
    }

    public static String getAddressLine2(SearchSuggestion searchSuggestion) {
        if (searchSuggestion == null) {
            return "Unknown";
        }

        SearchAddress searchAddress = searchSuggestion.getAddress();
        if (searchAddress == null) {
            return "Unknown";
        }

        String place = searchAddress.getPlace();
        String region = searchAddress.getRegion();

        if (TextUtils.isEmpty(place) && TextUtils.isEmpty(region)) {
            return "Unknown";
        } else if (TextUtils.isEmpty(place)) {
            return region;
        } else {
            return place + ", " + region;
        }
    }

    public static String getPostalCode(SearchSuggestion searchSuggestion) {
        if (searchSuggestion == null) {
            return "Unknown";
        }

        SearchAddress searchAddress = searchSuggestion.getAddress();
        if (searchAddress == null) {
            return "Unknown";
        }

        return searchAddress.getPostcode();
    }
}
