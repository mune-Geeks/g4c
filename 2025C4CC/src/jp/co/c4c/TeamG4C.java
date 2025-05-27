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
     *
     * @param turn 現在のターン数
     * @param p1History 自分（プレイヤー1）の行動履歴
     * @param p2History 相手（プレイヤー2）の行動履歴
     * @return 次の行動（協力または裏切り）
     */
    public Card tactics1(int turn, List<Card> p1History, List<Card> p2History) {
        Random random = new Random();

        // 1〜100ターン：鈴木作戦（仮）→ ランダムで協力または裏切り
        if (turn <= 100) {
            return random.nextBoolean() ? Card.COOPERATE : Card.BETRAY;
        }

        // --- フェーズ境界の定義 ---
        final int PHASE1_START = 101;
        final int PHASE1_2_START = 106;
        final int PHASE1_3_START = 131;
        final int PHASE2_START = 151;
        final int PHASE3_START = 156;
        final int PHASE4_START = 161;

        // --- 協力確率（0〜99） ---
        final int COOP_PROB_85 = 85;
        final int COOP_PROB_50 = 50;
        final int COOP_PROB_25 = 25;
        final int COOP_PROB_65 = 65;
        final int COOP_PROB_30 = 30;
        final int COOP_PROB_15 = 15;

        // --- フェーズ1：101〜150 ---
        if (turn < PHASE2_START) {
            // フェーズ1-1：101〜105 → 無条件で協力
            if (turn < PHASE1_2_START) {
                return Card.COOPERATE;
            }

            // フェーズ1-2：106〜130 → 「協力・協力・裏切り」の3ターンパターン
            if (turn < PHASE1_3_START) {
                Card[] pattern = { Card.COOPERATE, Card.COOPERATE, Card.BETRAY };
                int patternIndex = (turn - PHASE1_2_START) % 3;
                return pattern[patternIndex];
            }

            // フェーズ1-3：131〜150 → 相手の直近10ターンの協力率で確率選択

            int recentTurns = 10;

            int rawStart = turn - recentTurns - 1;
            int rawEnd = turn - 1;

            int startIdx = Math.max(0, rawStart);
            int endIdx = Math.min(p2History.size(), rawEnd);

            if (endIdx > startIdx) {
                int coopCount = 0;
                for (int i = startIdx; i < endIdx; i++) {
                    if (p2History.get(i) == Card.COOPERATE) {
                        coopCount++;
                    }
                }

                double coopRate = (double) coopCount / (endIdx - startIdx);

                if (coopRate >= 0.7) {
                    return (random.nextInt(100) < COOP_PROB_85) ? Card.COOPERATE : Card.BETRAY;
                } else if (coopRate >= 0.5) {
                    return (random.nextInt(100) < COOP_PROB_50) ? Card.COOPERATE : Card.BETRAY;
                } else {
                    return (random.nextInt(100) < COOP_PROB_25) ? Card.COOPERATE : Card.BETRAY;
                }
            }
            return Card.COOPERATE; // フォールバック
        }

        // --- フェーズ2：151〜155 → 無条件で協力 ---
        if (turn < PHASE3_START) {
            return Card.COOPERATE;
        }

        // --- フェーズ3：156〜160 ---
        if (turn < PHASE4_START) {
            // 直前フェーズ（151〜155）での相手の協力回数をカウント
            int phase2CoopCount = 0;
            for (int i = PHASE2_START - 1; i < PHASE3_START - 1 && i < p2History.size(); i++) {
                if (p2History.get(i) == Card.COOPERATE) {
                    phase2CoopCount++;
                }
            }

            // 4回以上協力されていれば全面協力
            if (phase2CoopCount >= 4) {
                return Card.COOPERATE;
            }

            // 最初のターン（156）は協力
            if (turn == PHASE3_START) {
                return Card.COOPERATE;
            }

            // 156〜現在までの裏切り回数をカウント
            int betrayCountFromPhase3 = 0;
            for (int i = PHASE3_START - 1; i < turn - 1 && i < p2History.size(); i++) {
                if (p2History.get(i) == Card.BETRAY) {
                    betrayCountFromPhase3++;
                }
            }

            // 2回以上裏切られたら裏切る
            return (betrayCountFromPhase3 >= 2) ? Card.BETRAY : Card.COOPERATE;
        }

        // --- フェーズ4：161〜200 ---
        int totalBetrayCount = 0;
        for (int i = PHASE2_START - 1; i < turn - 1 && i < p2History.size(); i++) {
            if (p2History.get(i) == Card.BETRAY) {
                totalBetrayCount++;
            }
        }

        if (totalBetrayCount <= 2) {
            return Card.COOPERATE;
        } else if (totalBetrayCount <= 4) {
            // 直近2ターン連続裏切りチェック
            if (p2History.size() >= 2) {
                int lastIdx = p2History.size() - 1;
                int secondLastIdx = p2History.size() - 2;
                if (p2History.get(lastIdx) == Card.BETRAY && p2History.get(secondLastIdx) == Card.BETRAY) {
                    return Card.BETRAY; // 強い報復（簡略化で1ターンのみ）
                }
            }
            return (random.nextInt(100) < COOP_PROB_65) ? Card.COOPERATE : Card.BETRAY;
        } else if (totalBetrayCount < 9) {
            return (random.nextInt(100) < COOP_PROB_30) ? Card.COOPERATE : Card.BETRAY;
        } else {
            // 相手が直近4ターン協力しているかチェック
            if (p2History.size() >= 4) {
                boolean fourConsecutiveCoop = true;
                for (int i = p2History.size() - 4; i < p2History.size(); i++) {
                    if (p2History.get(i) != Card.COOPERATE) {
                        fourConsecutiveCoop = false;
                        break;
                    }
                }
                if (fourConsecutiveCoop) {
                    return Card.COOPERATE;
                }
            }
            return (random.nextInt(100) < COOP_PROB_15) ? Card.COOPERATE : Card.BETRAY;
        }
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
