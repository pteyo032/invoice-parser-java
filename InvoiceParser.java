package com.invoiceparser;

import com.invoiceparser.extractors.CSVExtractor;
import com.invoiceparser.extractors.PDFExtractor;
import com.invoiceparser.formatters.OutputFormatter;
import com.invoiceparser.models.InvoiceData;

import java.io.File;
import java.io.IOException;

/**
 * Main parser class that orchestrates invoice extraction and formatting
 */
public class InvoiceParser {
    private final PDFExtractor pdfExtractor;
    private final CSVExtractor csvExtractor;
    private final OutputFormatter outputFormatter;

    public InvoiceParser() {
        this.pdfExtractor = new PDFExtractor();
        this.csvExtractor = new CSVExtractor();
        this.outputFormatter = new OutputFormatter();
    }

    /**
     * Parse an invoice file and return structured data
     */
    public InvoiceData parse(File file) throws Exception {
        if (!file.exists()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }

        String fileName = file.getName().toLowerCase();
        
        if (fileName.endsWith(".pdf")) {
            return pdfExtractor.extract(file);
        } else if (fileName.endsWith(".csv")) {
            return csvExtractor.extract(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only PDF and CSV are supported.");
        }
    }

    /**
     * Parse invoice and save to output file
     */
    public void parseAndSave(File inputFile, File outputFile, String format) throws Exception {
        InvoiceData invoice = parse(inputFile);
        
        format = format.toLowerCase();
        
        switch (format) {
            case "json":
                outputFormatter.writeJson(invoice, outputFile.getAbsolutePath());
                break;
            case "csv":
                outputFormatter.writeCsv(invoice, outputFile.getAbsolutePath());
                break;
            case "both":
                // Save both formats
                String basePath = outputFile.getAbsolutePath();
                String jsonPath = basePath.replaceAll("\\.\\w+$", ".json");
                String csvPath = basePath.replaceAll("\\.\\w+$", ".csv");
                outputFormatter.writeJson(invoice, jsonPath);
                outputFormatter.writeCsv(invoice, csvPath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported output format: " + format + ". Use 'json', 'csv', or 'both'.");
        }
    }

    /**
     * Parse multiple invoices from a directory
     */
    public void parseDirectory(File inputDir, File outputDir, String format) throws Exception {
        if (!inputDir.isDirectory()) {
            throw new IllegalArgumentException("Input path is not a directory: " + inputDir);
        }

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File[] files = inputDir.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".pdf") || name.toLowerCase().endsWith(".csv")
        );

        if (files == null || files.length == 0) {
            System.out.println("No PDF or CSV files found in directory: " + inputDir);
            return;
        }

        System.out.println("Processing " + files.length + " files...\n");

        int successCount = 0;
        int failCount = 0;

        for (File file : files) {
            try {
                String baseName = file.getName().replaceAll("\\.\\w+$", "");
                File outputFile = new File(outputDir, baseName + ".json");
                
                parseAndSave(file, outputFile, format);
                
                System.out.println("✓ Successfully processed: " + file.getName());
                successCount++;
            } catch (Exception e) {
                System.err.println("✗ Failed to process: " + file.getName());
                System.err.println("  Error: " + e.getMessage());
                failCount++;
            }
        }

        System.out.println("\n=== Summary ===");
        System.out.println("Successful: " + successCount);
        System.out.println("Failed: " + failCount);
    }

    /**
     * Get the output formatter for direct use
     */
    public OutputFormatter getOutputFormatter() {
        return outputFormatter;
    }
}
