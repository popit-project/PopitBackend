package com.popit.popitproject.config;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;


    // @Scheduled(cron = "0 */5 * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    public void runJob () {
        // job parameter
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            log.info("batch run!");
            jobLauncher.run(batchConfig.job(), jobParameters);
        } catch (JobParametersInvalidException | JobInstanceAlreadyCompleteException
        | JobExecutionAlreadyRunningException | JobRestartException exception) {
            log.error(exception.getMessage());
        }
    }

    // 오픈, 종료 하루 전 알림
    @Scheduled(cron = "0 0 0 * * *")
    // @Scheduled(cron = "0 */5 * * * *")
    public void runOpeningOrClosingNotificationsJob() {
        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            log.info("오픈, 종료 알림료 run!");
            jobLauncher.run(batchConfig.sendOpeningOrClosingNotificationsJob(), jobParameters);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
}