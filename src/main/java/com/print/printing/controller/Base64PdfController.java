package com.print.printing.controller;

import com.print.printing.Service.PrintPdfService;
import com.print.printing.repository.Base64PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Base64PdfController {

    private final PrintPdfService printPdfService;

    @Autowired
    public Base64PdfController(PrintPdfService printPdfService) {
        this.printPdfService = printPdfService;
    }

    @PostMapping("/printpdf/base64")
    public ResponseEntity<Object> printBase64Pdf(@RequestBody String base64EncodedPdf) {
        try {
            // Check if a printer is found before attempting to print
            if (printPdfService.isPrinterAvailable()) {
                System.out.println("Printer found.");
                printPdfService.printPdf(null, base64EncodedPdf);
                System.out.println("Printing started.");
                return ResponseEntity.ok("Printing initiated.");
            } else {
                System.out.println("Printer not found.");
                return ResponseEntity.badRequest().body("Printer not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to initiate printing: " + e.getMessage());
        }
    }
}
