package com.tr.tennis.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TennisMatch {
    /**
     * This class is to note down the match process
     * history stands for the history score
     * currentTennisSet stands for the current set
     * match status stands for the match result (in_process, or has already a winner)
     * */
    private Player player1;
    private Player player2;
    private List<Score<Integer>> history = new ArrayList<>();
    private TennisSet currentTennisSet = new TennisSet();
    private EnumMatchStatus matchStatus = EnumMatchStatus.IN_PROGRESS;
}
