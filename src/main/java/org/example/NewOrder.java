package org.example;

import java.util.ArrayList;

public class NewOrder {
    private ArrayList<String> ingredients;

    public NewOrder(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
