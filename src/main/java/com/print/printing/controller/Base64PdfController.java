package com.print.printing.controller;

import com.print.printing.Service.PrintPdfService;
import com.print.printing.repository.Base64PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Base64PdfController {

    private final Base64PdfService base64PdfService;
    private final PrintPdfService printPdfService;

    @Autowired
    public Base64PdfController(Base64PdfService base64PdfService, PrintPdfService printPdfService) {
        this.base64PdfService = base64PdfService;
        this.printPdfService = printPdfService;
    }

    @PostMapping("/printpdf/base64")
    public ResponseEntity<Object> printBase64Pdf(@RequestBody String base64EncodedPdf){
        return  base64PdfService.printBase64Pdf(base64EncodedPdf);
    }

    @PostMapping("/printpdf/physical")
    public ResponseEntity<Object> printPhysicalPdf(@RequestBody String pdfFilePath){
        try {
            // Delegate printing to the PrintPdfService
            printPdfService.printPdf(pdfFilePath, null); // Assuming no base64-encoded PDF is provided
            return ResponseEntity.ok("Printing initiated.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to initiate printing.");
        }
    }

//    @PostMapping("/printpdf/base64")
//    public ResponseEntity<Object> printBase64Pdf(@RequestBody String base64EncodedPdf) {
//        ResponseEntity<Object> response = base64PdfService.printBase64Pdf(base64EncodedPdf);
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try {
//                printPdfService.printPdf(null, base64EncodedPdf); // Assuming null for pdfFilePath
//                // Printing process initiated successfully
//                return ResponseEntity.ok("Printing process initiated.");
//            } catch (Exception e) {
//                // Error occurred during printing
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error initiating printing process.");
//            }
//        } else {
//            // Base64 PDF decoding failed
//            return response; // Return the original response from base64PdfService
//        }
//    }
}
