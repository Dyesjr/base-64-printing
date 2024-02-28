package com.print.printing.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

import static javax.print.attribute.standard.MediaSize.*;


//@Service
//public class PdfPrintingService {
//
//    public PdfPrintingService() {
//    }
//
//    public boolean isPrinterAvailable() {
//        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
//        return printServices.length > 0;
//    }
//
//    public boolean isDefaultPrinterConfigured() {
//        return PrintServiceLookup.lookupDefaultPrintService() != null;
//    }

//    public boolean printPdf(String pdfFilePath) {
//        Logger logger = LoggerFactory.getLogger(this.getClass());
//
//        // Check for printer availability
//        if (!isPrinterAvailable()) {
//            logger.error("No printer found. Printing cannot proceed.");
//            return false;
//        }
//
//        try {
//            // Load PDF document from the file path
//            PDDocument document = PDDocument.load(new File(pdfFilePath));
//
//            // Convert PDF document to Base64
//            byte[] pdfBytes = Files.readAllBytes(new File(pdfFilePath).toPath());
//            String base64EncodedPDF = Base64.getEncoder().encodeToString(pdfBytes);
//
//            // Get default printer
//            PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
//
//            // Log printer found message
//            logger.info("Printer found.");
//
//            // Create Printable instance for PDF document
//            Printable printable = new PDFPrintable(document);
//
//            // Create PrinterJob
//            PrinterJob printerJob = PrinterJob.getPrinterJob();
//
//            // Set printer
//            printerJob.setPrintService(defaultPrinter);
//
//            // Set printable
//            printerJob.setPrintable(printable);
//
//            // Log printing started message
//            logger.info("Printing has started.");
//
//            // Set print attributes
//            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
//
//            // Print document
//            printerJob.print(attributes);
//
//            // Close PDF document
//            document.close();
//
//            // Log printing complete message
//            logger.info("Printing complete.");
//
//            return true;
//        } catch (IOException | PrinterException e) {
//            logger.error("Error printing PDF: {}", e.getMessage());
//            return false;
//        }
//    }
//
//
//}

@Service
public class PdfPrintingService {

    private static final Logger logger = LoggerFactory.getLogger(PdfPrintingService.class);

    public boolean isPrinterAvailable() {
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        return defaultPrinter != null;
    }

    public boolean isDefaultPrinterConfigured() {
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        return defaultPrinter != null;
    }

    public boolean printPdf(byte[] pdfBytes) {
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {

            PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();

// Set the paper size directly to 80mm width
            attributeSet.add(new MediaPrintableArea(0, 0, 80, 100, MediaPrintableArea.MM));

            PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
            if (defaultPrinter != null) {
                try {
                    PrinterJob printerJob = PrinterJob.getPrinterJob();
                    printerJob.setPrintService(defaultPrinter);
                    printerJob.setPrintable(new PDFPrintable(document));
                    printerJob.print(attributeSet); // Pass the attribute set to the print method
                    logger.info("PDF printed successfully.");
                    return true;
                } catch (PrinterException e) {
                    logger.error("Error printing PDF: {}", e.getMessage());
                    return false;
                }
            } else {
                logger.error("Default printer is not configured.");
                return false;
            }



//            PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
//            if (defaultPrinter == null) {
//                logger.error("Default printer is not configured.");
//                return false;
//            }
//            PrinterJob printerJob = PrinterJob.getPrinterJob();
//            printerJob.setPrintService(defaultPrinter);
//            printerJob.setPrintable(new PDFPrintable(document));
//            printerJob.print();
//            logger.info("PDF printed successfully.");
//            return true;
        } catch (IOException e) {
            logger.error("Error printing PDF: {}", e.getMessage());
            return false;
        }
    }
}