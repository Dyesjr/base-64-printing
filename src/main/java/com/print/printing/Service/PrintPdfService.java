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

    public void printPdf() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        if (!isDefaultPrinterConfigured()) {
            logger.info("No default printer configured. Skipping printing process.");
            return;
        }

        if (!isPrinterAvailable()) {
            logger.info("No printer found. Skipping printing process.");
            return;
        }


        List<File> scannedFiles = pdfScannerService.scanPdfs();


        for (File file : scannedFiles) {
            String filePath;
            try {
                filePath = constructFilePath();

                if (!pdfScannerService.isPrinterAvailable()) {
                    logger.info("No printer found. Skipping printing of {}", file.getName());
                    continue;
                }

                logger.info("Printing PDF: {}", filePath);

                byte[] pdfBytes = getPdfBytesFromFile(filePath);

                // Print the PDF using the acquired bytes
                try (PDDocument document = PDDocument.load(pdfBytes)) {
                    PrinterJob job = PrinterJob.getPrinterJob();
                    job.setPageable(new PDFPageable(document));
                    job.print();
                }

                logger.info("Successfully printed PDF: {}", filePath);

                // Delete the printed PDF after successful printing
                pdfScannerService.deletePdf(file);

            }  catch (Exception e) {
            logger.error("Error processing PDF: {}", e.getMessage());
            // Handle general errors here (e.g., logging, error codes, recovery actions)
            // return appropriate ResponseEntity based on your error handling strategy
        }

            }


        ResponseEntity.ok("Printing process completed.").hasBody();
    }

    private boolean isDefaultPrinterConfigured() {
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        return defaultPrinter!= null;
    }

    public byte[] getPdfBytesFromFile(String filePath) throws IOException {
        if (pdfProvider != null) {
            // Delegate to PdfProvider if present
            return pdfProvider.getPdfBytes(filePath);
        } else {
            // Implement your own logic for reading local PDFs if no PdfProvider
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        }
    }

    public boolean isPrinterAvailable() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length > 0) {
                logger.info("Printer found.");
                return true;
            } else {
                logger.info("No printer found.");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error checking printer availability: " + e.getMessage());
            return false;
        }
    }

    // Implement this method based on your scanned file data structure and how you store file paths
    private String constructFilePath() {
        // This may involve accessing properties or attributes within the scannedFile object
        throw new UnsupportedOperationException("Implement constructFilePath based on your file structure");
    }


}
