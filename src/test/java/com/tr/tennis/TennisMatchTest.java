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
class TennisMatchTest extends BaseTest{

    @Test
    void test_playTennisGame_beginsWithHistory_6to1_P2AlwaysWins() {
        /*
         * Init status
         * History: (6:1)
         * Current score: (6:5)
         * Current game score: 0 - 0
         * */
        TennisMatch tennisMatch = new TennisMatch();
        TennisSet currentSet = new TennisSet();
        currentSet.setCurrentSetScore(new Score<>(6, 5));
        currentSet.setStandard(new Score<>(EnumStandardGamePoint.ZERO, EnumStandardGamePoint.ZERO));
        tennisMatch.setCurrentTennisSet(currentSet);
        List<Score<Integer>> history = new ArrayList<>();
        history.add(new Score<>(6, 1));
        tennisMatch.setHistory(history);
        /*
         * Automatically play the game with P2 always wins as an condition
         * History: (6:1), (6,7), (0,6), (0,6)
         * Current score: (0,0)
         * Current game status: 0 - 0
         * Match status: P2 wins
         *  */
        Mockito.when(tennisUtil.isP1Winner()).thenReturn(false);
        this.tennisStandardGameService.initStandardGame(tennisMatch);

        Assertions.assertSame(EnumMatchStatus.P2WINS, tennisMatch.getMatchStatus());
        Assertions.assertSame(4, tennisMatch.getHistory().size());

        Assertions.assertSame(6, tennisMatch.getHistory().get(0).getScorePlayer1());
        Assertions.assertSame(1, tennisMatch.getHistory().get(0).getScorePlayer2());

        Assertions.assertSame(6, tennisMatch.getHistory().get(1).getScorePlayer1());
        Assertions.assertSame(7, tennisMatch.getHistory().get(1).getScorePlayer2());

        Assertions.assertSame(0, tennisMatch.getHistory().get(2).getScorePlayer1());
        Assertions.assertSame(6, tennisMatch.getHistory().get(2).getScorePlayer2());

        Assertions.assertSame(0, tennisMatch.getHistory().get(3).getScorePlayer1());
        Assertions.assertSame(6, tennisMatch.getHistory().get(3).getScorePlayer2());
    }
}
