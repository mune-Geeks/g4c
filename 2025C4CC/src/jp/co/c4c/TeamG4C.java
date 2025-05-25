package jp.co.c4c;

import java.util.List;
import java.util.Random;

import jp.co.c4c.IteratedGame.Card;

public class TeamG4C {

    int totalReward = 0;

    /**
     * 報酬計算
     * 
     * @param reward
     */
    public void addReward(int reward) {
        totalReward += reward;
    }

    /**
     * 作戦1 鈴木＋シヌアン作戦
     * 
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics1(int turn, List<Card> p1History, List<Card> p2History) {
        Random random = new Random();

        if (turn <= 100) {
            // 鈴木作戦が未実装のためランダムな選択を行う
            return random.nextBoolean() ? Card.COOPERATE : Card.BETRAY;
        }

        // 101~200ターン: シヌアン作戦開始
        int relativeTurn = turn - 100;

        // フェーズ1: (101-105)
        if (relativeTurn <= 5) {
            return Card.COOPERATE;
        }

        // フェーズ2: (106-130)
        if (relativeTurn <= 30) {
            int recentStart = turn - 4;
            if (recentStart < 1)
                recentStart = 1;
            int fromIdx = Math.max(0, recentStart);
            int toIdx = Math.max(0, turn - 1);
            if (fromIdx >= p2History.size())
                fromIdx = p2History.size();
            if (toIdx > p2History.size())
                toIdx = p2History.size();
            List<Card> recent = p2History.subList(fromIdx, toIdx);

            // パターンを解析
            String pattern = recent.stream()
                    .map(Card::getLabel)
                    .reduce("", (a, b) -> a + b);

            if (pattern.endsWith("協力協力協力")) {
                return random.nextDouble() < 0.8 ? Card.COOPERATE : Card.BETRAY;
            } else if (pattern.endsWith("協力協力裏切り")) {
                return Card.BETRAY;
            } else if (pattern.endsWith("協力裏切り")) {
                return Card.BETRAY;
            } else if (pattern.endsWith("裏切り裏切り")) {
                return Card.BETRAY;
            } else {
                return random.nextBoolean() ? Card.COOPERATE : Card.BETRAY;
            }
        }

        if (relativeTurn <= 50) {
            int recentStart = turn - 10;
            if (recentStart < 0)
                recentStart = 0;
            int fromIdx = recentStart;
            int toIdx = turn - 1;
            if (fromIdx >= p2History.size())
                fromIdx = p2History.size();
            if (toIdx > p2History.size())
                toIdx = p2History.size();
            long coopCount = p2History.subList(fromIdx, toIdx).stream()
                    .filter(card -> card == Card.COOPERATE).count();
            double rate = coopCount / 10.0;
            double r = random.nextDouble();
            if (rate >= 0.7) {
                return r < 0.85 ? Card.COOPERATE : Card.BETRAY;
            } else if (rate >= 0.5) {
                return r < 0.5 ? Card.COOPERATE : Card.BETRAY;
            } else {
                return r < 0.25 ? Card.COOPERATE : Card.BETRAY;
            }
        }

        // フェーズ3: (151-200)
        if (relativeTurn <= 55) {
            return Card.COOPERATE; // 151-155: 全て協力し様子を見る
        }

        int coopCount = 0;
        for (int i = 150; i < 155 && i < p2History.size(); i++) {
            if (p2History.get(i) == Card.COOPERATE)
                coopCount++;
        }

        if (coopCount >= 4) {
            if (relativeTurn <= 60)
                return Card.COOPERATE;
        } else {
            if (relativeTurn == 56)
                return Card.COOPERATE;
            int betrayCount = 0;
            for (int i = 151; i < turn && i < p2History.size(); i++) {
                if (p2History.get(i) == Card.BETRAY)
                    betrayCount++;
            }
            if (betrayCount >= 2)
                return Card.BETRAY;
        }

        int betrayCount = 0;
        for (int i = 150; i < turn - 1 && i < p2History.size(); i++) {
            if (p2History.get(i) == Card.BETRAY)
                betrayCount++;
        }

        if (betrayCount <= 2)
            return Card.COOPERATE;
        if (betrayCount <= 4) {
            if (turn - 2 >= 0 && turn - 3 >= 0 && turn - 2 < p2History.size() && turn - 3 < p2History.size()) {
                if (p2History.get(turn - 2) == Card.BETRAY && p2History.get(turn - 3) == Card.BETRAY) {
                    return Card.BETRAY;
                }
            }
            return random.nextDouble() < 0.65 ? Card.COOPERATE : Card.BETRAY;
        }
        if (betrayCount < 9) {
            return random.nextDouble() < 0.3 ? Card.COOPERATE : Card.BETRAY;
        }

        // betrayCount >= 9
        boolean lastFourCoop = true;
        for (int i = turn - 5; i < turn - 1; i++) {
            if (i < 0 || i >= p2History.size() || p2History.get(i) != Card.COOPERATE) {
                lastFourCoop = false;
                break;
            }
        }
        if (lastFourCoop)
            return Card.COOPERATE;

        return random.nextDouble() < 0.15 ? Card.COOPERATE : Card.BETRAY;
    }

    /**
     * 作戦2 吉田作戦
     * 
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics2(int turn, List<Card> p1History, List<Card> p2History) {
        // 今はランダムで返す
        Random random = new Random();
        return random.nextBoolean() ? Card.COOPERATE : Card.BETRAY;
    }

    /**
     * 作戦3 錦作戦
     * 
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics3(int turn, List<Card> p1History, List<Card> p2History) {
        // 今はランダムで返す
        Random random = new Random();
        return random.nextBoolean() ? Card.COOPERATE : Card.BETRAY;
    }

}
