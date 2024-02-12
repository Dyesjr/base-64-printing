package com.print.printing.repository;

import java.io.IOException;

public interface PdfProvider {
    byte[] getPdfBytes(String filePath) throws IOException;

}
