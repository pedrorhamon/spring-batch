package com.starking.jobs;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

/**
 * @author pedroRhamon
 */

@Configuration
public class BatchConfig {
	
	private JobRepository jobRepository;
	private PlatformTransactionManager transactionManager;
	private RestTemplate restTemplate;
	
	public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, RestTemplate restTemplate) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.restTemplate = restTemplate;
	}

}
