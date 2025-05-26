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
        // 1～100ターン：鈴木の作戦
        final int EARLY_TURN_LIMIT = 10;
        final double COOPERATE_RATE_LOW = 0.3;
        final double COOPERATE_RATE_HIGH = 0.7;

        // 1～10ターン
        if (turn <= EARLY_TURN_LIMIT) {
            return Card.COOPERATE;
        }

        // 11～80ターン
        int opponentCooperateCount = 0;
        for (int i = 0; i < p2History.size(); i++) {
            // 81ターン以降はカウントしない
            if (i > 80) { break; }
            Card p2Move = p2History.get(i);
            if (p2Move == Card.COOPERATE) {
                opponentCooperateCount++;
            }
        }

        if (turn <= 80) { // 相手の「協力」率（1～前回ターン数までの合計）によって3パターンの行動を切り替える
            // 相手の「協力」率を算出
            double coopRatio = (double) opponentCooperateCount / turn; // opponentCooperateCountをdoubleに変換すれば小数の計算ができるようになる

            // パターン①：相手の「協力」率が30%未満
            if (coopRatio < COOPERATE_RATE_LOW) {
                System.out.println("→ 相手の「協力」率が30%未満 → 裏切りを出す");
                return Card.BETRAY;
            }
            // パターン②：相手の「協力」率が30%以上70％未満
            if (coopRatio < COOPERATE_RATE_HIGH) {
                Card lastMyMove = p1History.get(p1History.size() -1);
                System.out.println("→ 相手の「協力」率が30%以上70%未満 → 自分の前回の行動\"" + lastMyMove + "\"と逆の行動を取る");

                if (lastMyMove == Card.COOPERATE) { // 自分の前回の行動が「協力」→「裏切り」を出す
                    return Card.BETRAY;
                } else { // 自分の前回の行動が「裏切り」→「協力」を出す
                    return Card.COOPERATE;
                }
            }

            // パターン③：相手の「協力」率が70%以上
            System.out.println("→ 相手の「協力」率が70%以上 → 協力を出す");
            return Card.COOPERATE;
        }
        // 81～100ターン
        System.out.println("相手の「協力」回数（1〜80ターン）: " + opponentCooperateCount);

        // パターン①：相手の「協力」回数が59回以下
        if (opponentCooperateCount <= 59) {
            System.out.println("→ 相手の「協力」回数が59回以下 → すべて協力を出す");
            return Card.COOPERATE;
        } else { // パターン②：相手の「協力」回数が60回以上
            System.out.println("→ 相手の「協力」回数が60回以上 → すべて裏切りを出す");
            return Card.BETRAY;
        }
    }

    /**
     * 作戦2　吉田作戦
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
