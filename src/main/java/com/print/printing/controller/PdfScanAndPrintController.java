package com.print.printing.controller;

import com.print.printing.Service.PdfDeletionService;
import com.print.printing.Service.PdfPrintingService;
import com.print.printing.Service.PdfScannerService;
import com.print.printing.Service.PrintPdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PdfScanAndPrintController {

    private final PdfScannerService pdfScannerService;
    private final PdfPrintingService pdfPrintingService;
    private final PdfDeletionService pdfDeletionService;

    public PdfScanAndPrintController(PdfScannerService pdfScannerService,
                                     PdfPrintingService pdfPrintingService,
                                     PdfDeletionService pdfDeletionService) {
        this.pdfScannerService = pdfScannerService;
        this.pdfPrintingService = pdfPrintingService;
        this.pdfDeletionService = pdfDeletionService;
    }

    @PostMapping("/scan-and-print")
    public ResponseEntity<String> scanAndPrintPdfs() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Starting scan and printing process");

        PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
        attributeSet.add(new MediaPrintableArea(0, 0, 75, 100, MediaPrintableArea.MM));

        // Check if default printer is configured
        if (!pdfPrintingService.isDefaultPrinterConfigured()) {
            logger.info("Default printer is not configured. Cannot proceed with printing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Default printer is not configured.");
        }

        // Check if printer is available
        if (!pdfPrintingService.isPrinterAvailable()) {
            logger.info("No printer available. Skipping printing process.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No printer found.");
        }

        // Scan PDFs only if a printer is configured
        List<File> scannedPdfs = pdfScannerService.scanPdfs();

        // If no PDFs found, return bad request
        if (scannedPdfs.isEmpty()) {
            logger.info("No PDFs found");
            return ResponseEntity.badRequest().body("No PDFs found.");
        }

        // Print each scanned PDF
        for (File pdf : scannedPdfs) {
            // Read the content of the PDF file into a byte array
            byte[] pdfBytes = pdfScannerService.convertPdfToByteArray(pdf);

            // Print the PDF
            boolean printingSuccessful = pdfPrintingService.printPdf(pdfBytes);

            if (printingSuccessful) {
                // If printing was successful, delete the file
                pdfDeletionService.deletePdf(pdf);
                logger.info("Printed PDF: {} deleted from {}", pdf.getName(), pdf.getPath());
            } else {
                logger.error("Failed to print PDF: {}", pdf.getName());
                // Handle the case where printing was not successful
                // You may want to log an error, return an appropriate response, or take other actions
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to print PDF: " + pdf.getName());
            }
        }

        logger.info("Scan and print process completed successfully");
        return ResponseEntity.ok("Scanned " + scannedPdfs.size() + ", printed " +
                scannedPdfs.size() + " and deleted " + scannedPdfs.size() + " PDFs.");
    }
}