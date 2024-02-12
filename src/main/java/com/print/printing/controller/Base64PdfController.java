package com.print.printing.controller;

import com.print.printing.repository.Base64PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Base64PdfController {

    private final Base64PdfService base64PdfService;

    @Autowired
    public Base64PdfController(Base64PdfService base64PdfService) {
        this.base64PdfService = base64PdfService;
    }

    @PostMapping("/printpdf/base64")
    public ResponseEntity<Object> printBase64Pdf(@RequestBody String base64EncodedPdf){
        return  base64PdfService.printBase64Pdf(base64EncodedPdf);
    }
}
