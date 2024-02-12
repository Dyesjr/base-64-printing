package com.print.printing.controller;

import com.print.printing.Service.PrintPdfService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Getter
@RestController
@RequestMapping("/printpdf")
public class PrintingController {

//    public PrintingController(PrintPdfService printPdfService) {
//    }

    private final PrintPdfService printPdfService;

    public PrintingController(PrintPdfService printPdfService) {
        this.printPdfService = printPdfService;
    }

    @GetMapping("/")
    public ResponseEntity<String> rootEndpoint() {
        String message = "Exactly one of pdfFilePath or base64EncodedPdf must be provided.";
        return ResponseEntity.ok().body(message);
    }

//    @GetMapping("/{pdfFileName}")
//    public ResponseEntity<Object> printPdf(
//            @PathVariable String pdfFileName,
//            @RequestParam(value = "base64EncodedPdf", required = false) String base64EncodedPdf) {
//        try {
//            printPdfService.printPdf(pdfFileName, base64EncodedPdf);
//            return ResponseEntity.ok("Printing receipt");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

//    @GetMapping("/printpdf")
//    public ResponseEntity<Object> printPdf(
//            @RequestParam(value = "pdfFilePath", required = false) String pdfFilePath,
//            @RequestParam(value = "base64EncodedPdf", required = false) String base64EncodedPdf) {
//        try {
//            printPdfService.printPdf(pdfFilePath, base64EncodedPdf);
//            return ResponseEntity.ok("Printing receipt");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

//    @GetMapping("/{pdfFileName}")
//    public ResponseEntity<Object> printPdf(
////            @PathVariable String pdfFileName,
//            @RequestParam(value = "pdfFileName", required = false)String pdfFileName,
//            @RequestParam(value = "base64EncodedPdf", required = false) String base64EncodedPdf) {
//        try {
//            // Construct the file path using the filename provided in the URL
////            String pdfFilePath = "/path/to/pdf/directory/" + pdfFileName; // Change this to your actual directory
//            printPdfService.printPdf(pdfFileName, base64EncodedPdf);
//            return ResponseEntity.ok("Printing receipt");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    //Take 3
    @GetMapping("/{pdfFileName:.+}")
    public ResponseEntity<byte[]> printPdf(@PathVariable String pdfFileName) {
        try {
            // Load the PDF file from the static folder
            Resource resource = new ClassPathResource("static/" + pdfFileName);
            byte[] pdfBytes = Files.readAllBytes(Path.of(resource.getURI()));

            // Set the content type to application/pdf
            MediaType mediaType = MediaType.APPLICATION_PDF;

            // Set the Content-Disposition header to inline; filename="filename.pdf"
            String contentDisposition = "inline; filename=\"" + pdfFileName + "\"";

            // Return the PDF file as a byte array with appropriate headers
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header("Content-Disposition", contentDisposition)
                    .body(pdfBytes);
        } catch (IOException e) {
            // If the PDF file is not found or cannot be read, return a 404 Not Found response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //This is just a test, we have to revert to take 3 above



}
