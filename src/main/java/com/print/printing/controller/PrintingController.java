package com.print.printing.controller;

import com.print.printing.Service.PrintPdfService;
import com.print.printing.repository.PdfProvider;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;



@Getter
@RestController
@RequestMapping("/printpdf")
public class PrintingController {

    private final PrintPdfService printPdfService;

    @Autowired
    public PrintingController(PrintPdfService printPdfService) {
        this.printPdfService = printPdfService;
    }

    @PostMapping
    public ResponseEntity<Object> printPdf(@RequestBody PrintPdfRequest request) throws IOException, PrinterException {
        if ((request.getPdfFilePath() == null && request.getBase64EncodedPdf() == null) ||
                (request.getPdfFilePath() != null && request.getBase64EncodedPdf() != null)) {
            throw new IllegalArgumentException("Exactly one of pdfFilePath or base64EncodedPdf must be provided.");
        }

        byte[] pdfBytes;
        try {
            if (request.getPdfFilePath() != null) {
                pdfBytes = printPdfService.getPdfBytesFromFile(request.getPdfFilePath());
            } else {
                String base64EncodedPdf = request.getBase64EncodedPdf();
                Pattern base64Pattern = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
                if (!base64Pattern.matcher(base64EncodedPdf).matches()) {
                    return ResponseEntity.badRequest().body("Invalid base64 encoded PDF.");
                }
                pdfBytes = Base64.getDecoder().decode(base64EncodedPdf);
            }



            try (PDDocument document = PDDocument.load(pdfBytes)) {
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPageable(new PDFPageable(document));
                job.print();
                return ResponseEntity.ok("Printing process initiated.");
            } catch (PrinterException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Printing failed due to: " + e.getMessage());
            }
        } catch (IOException e) {
            // Handle file access or decoding errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to access or decode PDF: " + e.getMessage());
        }
    }
}

