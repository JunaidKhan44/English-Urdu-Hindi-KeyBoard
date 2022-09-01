package com.example.create_keyboard1.model;

public class KeyThemeModel {


    public  int theme_image;
    public String  theme_name;

    public KeyThemeModel(int theme_image, String theme_name) {
        this.theme_image = theme_image;
        this.theme_name = theme_name;
    }

    public int getTheme_image() {
        return theme_image;
    }

    public void setTheme_image(int theme_image) {
        this.theme_image = theme_image;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }
}
