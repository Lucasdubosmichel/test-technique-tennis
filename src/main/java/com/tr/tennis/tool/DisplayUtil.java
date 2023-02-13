package com.tr.tennis.tool;

import com.tr.tennis.domain.Player;
import com.tr.tennis.domain.TennisGame;
import com.tr.tennis.domain.TennisMatch;
import org.springframework.stereotype.Component;

@Component
public class DisplayUtil {

    public void displayStandardGameStatus(TennisMatch tennisMatch) {
        System.out.printf("Current standard game status: %s - %s  \n",
                tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer1().getValue(),
                tennisMatch.getCurrentTennisSet().getStandard().getScorePlayer2().getValue());

    }
    public void displayTieBreakStatus(TennisMatch tennisMatch){
        System.out.printf("Current tie-break status : %s - %s \n", tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer1(),
                tennisMatch.getCurrentTennisSet().getTieBreak().getScorePlayer2());
    }
    public void displayMatchStatus(TennisMatch tennisMatch){
        System.out.printf( "Match status: %s \n",
                tennisMatch.getMatchStatus().getValue());
    }

    public void displayPlayerInfo(TennisMatch tennisMatch){
        System.out.print("\n");
        System.out.printf( "Player 1: %s \n", tennisMatch.getPlayer1().getName());
        System.out.printf( "Player 2: %s \n", tennisMatch.getPlayer2().getName());
    }

    public void displayCurrentSetStatus(TennisMatch tennisMatch){
        System.out.printf( "Current set status: %s \n", tennisMatch.getCurrentTennisSet().getCurrentSetStatus());
    }
    public void displayHistoryScore(TennisMatch tennisMatch){
        System.out.print("History Score : ");
        tennisMatch.getHistory().stream().forEach(score -> System.out.printf("( %s , %s ) ",
                score.getScorePlayer1(), score.getScorePlayer2()));
        System.out.print("\n");
    }

    public void displayCurrentScore(TennisMatch tennisMatch){
        System.out.printf("Current Score : ( %s : %s ) \n",
                tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer1(),
                tennisMatch.getCurrentTennisSet().getCurrentSetScore().getScorePlayer2());
    }
}
