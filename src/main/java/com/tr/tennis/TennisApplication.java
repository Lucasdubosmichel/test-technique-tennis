package com.tr.tennis;

import com.tr.tennis.domain.TennisMatch;
import com.tr.tennis.service.TennisMatchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TennisApplication implements CommandLineRunner {
    /**
     * Run 'TennisMatchTest' to test a match with tiebreak;
     *
     *  ---- General idea of this exercise: ------------
     *      * There is three levels of this exercise:
     *      *  Game (standard/tiebreak) -> Set -> Match
     *      * A chain function, if a game has a result, need to update the current set
     *      * the current set update will trigger the check for match update
     *
     * */
    private final TennisMatchService tennisMatchService;
    public static TennisMatch tennisMatch= new TennisMatch();

    public TennisApplication(TennisMatchService tennisMatchService) {
        this.tennisMatchService = tennisMatchService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TennisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.tennisMatchService.startPlay(tennisMatch);
    }
}
