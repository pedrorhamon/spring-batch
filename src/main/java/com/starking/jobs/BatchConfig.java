package com.starking.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import com.starking.jobs.BatchConfig.Pessoa;

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
	  public ItemReader<Pessoa> reader() {
	    return new FlatFileItemReaderBuilder<Pessoa>()
	        .name("pessoasFileReader")
	        .resource(new FileSystemResource("files/pessoas.csv"))
	        .delimited()
	        .names("nome", "email", "dataNascimento", "idade", "id")
	        .addComment("--")
	        .fieldSetMapper((FieldSet fieldSet) -> {
	          return new Pessoa(fieldSet.readLong("id"),
	              fieldSet.readString("nome"), fieldSet.readString("email"),
	              fieldSet.readString("dataNascimento"), fieldSet.readInt("idade"), null);
	        })
	        .build();
	  }
	
	
	record Pessoa(Long id, String nome, String email, String dataNascimento, Integer idade, String thumbnail) {
	}

}
