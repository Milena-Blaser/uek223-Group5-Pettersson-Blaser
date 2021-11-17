package com.example.demo.domain.listentry;

public enum Importance {
    NOT_IMPORTANT(1), NEUTRAL(2), LESS_IMPORTANT(3), IMPORTANT(4), VERY_IMPORTANT(5);
    private int numVal;

    Importance(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }

}
