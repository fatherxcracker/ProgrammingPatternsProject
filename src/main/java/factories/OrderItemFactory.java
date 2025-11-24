package factories;

import com.example.sharpburgermanager.models.MenuItem;
import com.example.sharpburgermanager.models.OrderItem;

public class OrderItemFactory {
    private static final String[] validTypes = {"Drive Thru", "Pick up", "Delivery", "Uber Eats", "Dine In"};

    public static OrderItem createOrderItem(String name, String type, boolean status, double price){
        if(!isValidType(type)){
            throw new IllegalArgumentException("Invalid Category: " + type);
        }

        return new OrderItem(name, type, status, price);
    }

    private static boolean isValidType(String type){
        for (String t : validTypes){
            if (t.equalsIgnoreCase(type))
                return true;
        }
        return false;
    }
}
