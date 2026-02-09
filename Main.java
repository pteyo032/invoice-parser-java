package com.invoiceparser;

import com.invoiceparser.models.InvoiceData;

import java.io.File;

/**
 * Command-line interface for the Invoice Parser
 */
public class Main {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            parseArguments(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void parseArguments(String[] args) throws Exception {
        String inputPath = null;
        String outputPath = null;
        String format = "json"; // default format
        boolean verbose = false;

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                case "--input":
                    if (i + 1 < args.length) {
                        inputPath = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing value for --input");
                    }
                    break;
                    
                case "-o":
                case "--output":
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing value for --output");
                    }
                    break;
                    
                case "-f":
                case "--format":
                    if (i + 1 < args.length) {
                        format = args[++i];
                    } else {
                        throw new IllegalArgumentException("Missing value for --format");
                    }
                    break;
                    
                case "-v":
                case "--verbose":
                    verbose = true;
                    break;
                    
                case "-h":
                case "--help":
                    printUsage();
                    System.exit(0);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }

        // Validate required arguments
        if (inputPath == null) {
            throw new IllegalArgumentException("Input path is required. Use --input <path>");
        }

        if (outputPath == null) {
            // Default output path
            outputPath = "output";
        }

        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);

        InvoiceParser parser = new InvoiceParser();

        // Process input
        if (inputFile.isDirectory()) {
            // Process directory
            System.out.println("Processing directory: " + inputFile.getAbsolutePath());
            parser.parseDirectory(inputFile, outputFile, format);
        } else {
            // Process single file
            System.out.println("Processing file: " + inputFile.getAbsolutePath());
            
            InvoiceData invoice = parser.parse(inputFile);
            
            if (verbose) {
                System.out.println("\n=== Extracted Data ===");
                System.out.println("Invoice Number: " + invoice.getInvoiceNumber());
                System.out.println("Date: " + invoice.getInvoiceDate());
                System.out.println("Vendor: " + invoice.getVendorName());
                System.out.println("Subtotal: $" + invoice.getSubtotal());
                System.out.println("Tax: $" + invoice.getTaxAmount());
                System.out.println("Total: $" + invoice.getTotalAmount());
                System.out.println("Line Items: " + invoice.getItems().size());
                System.out.println("Valid: " + (invoice.isValid() ? "Yes" : "No"));
                System.out.println();
            }
            
            // Save to output
            parser.parseAndSave(inputFile, outputFile, format);
            
            System.out.println("✓ Successfully saved to: " + outputFile.getAbsolutePath());
            
            if (format.equals("both")) {
                String basePath = outputFile.getAbsolutePath();
                String jsonPath = basePath.replaceAll("\\.\\w+$", ".json");
                String csvPath = basePath.replaceAll("\\.\\w+$", ".csv");
                System.out.println("  - JSON: " + jsonPath);
                System.out.println("  - CSV: " + csvPath);
            }
        }
    }

    private static void printUsage() {
        System.out.println("Invoice Parser CLI - Extract structured data from invoice PDFs and CSV files");
        System.out.println();
        System.out.println("USAGE:");
        System.out.println("  java -jar invoice-parser.jar [OPTIONS]");
        System.out.println();
        System.out.println("OPTIONS:");
        System.out.println("  -i, --input <path>     Input file or directory (required)");
        System.out.println("  -o, --output <path>    Output file or directory (default: 'output')");
        System.out.println("  -f, --format <format>  Output format: json, csv, or both (default: json)");
        System.out.println("  -v, --verbose          Print extracted data to console");
        System.out.println("  -h, --help             Show this help message");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  # Parse single PDF to JSON");
        System.out.println("  java -jar invoice-parser.jar --input invoice.pdf --output result.json");
        System.out.println();
        System.out.println("  # Parse single file to both JSON and CSV");
        System.out.println("  java -jar invoice-parser.jar -i invoice.pdf -o result -f both");
        System.out.println();
        System.out.println("  # Parse all files in directory");
        System.out.println("  java -jar invoice-parser.jar -i invoices/ -o results/ -f json");
        System.out.println();
        System.out.println("  # Parse with verbose output");
        System.out.println("  java -jar invoice-parser.jar -i invoice.pdf -o result.json -v");
        System.out.println();
    }
}
