package com.tr.tennis.service;

import com.tr.tennis.domain.*;
import com.tr.tennis.tool.DisplayUtil;
import com.tr.tennis.tool.TennisUtil;
import org.springframework.stereotype.Component;

@Component
public class TennisStandardGameService extends TennisGameService<EnumStandardGamePoint>{
    private final TennisTieBreakGameService tennisTieBreakGameService;
    public TennisStandardGameService(TennisUtil tennisUtil, DisplayUtil displayUtil, TennisTieBreakGameService tennisTieBreakGameService) {
        super(tennisUtil, displayUtil);
        this.tennisTieBreakGameService = tennisTieBreakGameService;
    }
    /**
     * Keep on generate new standard game until match has a result or tie break game is ongoing
     * */
    public void initStandardGame(TennisMatch tennisMatch){
        while(tennisMatch.getMatchStatus()== EnumMatchStatus.IN_PROGRESS
                && tennisMatch.getCurrentTennisSet().getCurrentSetStatus()!=EnumSetStatus.TIE_BREAK){
            //need to init a new game
            Score<EnumStandardGamePoint> initStandardGame= tennisMatch.getCurrentTennisSet().getStandard();
            TennisGame<EnumStandardGamePoint> game = initGame(initStandardGame);
            Score<EnumStandardGamePoint> newPoints = updateGame(tennisMatch, game);
            tennisMatch.getCurrentTennisSet().setStandard(newPoints);
            //Display the status of this match
            super.displayUtil.displayPlayerInfo(tennisMatch);
            this.displayUtil.displayHistoryScore(tennisMatch);
            this.displayUtil.displayCurrentScore(tennisMatch);
            this.displayUtil.displayCurrentSetStatus(tennisMatch);
            this.displayUtil.displayStandardGameStatus(tennisMatch);
            this.displayUtil.displayMatchStatus(tennisMatch);
        }
    }
    /**
     * There is three levels of this exercise:
     *  Game (standard/tiebreak) -> Set -> Match
     * A chain function, if a game has a result, need to update the current set
     * the current set update will trigger the check for match update
     * */
    @Override
    public Score<EnumStandardGamePoint> updateGame(TennisMatch tennisMatch, TennisGame<EnumStandardGamePoint> game) {
        // Calculate points with standardStrategy
        EnumStandardGamePoint winnerNewPoint= updateGameStatus(game.getScoreWinner(), game.getScoreLoser(), game.isP1Winner());
        Score<EnumStandardGamePoint> newPoints= updateGameStrategy(game.getScoreLoser(), game.isP1Winner(), winnerNewPoint);
        // TennisSet New Scores
        switch (winnerNewPoint){
            case Player1Wins:
            case Player2Wins:
                updateCurrentSetScore(tennisMatch, tennisMatch.getCurrentTennisSet().getCurrentSetScore(), game.isP1Winner());
                break;
            default:
                break;
        }
        return newPoints;
    }
    /**
     * A state machine of standard game, the next of ZERO is FIFTEEN, etc...
     * ZERO -> FIFTEEN -> THIRTY -> FORTY -> Advantage <-> DEUCE
     *                                          |
     *                                    ->  Win
     *
     * */
    public EnumStandardGamePoint updateGameStatus(EnumStandardGamePoint winnerPoint, EnumStandardGamePoint loserPoint, boolean isP1Winner){
        //Calculate next score
        EnumStandardGamePoint next = EnumStandardGamePoint.ZERO;
        switch (winnerPoint) {
            case ZERO:
                next = EnumStandardGamePoint.FIFTEEN;
                break;
            case FIFTEEN:
                next = EnumStandardGamePoint.THIRTY;
                break;
            case THIRTY:
                next = EnumStandardGamePoint.FORTY;
                break;
            case DEUCE:
                next = EnumStandardGamePoint.ADVANTAGE;
                break;
            case FORTY:
            case ADVANTAGE: {
                if (EnumStandardGamePoint.FORTY.equals(winnerPoint) && EnumStandardGamePoint.FORTY.equals(loserPoint))
                    next = EnumStandardGamePoint.ADVANTAGE;
                else if (EnumStandardGamePoint.FORTY.equals(winnerPoint) && EnumStandardGamePoint.ADVANTAGE.equals(loserPoint))
                    next = EnumStandardGamePoint.DEUCE;
                else {
                    next= isP1Winner? EnumStandardGamePoint.Player1Wins: EnumStandardGamePoint.Player2Wins;
                }
                break;
            }
        }
        return next;
    }
    /**
     * Refresh standard game score
     * */
    public Score<EnumStandardGamePoint> updateGameStrategy(EnumStandardGamePoint loserScore, boolean isP1Winner, EnumStandardGamePoint newScore) {
        //Keep loser original score and update winner with its new score
        Score<EnumStandardGamePoint> newGameScore=new Score<>();
        if (EnumStandardGamePoint.ADVANTAGE.equals(newScore)) {
            newGameScore.setScorePlayer1(newScore);
            newGameScore.setScorePlayer2(EnumStandardGamePoint.FORTY);
        } else if (EnumStandardGamePoint.DEUCE.equals(newScore)) {
            newGameScore.setScorePlayer1(newScore);
            newGameScore.setScorePlayer2(newScore);
        } else if (EnumStandardGamePoint.Player1Wins.equals(newScore)||EnumStandardGamePoint.Player2Wins.equals(newScore)) {
            newGameScore.setScorePlayer1(EnumStandardGamePoint.ZERO);
            newGameScore.setScorePlayer2(EnumStandardGamePoint.ZERO);
        } else {
            if(isP1Winner) {
                newGameScore.setScorePlayer1(newScore);
                newGameScore.setScorePlayer2(loserScore);
            } else {
            newGameScore.setScorePlayer1(loserScore);
            newGameScore.setScorePlayer2(newScore);
            }
        }
        return newGameScore;
    }

