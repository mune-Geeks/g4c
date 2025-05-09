package jp.co.c4c;

import java.util.ArrayList;
import java.util.List;

/**
 * 繰り返しゲーム
 */
public class IteratedGame {
    /** カード */
    public enum Card {
        COOPERATE("協力"),
        BETRAY("裏切り");
        private final String label;
        Card(String label) {this.label = label; }
        public String getLabel() { return label; }
    }

    /**
     * ゲームメイン処理
     * @param args
     */
    public static void main(String[] args) {
        // 対戦するチームを指定
        TeamG4C player1 = new TeamG4C();
        TeamMegane player2 = new TeamMegane();
//        TeamB player2 = new TeamB();

        // ターン毎の履歴
        List<Card> p1History = new ArrayList<>();
        List<Card> p2History = new ArrayList<>();

        final int MAX_TURN = 200;
        for (int i = 1; i <= MAX_TURN; i++) {
            // 作戦を指定してカードを決定
            Card card1 = player1.tactics1(i, p1History, p2History);
            Card card2 = player2.tactics1(i, p1History, p2History);            

            // 行動をもとに報酬計算 
            calculateReward(i, card1, card2, player1, player2);
            
            // 履歴更新
            p1History.add(card1);
            p2History.add(card2);
        }
        
        System.out.println("=== 最終結果 ===");
        System.out.println("プレイヤー1の総獲得金額: " + player1.totalReward + "円");
        System.out.println("プレイヤー2の総獲得金額: " + player2.totalReward + "円");
    }
    
    /**
     * 報酬計算
     * @param turn
     * @param card1
     * @param card2
     * @param player1
     * @param player2
     */
    private static void calculateReward(int turn, Card card1, Card card2, TeamG4C player1, TeamMegane player2) {
        if (card1 == Card.COOPERATE && card2 == Card.COOPERATE) {
            player1.addReward(3000);
            player2.addReward(3000);
            System.out.println("ラウンド " + turn + ": 両者協力 → 両者に3000円");

        } else if (card1 == Card.BETRAY && card2 == Card.COOPERATE) {
            player1.addReward(5000);
            player2.addReward(0);
            System.out.println("ラウンド " + turn + ": プレイヤー1が裏切り、プレイヤー2が協力 → P1に5000円, P2に0円");

        } else if (card1 == Card.COOPERATE && card2 == Card.BETRAY) {
            player1.addReward(0);
            player2.addReward(5000);
            System.out.println("ラウンド " + turn + ": プレイヤー1が協力、プレイヤー2が裏切り → P1に0円, P2に5000円");

        } else {
            player1.addReward(0);
            player2.addReward(0);
            System.out.println("ラウンド " + turn + ": 両者裏切り → 両者に0円");
        }
    }
    
}
