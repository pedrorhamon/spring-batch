package com.starking.jobs;

import java.util.concurrent.Future;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
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
	  
	  @Bean
	  public ItemProcessor<Pessoa, Future<Pessoa>> asyncProcessor(ItemProcessor<Pessoa, Pessoa> itemProcessor,
	      TaskExecutor taskExecutor) {
	    var asyncProcessor = new AsyncItemProcessor<Pessoa, Pessoa>();
	    asyncProcessor.setTaskExecutor(taskExecutor);
	    asyncProcessor.setDelegate(itemProcessor);
	    return asyncProcessor;
	  }
	  
	  @Bean
	  public ItemProcessor<Pessoa, Pessoa> processor() {
	    return pessoa -> {
	      var uri = "https://jsonplaceholder.typicode.com/photos/" + pessoa.id();
	      var photo = restTemplate.getForObject(uri, Photo.class);
	      var newPessoa = new Pessoa(pessoa.id(), pessoa.nome(), pessoa.email(), pessoa.dataNascimento(), pessoa.idade(),
	          photo.thumbnailUrl());
	      return newPessoa;
	    };
	  }
	  
	  @Bean
	  public ItemWriter<Future<Pessoa>> asyncWriter(ItemWriter<Pessoa> writer) {
	    var asyncWriter = new AsyncItemWriter<Pessoa>();
	    asyncWriter.setDelegate(writer);
	    return asyncWriter;
	  }
	
	
	record Pessoa(Long id, String nome, String email, String dataNascimento, Integer idade, String thumbnail) {}
	
	record Photo(Long id, String thumbnailUrl) {}

}
