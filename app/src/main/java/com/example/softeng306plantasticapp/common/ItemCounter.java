package com.example.softeng306plantasticapp.common;

public class ItemCounter {

    private int count;

    public ItemCounter() {
        this.count = 1;
    }
    public void increment() {
        if(count<99) {
            count++;
        }
    }
    public void decrement() {
        if(count>1) {
            count--;
        }
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
