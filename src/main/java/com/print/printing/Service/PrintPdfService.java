package com.print.printing.Service;

import com.print.printing.repository.PdfProvider;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class PrintPdfService {

    private final PdfProvider pdfProvider;

    @Autowired
    public PrintPdfService(PdfProvider pdfProvider) {
        this.pdfProvider = pdfProvider;
    }

    public ResponseEntity<Object> printPdf(String pdfFilePath, String base64EncodedPdf) throws IOException, PrinterException {
        if ((pdfFilePath == null && base64EncodedPdf == null) || (pdfFilePath != null && base64EncodedPdf != null)) {
            throw new IllegalArgumentException("Exactly one of pdfFilePath or base64EncodedPdf must be provided.");
        }

        byte[] pdfBytes;
        if (pdfFilePath != null) {
            pdfBytes = pdfProvider.getPdfBytes(pdfFilePath);
        } else {
            pdfBytes = decodeBase64(base64EncodedPdf);
        }

        try (PDDocument document = PDDocument.load(pdfBytes)) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            job.print();
        }

        return ResponseEntity.ok("Printing process initiated.");
    }

    private byte[] decodeBase64(String base64EncodedPdf) {
        return Base64.getDecoder().decode(base64EncodedPdf);
    }

    public boolean isPrinterAvailable() {
        try {
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
            return printServices.length > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public byte[] getPdfBytesFromFile(String filePath) throws IOException {
        if (pdfProvider != null) {
            // Delegate to PdfProvider if present
            return pdfProvider.getPdfBytes(filePath);
        } else {
            // Implement your own logic for reading local PDFs if no PdfProvider
            Path path = Paths.get(filePath);
            return Files.readAllBytes(path);
        }
    }
}
