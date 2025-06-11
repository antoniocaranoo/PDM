package com.unimib.cooking.model;

public class Category {
    private String name;
    private int flagResourceId;

    public Category(String name, int flagResourceId) {
        this.name = name;
        this.flagResourceId = flagResourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFlagResourceId() {
        return flagResourceId;
    }

    public void setFlagResourceId(int flagResourceId) {
        this.flagResourceId = flagResourceId;
    }
}
