package com.golan.amit.softsuduku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SoftSudukuActivity extends AppCompatActivity implements View.OnClickListener {

    Button[][] btnSquares;
    Button btnPlayAgain;
    TextView tvDigit, tvTimerDisplay, tvRounds, tvScore;
    SoftSudukuHelper ssh;

    /**
     * Counter / Timer
     */

    private static final long MINUTES = 2;  //  2 minutes
    private static final long TIMER = MINUTES * 60 * 1000;
    private long minutesRemain, secondsRemain;
    private int countDownInterval;
    private long timeToRemain;
    CountDownTimer cTimer;

    /**
     * Animation
     */
    Animation[] animRotate;
    Animation animScale;

    /**
     * Sounds
     */
    SoundPool sp;
    int wine_sp, trombone_sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_suduku);

        init();

        setListener();

        setDigitsOnBoard();

        setDigit();

    }


    private void init() {
        btnSquares = new Button[][]{
                {findViewById(R.id.btnR0C0), findViewById(R.id.btnR0C1), findViewById(R.id.btnR0C2), findViewById(R.id.btnR0C3),
                        findViewById(R.id.btnR0C4), findViewById(R.id.btnR0C5), findViewById(R.id.btnR0C6), findViewById(R.id.btnR0C7),
                        findViewById(R.id.btnR0C8), findViewById(R.id.btnR0C9)},
                {findViewById(R.id.btnR1C0), findViewById(R.id.btnR1C1), findViewById(R.id.btnR1C2), findViewById(R.id.btnR1C3),
                        findViewById(R.id.btnR1C4), findViewById(R.id.btnR1C5), findViewById(R.id.btnR1C6), findViewById(R.id.btnR1C7),
                        findViewById(R.id.btnR1C8), findViewById(R.id.btnR1C9)},
                {findViewById(R.id.btnR2C0), findViewById(R.id.btnR2C1), findViewById(R.id.btnR2C2), findViewById(R.id.btnR2C3),
                        findViewById(R.id.btnR2C4), findViewById(R.id.btnR2C5), findViewById(R.id.btnR2C6), findViewById(R.id.btnR2C7),
                        findViewById(R.id.btnR2C8), findViewById(R.id.btnR2C9)},
                {findViewById(R.id.btnR3C0), findViewById(R.id.btnR3C1), findViewById(R.id.btnR3C2), findViewById(R.id.btnR3C3),
                        findViewById(R.id.btnR3C4), findViewById(R.id.btnR3C5), findViewById(R.id.btnR3C6), findViewById(R.id.btnR3C7),
                        findViewById(R.id.btnR3C8), findViewById(R.id.btnR3C9)},
                {findViewById(R.id.btnR4C0), findViewById(R.id.btnR4C1), findViewById(R.id.btnR4C2), findViewById(R.id.btnR4C3),
                        findViewById(R.id.btnR4C4), findViewById(R.id.btnR4C5), findViewById(R.id.btnR4C6), findViewById(R.id.btnR4C7),
                        findViewById(R.id.btnR4C8), findViewById(R.id.btnR4C9)},
                {findViewById(R.id.btnR5C0), findViewById(R.id.btnR5C1), findViewById(R.id.btnR5C2), findViewById(R.id.btnR5C3),
                        findViewById(R.id.btnR5C4), findViewById(R.id.btnR5C5), findViewById(R.id.btnR5C6), findViewById(R.id.btnR5C7),
                        findViewById(R.id.btnR5C8), findViewById(R.id.btnR5C9)},
                {findViewById(R.id.btnR6C0), findViewById(R.id.btnR6C1), findViewById(R.id.btnR6C2), findViewById(R.id.btnR6C3),
                        findViewById(R.id.btnR6C4), findViewById(R.id.btnR6C5), findViewById(R.id.btnR6C6), findViewById(R.id.btnR6C7),
                        findViewById(R.id.btnR6C8), findViewById(R.id.btnR6C9)},
                {findViewById(R.id.btnR7C0), findViewById(R.id.btnR7C1), findViewById(R.id.btnR7C2), findViewById(R.id.btnR7C3),
                        findViewById(R.id.btnR7C4), findViewById(R.id.btnR7C5), findViewById(R.id.btnR7C6), findViewById(R.id.btnR7C7),
                        findViewById(R.id.btnR7C8), findViewById(R.id.btnR7C9)},
                {findViewById(R.id.btnR8C0), findViewById(R.id.btnR8C1), findViewById(R.id.btnR8C2), findViewById(R.id.btnR8C3),
                        findViewById(R.id.btnR8C4), findViewById(R.id.btnR8C5), findViewById(R.id.btnR8C6), findViewById(R.id.btnR8C7),
                        findViewById(R.id.btnR8C8), findViewById(R.id.btnR8C9)},
                {findViewById(R.id.btnR9C0), findViewById(R.id.btnR9C1), findViewById(R.id.btnR9C2), findViewById(R.id.btnR9C3),
                        findViewById(R.id.btnR9C4), findViewById(R.id.btnR9C5), findViewById(R.id.btnR9C6), findViewById(R.id.btnR9C7),
                        findViewById(R.id.btnR9C8), findViewById(R.id.btnR9C9)}
        };
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        ssh = new SoftSudukuHelper();
        tvDigit = findViewById(R.id.tvDigitId);
        tvTimerDisplay = findViewById(R.id.tvTimerDisplay);
        tvRounds = findViewById(R.id.tvRoundsId);
        tvScore = findViewById(R.id.tvScoreId);

        cTimer = null;
        timeToRemain = TIMER;

        animRotate = new Animation[]{
                AnimationUtils.loadAnimation(this, R.anim.anim_rotate_right),
                AnimationUtils.loadAnimation(this, R.anim.anim_rotate_left)
        };
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale_inout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME).build();
            sp = new SoundPool.Builder().setMaxStreams(10)
                    .setAudioAttributes(aa).build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        wine_sp = sp.load(this, R.raw.wine, 1);
        trombone_sp = sp.load(this, R.raw.failtrombone, 1);
    }

    private void setListener() {
        for (int i = 0; i < btnSquares.length; i++) {
            for (int j = 0; j < btnSquares[i].length; j++) {
                btnSquares[i][j].setOnClickListener(this);
            }
        }
        btnPlayAgain.setOnClickListener(this);
    }

    private void setDigitsOnBoard() {
        for (int s = 0; s < 7; s++) {
            boolean squareEmpty = false;
            while (!squareEmpty) {
                int col = (int) (Math.random() * btnSquares.length);
                int row = (int) (Math.random() * btnSquares[col].length);
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "in 'setDigitsOnBoard': picked row=" + row + ", col=" + col);
                }
                if (btnSquares[row][col].getText().toString().trim().isEmpty()) {

                    ssh.generateRandomNumber();
                    while (ssh.isNumInRandomArray()) {
                        ssh.generateRandomNumber();
                    }
                    ssh.insertRndNumToArray();
                    ssh.increaseRndIndexer();
                    btnSquares[row][col].setText(String.valueOf(ssh.getRandomNumber()));
                    revokeSquareClickableByCoords(row, col);
                    squareEmpty = true;
                } else {
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "didn't set A to row=" + row + ", col=" + col);
                    }
                }
            }
        }
    }

    private void setDigit() {
        ssh.generateRandomNumber();
        while (ssh.isNumInRandomArray()) {
            ssh.generateRandomNumber();
        }
        ssh.insertRndNumToArray();
        ssh.increaseRndIndexer();

        //  testing if the selected random digit is valid:
        if (isRandomNumberValid()) {
            tvDigit.setText(String.valueOf(ssh.getRandomNumber()));
            return;
        }
        if (MainActivity.DEBUG) {
            Log.i(MainActivity.DEBUGTAG, "OOooopsss. need to re select digit. [" + ssh.getRandomNumber() + "] is NOT a valid random number");
        }
        for (int rndi = 0; rndi < SoftSudukuHelper.SQUARES; rndi++) {
            ssh.setRandomNumber(rndi);
            if (MainActivity.DEBUG) {
                Log.i(MainActivity.DEBUGTAG, "in 'setDigit': checking digit: " + ssh.getRandomNumber() + " if it's a valid random number");
            }
            if (isRandomNumberValid()) {
                if (MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "in 'setDigit': digit [" + ssh.getRandomNumber() + "] is a valid random number");
                }
                tvDigit.setText(String.valueOf(rndi));
                return;
            } else {
                if (MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "in 'setDigit': digit [" + ssh.getRandomNumber() + "] is NOT valid random number");
                }
            }
        }
        //  if reached here the game is "stuck" - no valid number can be chosen - user won
        Toast.makeText(this, "Computer can not pick a new random valid number to play with. User won!!", Toast.LENGTH_LONG).show();
        if (MainActivity.DEBUG) {
            Log.i(MainActivity.DEBUGTAG, "Computer can not pick a new random valid number to play with. User won!!");
        }
        Intent i = new Intent(this, CompletedMatrixActivity.class);
        i.putExtra(getString(R.string.end_message), "No valid random number could be set.\nUser won!!\nScore: " + ssh.getScore() +
                ", Rounds completed: " + ssh.getRound_counter());
        startActivity(i);
    }

    //  Checking if the random digit that the computer picked can be set on matrix board
    private boolean isRandomNumberValid() {
        ArrayList<Point> pointsList = new ArrayList<>();
        for (int i = 0; i < btnSquares.length; i++) {           //  y (column)
            for (int j = 0; j < btnSquares[i].length; j++) {    //  x (row)
                if (btnSquares[i][j].getText().toString().trim().isEmpty()) {
                    pointsList.add(new Point(j, i));
                    if (MainActivity.DEBUG) {
//                        Log.i(MainActivity.DEBUGTAG, "in 'isRandomNumberValid': square [" + i + "," + j + "] is empty. adding to point list");
                    }
                }
            }
        }
        String currDig = String.valueOf(ssh.getRandomNumber());
        for (int ipl = 0; ipl < pointsList.size(); ipl++) {
            int row = pointsList.get(ipl).y;
            int col = pointsList.get(ipl).x;
            int markedSquaresInRow = getNumberOfMarkedSquaresInRow(row);
            int markedSquaresInColumn = getNumberOfMarkedSquaresInColumn(col);

            //  checking up, down, left and right - no red marking
            if (checkingUp(row, col, markedSquaresInRow+1, currDig, false) ||
                    checkingDown(row, col, markedSquaresInRow+1, currDig, false) ||
                    checkingLeft(row, col, markedSquaresInColumn+1, currDig, false) ||
                    checkingRight(row, col, markedSquaresInColumn+1, currDig, false)) {
                if (MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "in 'isRandomNumberValid': '" + currDig + "' can NOT be in point (" + row + "," + col + ")");
                }
            } else {
                if (MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "in 'isRandomNumberValid': '" + currDig + "' can be put in point (" + row + "," + col + ")");
                }
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////   fix painting in blue rows & columns
    private void paintCompletedRowByRowIndex(int row) {
        for(int j = 0; j < btnSquares[row].length; j++) {
            btnSquares[row][j].setBackgroundColor(Color.BLUE);
        }
    }

    private void paintCompletedColumnByColumnIndex(int col) {
        for(int i = 0; i < btnSquares.length; i++) {
            btnSquares[i][col].setBackgroundColor(Color.BLUE);
        }
    }
    /////////////////////////////////////////   fix painting in blue rows & columns

    private boolean isAllMatrixCompleted() {
        for (int i = 0; i < btnSquares.length; i++) {
            for (int j = 0; j < btnSquares[i].length; j++) {
                if (btnSquares[i][j].getText().toString().trim().isEmpty()) {
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "in 'isAllMatrixCompleted': square [" + i + "," + j + "] is empty. Returning false");
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == btnPlayAgain) {
            Intent i = new Intent(this, SoftSudukuActivity.class);
            startActivity(i);
            return;
        }
        int row = -1, col = -1;
        for (int i = 0; i < btnSquares.length; i++) {
            for (int j = 0; j < btnSquares[i].length; j++) {
                if (v == btnSquares[i][j]) {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1 && col != -1)
                break;
        }
        if (row == -1 || col == -1) {
            Log.e(MainActivity.DEBUGTAG, "no button detected");
            Toast.makeText(this, "no button detected", Toast.LENGTH_SHORT).show();
            return;
        }

        //  Let it begin

        btnSquares[row][col].setText(tvDigit.getText().toString().trim());
        revokeSquareClickableByCoords(row, col);
        btnSquares[row][col].setBackgroundColor(Color.YELLOW);
        btnSquares[row][col].startAnimation(animRotate[(int) (Math.random() * animRotate.length)]);
        ssh.increaseRoundCounter();
        tvRounds.setText("Round " + String.valueOf(ssh.getRound_counter()));

        //  tests / checks if all squares are occupied (matrix is full)
        if(isAllMatrixCompleted()) {
            Intent i = new Intent(this, CompletedMatrixActivity.class);
            i.putExtra(getString(R.string.end_message), "All matrix squares are completed. \nScore: " + ssh.getScore() +
                    ", Rounds completed: " + ssh.getRound_counter());
            startActivity(i);
            return;
        }

        if (disqualified(row, col)) {
            //  GAME OVER - user lost
            endGame();
            Toast.makeText(this, "GAME OVER after " + ssh.getRound_counter() + " times.", Toast.LENGTH_SHORT).show();
            tvDigit.startAnimation(animScale);
            return;
        }

        //  Checking if a row or a column is completed
        if(isRowCompletedByRow(row)) {
            //  checking if it's already been checked
            if(ssh.getRowArrayByIndex(row)) {
                if(MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "row " + row + " is already marked as completed");
                }
            } else {
                //  increase score, set array to true and paint this row
                ssh.setRowCompletedByIndex(row);
                ssh.increaseScore();
                tvScore.setText("Score: " + ssh.getScore());
                paintCompletedRowByRowIndex(row);
            }
        }

        //  checking if a column is completed
        if(isColumnCompletedByColumn(col)) {
            //  checking if it's already set as checked
            if(ssh.getColumnArrayByIndex(col)) {
                if(MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "column " + col + " is already marked as completed");
                }
            } else {
                //  increase score, set array to true and paint this column by the row
                ssh.setColumnCompletedByIndex(col);
                ssh.increaseScore();
                tvScore.setText("Score: " + ssh.getScore());
                paintCompletedColumnByColumnIndex(col);
            }
        }

        setDigit();
        cTimer.cancel();
        timerDemo(TIMER);

    }

    /////////////////////////////////////////////////////// fix of checking row & column are completed
    private boolean isRowCompletedByRow(int row) {
        boolean isRowCompleted = true;
        for(int i = 0; i < btnSquares[row].length; i++) {
            if(btnSquares[row][i].getText().toString().isEmpty()) {
                isRowCompleted = false;
                break;
            }
        }
        return isRowCompleted;
    }

    private boolean isColumnCompletedByColumn(int col) {
        boolean isColumnCompleted = true;
        for(int j = 0; j < btnSquares.length; j++) {
            if(btnSquares[j][col].getText().toString().isEmpty()) {
                isColumnCompleted = false;
                break;
            }
        }
        return isColumnCompleted;
    }
    /////////////////////////////////////////////////////// fix of checking row & column are completed

    private int getNumberOfMarkedSquaresInRow(int row) {
        int tmpRowSquaresCount = 0;
        for (int i = 0; i < btnSquares[row].length; i++) {
            if (!btnSquares[row][i].getText().toString().trim().isEmpty()) {
                tmpRowSquaresCount++;
            }
        }
        return tmpRowSquaresCount;
    }

    private int getNumberOfMarkedSquaresInColumn(int column) {

        int tmpColumnSquaresCount = 0;
        for (int j = 0; j < btnSquares.length; j++) {
            if (!btnSquares[j][column].getText().toString().trim().isEmpty()) {
                tmpColumnSquaresCount++;
            }
        }
        return tmpColumnSquaresCount;
    }

    private boolean disqualified(int row, int col) {

        int markedSquaresInRow = getNumberOfMarkedSquaresInRow(row);
        int markedSquaresInColumn = getNumberOfMarkedSquaresInColumn(col);
        if(MainActivity.DEBUG) {
            Log.d(MainActivity.DEBUGTAG, "marked squares in row: " + markedSquaresInRow);
            Log.d(MainActivity.DEBUGTAG, "marked squares in column: " + markedSquaresInColumn);
        }

        String currDig = btnSquares[row][col].getText().toString().trim();
        //  checking up
        if (checkingUp(row, col, markedSquaresInRow, currDig, true)) return true;

        //  checking down
        if (checkingDown(row, col, markedSquaresInRow, currDig, true)) return true;

        //  checking left
        if (checkingLeft(row, col, markedSquaresInColumn, currDig, true)) return true;

        //  checking right
        if (checkingRight(row, col, markedSquaresInColumn, currDig, true)) return true;

        return false;
    }

    private boolean checkingRight(int row, int col, int markedSquaresInColumn, String currDig, boolean toPaintInRed) {
        if (col < (btnSquares[row].length - 1)) {
            int tmpCol = col + 1;
            while (tmpCol < btnSquares[row].length) {
                String tmp = btnSquares[row][tmpCol].getText().toString().trim();
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "running right. col=" + tmpCol + ", value is: " + tmp + ", curr is: " + currDig);
                }
                if (tmp.equals(currDig)) {
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "in 'checkingRight': found a match, returning true");
                    }
                    if(markedSquaresInColumn < SoftSudukuHelper.SQUARES) {
                        if(toPaintInRed) {
                            btnSquares[row][tmpCol].setBackgroundColor(Color.RED);
                        }
                        return true;
                    }   //  fix the "special case"
                    else {
                        int expected = 0;
                        if(toPaintInRed) {
                            expected = 1;
                        }
                        if(howManyTimesDigitInColumn(currDig, col) > expected) {
                            return true;
                        }
                    }
                }
                tmpCol++;
            }
        }
        return false;
    }

    private boolean checkingLeft(int row, int col, int markedSquaresInColumn, String currDig, boolean toPaintInRed) {
        if (col > 0) {
            int tmpCol = col - 1;
            while (tmpCol >= 0) {
                String tmp = btnSquares[row][tmpCol].getText().toString().trim();
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "running left. col=" + tmpCol + ", value is: " + tmp + ", curr is: " + currDig);
                }
                if (tmp.equals(currDig)) {
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "in 'checkingLeft': found a match, returning true");
                    }
                    if(markedSquaresInColumn < SoftSudukuHelper.SQUARES) {
                        if(toPaintInRed) {
                            btnSquares[row][tmpCol].setBackgroundColor(Color.RED);
                        }
                        return true;
                    } //  fix the "special case"
                    else {
                        int expected = 0;
                        if(toPaintInRed) {
                            expected = 1;
                        }
                        if(howManyTimesDigitInColumn(currDig, col) > expected) {
                            return true;
                        }
                    }
                }
                tmpCol--;
            }
        }
        return false;
    }

    private boolean checkingDown(int row, int col, int markedSquaresInRow, String currDig, boolean toPaintInRed) {
        if (row < (btnSquares.length - 1)) {
            int tmpRow = row + 1;
            while (tmpRow < btnSquares.length) {
                String tmp = btnSquares[tmpRow][col].getText().toString().trim();
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "running down. row=" + tmpRow + ", value is: " + tmp + ", curr is: " + currDig);
                }
                if (tmp.equals(currDig)) {
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "in 'checkingDown': found a match, returning true");
                    }
                    if(markedSquaresInRow < SoftSudukuHelper.SQUARES) {
                        if(toPaintInRed) {
                            btnSquares[tmpRow][col].setBackgroundColor(Color.RED);
                        }
                        return true;
                    }    //  fix the "special case"
                    else {
                        int expected = 0;
                        if(toPaintInRed) {
                            expected = 1;
                        }
                        if(howManyTimesDigitInRow(currDig, row) > expected) {
                            return true;
                        }
                    }
                }
                tmpRow++;
            }
        }
        return false;
    }

    private boolean checkingUp(int row, int col, int markedSquaresInRow, String currDig, boolean toPaintInRed) {
        if (row > 0) {
            int tmpRow = row - 1;
            while (tmpRow >= 0) {
                String tmp = btnSquares[tmpRow][col].getText().toString().trim();
                if (MainActivity.DEBUG) {
                    Log.d(MainActivity.DEBUGTAG, "running up. row=" + tmpRow + ", value is: " + tmp + ", curr is: " + currDig);
                }
                if (tmp.equals(currDig)) {
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "in 'checkingUp': found a match in " + tmpRow + ", checking if it completes a row");
                    }
                    if(markedSquaresInRow < SoftSudukuHelper.SQUARES) {
                        if(toPaintInRed) {
                            btnSquares[tmpRow][col].setBackgroundColor(Color.RED);
                        }
                        return true;
                    }   //  fix the "special case"
                    else {
                        int expected = 0;
                        if(toPaintInRed) {
                            expected = 1;
                        }
                        if(howManyTimesDigitInRow(currDig, row) > expected) {
                            return true;
                        }
                    }
                }
                tmpRow--;
            }
        }
        return false;
    }

    private int howManyTimesDigitInRow(String digit, int row) {
        int cnt = 0;
        for(int i = 0; i < btnSquares[row].length; i++) {
            if(btnSquares[row][i].getText().toString().trim().equalsIgnoreCase(digit)) {
                cnt++;
            }
        }
        return cnt;
    }

    private int howManyTimesDigitInColumn(String digit, int col) {
        int cnt = 0;
        for(int j = 0; j < btnSquares.length; j++) {
            if(btnSquares[j][col].getText().toString().trim().equalsIgnoreCase(digit)) {
                cnt++;
            }
        }
        return cnt;
    }

    private void makeAllSquaresClickable() {
        for (int i = 0; i < btnSquares.length; i++) {
            for (int j = 0; j < btnSquares[i].length; j++) {
                btnSquares[i][j].setClickable(true);
            }
        }
    }

    private void makeSquareClickableByCoords(int row, int col) {
        try {
            btnSquares[row][col].setClickable(true);
        } catch (Exception e) {
        }
    }

    private void revokeSquareClickableByCoords(int row, int col) {
        try {
            btnSquares[row][col].setClickable(false);
        } catch (Exception e) {
        }
    }

    private void revokeAllSquaresClickable() {
        for (int i = 0; i < btnSquares.length; i++) {
            for (int j = 0; j < btnSquares[i].length; j++) {
                btnSquares[i][j].setClickable(false);
            }
        }
    }


    private void timerDemo(final long millisInFuture) {
        countDownInterval = 1000;
        cTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeToRemain = millisUntilFinished;
                long Minutes = timeToRemain / (60 * 1000) % 60;
                long Seconds = timeToRemain / 1000 % 60;

                minutesRemain = MINUTES - (Minutes + 1);
                secondsRemain = 60 - Seconds;
                if (secondsRemain == 60) {
                    secondsRemain = 0;
                    minutesRemain++;
                }

                if (Minutes == 1 && Seconds == 0) {
                    Toast.makeText(SoftSudukuActivity.this, "הזמן אוזל...", Toast.LENGTH_LONG).show();
                }
                if (Minutes < 1) {
                    tvTimerDisplay.setTextColor(Color.RED);
                } else {
                    tvTimerDisplay.setTextColor(Color.BLUE);
                }

//                String tmpTime = String.format("remain: %02d:%02d , passed: %01d:%02d", Minutes, Seconds, minutesRemain, secondsRemain);
                String tmpTime = String.format("%02d:%02d", Minutes, Seconds);
                tvTimerDisplay.setText(tmpTime);
            }

            @Override
            public void onFinish() {
                endGame();
                Toast.makeText(SoftSudukuActivity.this, "TIMEOUT - GAME OVER, after " + ssh.getRound_counter() + " times.", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void endGame() {
        cTimer.cancel();
        revokeAllSquaresClickable();
        tvDigit.setText("X");
        tvDigit.setTextSize(18);
        btnPlayAgain.setVisibility(View.VISIBLE);
        tvTimerDisplay.setText(null);
        tvTimerDisplay.setVisibility(View.GONE);
        sp.play(trombone_sp, 1, 1, 0, 0, 1);
        timerDemo(30 * 60 * 1000);
        ssh.resetRoundCounter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerDemo(timeToRemain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cTimer.cancel();
    }
}
