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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@RestController
@RequestMapping("/api")
public class PdfScanAndPrintController {

    private final PdfScannerService pdfScannerService;
    private final PdfPrintingService pdfPrintingService;
    private final PdfDeletionService pdfDeletionService;

    private final PrintPdfService printPdfService;


    public PdfScanAndPrintController(PdfScannerService pdfScannerService,
                                     PdfPrintingService pdfPrintingService,
                                     PdfDeletionService pdfDeletionService,
                                     PrintPdfService printPdfService) {
        this.pdfScannerService = pdfScannerService;
        this.pdfPrintingService = pdfPrintingService;
        this.pdfDeletionService = pdfDeletionService;
        this.printPdfService = printPdfService;
    }

    @PostMapping("/scan-and-print")


    public ResponseEntity<String> scanAndPrintPdfs() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Starting scan and printing process");

        if (!pdfPrintingService.isDefaultPrinterConfigured()) {
            logger.info("Default printer is not configured. Cannot proceed with printing.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Default printer is not configured.");
        }

        if (pdfPrintingService.isPrinterAvailable()) {
            logger.info("No printer available. Skipping printing process.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No printer found.");
        }

        List<File> scannedPdfs = pdfScannerService.scanPdfs();

        if (scannedPdfs.isEmpty()) {
            logger.info("No PDFs found");
            return ResponseEntity.badRequest().build();
        }

        for (File pdf : scannedPdfs) {
            try {

                getPdfBytesFromFile(pdf.getPath());
                printPdfService.printPdf();

                pdfDeletionService.deletePdf(pdf); // Call deletion only after successful printing

                if (pdfPrintingService.isPrinterAvailable()) {
                    logger.info("No printer available, skipping printing and deletion...");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No printer found.");
                }

                logger.info("Printed PDF: " + pdf.getName() + " deleted from {}", pdf.getPath());
            } catch (Exception e) {
                logger.error("Error processing PDF: {}", e.getMessage());

                return ResponseEntity.internalServerError().body("Error processing PDF: " + e.getMessage());
            }
        }

        logger.info("Scan and print process completed successfully");
        return ResponseEntity.ok("Scanned " + scannedPdfs.size() + " ,printed " + scannedPdfs.size() + " and deleted " + scannedPdfs.size() + " PDFs.");
    }

    private void getPdfBytesFromFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        Files.readAllBytes(path);
    }
}

