package com.tr.tennis;
import com.tr.tennis.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
class TennisTieBreakGameTest extends BaseTest{
    private Score<Integer> beginningScore;
    @Test
    void test_initTieBreakGame(){
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        beginningScore=new Score<>(0,0);
        TennisGame<Integer> game= this.tennisTieBreakGameService.initGame(beginningScore);
        Assertions.assertSame(true,game.isP1Winner());
        Assertions.assertSame(0,game.getScoreWinner());
        Assertions.assertSame(0,game.getScoreLoser());
    }
    @Test
    void test_tieBreakGame_BeginningScore0to0_EndingScore1to0_P1WinsTheFirstPoint(){
        /*
         * init tie-break game
         * History:
         * Current score: (6,6)
         * Current Tie-break game score: 0 - 0
         * */
        TennisMatch tennisMatch= new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(6,6));
        tennisMatch.setCurrentTennisSet(currentSet);
        /*
         * After one round, P1 wins the first point
         * History:
         * Current score: (6,6)
         * Current Tie-break game score: 1 - 0
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        beginningScore=tennisMatch.getCurrentTennisSet().getTieBreak();
        Assertions.assertSame(0,beginningScore.getScorePlayer1());
        Assertions.assertSame(0,beginningScore.getScorePlayer2());

        TennisGame<Integer> game= this.tennisTieBreakGameService.initGame(beginningScore);
        Score<Integer> score= this.tennisTieBreakGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(1,score.getScorePlayer1());
        Assertions.assertSame(0,score.getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.TIE_BREAK,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
        Assertions.assertSame(6,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(6,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());

        /*Assert Tie break game changed*/
        Assertions.assertSame(1,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer2());

        /*Assert Standard game is not changed*/
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer2());
    }
    @Test
    void test_tieBreakGame_BeginningScore1to0_EndingScore1to1_P2WinsTheSecondPoint(){
        /*
         * init tie-break game
         * History:
         * Current score: (6,6)
         * Current Tie-break game score: 1 - 0
         * */
        TennisMatch tennisMatch= new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(6,6));
        currentSet.setTieBreak(new Score<>(1,0));
        tennisMatch.setCurrentTennisSet(currentSet);
        /*
         * After one round, P2 wins the first point
         * History:
         * Current score: (6,6)
         * Current Tie-break game score: 1 - 1
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(false);
        beginningScore=tennisMatch.getCurrentTennisSet().getTieBreak();
        Assertions.assertSame(1,beginningScore.getScorePlayer1());
        Assertions.assertSame(0,beginningScore.getScorePlayer2());

        TennisGame<Integer> game= this.tennisTieBreakGameService.initGame(beginningScore);
        Score<Integer> score= this.tennisTieBreakGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(1,score.getScorePlayer1());
        Assertions.assertSame(1,score.getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.TIE_BREAK,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
        Assertions.assertSame(6,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(6,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());

        /*Assert Tie break game changed*/
        Assertions.assertSame(1,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer1());
        Assertions.assertSame(1,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer2());

        /*Assert Standard game is not changed*/
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer2());
    }
    @Test
    void test_tieBreakGame_BeginningScore7to6_P1WinsTieBreak(){
        /*
         * init tie-break game
         * History:
         * Current score: (6,6)
         * Current Tie-break game score: 7 - 6
         * Tie break condition, dif(player1, player2) > 2
         */
        TennisMatch tennisMatch= new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(6,6));
        currentSet.setTieBreak(new Score<>(7,6)); /* beginning score*/
        tennisMatch.setCurrentTennisSet(currentSet);
        /*
         * Based on the beginning score, P1 wins another point
         * History: (7,6)
         * Current score: (0,0)
         * Current Tie-break game score: 0 - 0
         */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        beginningScore=tennisMatch.getCurrentTennisSet().getTieBreak();
        Assertions.assertSame(7, beginningScore.getScorePlayer1());
        Assertions.assertSame(6, beginningScore.getScorePlayer2());

        TennisGame<Integer> game= this.tennisTieBreakGameService.initGame(beginningScore);
        Score<Integer> score= this.tennisTieBreakGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(0,score.getScorePlayer1());
        Assertions.assertSame(0,score.getScorePlayer2());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());

        /*Assert Tie break game changed back to 0 - 0*/
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer2());

        /*Assert Standard game is not changed*/
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer2());

        Assertions.assertSame(1, tennisMatch.getHistory().size());
        Assertions.assertSame(7, tennisMatch.getHistory().get(0).getScorePlayer1());
        Assertions.assertSame(6, tennisMatch.getHistory().get(0).getScorePlayer2());

        //When tie break game end, Set status is set back to STANDARD instead of TIE
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());

    }

    @Test
    void test_tieBreakGame_P1WinsTheMatchByWinningTieBreak(){
        /*
         * init tie-break game
         * History: (6,4), (6,3)
         * Current score: (6,6)
         * Current Tie-break game score: 6 - 2
         * Tie break condition, dif(player1, player2) > 2, winner score >=7
         */
        TennisMatch tennisMatch= new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(6,6));
        currentSet.setTieBreak(new Score<>(6,2)); /* beginning score*/
        tennisMatch.setCurrentTennisSet(currentSet);
        List<Score<Integer>> history=new ArrayList<>();
        history.add(new Score<>(6,4));
        history.add(new Score<>(6,3));
        tennisMatch.setHistory(history);
        /*
         * Based on the beginning score, P1 wins another point and finally P1 wins this match
         * History: (6,4), (6,3), (7,6)
         * Current score: (0,0)
         * Current Tie-break game score: 0 - 0
         */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        beginningScore=tennisMatch.getCurrentTennisSet().getTieBreak();
        Assertions.assertSame(6,beginningScore.getScorePlayer1());
        Assertions.assertSame(2,beginningScore.getScorePlayer2());

        TennisGame<Integer> game= this.tennisTieBreakGameService.initGame(beginningScore);
        Score<Integer> score= this.tennisTieBreakGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(6, game.getScoreWinner());
        Assertions.assertSame(2, game.getScoreLoser());
        Assertions.assertSame(0,score.getScorePlayer1());
        Assertions.assertSame(0,score.getScorePlayer2());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());

        /*Assert Tie break game changed back to 0 - 0*/
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer2());

        /*Assert Standard game is not changed*/
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer2());

        Assertions.assertSame(3, tennisMatch.getHistory().size());
        Assertions.assertSame(7, tennisMatch.getHistory().get(2).getScorePlayer1());
        Assertions.assertSame(6, tennisMatch.getHistory().get(2).getScorePlayer2());

        //When tie break game end, Set status is set back to STANDARD instead of TIE
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());

        //Assert P1 win the match
        Assertions.assertSame(EnumMatchStatus.P1WINS, tennisMatch.getMatchStatus());

    }

}
