package com.ruoyi.system.domain.undefine;

import java.util.ArrayList;

public class VehicleBehaviorColor {
    private ArrayList<String> categories;
    private ArrayList<Integer> values;

    public VehicleBehaviorColor() {
    }

    public VehicleBehaviorColor(ArrayList<String> categories ,ArrayList<Integer> values) {
        this.values = values;
        this.categories = categories;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<Integer> getValues() {
        return values;
    }

    public void setValues(ArrayList<Integer> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "VehicleBehaviorColor{" +
                "categories=" + categories +
                ", values=" + values +
                '}';
    }
}
