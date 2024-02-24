package com.print.printing.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfScannerService {

    @Value("${pdf.file.path}")
    private String targetFolderPath;
//    private Logger logger;
private static final Logger logger = LoggerFactory.getLogger(PdfScannerService.class);


    public PdfScannerService(String targetFolder) {

        logger.info("Target folder found at: {}" + targetFolder);
    }

    public List<File> scanPdfs() {
        List<File> pdfs = new ArrayList<>();
        try {
            Stream<Path> stream = Files.walk(Paths.get(targetFolderPath));
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".pdf"))
                    .forEach(path -> {
                        pdfs.add(path.toFile());
                        logger.info("Found PDF file: {}", path);
                    });
        } catch (IOException e) {
            logger.error("Error scanning PDFs: " + e.getMessage());
        }
        return pdfs;
    }

    public void deletePdf(File pdf) {
        try {
            Files.delete(pdf.toPath());
            logger.info("Deleting PDF: {}", pdf.getPath());
        } catch (IOException e) {
            logger.error("Failed to delete PDF " + e.getMessage());
        }
    }

    public boolean isPrinterAvailable() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        return printServices.length > 0;
    }
}
