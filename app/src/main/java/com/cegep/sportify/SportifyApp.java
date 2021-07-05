package com.cegep.sportify;

import androidx.multidex.MultiDexApplication;
import com.cegep.sportify.model.Order;
import java.util.ArrayList;
import java.util.List;

public class SportifyApp extends MultiDexApplication {

    public static User user = null;

    public static List<Order> orders = new ArrayList<>();

    public static boolean isBuyMode = true;
}
