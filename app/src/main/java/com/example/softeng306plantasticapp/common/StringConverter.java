package com.example.softeng306plantasticapp.common;

public class StringConverter {

    public String convertCommasToNewlines(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(", ", "\n");
    }
}

