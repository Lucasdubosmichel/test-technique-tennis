package com.tr.tennis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Score<T> {
    /**
     * T can be int for tiebreak or enum for standard game, int for set result
     * Score can be used for note down tiebreak game result/ standard game result/ set result
     * */
    private T scorePlayer1;
    private T scorePlayer2;
}
