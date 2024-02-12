package com.print.printing.util;

import com.print.printing.repository.PdfProvider;
//import com.printdocs.print.repository.PdfProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FilePdfProvider implements PdfProvider {

    @Value("${pdf.file.path}")
    private String defaultfilePath;

//    public FilePdfProvider(String defaultfilePath) {
//        this.defaultfilePath = defaultfilePath;
//    }

    @Override
    public byte[] getPdfBytes(String filePath) throws IOException {
        if (filePath == null || filePath.isEmpty()) {
            filePath = defaultfilePath;
        }

        return Files.readAllBytes(Paths.get(filePath));
    }
}

