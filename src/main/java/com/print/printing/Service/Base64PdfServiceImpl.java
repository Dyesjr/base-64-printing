package com.print.printing.Service;

import com.print.printing.repository.Base64PdfService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Base64PdfServiceImpl implements Base64PdfService {

    @Override
    public ResponseEntity<Object> printBase64Pdf(String base64EncodedPdf) {
        try {
            byte[] pdfBytes = Base64.decodeBase64(base64EncodedPdf);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            headers.setContentDispositionFormData("document.pdf", "document.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid base64 encoding");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


