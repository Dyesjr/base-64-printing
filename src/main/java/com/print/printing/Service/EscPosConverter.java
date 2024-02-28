package com.print.printing.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static com.print.printing.Service.PdfScannerService.logger;


public class EscPosConverter {



    public static void sendEscPosCommandsToPrinter(String escPosCommands) throws PrinterException {
        DocPrintJob job = getPrintService().createPrintJob();
        try {
            // Convert the String to a byte array
            byte[] bytes = escPosCommands.getBytes(StandardCharsets.UTF_8);

            // Create a Doc from the byte array
            Doc doc = new SimpleDoc(bytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

            // Print the document
            job.print(doc, null);
        } catch (PrintException e) {
            e.printStackTrace();
        }

    }

    private static PrintService getPrintService() {
        // Obtain the default print service
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();

        // Check if default print service is available
        if (defaultPrintService != null) {
            return defaultPrintService;
        } else {
            // If no default print service is found, handle the situation accordingly
            throw new RuntimeException("No default printer found.");
        }
}}
