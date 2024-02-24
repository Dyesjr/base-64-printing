package com.print.printing.controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PrintPdfRequest {

    private String pdfFilePath;
    private String base64EncodedPdf;



    public PrintPdfRequest(String pdfFilePath, String base64EncodedPdf) {
        this.pdfFilePath = pdfFilePath;
        this.base64EncodedPdf = base64EncodedPdf;
    }

}
