package com.print.printing.Service;


import com.print.printing.repository.PdfProvider;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PrintPdfService {

    private final PdfScannerService pdfScannerService;
    private final PdfProvider pdfProvider;

    @Autowired
    public PrintPdfService(PdfScannerService pdfScannerService, PdfProvider pdfProvider) {
        this.pdfScannerService = pdfScannerService;
        this.pdfProvider = pdfProvider;
    }



    public byte[] getPdfBytesFromFile(String filePath) throws IOException {
        if (pdfProvider != null) {
            return pdfProvider.getPdfBytes(filePath);
        } else {
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        }
    }





}
