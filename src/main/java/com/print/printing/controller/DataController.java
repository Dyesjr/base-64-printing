package com.print.printing.controller;

import com.print.printing.Service.DataService;
import com.print.printing.Service.pdfGenerator;
import com.print.printing.model.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
//import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;

@RestController
public class DataController {

    @Autowired
    private pdfGenerator pdfGenerator;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private DataService dataService;

    @PostMapping("/generatepdf")
    public String generatePDF(@RequestBody List<user> user) throws IOException {
        String finalhtml = null;

        Context data = dataService.setData(user);
        finalhtml = springTemplateEngine.process("index", data);
        pdfGenerator.generateAndPrintPDF(finalhtml);

        return "success";



    }
}
