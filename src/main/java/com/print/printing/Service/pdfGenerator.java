package com.print.printing.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class pdfGenerator {

    @Autowired
    private PrintPdfService printPdfService;

//    public String htmltopdf(String html){
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try {
//            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
//            DefaultFontProvider defaultFontProvider = new DefaultFontProvider();
//            ConverterProperties converterProperties = new ConverterProperties();
//            converterProperties.setFontProvider(defaultFontProvider);
//            HtmlConverter.convertToPdf(html, pdfWriter, converterProperties);
//
//            FileOutputStream fout = new FileOutputStream("file:///home/mkuru/pdfs/generated_pdf.pdf");
//            byteArrayOutputStream.writeTo(fout);
//            byteArrayOutputStream.close();
//
//            byteArrayOutputStream.flush();
//            fout.close();
//            return null;
//
//        }
//        catch (Exception e){
//
//        }
//
//        return null;
//    }
//}

    public String generateAndPrintPDF(String html) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            DefaultFontProvider defaultFontProvider = new DefaultFontProvider();
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setFontProvider(defaultFontProvider);
            HtmlConverter.convertToPdf(html, pdfWriter, converterProperties);

            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            String base64EncodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

            // Check if a printer is found before attempting to print
            if (printPdfService.isPrinterAvailable()) {
                System.out.println("Printer found.");
                // Print the PDF
                printPdfService.printPdf(null, base64EncodedPdf);
                System.out.println("Printing has started.");
                return "Printing process initiated.";
            } else {
                System.out.println("No printer found.");
                return "Printer not found.";
            }
        } catch (Exception e) {
            System.out.println("Error encountered while generating or printing PDF: " + e.getMessage());
            e.printStackTrace();
            return "Failed to generate and print PDF: " + e.getMessage();
        } finally {
            byteArrayOutputStream.close();
        }
    }
}

