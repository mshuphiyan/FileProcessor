package com.file.fileprocessor.facade;


import com.file.fileprocessor.service.SaveAsTxt;
import com.file.fileprocessor.service.SaveAsXls;
import com.file.fileprocessor.service.SaveAsXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileFacade {
    private static final String filename="transformed";

    @Value("${location}")
    private String location;

    @Autowired
    private SaveAsTxt saveAsTxt;

    @Autowired
    private SaveAsXls saveAsXls;

//    @Autowired
//    private SaveAsXml saveAsXml;

    public void writeTofile(){
        saveAsXls.saveFile("", filename);
//        saveAsXml.saveFile("", filename);
        saveAsTxt.saveFile("", filename);
    }


}
