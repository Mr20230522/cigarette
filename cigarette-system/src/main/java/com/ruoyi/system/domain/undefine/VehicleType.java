package com.ruoyi.system.domain.undefine;

import com.ruoyi.common.core.domain.BaseEntity;

public class VehicleType extends BaseEntity {
    private int value;
    private String name;

    public VehicleType(){

    }

    public VehicleType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
