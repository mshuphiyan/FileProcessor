package com.file.fileprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CSVFileModel {
    private String anzsic06;
    private String area;
    private String year;
    private String geoCount;
    private String ecCount;
}
