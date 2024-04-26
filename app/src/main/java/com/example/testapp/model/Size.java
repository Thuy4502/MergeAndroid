package com.example.testapp.model;

public class Size {
    private int category_size_id;
    private SizeComponent size;
    private Category category;
    private float percent;

    public Size() {
    }

    public Size(int category_size_id, SizeComponent size, Category category, float percent) {
        this.category_size_id = category_size_id;
        this.size = size;
        this.category = category;
        this.percent = percent;
    }

    public int getCategory_size_id() {
        return category_size_id;
    }

    public void setCategory_size_id(int category_size_id) {
        this.category_size_id = category_size_id;
    }

    public SizeComponent getSize() {
        return size;
    }

    public void setSize(SizeComponent size) {
        this.size = size;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
