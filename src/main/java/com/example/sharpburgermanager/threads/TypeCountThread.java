package com.example.sharpburgermanager.threads;

import com.example.sharpburgermanager.models.OrderItem;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class TypeCountThread extends Thread {
    private final List<OrderItem> items;
    private final Consumer<HashMap<String, Integer>> callback;

    public TypeCountThread(List<OrderItem> items, Consumer<HashMap<String, Integer>> callback) {
        this.items = items;
        this.callback = callback;
    }

    @Override
    public void run() {
        HashMap<String, Integer> freq = new HashMap<>();

        for (OrderItem item : items) {
            String type = item.getType();
            freq.put(type, freq.getOrDefault(type, 0) + 1);
        }

        Platform.runLater(() -> callback.accept(freq));
    }
}
