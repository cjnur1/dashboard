package com.example.dashboard.config;

import com.example.dashboard.OutPutRepo;
import com.example.dashboard.data.Listner;
import com.example.dashboard.data.MatchInput;
import com.example.dashboard.data.MatchProcessor;
import com.example.dashboard.model.MatchOutput;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
@EnableBatchProcessing
public class Configuration {
@Autowired
private JobBuilderFactory jobBuilderFactory;
@Autowired
OutPutRepo repo;
@Autowired
private StepBuilderFactory stepBuilderFactory;

private final String[]  FIELDS={ "id", "city", "date","player_of_match", "venue","neutral_venue", "team1","team2", "toss_winner","winner","toss_decision","result", "result_margin",
        " eliminator", "method","umpire1", "umpire2"};
@Bean
public FlatFileItemReader<MatchInput> reader()
{
    return new FlatFileItemReaderBuilder<MatchInput>()
                   .name("ipl-csv-reader")
                   .resource(new ClassPathResource("match-data.csv"))
                   .delimited()
                   .names(FIELDS)
                   .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInput>() {{
                       setTargetType(MatchInput.class);
                   }})
                   .build();
}
@Bean
public MatchProcessor processor() {
    return new MatchProcessor();
}

//@Bean
//public JdbcBatchItemWriter<MatchOutput> writer(DataSource dataSource) {
//    return new JdbcBatchItemWriterBuilder<MatchOutput>()
//                   .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//.sql("INSERT INTO match_output " +
//        "(id,city,date,player_of_match,venue,team1,team2,toss_winner,toss_decision,match_winner,result, result_margin,umpire1,umpire2) " +
// "VALUES (:id,:city,:date,:playerOfMatch,:venue,:team1,:team2,:tossWinner,:tossDecision,:matchWinner,:result, :resultMargin,:umpire1,:umpire2)")
//                   .dataSource(dataSource)
//                   .build();
//}
@Bean
public RepositoryItemWriter<MatchOutput> writer() {
    RepositoryItemWriter<MatchOutput> itemWriter = new RepositoryItemWriter<>();
    itemWriter.setRepository(repo);
    itemWriter.setMethodName("save");
    return itemWriter;
}
@Bean
public Job importUserJob(Listner listner,Step step1) {
    return jobBuilderFactory.get("importUserJob")
                   .incrementer(new RunIdIncrementer())
                   .listener(listner)
                   .flow(step1)
                   .end()
                   .build();
}

@Bean
public Step step1() {
    return stepBuilderFactory.get("step1")
                   .<MatchInput, MatchOutput> chunk(10)
                   .reader(reader())
                   .processor(processor())
                   .writer(writer())
                   .taskExecutor(excuter())
                   .build();
}

private TaskExecutor excuter() {
    SimpleAsyncTaskExecutor task=new SimpleAsyncTaskExecutor();
    task.setConcurrencyLimit(4);
return task;
}
}
