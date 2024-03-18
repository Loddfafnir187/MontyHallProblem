package com.montyhall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {
    private int amount;
    private int pick1;
    private int pick2;
    private int wins;

    public void addAmount() {
        amount++;
    }

    public void addWin() {
        wins++;
    }

    public int firstResult() {
        if (amount == 0)
            return 0;
        return (int) (pick1 / (float) amount * 100);
    }

    public int secondResult() {
        if (amount == 0)
            return 0;
        return (int) (pick2 / (float) amount * 100);
    }
}

