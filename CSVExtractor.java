package com.invoiceparser.extractors;

import com.invoiceparser.models.InvoiceData;
import com.invoiceparser.models.LineItem;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Extracts invoice data from CSV files
 */
public class CSVExtractor {

    /**
     * Extract invoice data from a CSV file
     * Expected format:
     * Header row with metadata (Invoice Number, Date, Vendor, etc.)
     * Followed by line items (Description, Quantity, Unit Price, Total)
     */
    public InvoiceData extract(File csvFile) throws IOException, CsvException {
        InvoiceData invoice = new InvoiceData();
        
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> rows = reader.readAll();
            
            if (rows.isEmpty()) {
                throw new IOException("CSV file is empty");
            }
            
            // Parse metadata from first few rows
            parseMetadata(rows, invoice);
            
            // Parse line items (skip metadata rows)
            parseLineItems(rows, invoice);
        }
        
        return invoice;
    }

    private void parseMetadata(List<String[]> rows, InvoiceData invoice) {
        // Simple approach: look for key-value pairs in first rows
        for (int i = 0; i < Math.min(10, rows.size()); i++) {
            String[] row = rows.get(i);
            if (row.length < 2) continue;
            
            String key = row[0].trim().toLowerCase();
            String value = row[1].trim();
            
            switch (key) {
                case "invoice number":
                case "invoice #":
                case "numéro de facture":
                    invoice.setInvoiceNumber(value);
                    break;
                case "date":
                case "invoice date":
                case "date de facture":
                    invoice.setInvoiceDate(value);
                    break;
                case "vendor":
                case "vendor name":
                case "fournisseur":
                    invoice.setVendorName(value);
                    break;
                case "subtotal":
                case "sous-total":
                    invoice.setSubtotal(parseAmount(value));
                    break;
                case "tax":
                case "taxes":
                case "gst":
                case "hst":
                    invoice.setTaxAmount(parseAmount(value));
                    break;
                case "total":
                case "grand total":
                    invoice.setTotalAmount(parseAmount(value));
                    break;
            }
        }
    }

    private void parseLineItems(List<String[]> rows, InvoiceData invoice) {
        // Find the header row for line items
        int itemsStartIndex = -1;
        
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length >= 4 && containsItemHeaders(row)) {
                itemsStartIndex = i + 1; // Items start after header
                break;
            }
        }
        
        if (itemsStartIndex == -1 || itemsStartIndex >= rows.size()) {
            return; // No line items found
        }
        
        // Parse each line item
        for (int i = itemsStartIndex; i < rows.size(); i++) {
            String[] row = rows.get(i);
            
            // Skip if not enough columns or looks like metadata
            if (row.length < 4 || row[0].trim().isEmpty()) {
                continue;
            }
            
            try {
                LineItem item = new LineItem();
                item.setDescription(row[0].trim());
                item.setQuantity(Integer.parseInt(row[1].trim()));
                item.setUnitPrice(parseAmount(row[2].trim()));
                item.setLineTotal(parseAmount(row[3].trim()));
                invoice.addItem(item);
            } catch (NumberFormatException e) {
                // Skip invalid rows
                continue;
            }
        }
    }

    private boolean containsItemHeaders(String[] row) {
        String combined = String.join(" ", row).toLowerCase();
        return combined.contains("description") && 
               (combined.contains("quantity") || combined.contains("qty")) &&
               (combined.contains("price") || combined.contains("amount"));
    }

    private double parseAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            return 0.0;
        }
        
        try {
            // Remove currency symbols, commas, and whitespace
            String cleaned = amount.replaceAll("[,$\\s€£¥]", "");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
