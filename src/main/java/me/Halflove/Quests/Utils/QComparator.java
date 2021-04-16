package me.Halflove.Quests.Utils;

import java.util.Comparator;

public class QComparator implements Comparator<String> {
    public int compare(String player1, String player2) {
        int score1 = Integer.parseInt(player1.split(";")[0]);
        int score2 = Integer.parseInt(player2.split(";")[0]);
        if (score1 > score2)
            return -1;
        if (score1 < score2)
            return 1;
        return 0;
    }
}
