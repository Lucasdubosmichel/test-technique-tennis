package com.tr.tennis.service;
import com.tr.tennis.domain.*;
import com.tr.tennis.tool.DisplayUtil;
import com.tr.tennis.tool.TennisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TennisMatchService {
    private final TennisUtil tennisUtil;
    private final DisplayUtil displayUtil;
    private final TennisStandardGameService tennisStandardGameService;
    private final static Logger matchServiceLogger= LoggerFactory.getLogger(TennisMatchService.class);

    public TennisMatchService(TennisUtil tennisUtil, DisplayUtil displayUtil, TennisStandardGameService tennisStandardGameService) {
        this.tennisUtil = tennisUtil;
        this.displayUtil=displayUtil;
        this.tennisStandardGameService = tennisStandardGameService;
    }

    /**
     * Entry point
     * */
    public void startPlay(TennisMatch tennisMatch){
        tennisMatch.setPlayer1(new Player("Ling"));
        tennisMatch.setPlayer2(new Player("Anna"));
        this.tennisStandardGameService.initStandardGame(tennisMatch);
    }

}
