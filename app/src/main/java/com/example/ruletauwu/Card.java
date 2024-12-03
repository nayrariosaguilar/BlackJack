package com.example.ruletauwu;

import android.graphics.Bitmap;

public class Card {
    private Bitmap image;
    private int value;

    public Card(Bitmap image, int value) {
        this.image = image;
        this.value = value;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getValue() {
        return value;
    }
}
