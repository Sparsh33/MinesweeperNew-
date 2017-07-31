package com.example.sparshgupta.minesweeper;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    LinearLayout mainLayout;
    LinearLayout rows[];
    int row = 14;
    int columns = 10;
    boolean isGameOver;
    custom_button buttons[][];
    int noOfMines = 24, score = 0;
    int minesInitial, minesScore;
    boolean tocheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.main_workspace);
        Button brestart = (Button) findViewById(R.id.b_restart);
        brestart.setOnClickListener(this);
        setUpBoard();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.easy){
            noOfMines = 24;
            setUpBoard();
        }
        if(id == R.id.medium){
            noOfMines = 35;
            setUpBoard();
        }
        if(id == R.id.hard){
            noOfMines = 45;
            setUpBoard();
        }
        return true;
    }

    private void setUpBoard() {
        tocheck = false;
        mainLayout.removeAllViews();
        rows = new LinearLayout[row];
        isGameOver = false;
        minesInitial = 0;
        minesScore = 0;
        score = 0;
        TextView text1 = (TextView) findViewById(R.id.dispArea1);
        text1.setText("0");
        TextView text2 = (TextView) findViewById(R.id.dispArea2);

        //loop for layouts
        for (int i = 0; i < row; i++) {
            rows[i] = new LinearLayout(this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            rows[i].setLayoutParams(p);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            mainLayout.addView(rows[i]);
        }

        //loop for buttons
        buttons = new custom_button[row][columns];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < columns; j++) {
                buttons[i][j] = new custom_button(this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                p.setMargins(1,1,1,1);
                buttons[i][j].setLayoutParams(p);
                buttons[i][j].setText("");
                buttons[i][j].setValue(0);
                buttons[i][j].setB_row(i);
                buttons[i][j].setB_column(j);
                buttons[i][j].setBackgroundColor(getResources().getColor(R.color.darkGrey));
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setOnLongClickListener(this);
                rows[i].addView(buttons[i][j]);
            }
        }


        //loop for evaluating mines!
        // how to make sure no set of nos are repeating
        for (int i = 0; i < noOfMines; i++) {
            Random r = new Random();
            int rowNo = r.nextInt(row - 1);
            int columnNo = r.nextInt(columns - 1);
            buttons[rowNo][columnNo].setValue(9);
            //setting values of surrounding buttons
            if(!buttons[rowNo][columnNo].isVisited){
                if((rowNo - 1 >= 0) && (columnNo - 1 >= 0)){
                    int a = buttons[rowNo - 1][columnNo - 1].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo - 1][columnNo - 1].setValue(a);}
                }
                if(rowNo - 1 >= 0){
                    int a = buttons[rowNo - 1][columnNo].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo - 1][columnNo].setValue(a);}
                }
                if((rowNo - 1 >= 0)  && (columnNo + 1 < columns)){
                    int a = buttons[rowNo - 1][columnNo + 1].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo - 1][columnNo + 1].setValue(a);}
                }
                if(columnNo - 1 >= 0){
                    int a = buttons[rowNo][columnNo - 1].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo][columnNo - 1].setValue(a);}
                }
                if(columnNo + 1 < columns){
                    int a = buttons[rowNo][columnNo + 1].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo][columnNo + 1].setValue(a);}
                }
                if(rowNo + 1 < row && columnNo - 1 >= 0){
                    int a = buttons[rowNo + 1][columnNo - 1].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo + 1][columnNo - 1].setValue(a);}
                }
                if(rowNo + 1 < row){
                    int a = buttons[rowNo + 1][columnNo].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo + 1][columnNo].setValue(a);}
                }
                if(rowNo + 1 < row && columnNo + 1 < columns){
                    int a = buttons[rowNo + 1][columnNo + 1].getValue();
                    if(a!=9){
                    a++;
                    buttons[rowNo + 1][columnNo + 1].setValue(a);}
                }
                buttons[rowNo][columnNo].setVisited(true);//if randomly generated values are same, then this can ensure that increment doesn't happen twice
                minesScore++;
                minesInitial++;
                text2.setText("Mines: " + minesInitial);
            }
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if(id == R.id.b_restart){
            setUpBoard();
            return ;
        }

        custom_button b = (custom_button) view;
        TextView text = (TextView) findViewById(R.id.dispArea1);

        if(b.isFlagged){
            return;
        }

        if(isGameOver){
            return;
        }

        if(b.getValue() == 9){
            b.setText("9");
            b.setTextColor(getResources().getColor(R.color.black));
            b.setTextSize(getResources().getDimension(R.dimen.b_size));
            isGameOver = true;
            showAll();
        }

        if(b.getValue() == 1){
            b.setText("1");
            b.setTextColor(getResources().getColor(R.color.black));
            b.setTextSize(getResources().getDimension(R.dimen.b_size));
            b.setEnabled(false);
            b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            score++;
            text.setText(score + "");
        }

        if(b.getValue() == 2){
            b.setText("2");
            b.setTextColor(getResources().getColor(R.color.black));
            b.setTextSize(getResources().getDimension(R.dimen.b_size));
            b.setEnabled(false);
            b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            score++;
            text.setText(score + "");
        }

        if(b.getValue() == 3){
            b.setText("3");
            b.setTextColor(getResources().getColor(R.color.black));
            b.setTextSize(getResources().getDimension(R.dimen.b_size));
            b.setEnabled(false);
            b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            score++;
            text.setText(score + "");
        }

        if(b.getValue() == 4){
            b.setText("4");
            b.setTextColor(getResources().getColor(R.color.black));
            b.setTextSize(getResources().getDimension(R.dimen.b_size));
            b.setEnabled(false);
            b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            score++;
            text.setText(score + "");
        }

        if(b.getValue() == 0){
            int r = b.getB_row();
            int c = b.getB_column();
            unlockAll(r, c);
        }

        boolean won = hasWon();
        if(won && !tocheck){
            Toast.makeText(this, "Congratulations! You are a genius", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private boolean hasWon() {
        int count = 0;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < columns; j++){
                if(buttons[i][j].getValue() != 9 && !buttons[i][j].isEnabled()){
                    count++;
                }
            }
        }
        if(row * columns - minesInitial == count){
            return true;
        }
        return false;
    }

    private void unlockAll(int r,int c) {
        if((r >= 0 && r < row) && (c >= 0 && c < columns)){
            custom_button b = buttons[r][c];
            TextView text = (TextView) findViewById(R.id.dispArea1);
            if(!b.isEnabled()){
                return;
            }
            else if(b.getValue() == 0){
                b.setText("");
                b.setTextColor(getResources().getColor(R.color.black));
                b.setTextSize(getResources().getDimension(R.dimen.b_size));
                b.setEnabled(false);
                b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                score++;
                text.setText(score + "");
                unlockAll(r - 1, c - 1);
                unlockAll(r - 1, c);
                unlockAll(r - 1, c + 1);
                unlockAll(r, c + 1);
                unlockAll(r + 1, c + 1);
                unlockAll(r + 1, c);
                unlockAll(r + 1, c - 1);
                unlockAll(r, c - 1);
            }
            else if(b.getValue() == 1){
                b.setText("1");
                b.setTextColor(getResources().getColor(R.color.black));
                b.setTextSize(getResources().getDimension(R.dimen.b_size));
                b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                text.setText(score++ + "");
                b.setEnabled(false);
                return;
            }
            else if(b.getValue() == 2){
                b.setText("2");
                b.setTextColor(getResources().getColor(R.color.black));
                b.setTextSize(getResources().getDimension(R.dimen.b_size));
                b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                text.setText(score++ + "");
                b.setEnabled(false);
                return;
            }
            else if(b.getValue() == 3){
                b.setText("3");
                b.setTextColor(getResources().getColor(R.color.black));
                b.setTextSize(getResources().getDimension(R.dimen.b_size));
                b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                text.setText(score++ + "");
                b.setEnabled(false);
                return;
            }
            else if(b.getValue() == 4){
                b.setText("4");
                b.setTextColor(getResources().getColor(R.color.black));
                b.setTextSize(getResources().getDimension(R.dimen.b_size));
                b.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                text.setText(score++ + "");
                b.setEnabled(false);
                return;
            }
            else{
                return;
            }
        }
        else{
            return;
        }

    }

    private void showAll() {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < columns; j++){
                if(!buttons[i][j].isFlagged){
                    if(buttons[i][j].getValue() == 9){
                        buttons[i][j].setText("M");
                        buttons[i][j].setTextSize(getResources().getDimension(R.dimen.b_size));
                        buttons[i][j].setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                    else if(buttons[i][j].getValue() == 0){
                        buttons[i][j].setText("");
                    }
                    else{
                        buttons[i][j].setText(buttons[i][j].getValue() + "");
                        buttons[i][j].setTextColor(getResources().getColor(R.color.black));
                        buttons[i][j].setTextSize(getResources().getDimension(R.dimen.b_size));
                    }
                buttons[i][j].setEnabled(false);
                buttons[i][j].setBackgroundColor(getResources().getColor(R.color.lightGrey));}
            }
        }
        Toast.makeText(this, "Game Over!!", Toast.LENGTH_SHORT).show();
        tocheck = true;
    }

    @Override
    public boolean onLongClick(View view) {
        TextView text2 = (TextView) findViewById(R.id.dispArea2);
        custom_button b = (custom_button) view;
        if(b.isFlagged){
            b.isFlagged = false;
            b.setText("");
            minesScore++;
            b.setBackgroundColor(getResources().getColor(R.color.darkGrey));
            b.setValue(0);
        }else{
            b.isFlagged = true;
            b.setText("F");
            b.setTextSize(getResources().getDimension(R.dimen.b_size));
            minesScore--;
            b.setBackgroundColor(getResources().getColor(R.color.flagRed));
        }
        text2.setText("Mines: " + minesScore);
        return true;      //returning true indicates that the whole of the touch has been registered by the long click and hence no further events should take place
    }
}
