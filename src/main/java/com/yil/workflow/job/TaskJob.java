/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.job;

import com.yil.workflow.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class TaskJob {

    private final TaskService taskService;

    /**
     * 1 saatte bir işleri kapat
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 60 * 60 * 1000)
    public void closeTasks() {
        try {
            System.out.println("********************");
            System.out.println("Task Close Job Start");
            System.out.println(new Date());
            taskService.closedTask();
            System.out.println(new Date());
            System.out.println("Task Close Job End");
            System.out.println("********************");
        } catch (Exception e) {
            System.out.println("Task Close Job Error");
            e.printStackTrace();
        }
    }

}
