package com.tr.tennis.service;

import com.tr.tennis.domain.*;
import com.tr.tennis.tool.DisplayUtil;
import com.tr.tennis.tool.TennisUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class TennisGameService<T> {
    protected final TennisUtil tennisUtil;
    protected final DisplayUtil displayUtil;
    public TennisGameService(TennisUtil tennisUtil, DisplayUtil displayUtil) {
        this.tennisUtil = tennisUtil;
        this.displayUtil = displayUtil;
    }
    /**
     * T can be EnumStandardGamePoint for standard game or Int for tiebreak game
     * if the first random number is larger than the second, we consider it as Player1 wins
     * else, Player 2 winds
     * */
    public TennisGame<T> initGame(Score<T> game){
        boolean isP1Winner = this.tennisUtil.isP1Winner();
        T originalPointWinner = isP1Winner ? game.getScorePlayer1()
                : game.getScorePlayer2();
        T originalPointLoser = isP1Winner ? game.getScorePlayer2()
                : game.getScorePlayer1();
        return new TennisGame<>(isP1Winner, originalPointWinner, originalPointLoser);
    }
    /**
     * Common function to check match status
     * If a player has won three game and more that the opponent, his wins and match ends
     * */
    public EnumMatchStatus updateMatchStatus(List<Score<Integer>> history) {
        EnumMatchStatus currentMatchStatus= EnumMatchStatus.IN_PROGRESS;
        if (history.size() >= 3) {
            Score<Integer> result = new Score<>(0, 0); //player 1 <-> player 2
            history.stream().forEach(score -> {
                if (score.getScorePlayer1() > score.getScorePlayer2())
                    result.setScorePlayer1(result.getScorePlayer1() + 1);
                else
                    result.setScorePlayer2(result.getScorePlayer2() + 1);
            });

            if (result.getScorePlayer1() == 3 && result.getScorePlayer2()< result.getScorePlayer1()) {
                currentMatchStatus= EnumMatchStatus.P1WINS;
            }
            if (result.getScorePlayer2() == 3 && result.getScorePlayer1()< result.getScorePlayer2()) {
                currentMatchStatus=EnumMatchStatus.P2WINS;
            }
        }
        return currentMatchStatus;
    }

    /**
     * This function is called when current set status equals SET_ENDS
     * update Match history, match status and reset current set
     * */
    public void handleSetEnd(TennisMatch tennisMatch){
        tennisMatch.getHistory().add(tennisMatch.getCurrentTennisSet().getCurrentSetScore());
        tennisMatch.setMatchStatus(updateMatchStatus(tennisMatch.getHistory()));
        tennisMatch.setCurrentTennisSet(new TennisSet());
    }
    /**
     * An abstract function which will be implemented by subclass
     * */
    abstract Score<T> updateGame(TennisMatch tennisMatch, TennisGame<T> game);
}
