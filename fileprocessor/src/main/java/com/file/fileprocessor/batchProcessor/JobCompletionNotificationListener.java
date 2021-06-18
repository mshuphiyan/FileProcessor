package com.file.fileprocessor.batchProcessor;

import com.file.fileprocessor.model.CSVFileModel;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
//            LOGGER.info("!!! JOB FINISHED! Time to verify the results");

            String query = "SELECT anzsic06,Area,file_year,geo_count,ec_count FROM csvfilemodel";
            jdbcTemplate.query(query, (rs, row) -> new CSVFileModel(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4), rs.getString(5)))
                    .forEach(coffee -> System.out.println("Found < {} > in the database."+ coffee));
        }
    }
}
