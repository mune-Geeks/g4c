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
