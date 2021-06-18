package com.file.fileprocessor.batchProcessor;

import com.file.fileprocessor.model.CSVFileModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CSVFileProcessor implements ItemProcessor<CSVFileModel, CSVFileModel> {

    @Override
    public CSVFileModel process(CSVFileModel csvFileModel) throws Exception {
        //anzsic06,Area,year,geo_count,ec_count
        String anzsic06= csvFileModel.getAnzsic06();
        String area = csvFileModel.getArea();
        String year = csvFileModel.getYear();
        String geoCount = csvFileModel.getGeoCount();
        String ecCount = csvFileModel.getEcCount();

        CSVFileModel csvFileModelTransformed = new CSVFileModel(anzsic06, area, year, geoCount, ecCount);
        return csvFileModelTransformed;
    }
}
