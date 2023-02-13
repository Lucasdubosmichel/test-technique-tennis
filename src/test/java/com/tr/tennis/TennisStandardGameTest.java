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
class TennisStandardGameTest extends BaseTest{
    private EnumStandardGamePoint winner;
    private EnumStandardGamePoint loser;
    private Score<EnumStandardGamePoint> currentScore;
    @Test
    void test_initStandardGame(){
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        currentScore=new Score<>(EnumStandardGamePoint.FORTY,EnumStandardGamePoint.ADVANTAGE);
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Assertions.assertTrue(game.isP1Winner());
        Assertions.assertSame(EnumStandardGamePoint.FORTY,game.getScoreWinner());
        Assertions.assertSame(EnumStandardGamePoint.ADVANTAGE, game.getScoreLoser());
    }
    @Test
    void test_updateSetStatus_SetEnd(){
        Score<Integer> currentScore= new Score<>(6,2);
        EnumSetStatus currentSetStatus = tennisStandardGameService.updateSetStatus(currentScore,true);
        Assertions.assertSame(EnumSetStatus.SET_END,currentSetStatus);
    }
    @Test
    void test_updateSetStatus_Standard(){
        Score<Integer> currentScore= new Score<>(6,5);
        EnumSetStatus currentSetStatus = tennisStandardGameService.updateSetStatus(currentScore,true);
        Assertions.assertSame(EnumSetStatus.STANDARD,currentSetStatus);
    }
    @Test
    void test_updateSetStatus_Tie(){
        Score<Integer> currentScore= new Score<>(6,6);
        EnumSetStatus currentSetStatus = tennisStandardGameService.updateSetStatus(currentScore,false);
        Assertions.assertSame(EnumSetStatus.TIE_BREAK,currentSetStatus);
    }
    @Test
    void test_updateGameStatus_ZeroToFifty() {
        winner=EnumStandardGamePoint.ZERO;
        loser=EnumStandardGamePoint.ZERO;
        EnumStandardGamePoint newGamePoint =super.tennisStandardGameService.updateGameStatus(winner,loser,true);
        Assertions.assertSame(EnumStandardGamePoint.FIFTEEN,newGamePoint);
    }
    @Test
    void test_updateGameStatus_FifteenToThirty() {
        winner=EnumStandardGamePoint.FIFTEEN;
        loser=EnumStandardGamePoint.FIFTEEN;
        EnumStandardGamePoint newGamePoint =this.tennisStandardGameService.updateGameStatus(winner,loser,false);
        Assertions.assertSame(EnumStandardGamePoint.THIRTY, newGamePoint);
    }
    @Test
    void test_updateGameStatus_ThirtyToForty() {
        winner=EnumStandardGamePoint.THIRTY;
        loser=EnumStandardGamePoint.FORTY;
        EnumStandardGamePoint newGamePoint =this.tennisStandardGameService.updateGameStatus(winner,loser,true);
        Assertions.assertSame(EnumStandardGamePoint.FORTY, newGamePoint);
    }
    @Test
    void test_updateGameStatus_FortyToAdvantage() {
        winner = EnumStandardGamePoint.FORTY;
        loser = EnumStandardGamePoint.FORTY;
        EnumStandardGamePoint newGamePoint = this.tennisStandardGameService.updateGameStatus(winner, loser, true);
        Assertions.assertSame( EnumStandardGamePoint.ADVANTAGE, newGamePoint);
    }
    @Test
    void test_updateGameStatus_FromAdvantageToDeuce() {
        winner = EnumStandardGamePoint.FORTY;
        loser = EnumStandardGamePoint.ADVANTAGE;
        EnumStandardGamePoint newGamePoint = this.tennisStandardGameService.updateGameStatus(winner, loser, false);
        Assertions.assertSame(EnumStandardGamePoint.DEUCE, newGamePoint);
    }
    @Test
    void test_updateGameStatus_FromDeuceToAdvantage() {
        winner = EnumStandardGamePoint.DEUCE;
        loser = EnumStandardGamePoint.DEUCE;
        EnumStandardGamePoint newGamePoint = this.tennisStandardGameService.updateGameStatus(winner, loser, false);
        Assertions.assertSame(EnumStandardGamePoint.ADVANTAGE, newGamePoint);
    }

