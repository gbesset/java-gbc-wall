package com.gbcreation.wall.migration.tool.batch;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import com.gbcreation.wall.model.Item;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {

			log.info("!!! JOB FINISHED !!! Time to verify the results");
			log.info("!!! ATTENTION !!! mettre à jour la BDD pour que le prochain ID soit correct.....");

			List<Item> results = Collections.emptyList(); 

			for (Item it : results) {
				log.info("Found <" + it + "> in the database.");
			}

		}
	}
}
