package com.print.printing.controller;

public class PrintPdfRequest {

    private String pdfFilePath;
    private String base64EncodedPdf;

    // Constructors, getters, and setters

    public PrintPdfRequest() {
    }

    public PrintPdfRequest(String pdfFilePath, String base64EncodedPdf) {
        this.pdfFilePath = pdfFilePath;
        this.base64EncodedPdf = base64EncodedPdf;
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public String getBase64EncodedPdf() {
        return base64EncodedPdf;
    }

    public void setBase64EncodedPdf(String base64EncodedPdf) {
        this.base64EncodedPdf = base64EncodedPdf;
    }
}
