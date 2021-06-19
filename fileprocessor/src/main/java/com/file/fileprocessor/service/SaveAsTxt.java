package com.file.fileprocessor.service;

import org.springframework.stereotype.Component;

@Component
public class SaveAsTxt implements FileSaveAs{
    private static final String fileExtension = "txt";

    @Override
    public void saveFile(String location, String fileName) {

    }
}