    @Test
    void test_updateGameStatus_FromAdvantageToP1WinAGame() {
        winner = EnumStandardGamePoint.ADVANTAGE;
        loser = EnumStandardGamePoint.FORTY;
        EnumStandardGamePoint newGamePoint = this.tennisStandardGameService.updateGameStatus(winner, loser, true);
        Assertions.assertSame(EnumStandardGamePoint.Player1Wins, newGamePoint);
    }
    @Test
    void test_updateGameStatus_FromFortyToP2WinAGame() {
        winner = EnumStandardGamePoint.FORTY;
        loser = EnumStandardGamePoint.FIFTEEN;
        EnumStandardGamePoint newGamePoint = this.tennisStandardGameService.updateGameStatus(winner, loser, false);
        Assertions.assertSame(EnumStandardGamePoint.Player2Wins,newGamePoint);
    }
    @Test
    void test_updateGame_FirstSetFirstGame_P1WinFirstGame(){
        /* Init match
         *
         * History:
         * Current Score: (0 : 0)
         * Current Game: 0 - 0
         **/
        TennisMatch tennisMatch =new TennisMatch();
        /*
         *  Init a new standard game,
         *  P1 wins the game
         *
         * History:
         * Current Score: ( 0 : 0)
         * Current Game: 0 - 15
         * Current set status: Standard
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true); /* P1 wins this standard game*/
        currentScore=tennisMatch.getCurrentTennisSet().getStandard(); /* Get original score of P1 and P2 */
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Score<EnumStandardGamePoint> score = this.tennisStandardGameService.updateGame(tennisMatch,game);

        Assertions.assertSame(EnumStandardGamePoint.FIFTEEN,score.getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer2());

        Assertions.assertSame(EnumMatchStatus.IN_PROGRESS,tennisMatch.getMatchStatus());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
        Assertions.assertSame(0, tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());
        Assertions.assertSame(0,tennisMatch.getHistory().size());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }
    @Test
    void test_updateGame_FirstSetLastGame_P2WinFirstSet(){
        /*
         * History:
         * Current Score: ( 2: 5)
         * Current Game: 40 - Advantage
         **/
        TennisMatch tennisMatch =new TennisMatch();
        TennisSet currentSet =new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(2,5));
        currentSet.setStandard(new Score<>(EnumStandardGamePoint.FORTY, EnumStandardGamePoint.ADVANTAGE));
        tennisMatch.setCurrentTennisSet(currentSet);
        /* Result
         *  Init a new standard game,
         *  P2 wins the set
         *
         * History:(2 : 6)
         * Current Score: ( 0 : 0)
         * Current Game: 0 - 0
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(false); /* P2 wins the game */
        currentScore=tennisMatch.getCurrentTennisSet().getStandard(); /* Get original score */
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Score<EnumStandardGamePoint> score = this.tennisStandardGameService.updateGame(tennisMatch,game);

        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer2());

        Assertions.assertSame(EnumMatchStatus.IN_PROGRESS,tennisMatch.getMatchStatus());
        Assertions.assertSame(1,tennisMatch.getHistory().size());
        Assertions.assertSame(2,tennisMatch.getHistory().get(0).getScorePlayer1());
        Assertions.assertSame(6,tennisMatch.getHistory().get(0).getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }
    @Test
    void test_updateGame_SecondSetFirstGame_P1WinsTheGame(){
        /*
         * History: (4,6)
         * Current Score: ( 0: 0)
         * Current Game: Advantage - 40
         **/
        TennisMatch tennisMatch =new TennisMatch();
        TennisSet currentSet =new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(0,0));
        currentSet.setStandard(new Score<>(EnumStandardGamePoint.ADVANTAGE, EnumStandardGamePoint.FORTY));
        tennisMatch.setCurrentTennisSet(currentSet);

        Score<Integer> first= new Score<>(4,6);
        List<Score<Integer>> history= new ArrayList<>();
        history.add(first);
        tennisMatch.setHistory(history);
        /* Result
         *  Init a new standard game,
         *  P2 wins the set
         *
         * History:(4 : 6)
         * Current Score: ( 1 : 0)
         * Current Game: 0 - 0
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true); /* P1 wins the game */
        currentScore=tennisMatch.getCurrentTennisSet().getStandard(); /* Get original score */
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Score<EnumStandardGamePoint> score = this.tennisStandardGameService.updateGame(tennisMatch,game);

        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer2());

        Assertions.assertSame(EnumMatchStatus.IN_PROGRESS,tennisMatch.getMatchStatus());
        Assertions.assertSame(1,tennisMatch.getHistory().size());
        Assertions.assertSame(4,tennisMatch.getHistory().get(0).getScorePlayer1());
        Assertions.assertSame(6,tennisMatch.getHistory().get(0).getScorePlayer2());

        Assertions.assertSame(1,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }
    @Test
    void test_updateGame_ThirdSetLastGame_P1WinsTheMatchAtTheThirdSet(){
        /* Init match
         *
         * History: (6: 1), ( 6: 2)
         * Current Score: (5 : 2)
         * Current Game: Advantage - 40
         **/
        TennisMatch tennisMatch =new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(5,2));
        currentSet.setStandard(new Score<>(EnumStandardGamePoint.ADVANTAGE, EnumStandardGamePoint.FORTY));
        tennisMatch.setCurrentTennisSet(currentSet);
        Score<Integer> first= new Score<>(6,1);
        Score<Integer> second= new Score<>(6,2);
        List<Score<Integer>> history= new ArrayList<>();
        history.add(first);
        history.add(second);
        tennisMatch.setHistory(history);
        /*
        * Init a new standard game,
        *  P1 wins the game and P1 wins the match,
        *  current score should be reset to zero
        *
        * History: (6: 1), ( 6: 2), (6 : 2)
        * Current Score: (0 : 0)
        * P1 wins
        * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        currentScore=tennisMatch.getCurrentTennisSet().getStandard();
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Score<EnumStandardGamePoint> score = this.tennisStandardGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(EnumMatchStatus.P1WINS,tennisMatch.getMatchStatus());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer2());
        Assertions.assertSame(3,tennisMatch.getHistory().size());
        Assertions.assertSame(6,tennisMatch.getHistory().get(2).getScorePlayer1());
        Assertions.assertSame(2,tennisMatch.getHistory().get(2).getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }
    @Test
    void test_updateGame_FourthSetLastGame_P2WinsTheMatchAtTheFourthSet(){
        /* Init match
         *
         * History: (6: 1), (2: 6), (1,6)
         * Current Score: (5 : 6)
         * Current Game: 40 - Advantage
         **/
        TennisMatch tennisMatch =new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(5,6));
        currentSet.setStandard(new Score<>(EnumStandardGamePoint.FORTY, EnumStandardGamePoint.ADVANTAGE));
        tennisMatch.setCurrentTennisSet(currentSet);

        Score<Integer> first= new Score<>(6,1);
        Score<Integer> second= new Score<>(2,6);
        Score<Integer> third= new Score<>(1,6);
        List<Score<Integer>> history= new ArrayList<>();
        history.add(first);
        history.add(second);
        history.add(third);
        tennisMatch.setHistory(history);
        /*
         * Init a new standard game,
         *  P2 wins the game and P2 wins the match,
         *  current score should be reset to zero
         *
         * History: (6: 1), ( 2: 6), (1 : 6), (5, 7)
         * Current Score: (0 : 0)
         * P2 wins
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(false);
        currentScore=tennisMatch.getCurrentTennisSet().getStandard();
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Score<EnumStandardGamePoint> score = this.tennisStandardGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(EnumMatchStatus.P2WINS,tennisMatch.getMatchStatus());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer2());
        Assertions.assertSame(4,tennisMatch.getHistory().size());
        Assertions.assertSame(5,tennisMatch.getHistory().get(3).getScorePlayer1());
        Assertions.assertSame(7,tennisMatch.getHistory().get(3).getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }
    @Test
    void test_updateGame_FifthSetLastGame_P1WinsTheMatchAtTheFifthSet(){
        /* Init match
         *
         * History: (6: 1), (2: 6), (1,6), (6,4)
         * Current Score: (5 : 2)
         * Current Game: 40 - 30
         **/
        TennisMatch tennisMatch =new TennisMatch();
        TennisSet currentSet=new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(5,2));
        currentSet.setStandard(new Score<>(EnumStandardGamePoint.FORTY, EnumStandardGamePoint.THIRTY));
        tennisMatch.setCurrentTennisSet(currentSet);

        Score<Integer> first= new Score<>(6,1);
        Score<Integer> second= new Score<>(2,6);
        Score<Integer> third= new Score<>(1,6);
        Score<Integer> fourth=new Score<>(6,4);
        List<Score<Integer>> history= new ArrayList<>();
        history.add(first);
        history.add(second);
        history.add(third);
        history.add(fourth);
        tennisMatch.setHistory(history);
        /*
         * Init a new standard game,
         *  P1 wins the game and P2 wins the match,
         *  current score should be reset to zero
         *
         * History: (6: 1), ( 2: 6), (1 : 6), (6, 4), (6,2)
         * Current Score: (0 : 0)
         * P1 wins
         * */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(true);
        currentScore=tennisMatch.getCurrentTennisSet().getStandard();
        TennisGame<EnumStandardGamePoint> game= this.tennisStandardGameService.initGame(currentScore);
        Score<EnumStandardGamePoint> score = this.tennisStandardGameService.updateGame(tennisMatch,game);
        Assertions.assertSame(EnumMatchStatus.P1WINS,tennisMatch.getMatchStatus());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer1());
        Assertions.assertSame(EnumStandardGamePoint.ZERO,score.getScorePlayer2());
        Assertions.assertSame(5,tennisMatch.getHistory().size());

        Assertions.assertSame(6,tennisMatch.getHistory().get(4).getScorePlayer1());
        Assertions.assertSame(2,tennisMatch.getHistory().get(4).getScorePlayer2());

        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1());
        Assertions.assertSame(0,tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());
        Assertions.assertSame(EnumSetStatus.STANDARD,tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }

}
