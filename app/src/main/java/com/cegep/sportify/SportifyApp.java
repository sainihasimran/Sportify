package com.cegep.sportify;

import androidx.multidex.MultiDexApplication;
import com.cegep.sportify.model.Order;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.location.DefaultLocationProvider;
import java.util.ArrayList;
import java.util.List;

public class SportifyApp extends MultiDexApplication {

    public static final String CLIENT_FIREBASE = "CLIENT_FIREBASE";

    public static final String ADMIN_FIREBASE = "ADMIN_FIREBASE";

    public static User user = null;

    public static List<Order> orders = new ArrayList<>();

    public static boolean isBuyMode = true;

    public static boolean productAddedInShoppingCart = false;

    public static boolean equipmentAddedInShoppingCart = false;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions adminOptions = new FirebaseOptions.Builder()
                .setProjectId("sportify-admin")
                .setApplicationId("1:311923457474:android:e27176c6832b9498fd6a5b")
                .setApiKey("AIzaSyCwk8W_Zm4dX6qhcJyMErRXiTceWPKpbcs")
                .setDatabaseUrl("https://sportify-admin-default-rtdb.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(this, adminOptions, ADMIN_FIREBASE);

        MapboxSearchSdk.initialize(this, getString(R.string.mapbox_access_token), new DefaultLocationProvider(this));

//        FirebaseOptions clientOptions = new FirebaseOptions.Builder()
//                .setProjectId("sportify-5484f")
//                .setApplicationId("1:1012491882693:android:80fea00eb160e82d49a0ac")
//                .setApiKey("AIzaSyAWywR_efXWmhsKe-bzGXimZAI4_pB0zsw")
//                .setDatabaseUrl("https://sportify-5484f-default-rtdb.firebaseio.com//")
//                .build();

//        FirebaseApp.initializeApp(this, clientOptions, CLIENT_FIREBASE);
    }
}
