package com.tr.tennis;

import com.tr.tennis.service.TennisStandardGameService;
import com.tr.tennis.service.TennisTieBreakGameService;
import com.tr.tennis.tool.DisplayUtil;
import com.tr.tennis.tool.TennisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class BaseTest {
    @MockBean
    protected TennisUtil tennisUtil;
    @MockBean
    protected DisplayUtil displayUtil;
    @Autowired
    protected TennisStandardGameService tennisStandardGameService;
    @Autowired
    protected TennisTieBreakGameService tennisTieBreakGameService;

}
