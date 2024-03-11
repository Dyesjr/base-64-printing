package com.print.printing.Service;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobAttributeEvent;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.print.printing.Service.EscPosConverter.sendEscPosCommandsToPrinter;
import static java.awt.SystemColor.text;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
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
        return PrintServiceLookup.lookupDefaultPrintService() != null;
    }

    public boolean isDefaultPrinterConfigured() {
        return PrintServiceLookup.lookupDefaultPrintService() != null;
    }

    public boolean printPdf(byte[] pdfBytes) {
        try {
            // Create a PD document from the byte array
            PDDocument document = PDDocument.load(pdfBytes);

            // Extract text from the document (necessary for ESC/POS printing)
            PDFTextStripper stripper = new PDFTextStripper();
            String plainText = stripper.getText(document);
            document.close(); // Close the document after extracting text

            // Convert extracted text to ESC/POS commands
            String escPosCommands = convertTextToEscPos(plainText);

            // Send ESC/POS commands to the printer
            sendEscPosCommandsToPrinter(escPosCommands);

            logger.info("PDF processed and ESC/POS commands sent to printer.");
            return true;
        } catch (IOException e) {
            logger.error("Error processing or printing PDF: {}", e.getMessage());
            return false;
        }
    }

    private String convertTextToEscPos(String text) {
        // Convert text to ESC/POS commands (example)
        StringBuilder escPosCommands = new StringBuilder();

        // Set font style and size (adjust based on your printer's capabilities)
        escPosCommands.append("\u001B~\u0001"); // Select font A (example)

        escPosCommands.append("\u000C");
        escPosCommands.append("\n\n\n");

        for (String line : text.split("\n")) {
            // Add line feed before each line except the first
            if (escPosCommands.length() > 0) {
                escPosCommands.append("\n");
            }

            escPosCommands.append(line);

            escPosCommands.append("\u000C");

        }

        // Reset font style (optional)
        escPosCommands.append("\u001B~\u0000"); // Cancel font selection (example)

        return escPosCommands.toString();
    }

    private void sendEscPosCommandsToPrinter(String escPosCommands) {
        try {
            // Convert the ESC/POS commands string to a byte array
            byte[] bytes = escPosCommands.getBytes(StandardCharsets.UTF_8);

            // Locate the default print service
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

            if (printService != null) {
                // Create a print job
                DocPrintJob printJob = printService.createPrintJob();

                // Create a doc object from the byte array with the correct MIME type
                Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

                // Send the doc to the printer
                printJob.print(doc, null);

                logger.info("ESC/POS commands sent to printer successfully.");
            } else {
                logger.error("No default print service found.");
            }
        } catch (PrintException e) {
            logger.error("Error sending ESC/POS commands to printer: {}", e.getMessage());
        }
    }



}