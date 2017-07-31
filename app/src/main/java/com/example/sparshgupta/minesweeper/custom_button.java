package com.example.sparshgupta.minesweeper;

import android.content.Context;
import android.widget.Button;


public class custom_button extends Button {

    int value;
    boolean isVisited = false, isFlagged = false;
    int b_row, b_column;

    public int getB_column() {
        return b_column;
    }

    public void setB_column(int b_column) {
        this.b_column = b_column;
    }

    public int getB_row() {

        return b_row;
    }

    public void setB_row(int b_row) {
        this.b_row = b_row;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public custom_button(Context context) {
        super(context);
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }
}
