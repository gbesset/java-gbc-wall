package com.gbcreation.wall.migration.tool.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.gbcreation.wall.migration.tool.items.CommentHeaderFooterCallBack;
import com.gbcreation.wall.migration.tool.items.ItemHeaderFooterCallBack;
import com.gbcreation.wall.migration.tool.items.MigrationSourceComment;
import com.gbcreation.wall.migration.tool.items.MigrationSourceItem;
import com.gbcreation.wall.migration.tool.items.SqlComment;
import com.gbcreation.wall.migration.tool.items.SqlItem;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	private static String EOF = System.getProperty("line.separator");
	private static String pathSource = "migrationTool/";
	private static String pathDestination = "migrationTool/migrated/";
	private static String fileName = "_demo.sql";
	//private static String fileName = "_prod.sql";
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<MigrationSourceItem> itemReader() {
        FlatFileItemReader<MigrationSourceItem> reader = new FlatFileItemReader<MigrationSourceItem>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource(pathSource + "item" + fileName));
        
        reader.setLineMapper(new DefaultLineMapper<MigrationSourceItem>() {{
	        	setLineTokenizer(new DelimitedLineTokenizer("', '") {{
	        		setNames(new String[] { "id-file", "description", "creationDate-ratio-ratioReverse-nbLike-type", "tags-end" });
	        	}});
	        	setFieldSetMapper(new BeanWrapperFieldSetMapper<MigrationSourceItem>() {{
	        		setTargetType(MigrationSourceItem.class);
	        	}});
        }});
        return reader;
    }
    
    @Bean
    public FlatFileItemReader<MigrationSourceComment> commentReader() {
        FlatFileItemReader<MigrationSourceComment> reader = new FlatFileItemReader<MigrationSourceComment>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource(pathSource + "comment" + fileName));
        
        reader.setLineMapper(new DefaultLineMapper<MigrationSourceComment>() {{
	        	setLineTokenizer(new DelimitedLineTokenizer(", ") {{
	        		setNames(new String[] { "TODO" });
	        	}});
	        	setFieldSetMapper(new BeanWrapperFieldSetMapper<MigrationSourceComment>() {{
	        		setTargetType(MigrationSourceComment.class);
	        	}});
        }});
        return reader;
    }

    @Bean
    public ItemMigrationToolProcessor itemProcessor() {
        return new ItemMigrationToolProcessor();
    }

    @Bean
    public CommentMigrationToolProcessor commentProcessor() {
        return new CommentMigrationToolProcessor();
    }
    
    @Bean
    public ItemWriter<SqlItem> itemWriter() {
    		FlatFileItemWriter<SqlItem> writer = new FlatFileItemWriter<SqlItem>();
    		
    		writer.setResource(new FileSystemResource(pathDestination + "item" + fileName));
    		writer.setLineSeparator("," + EOF);    		 

    		SqlLineAggregator<SqlItem> lineAggregator = new SqlLineAggregator<SqlItem>();
    		writer.setLineAggregator(lineAggregator); 
    		
    		//Setting header and footer.
    		ItemHeaderFooterCallBack headerFooterCallback = new ItemHeaderFooterCallBack();
    		writer.setHeaderCallback(headerFooterCallback);
    		writer.setFooterCallback(headerFooterCallback);

    		writer.setShouldDeleteIfExists(true);        

    		return writer;
    }
    
    @Bean
    public ItemWriter<SqlComment> commentWriter() {
    		FlatFileItemWriter<SqlComment> writer = new FlatFileItemWriter<SqlComment>();
    		
    		writer.setResource(new FileSystemResource(pathDestination + "comment" + fileName));
    		writer.setLineSeparator("," + EOF);    		 

    		SqlLineAggregator<SqlComment> lineAggregator = new SqlLineAggregator<SqlComment>();
    		writer.setLineAggregator(lineAggregator); 
    		
    		//Setting header and footer.
    		CommentHeaderFooterCallBack headerFooterCallback = new CommentHeaderFooterCallBack();
    		writer.setHeaderCallback(headerFooterCallback);
    		writer.setFooterCallback(headerFooterCallback);

    		writer.setShouldDeleteIfExists(true);        

    		return writer;
    }
    // end::readerwriterprocessor[]

    private LineAggregator<SqlItem> createItemLineAggregator() {
        DelimitedLineAggregator<SqlItem> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(", ");
 
        FieldExtractor<SqlItem> fieldExtractor = createItemFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);
 
        return lineAggregator;
    }
 
    private FieldExtractor<SqlItem> createItemFieldExtractor() {
        BeanWrapperFieldExtractor<SqlItem> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"id", "file", "path", "description"});
        return extractor;
    }
    
    // tag::jobstep[]
    @Bean
    public Job importItemJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("Migrate SQL v1 to V2")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
               // .next(step2())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1 : Item import")
                .<MigrationSourceItem, SqlItem> chunk(10)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }
    
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2 : Comment import")
                .<MigrationSourceComment, SqlComment> chunk(10)
                .reader(commentReader())
                .processor(commentProcessor())
                .writer(commentWriter())
                .build();
    }
    // end::jobstep[]
}
