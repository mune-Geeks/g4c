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
        final int TACTICS1_1_TURN_LIMIT = 100;

        if (turn <= TACTICS1_1_TURN_LIMIT) {
            return tactics1_1(turn, p1History, p2History);
        }
        return tactics1_2(turn, p1History, p2History);
    }

    /**
     * 作戦1-1 鈴木作戦
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics1_1(int turn, List<Card> p1History, List<Card> p2History) {
        // フェーズ1の境界値
        final int EARLY_TURN_LIMIT = 10;
        // 協力的でないと見なす協力率の下限
        final double COOPERATE_RATE_LOW = 0.3;
        // 協力的と見なす協力率の上限
        final double COOPERATE_RATE_HIGH = 0.7;

        // 1~10ターン
        if (turn <= EARLY_TURN_LIMIT) {
            return Card.COOPERATE;
        }

        // 11~80ターン
        int opponentCooperateCount = 0;
        for (int i = 0; i < p2History.size(); i++) {
            // 81ターン以降はカウントしない
            if (i > 80) { break; }
            Card p2Move = p2History.get(i);
            if (p2Move == Card.COOPERATE) {
                opponentCooperateCount++;
            }
        }

        // 相手の「協力」率（1～前回ターン数までの合計）によって3パターンの行動を切り替える
        if (turn <= 80) {
            // 相手の「協力」率を算出
            double coopRatio = (double) opponentCooperateCount / turn;

            // パターン1:相手の「協力」率が30%未満
            if (coopRatio < COOPERATE_RATE_LOW) {
                return Card.BETRAY;
            }
            // パターン2:相手の「協力」率が30%以上70％未満
            if (coopRatio < COOPERATE_RATE_HIGH) {
                Card lastMyMove = p1History.get(p1History.size() - 1);
                if (lastMyMove == Card.COOPERATE) { // 自分の前回の行動が「協力」→「裏切り」を出す
                    return Card.BETRAY;
                } else { // 自分の前回の行動が「裏切り」→「協力」を出す
                    return Card.COOPERATE;
                }
            }
            // パターン3:相手の「協力」率が70%以上
            return Card.COOPERATE;
        }

        // 81~100ターン
        if (opponentCooperateCount <= 59) { // 相手の「協力」回数が59回以下
            return Card.COOPERATE;
        } else {
            return Card.BETRAY;
        }
    }

    /**
     * 作戦1-2 シヌアン作戦
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics1_2(int turn, List<Card> p1History, List<Card> p2History) {
        Random random = new Random();

        // フェーズ切り替えのターン境界値を定義
        final int PHASE1_2_START = 106;
        final int PHASE1_3_START = 131;
        final int PHASE2_START = 151;
        final int PHASE3_START = 156;
        final int PHASE4_START = 161;

        // フェーズ1:ターン101~150の処理
        if (turn < PHASE2_START) {
            // ターン101~105は必ず「協力」
            if (turn < PHASE1_2_START) {
                return Card.COOPERATE;
            }

            // ターン106~130は「協力」2回→「裏切り」1回のパターン繰り返し
            if (turn < PHASE1_3_START) {
                final Card[] COOPERATION_PATTERN = { Card.COOPERATE, Card.COOPERATE, Card.BETRAY };
                int patternIndex = (turn - PHASE1_2_START) % 3;
                return COOPERATION_PATTERN[patternIndex];
            }

            // ターン131~150は信頼度ベースの確率的戦略
            // 直近10ターンの相手の「協力」率を分析して次の行動を決定
            final int EVALUATION_WINDOW = 10;
            final double HIGH_TRUST_THRESHOLD = 0.7; // 高信頼度の閾値
            final double MEDIUM_TRUST_THRESHOLD = 0.5; // 中信頼度の閾値

            final int HIGH_TRUST_COOP_RATE = 85; // 高信頼時の「協力」確率
            final int MEDIUM_TRUST_COOP_RATE = 50; // 中信頼時の「協力」確率
            final int LOW_TRUST_COOP_RATE = 25; // 低信頼時の「協力」確率

            // 直近のターンでの「協力」回数をカウント
            int cooperationCount = 0;
            int historySize = p2History.size();

            // 直近10ターン（または履歴が10ターン未満の場合は全履歴）をチェック
            for (int i = 0; i < EVALUATION_WINDOW && i < historySize; i++) {
                // 履歴の後ろから順番にチェック（最新のものから）
                if (p2History.get(historySize - 1 - i) == Card.COOPERATE) {
                    cooperationCount++;
                }
            }

            // 「協力」率を計算（0.0~1.0の値）
            double cooperationRatio = (double) cooperationCount / Math.min(EVALUATION_WINDOW, historySize);

            // 信頼レベルに基づいて「協力」確率を決定
            if (cooperationRatio >= HIGH_TRUST_THRESHOLD) {
                // 高信頼:相手が70%以上「協力」している場合
                return (random.nextInt(100) < HIGH_TRUST_COOP_RATE) ? Card.COOPERATE : Card.BETRAY;
            } else if (cooperationRatio >= MEDIUM_TRUST_THRESHOLD) {
                // 中信頼:相手が50~70%「協力」している場合
                return (random.nextInt(100) < MEDIUM_TRUST_COOP_RATE) ? Card.COOPERATE : Card.BETRAY;
            } else {
                // 低信頼:相手の「協力」が50%未満の場合
                return (random.nextInt(100) < LOW_TRUST_COOP_RATE) ? Card.COOPERATE : Card.BETRAY;
            }
        }

        // ターン151~155は全て「協力」
        if (turn < PHASE3_START) {
            return Card.COOPERATE;
        }

        // フェーズ3:ターン156~160の処理
        // フェーズ2での相手の行動を分析して戦略を調整
        if (turn < PHASE4_START) {
            final int COOPERATION_THRESHOLD = 4; // 協力と判断する最低回数
            final int BETRAYAL_THRESHOLD = 1; // 裏切りと判断する最低回数

            // フェーズ2（151~155ターン）での相手の行動を集計
            int cooperationCount = 0;
            int betrayalCount = 0;

            // フェーズ2の範囲をループして集計
            for (int i = (PHASE2_START - 1); i < (PHASE3_START - 1); i++) {
                if (p2History.get(i) == Card.COOPERATE) {
                    cooperationCount++;
                } else {
                    betrayalCount++;
                }
            }

            // 相手が4回以上「協力」した場合は信頼して「協力」
            if (cooperationCount >= COOPERATION_THRESHOLD) {
                return Card.COOPERATE;
            }

            // 相手の「協力」が3回以下の場合は「裏切り」回数で判断
            // 相手の「裏切り」が2回以上なら報復として「裏切り」、それ以外は「協力」
            return (betrayalCount > BETRAYAL_THRESHOLD) ? Card.BETRAY : Card.COOPERATE;
        }

        // フェーズ4:ターン161以降の処理
        // 151ターン目から現在までの累積裏切り回数で戦略を決定

        // 151ターン目から直前ターンまでの相手の累積裏切り回数を計算
        int totalBetrayalCount = 0;
        boolean isRecentTwoBetrayals = true; // 直前2ターンが連続裏切りかのフラグ
        boolean isRecentFourCooperations = true; // 直前4ターンが連続協力かのフラグ

        // 151ターン目から現在の履歴サイズまでループ
        for (int i = (PHASE2_START - 1); i < p2History.size(); i++) {
            if (p2History.get(i) == Card.BETRAY) {
                totalBetrayalCount++;
            }

            // 直前2ターンの連続裏切りチェック
            if (i >= (p2History.size() - 2)) {
                // 一つでも協力があったらフラグをfalseに
                isRecentTwoBetrayals &= (p2History.get(i) == Card.BETRAY);
            }

            // 直前4ターンの連続協力チェック
            if (i >= (p2History.size() - 4)) {
                // 一つでも裏切りがあったらフラグをfalseに
                isRecentFourCooperations &= (p2History.get(i) == Card.COOPERATE);
            }
        }

        // パターン分類のための裏切り回数の境界値
        final int PATTERN1_BETRAY_LIMIT = 2;
        final int PATTERN2_BETRAY_LIMIT = 4;
        final int PATTERN3_BETRAY_LIMIT = 8;

        // 各パターンでの協力確率
        final int PATTERN2_COOP_RATE = 65;
        final int PATTERN3_COOP_RATE = 30;
        final int PATTERN4_COOP_RATE = 15;

        // パターン1:累積裏切り回数が2回以下の場合
        if (totalBetrayalCount <= PATTERN1_BETRAY_LIMIT) {
            return Card.COOPERATE;
        }

        // パターン2:累積裏切り回数が3~4回の場合
        if (totalBetrayalCount <= PATTERN2_BETRAY_LIMIT) {
            // 直前2ターンが連続裏切りなら報復として「裏切り」
            if (isRecentTwoBetrayals) {
                return Card.BETRAY;
            }
            // それ以外は65%の確率で「協力」
            return (random.nextInt(100) < PATTERN2_COOP_RATE) ? Card.COOPERATE : Card.BETRAY;
        }

        // パターン3:累積裏切り回数が5~8回の場合
        if (totalBetrayalCount <= PATTERN3_BETRAY_LIMIT) {
            // 30%の確率で「協力」、70%の確率で「裏切り」
            return (random.nextInt(100) < PATTERN3_COOP_RATE) ? Card.COOPERATE : Card.BETRAY;
        }

        // パターン4:累積裏切り回数が9回以上の場合
        if (isRecentFourCooperations) {
            return Card.COOPERATE;
        }

        // それ以外は15%の確率で「協力」、85%の確率で「裏切り」
        return (random.nextInt(100) < PATTERN4_COOP_RATE) ? Card.COOPERATE : Card.BETRAY;
    }

    /**
     * 作戦2 吉田作戦
     * @param turn
     * @param p1History
     * @param p2History
     * @return
     */
    public Card tactics2(int turn, List<Card> p1History, List<Card> p2History) {
        // フェーズ切り替えのターン境界値を定義
        final int PHASE1_END_TURN = 50;
        final int COOPERATION_THRESHOLD = 25;
        final int PHASE3_STR_TURN = 52;

        // 相手の合計協力回数
        int cooperateCount = 0;
        // 相手の協力率
        double cooperateRate = 0.00;
        // 相手の連続協力回数
        int consecutiveCooperateCount = 0;
        // 相手の連続裏切り回数
        int consecutiveBetrayalCount = 0;
        // 相手の協力率の境界線
        final double COOPERATION_RATE_THRESHOLD = 0.5;
        // 直近ターン
        final int RECENT_TURN_COUNT = 3;

        // フェーズ1
        if (turn <= PHASE1_END_TURN) {
            return Card.BETRAY;
        }

        // 相手の「協力」回数をカウント
        for (int i = 0; i < p2History.size(); i++) {
            if (p2History.get(i) == Card.COOPERATE) {
                cooperateCount++;
            }

            // フェーズ3のときの直近3ターンの結果をカウント
            if (turn >= PHASE3_STR_TURN - RECENT_TURN_COUNT) {
                if (p2History.get(i) == Card.COOPERATE) {
                    consecutiveCooperateCount++;
                } else {
                    consecutiveBetrayalCount++;
                }
            }
        }

        // フェーズ2
        if (turn == PHASE1_END_TURN + 1) {
            // 相手の「協力」回数が25回以下だった場合、「裏切り」
            if (cooperateCount <= COOPERATION_THRESHOLD) {
                return Card.BETRAY;
            } else {
                return Card.COOPERATE;
            }
        }

        // フェーズ3
        cooperateRate = (double) cooperateCount / p2History.size();
        // 「協力」率が50%以上だった場合
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
     * 作戦3 錦作戦
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
