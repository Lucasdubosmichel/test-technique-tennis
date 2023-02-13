package com.tr.tennis.service;

import com.tr.tennis.domain.*;
import com.tr.tennis.tool.DisplayUtil;
import com.tr.tennis.tool.TennisUtil;
import org.springframework.stereotype.Component;

@Component
public class TennisTieBreakGameService extends TennisGameService<Integer> {
    public TennisTieBreakGameService(TennisUtil tennisUtil, DisplayUtil displayUtil) {
        super(tennisUtil, displayUtil);
    }

    // Tie Break
    public void initTieBreakGame(TennisMatch tennisMatch) {
        tennisMatch.getCurrentTennisSet().setCurrentSetStatus(EnumSetStatus.TIE_BREAK);
        while(tennisMatch.getCurrentTennisSet().getCurrentSetStatus()==EnumSetStatus.TIE_BREAK){
            TennisGame<Integer> game= initGame(tennisMatch.getCurrentTennisSet().getTieBreak());
            Score<Integer> newPoints= updateGame(tennisMatch, game);
            tennisMatch.getCurrentTennisSet().setTieBreak(newPoints);
            this.displayUtil.displayPlayerInfo(tennisMatch);
            this.displayUtil.displayHistoryScore(tennisMatch);
            this.displayUtil.displayCurrentScore(tennisMatch);
            this.displayUtil.displayCurrentSetStatus(tennisMatch);
            this.displayUtil.displayTieBreakStatus(tennisMatch);
        }
    }

    @Override
    public Score<Integer> updateGame(TennisMatch tennisMatch, TennisGame<Integer> game) {
        tennisMatch.getCurrentTennisSet().setCurrentSetStatus(EnumSetStatus.TIE_BREAK);
        //update tie break score while knowing the game result
        if (game.isP1Winner()) {
            tennisMatch.getCurrentTennisSet().getTieBreak().setScorePlayer1(game.getScoreWinner() + 1);
        } else {
            tennisMatch.getCurrentTennisSet().getTieBreak().setScorePlayer2(game.getScoreWinner() + 1);
        }
        return updateTieBreakStatus(tennisMatch, game.isP1Winner());
    }

    public Score<Integer> updateTieBreakStatus(TennisMatch tennisMatch, boolean isP1Winner){
        Score<Integer> currentScore= tennisMatch.getCurrentTennisSet().getTieBreak();
        int diff = Math.abs(currentScore.getScorePlayer1()-currentScore.getScorePlayer2());
        if ((currentScore.getScorePlayer1()>= 7|| currentScore.getScorePlayer2()>=7) && diff >= 2) {
            //update current set score
            if (isP1Winner) {
                tennisMatch.getCurrentTennisSet().getCurrentSetScore()
                        .setScorePlayer1(tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1() + 1);
            } else {
                tennisMatch.getCurrentTennisSet().getCurrentSetScore()
                        .setScorePlayer2(tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2() + 1);
            }
            handleSetEnd(tennisMatch);
            tennisMatch.getCurrentTennisSet().setTieBreak(new Score<>(0, 0));
        }
        return tennisMatch.getCurrentTennisSet().getTieBreak();
    }
}
