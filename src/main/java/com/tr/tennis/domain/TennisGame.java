package com.tr.tennis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TennisGame<T>{
    /**
     * T can be int for tie-break game, EnumStandardGamePoint for standard game
     * This class is to note down the beginScore of each player and the game result
     * */
    boolean isP1Winner;
    T scoreWinner;
    T scoreLoser;
}
