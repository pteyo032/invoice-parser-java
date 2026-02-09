package com.invoiceparser.extractors;

import com.invoiceparser.models.InvoiceData;
import com.invoiceparser.models.LineItem;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts invoice data from PDF files
 */
public class PDFExtractor {
    
    // Regex patterns for common invoice fields
    private static final Pattern INVOICE_NUMBER_PATTERN = Pattern.compile(
        "(?:Invoice|Facture|INV)\\s*[#:]?\\s*([A-Z0-9-]+)", 
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "(\\d{4}-\\d{2}-\\d{2}|\\d{2}/\\d{2}/\\d{4}|\\d{2}-\\d{2}-\\d{4})"
    );
    
    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
        "\\$?\\s*([0-9,]+\\.\\d{2})\\s*\\$?"
    );
    
    private static final Pattern TOTAL_PATTERN = Pattern.compile(
        "(?:Total|TOTAL|Grand Total)\\s*:?\\s*\\$?\\s*([0-9,]+\\.\\d{2})",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern SUBTOTAL_PATTERN = Pattern.compile(
        "(?:Subtotal|Sub-Total|SUBTOTAL)\\s*:?\\s*\\$?\\s*([0-9,]+\\.\\d{2})",
        Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern TAX_PATTERN = Pattern.compile(
        "(?:Tax|GST|HST|TVH|TPS|TVQ)\\s*:?\\s*\\$?\\s*([0-9,]+\\.\\d{2})",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * Extract invoice data from a PDF file
     */
    public InvoiceData extract(File pdfFile) throws IOException {
        InvoiceData invoice = new InvoiceData();
        
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            // Extract fields using regex patterns
            invoice.setInvoiceNumber(extractInvoiceNumber(text));
            invoice.setInvoiceDate(extractDate(text));
            invoice.setTotalAmount(extractTotal(text));
            invoice.setSubtotal(extractSubtotal(text));
            invoice.setTaxAmount(extractTax(text));
            invoice.setVendorName(extractVendorName(text));
            
            // Extract line items (simplified)
            extractLineItems(text, invoice);
        }
        
        return invoice;
    }

    private String extractInvoiceNumber(String text) {
        Matcher matcher = INVOICE_NUMBER_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "N/A";
    }

    private String extractDate(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "N/A";
    }

    private double extractTotal(String text) {
        Matcher matcher = TOTAL_PATTERN.matcher(text);
        if (matcher.find()) {
            return parseAmount(matcher.group(1));
        }
        return 0.0;
    }

    private double extractSubtotal(String text) {
        Matcher matcher = SUBTOTAL_PATTERN.matcher(text);
        if (matcher.find()) {
            return parseAmount(matcher.group(1));
        }
        return 0.0;
    }

    private double extractTax(String text) {
        Matcher matcher = TAX_PATTERN.matcher(text);
        if (matcher.find()) {
            return parseAmount(matcher.group(1));
        }
        return 0.0;
    }

    private String extractVendorName(String text) {
        // Simple heuristic: first non-empty line is often the vendor name
        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty() && line.length() > 3) {
                return line;
            }
        }
        return "N/A";
    }

    private void extractLineItems(String text, InvoiceData invoice) {
        // Simplified line item extraction
        // Look for patterns like: Description Qty Price Amount
        Pattern itemPattern = Pattern.compile(
            "([A-Za-z][A-Za-z\\s]+)\\s+(\\d+)\\s+\\$?([0-9,]+\\.\\d{2})\\s+\\$?([0-9,]+\\.\\d{2})"
        );
        
        Matcher matcher = itemPattern.matcher(text);
        while (matcher.find()) {
            LineItem item = new LineItem();
            item.setDescription(matcher.group(1).trim());
            item.setQuantity(Integer.parseInt(matcher.group(2)));
            item.setUnitPrice(parseAmount(matcher.group(3)));
            item.setLineTotal(parseAmount(matcher.group(4)));
            invoice.addItem(item);
        }
    }

    /**
     * Parse amount string to double, removing commas and dollar signs
     */
    private double parseAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            return 0.0;
        }
        
        try {
            // Remove commas and whitespace
            String cleaned = amount.replaceAll("[,$\\s]", "");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
