package com.jonathonhenderson.nhs.model;

public enum Frequency {
    // Monthly incomes cannot be divided into whole weekly values since months are different lengths. Use value 0 so
    // validation will always pass regardless of values for MONTH frequency.
    WEEK(1), TWO_WEEK(2), FOUR_WEEK(4), MONTH(0), QUARTER(13), YEAR(52);

    private int weeks;

    public int getWeeks() {
        return this.weeks;
    }

    Frequency(int weeks) {
        this.weeks = weeks;
    }
}
