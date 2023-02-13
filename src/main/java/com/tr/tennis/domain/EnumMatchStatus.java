package com.tr.tennis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumMatchStatus {
    IN_PROGRESS("In progress"),
    P1WINS("Player 1 wins"),
    P2WINS("Player 2 wins");

    private String value;
}
