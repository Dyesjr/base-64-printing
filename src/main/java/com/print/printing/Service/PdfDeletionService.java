package com.print.printing.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class PdfDeletionService {

    public void deletePdf(File pdf) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Deleting file: {}", pdf.getPath());

        try {
            Files.delete(pdf.toPath());
        } catch (IOException e) {
            logger.error("Error deleting PDF: " + e.getMessage());
        }
    }
}
