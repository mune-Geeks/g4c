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
        final int PHESE1_END_TURN = 50;

        // フェーズ2で使用するしきい値
        final int COOPERATION_THRESHOLD = 25;

        // フェーズ3
        final int PHESE3_STR_TURN = 52;

        // 合計協力回数
        int cooperateCount = 0;

        // 協力率
        double cooperateRate = 0.00;

        // 連続協力回数
        int consecutiveCooperateCount = 0;

        // 連続裏切り回数
        int consecutiveBetrayalCount = 0;

        // 相手の協力
        final double COOPERATION_RATE_THRESHOLD = 0.5;

        // 直近3ターン
        final int RECENT_TURN_COUNT = 3;

        // フェーズ1
        if (turn <= PHESE1_END_TURN) {
            return Card.BETRAY;
        }

        // 相手の協力回数をカウント
        for (int i = 0; i < p2History.size(); i++) {
            if (p2History.get(i) == Card.COOPERATE) {
                cooperateCount += 1;
            }

            // フェーズ3のときの直近3ターンの結果をカウント
            if (turn >= PHESE3_STR_TURN - RECENT_TURN_COUNT) {
                if (p2History.get(i) == Card.COOPERATE) {
                    consecutiveCooperateCount += 1;
                } else {
                    consecutiveBetrayalCount += 1;
                }
            }
        }

        // フェーズ2
        if (turn == PHESE1_END_TURN + 1) {
            // 相手の協力回数が25回以下だった場合、裏切り
            if (cooperateCount <= COOPERATION_THRESHOLD) {
                return Card.BETRAY;
            } else {
                return Card.COOPERATE;
            }
        }

        // フェーズ3
        cooperateRate = (double) cooperateCount / p2History.size();

        // 協力率が50%以上だった場合
        if (cooperateRate >= COOPERATION_RATE_THRESHOLD) {
            if (consecutiveBetrayalCount == RECENT_TURN_COUNT) {
                return Card.BETRAY;
            }
            return Card.COOPERATE;
        } else {
            if (consecutiveCooperateCount == RECENT_TURN_COUNT) {
                return Card.COOPERATE;
            }
            return Card.BETRAY;
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
