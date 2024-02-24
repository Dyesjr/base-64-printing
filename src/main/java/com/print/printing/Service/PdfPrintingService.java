package com.print.printing.Service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;



@Service
public class PdfPrintingService {

    public PdfPrintingService() {
    }


    public boolean isPrinterAvailable() {
        org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            if (printServices.length > 0) {
                logger.info("Printer found.");
                return false;
            } else {
                logger.info("No printer found.");
                return true;
            }
        } catch (Exception e) {
            logger.error("Error checking for printer availability: " + e.getMessage());
            return true;
        }
    }

    public boolean isDefaultPrinterConfigured() {
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        return defaultPrinter != null;
    }
}
