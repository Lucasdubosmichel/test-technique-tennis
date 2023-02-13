package com.tr.tennis.tool;

import org.springframework.stereotype.Component;

@Component
public class TennisUtil {
    private double getRandomSeed() {
        return Math.random();
    }
    public boolean isP1Winner(){
        return getRandomSeed()>getRandomSeed();
    }

}
