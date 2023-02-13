package com.tr.tennis.domain;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TennisSet {
    /**
     * This class stands is used for store the current set result
     * The current game can be a standard game or a tiebreak game
     * current set status equals STANDARD, this means a standard game is ongoing
     * current set status equals TIE_BREAK, this means a tie_break game is ongoing
     * when Tie-break has a result and game not finished, a default standard game will be launched
     * */
    private Score<Integer> currentSetScore = new Score<>(0, 0);
    private EnumSetStatus currentSetStatus = EnumSetStatus.STANDARD;
    //Current Game
    private Score<EnumStandardGamePoint> standard = new Score<>(EnumStandardGamePoint.ZERO, EnumStandardGamePoint.ZERO);
    //Current Game
    private Score<Integer> tieBreak = new Score<>(0, 0);
}
