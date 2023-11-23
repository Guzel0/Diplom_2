package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsResponse {
    private String success;
    private ArrayList<Ingredient> data;

    public IngredientsResponse(String success, ArrayList<Ingredient> data) {
        this.success = success;
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<Ingredient> getData() {
        return data;
    }

    public void setData(ArrayList<Ingredient> data) {
        this.data = data;
    }

    public ArrayList getIngredients(int count) {
        ArrayList<String> ingredients = new ArrayList<>();
        for (int i=0; i < data.size(); i++) {
            if(i == count) {
                break;
            }
            String ingredientId = data.get(i).get_id();
            ingredients.add(ingredientId);
        }
        return ingredients;
    }
}
