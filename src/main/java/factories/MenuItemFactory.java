package factories;

import com.example.sharpburgermanager.models.MenuItem;

public class MenuItemFactory {
    private static final String[] validCategories = {"Burgers", "Beverages", "Sides", "Desserts"};

    public static MenuItem createMenuItem(String name, String category, double price){
        if(!isValidCategory(category)){
            throw new IllegalArgumentException("Invalid Category: " + category);
        }

        return new MenuItem(name, category, price);
    }

    private static boolean isValidCategory(String category){
        for (String c : validCategories){
            if (c.equalsIgnoreCase(category))
                return true;
        }
        return false;
    }

}
