package com.n26.exercise.controller;

import com.n26.exercise.model.Statistics;
import com.n26.exercise.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class TransactionsStatisticsController {


    @Autowired
    private StatisticsService statisticsService;



    @GetMapping("/statistics")
    public Statistics getStatistics() {
        return statisticsService.getStatistics();
    }

}
