package com.example.sharpburgermanager.threads;

import com.example.sharpburgermanager.models.MenuItem;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CategoryCountThread extends Thread{
    private final List<MenuItem> items;
    private final Consumer<HashMap<String, Integer>> callback;

    public CategoryCountThread(List<MenuItem> items, Consumer<HashMap<String, Integer>> callback) {
        this.items = items;
        this.callback = callback;
    }

    @Override
    public void run() {
        HashMap<String, Integer> freq = new HashMap<>();

        // Now there is a Background Calculation
        for (MenuItem item : items) {
            String category = item.getCategory();
            freq.put(category, freq.getOrDefault(category, 0) + 1);
        }

        Platform.runLater(() -> callback.accept(freq));
    }
}
