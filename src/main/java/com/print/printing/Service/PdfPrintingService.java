package com.print.printing.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.text.PDFTextStripper;
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

import static com.print.printing.Service.EscPosConverter.sendEscPosCommandsToPrinter;
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

//    public boolean printPdf(byte[] pdfBytes) {
//        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
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
//        } catch (IOException | PrinterException e) {
//            logger.error("Error printing PDF: {}", e.getMessage());
//            return false;
//        }
//    }




    public boolean printPdf(byte[] pdfBytes) {
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            // Extract text from PDF
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);

            // Convert text to ESC/POS commands (pseudocode)
            String escPosCommands = convertTextToEscPos(text);

            // Send ESC/POS commands to printer
            sendEscPosCommandsToPrinter(escPosCommands);

            logger.info("PDF printed successfully.");
            return true;
        } catch (IOException | PrinterException e) {
            logger.error("Error printing PDF: {}", e.getMessage());
            return false;
        }
    }

    public String convertTextToEscPos(String plainText) {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        // Set font style and size

        return "\u001B~\u0001" + // Select font A
                "\u001B}\u0001" + // Select double-height mode
                "\u001B{\u0001" + // Select double-width mode

                // Print text
                plainText +

                // Reset font style and size
                "\u001B~\u0000" + // Cancel font selection
                "\u001B}\u0000" + // Cancel double-height mode
                "\u001B{\u0000" + // Cancel double-width mode

                // Add paper cut command
                "\u001DV\u0001";
    }


}