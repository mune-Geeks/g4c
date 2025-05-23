package jp.co.c4c;

import java.util.List;
import java.util.Random;

import jp.co.c4c.IteratedGame.Card;

public class TeamMegane {

    /**
     * 報酬計算用
     * @param reward
     */
    int totalReward = 0;
    public void addReward(int reward) { 
        totalReward += reward;
    }

    /**
     * 作戦1
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics1(int turn, List<Card> p1History, List<Card> p2History) {
        // 今はランダムで返す
        Random random = new Random();
        return random.nextBoolean() ? Card.COOPERATE : Card.BETRAY;
    }
}
