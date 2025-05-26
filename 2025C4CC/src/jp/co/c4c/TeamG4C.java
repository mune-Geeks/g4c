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

        // 相手の行動履歴リストをpreviousMoveに設定
        final List<Card> previousMove = p2History;

        // 相手の行動履歴リストの要素数を取得
        final int previousMoveSize = previousMove.size();

        // 怒りポイント初期値を設定
        int angerPoints = 0;

        // 最初のターンから現在のターンまでの相手の行動履歴を取得して怒りポイントを計算
        for (int i = 0; i < previousMoveSize; i++) {
            // 協力なら－１、裏切りなら＋１
            angerPoints = previousMove.get(i) == Card.COOPERATE ? angerPoints -1 : angerPoints + 1;
            // 怒りポイントが－１以下なら０を設定
            if (angerPoints < 0) {
                angerPoints = 0;
            }
            // 怒りポイントが４以上なら３を設定
            if (angerPoints > 3) {
                angerPoints = 3;
            }
        }

        // 怒りポイントが２以上なら「裏切り」、２未満なら「協力」を返す
        return angerPoints > 1 ? Card.BETRAY : Card.COOPERATE;

    }

}
