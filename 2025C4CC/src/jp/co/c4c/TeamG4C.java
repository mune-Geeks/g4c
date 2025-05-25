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
        if (turn <= 100) {
            // 1～10ターン
            if (turn <= 10) {
                return Card.COOPERATE;
            }

            // 11～80ターン
            if (turn <= 80) { // 相手の「協力」の割合（1～前回ターン数までの合計）によって3パターンの行動を切り替える
                int previousCount = turn - 1;
                int opponentCooperateCount = 0;

                // 相手の「協力」回数をカウント
                for (int i = 0; i < previousCount; i++) {
                    Card p2Move = p2History.get(i);
                    if (p2Move == Card.COOPERATE) {
                        opponentCooperateCount++;
                    }
                }

                System.out.println("相手の「協力」回数: " + opponentCooperateCount + " / " + previousCount + "ターン");

                int lowLimit = previousCount * 3 / 10; // 相手の「協力」回数が30%未満
                int highLimit = previousCount * 7 / 10;// 相手の「協力」回数が70%以上

                // パターン①：相手の「協力」回数が30%未満
                if (opponentCooperateCount < lowLimit) {
                    System.out.println("→ 相手の「協力」回数が30%未満 → 裏切りを出す");
                    return Card.BETRAY;
                // パターン②：相手の「協力」回数が30%以上70％未満
                } else if (opponentCooperateCount < highLimit) {
                    Card lastMyMove = p1History.get(p1History.size() - 1);
                    System.out.println("→ 相手の「協力」回数が30%以上70%未満 → 自分の前回の行動\"" + lastMyMove + "\"と逆の行動を取る");

                    Card nextMove;
                    if (lastMyMove == Card.COOPERATE) { // 自分の前回の行動が「協力」→「裏切り」を出す
                        nextMove = Card.BETRAY;
                    } else { // 自分の前回の行動が「裏切り」→「協力」を出す
                        nextMove = Card.COOPERATE;
                    }
                    return nextMove;

                } else { // パターン③：相手の「協力」回数が70%以上
                    System.out.println("→ 相手の「協力」回数が70%以上 → 協力を出す");
                    return Card.COOPERATE;
                }
            }
            // 81～100ターン
            int opponentCooperateCount = 0;

            // 相手の「協力」回数によって2パターンの行動を切り替える※条件判定は初回のみで、以降はパターンを固定する
            for (int i = 0; i < 80; i++) {
                Card opponentMove = p2History.get(i);
                if (opponentMove == Card.COOPERATE) {
                    opponentCooperateCount++;
                }
            }

            System.out.println("相手の協力回数（1〜80ターン）: " + opponentCooperateCount);

            // パターン①：相手の「協力」回数が59回以下
            if (opponentCooperateCount <= 59) {
                System.out.println("→ 相手の「協力」回数が59回以下 → すべて協力を出す");
                return Card.COOPERATE;
            // パターン②：相手の「協力」回数が60回以上
            } else {
                System.out.println("→ 相手の「協力」回数が60回以上 → すべて裏切りを出す");
                return Card.BETRAY;
            }
        }
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
