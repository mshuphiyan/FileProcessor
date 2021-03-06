package com.file.fileprocessor.configuration;

import com.file.fileprocessor.batchProcessor.CSVFileProcessor;
import com.file.fileprocessor.batchProcessor.JobCompletionNotificationListener;
import com.file.fileprocessor.model.CSVFileModel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${file.input}")
    private String inputFile;

    @Bean
    public FlatFileItemReader<CSVFileModel> reader(){
        return new FlatFileItemReaderBuilder<CSVFileModel>().name(CSVFileModel.class.getName())
                .resource(new PathResource(inputFile))
                .delimited()
                .names(new String[] { "anzsic06","Area","year","geo_count","ec_count"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CSVFileModel>(){{
                    setTargetType(CSVFileModel.class);
                }}).build();
    }

    @Bean
    public CSVFileProcessor processor(){
        return new CSVFileProcessor();
    }

//    @Bean
//    public JdbcBatchItemWriter<CSVFileModel> writer(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<CSVFileModel>().itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("INSERT INTO csvfilemodel (anzsic06,Area,file_year,geo_count,ec_count) VALUES (:anzsic06, :area, :year, :geoCount, :ecCount)")
//                .dataSource(dataSource)
//                .build();
//    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<CSVFileModel, CSVFileModel> chunk(10)
                .reader(reader())
                .writer(txtFileWriter())
                .processor(processor())
                .build();
    }

    @Bean
    public StaxEventItemWriter<CSVFileModel> xmlFileWriter(){
        StaxEventItemWriter<CSVFileModel> writer = new StaxEventItemWriter<>();
        writer.setResource(new FileSystemResource("/Volumes/Data/Work/report.xml"));
        writer.setMarshaller(reportUnmarshaller());
        writer.setRootTagName("report");

        return writer;
    }

    @Bean
    public XStreamMarshaller reportUnmarshaller(){
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap<String, Class>();
        aliases.put("report", CSVFileModel.class);
        unMarshaller.setAliases(aliases);
        return unMarshaller;
    }

    @Bean
    public FlatFileItemWriter txtFileWriter() {
        DelimitedLineAggregator<CSVFileModel> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);

        return  new FlatFileItemWriterBuilder<CSVFileModel>()
                .name("txtFileWriter")
                .resource(new FileSystemResource("/Volumes/Data/Work/report.txt"))
                .lineAggregator(lineAggregator)
                .build();
    }
}
