package com.print.printing.repository;

import org.springframework.http.ResponseEntity;

public interface Base64PdfService {
    ResponseEntity<Object> printBase64Pdf(String base64EncodedPdf);
}
