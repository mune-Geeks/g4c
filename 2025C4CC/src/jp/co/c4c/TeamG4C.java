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

        // １ターン目は「協力」のカードを出す
        if (turn == 1) {
            return Card.COOPERATE;
        }

        // 怒りポイントの下限値
        final int ANGER_POINTS_MIN = 0;
        // 怒りポイントの上限値
        final int ANGER_POINTS_MAX = 3;
        // 怒りのライン
        final int ANGER_THRESHOLD = 1;

        // 怒りポイント初期値を設定
        int angerPoints = 0;

        // 相手の行動履歴を取得して怒りポイントを計算(ANGER_POINTS_MIN～ANGER_POINTS_MAXで変動)
        for (Card p2Card : p2History) {
            // 協力なら－１、裏切りなら＋１
            angerPoints += p2Card == Card.COOPERATE ? -1 : +1;

            // 怒りポイントが－１以下なら０を設定
            if (angerPoints < ANGER_POINTS_MIN) {
                angerPoints = ANGER_POINTS_MIN;
            }
            // 怒りポイントが４以上なら３を設定
            if (angerPoints > ANGER_POINTS_MAX) {
                angerPoints = ANGER_POINTS_MAX;
            }
        }

        // 怒りポイントがANGER_THRESHOLDを超えたら「裏切り」のカードを出す
        return angerPoints > ANGER_THRESHOLD ? Card.BETRAY : Card.COOPERATE;

    }

}
