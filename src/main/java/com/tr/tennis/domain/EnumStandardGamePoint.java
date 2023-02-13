package com.tr.tennis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStandardGamePoint {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    DEUCE("Deuce"),
    ADVANTAGE("Advantage"),
    Player1Wins("Player1 Wins"),
    Player2Wins("Player2 Wins");

    private String value;

}
