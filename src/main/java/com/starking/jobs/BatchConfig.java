package com.starking.jobs;

import java.util.concurrent.Future;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
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
	
	@Bean
	public Job importarClientesJob(JobRepository jobRepository, Step importaClientesStep) {
		return new JobBuilder("importaClientStep", jobRepository)
				.start(importaClientesStep)
				.build();
	}
	
	@Bean
	public Step importaClientesStep(ItemReader<Pessoa> reader, ItemProcessor<Pessoa, Future<Pessoa>> processor, ItemWriter<Future<Pessoa>> writer) {
		
	}
	
	
	record Pessoa(Long id, String nome, String email, String dataNascimento, Integer idade, String thumbnail) {
	}

}
