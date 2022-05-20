package com.golan.amit.softsuduku;

import android.util.Log;

public class SoftSudukuHelper {

    public static final int RND_RECENT = 3;
    public static final int SQUARES = 10;
    private int rnd_help_indexer;
    private int[] rnd_nums;
    private int randomNumber;
    private int round_counter;
    private boolean[] columnsCompleted;
    private boolean[] rowsCompleted;
    private int score; //   rows / columns completed


    public SoftSudukuHelper() {
        this.rnd_help_indexer = 0;
        this.score = 0;
        this.round_counter = 0;
        this.randomNumber = 0;
        this.rnd_nums = new int[RND_RECENT];
        resetRndNums();
        columnsCompleted = new boolean[SQUARES];
        rowsCompleted = new boolean[SQUARES];
        resetCompletedRowsAndColumns();

    }

    private void resetCompletedRowsAndColumns() {
        for(int i = 0; i < columnsCompleted.length; i++) {
            columnsCompleted[i] = false;
            rowsCompleted[i] = false;
        }
    }

    public boolean getRowArrayByIndex(int ind) {
        return rowsCompleted[ind];
    }

    public boolean getColumnArrayByIndex(int ind) {
        return columnsCompleted[ind];
    }

    public void setRowCompletedByIndex(int index) {
        if(index < 0 || index > (rowsCompleted.length - 1))
            return;
        rowsCompleted[index] = true;
    }

    public void setColumnCompletedByIndex(int index) {
        if(index < 0 || index > (columnsCompleted.length - 1))
            return;
        columnsCompleted[index] = true;
    }

    public boolean isRowCompleted() {
        boolean completed = true;
        for(int i = 0; i < rowsCompleted.length; i++) {
            if(rowsCompleted[i] == false) {
                completed = false;
                break;
            }
        }
        return completed;
    }

    public boolean isColumnCompleted() {
        boolean completed = true;
        for(int i = 0; i < columnsCompleted.length; i++) {
            if(columnsCompleted[i] == false) {
                completed = false;
                break;
            }
        }
        return completed;
    }

    public void outputCompletedMatrix() {
        StringBuilder sbRows, sbCols;
        sbRows = new StringBuilder();
        sbCols = new StringBuilder();
        sbRows.append("["); sbCols.append("[");
        for(int i = 0; i < rowsCompleted.length; i++) {
            sbRows.append(rowsCompleted[i]);
            sbCols.append(columnsCompleted[i]);
            if(i < (rowsCompleted.length - 1)) {
                sbRows.append(",");
                sbCols.append(",");
            }
        }
        sbRows.append("]"); sbCols.append("]");
        Log.d(MainActivity.DEBUGTAG, sbRows.toString());
        Log.d(MainActivity.DEBUGTAG, sbCols.toString());
    }

    public void generateRandomNumber() {
        this.randomNumber = (int)(Math.random() * 10);
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(int num) {
        this.randomNumber = num;
    }

    private void resetRndNums() {
        for(int i = 0; i < rnd_nums.length; i++) {
            rnd_nums[i] = -1;
        }
    }

    public void displayRndNums() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < rnd_nums.length; i++) {
            sb.append(rnd_nums[i]);
            if(i < (rnd_nums.length - 1))
                sb.append(",");
        }
        if(MainActivity.DEBUG) {
            Log.i(MainActivity.DEBUGTAG, sb.toString());
        }
    }

    public String returnRndNumsAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rnd_nums.length; i++) {
            sb.append(rnd_nums[i]);
            if (i < (rnd_nums.length - 1))
                sb.append(",");
        }
        return sb.toString();
    }

    public void increaseRndIndexer() {
        this.rnd_help_indexer++;
    }

    public void insertRndNumToArray() {
        rnd_nums[rnd_help_indexer % RND_RECENT] = this.randomNumber;
    }

    public boolean isNumInRandomArray() {
        boolean isHere = false;
        for (int i = 0; i < rnd_nums.length; i++) {
            if (randomNumber == rnd_nums[i]) {
                isHere = true;
                break;
            }
        }
        return isHere;
    }

    public void increaseRoundCounter() {
        this.round_counter++;
    }

    public void resetRoundCounter() {
        this.round_counter++;
    }

    public int getRound_counter() {
        return round_counter;
    }

    public void increaseScore() {
        this.score++;
    }

    public int getScore() {
        return score;
    }
}
