package com.tr.tennis.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSetStatus {
    STANDARD("Standard"),
    TIE_BREAK("TieBreak"),
    SET_END("Set ends");
    private String value;
}
