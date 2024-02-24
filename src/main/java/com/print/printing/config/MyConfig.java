package com.print.printing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.print.DocFlavor;
import javax.print.MultiDocPrintService;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;

@Configuration
public class MyConfig {
    @Bean
    public PrintServiceLookup printServiceLookup() {
        return new PrintServiceLookup() {
            @Override
            public PrintService[] getPrintServices(DocFlavor docFlavor, AttributeSet attributeSet) {
                return new PrintService[0];
            }

            @Override
            public PrintService[] getPrintServices() {
                return new PrintService[0];
            }

            @Override
            public MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] docFlavors, AttributeSet attributeSet) {
                return new MultiDocPrintService[0];
            }

            @Override
            public PrintService getDefaultPrintService() {
                return null;
            }
        };
    }
}