    /**
     * Calculate the set status
     * if the winner has at least 6 points and at least 2 in advance, he wins the set
     * if both players has 6 points, next game will be a tie-break instead of standard game
     * */
    public EnumSetStatus updateSetStatus(Score<Integer> before, boolean isP1Winner){
        EnumSetStatus currentSetStatus= EnumSetStatus.STANDARD;
        int winnerScore = isP1Winner?before.getScorePlayer1(): before.getScorePlayer2();
        int dif = Math.abs(before.getScorePlayer1()- before.getScorePlayer2());
        if(winnerScore>=6){
            if(dif>=2) currentSetStatus=EnumSetStatus.SET_END;
            if(dif==0) currentSetStatus=EnumSetStatus.TIE_BREAK;
        }
        return currentSetStatus;
    }
    /**
     * Guide for handle different set status
     * when score is 6 - 6, a tie break game will be launched instead of a standard game
     * when set has a winner, the handler function will update the history
     * */
    public Score<Integer> updateCurrentSetScore(TennisMatch tennisMatch, Score<Integer> before, boolean isP1Winner) {
        Score<Integer> newScore = before;
        EnumSetStatus currentSetStatus=updateSetStatus(newScore, isP1Winner);
        switch (currentSetStatus){
            case STANDARD:
                if (isP1Winner) {
                    newScore.setScorePlayer1(before.getScorePlayer1() + 1);
                } else {
                    newScore.setScorePlayer2(before.getScorePlayer2() + 1);
                }
                currentSetStatus=updateSetStatus(newScore,isP1Winner);
                if(currentSetStatus==EnumSetStatus.SET_END){
                    handleSetEnd(tennisMatch);
                    break;
                } else if (currentSetStatus==EnumSetStatus.TIE_BREAK) {
                    this.tennisTieBreakGameService.initTieBreakGame(tennisMatch);
                    break;
                }
                break;
        }
       return newScore;
    }
}
