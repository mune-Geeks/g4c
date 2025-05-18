package jp.co.c4c;

import java.util.List;
import java.util.Random;

import jp.co.c4c.IteratedGame.Card;

public class TeamG4C {

    int totalReward = 0;
    /**
     * 報酬計算
     * @param reward
     */
    public void addReward(int reward) {
        totalReward += reward;
    }
    /**
     * 作戦1 鈴木＋シヌアン作戦
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

    /**
     * 作戦2　吉田作戦
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics2(int turn, List<Card> p1History, List<Card> p2History) {

        // フェーズ1
        int phese1 = 50;

        // フェーズ2
        int phese2 = 51;

        // 合計協力回数
        int cooperateCount = 0;

        // 協力率
        double cooperateRate = 0.00;

        // 連続協力回数
        int consecutiveCooperateCount = 0;

        // 連続裏切り回数
        int consecutiveBetrayalCount = 0;

        if (turn <= phese1) {
            return Card.BETRAY;
        } else if (turn == phese2) {
            for (int i = 0; i < p2History.size(); i++) {
                if (p2History.get(i) == Card.COOPERATE) {
                    cooperateCount += 1;
                }
            }

            if (cooperateCount <= 25) {
                return Card.BETRAY;
            } else {
                return Card.COOPERATE;
            }
        } else {
            consecutiveBetrayalCount = 0;
            consecutiveCooperateCount = 0;

            for (int i = 0; i < p2History.size(); i++) {
                if (p2History.get(i) == Card.COOPERATE) {
                    cooperateCount += 1;
                }
            }

            // 協力率を算出
            cooperateRate = (double) cooperateCount / p2History.size();

            System.out.println("相手の協力率 " + cooperateRate);
            if (cooperateRate >= 0.5) {
                for (int i = p2History.size() - 1; i >= p2History.size() - 3; i--) {
                    if (p2History.get(i) == Card.BETRAY) {
                        consecutiveBetrayalCount += 1;
                    }
                }
                if (consecutiveBetrayalCount == 3) {
                    return Card.BETRAY;
                }
                return Card.COOPERATE;
            } else {
                for (int i = p2History.size() - 1; i >= p2History.size() - 3; i--) {
                    if (p2History.get(i) == Card.COOPERATE) {
                        consecutiveCooperateCount += 1;
                    }
                }
                if (consecutiveCooperateCount == 3) {
                    return Card.COOPERATE;
                }
                return Card.BETRAY;
            }
        }

    }

    /**
     * 作戦3　錦作戦
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
