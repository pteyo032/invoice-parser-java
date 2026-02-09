package com.invoiceparser.formatters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.invoiceparser.models.InvoiceData;
import com.invoiceparser.models.LineItem;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Formats and writes invoice data to different output formats
 */
public class OutputFormatter {
    private final Gson gson;

    public OutputFormatter() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Write invoice data to JSON file
     */
    public void writeJson(InvoiceData invoice, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            gson.toJson(invoice, writer);
        }
    }

    /**
     * Write invoice data to CSV file
     */
    public void writeCsv(InvoiceData invoice, String outputPath) throws IOException {
        try (FileWriter writer = new FileWriter(outputPath)) {
            // Write metadata
            writer.write("Invoice Metadata\n");
            writer.write("Invoice Number," + invoice.getInvoiceNumber() + "\n");
            writer.write("Date," + invoice.getInvoiceDate() + "\n");
            writer.write("Vendor," + invoice.getVendorName() + "\n");
            writer.write("Subtotal," + invoice.getSubtotal() + "\n");
            writer.write("Tax," + invoice.getTaxAmount() + "\n");
            writer.write("Total," + invoice.getTotalAmount() + "\n");
            writer.write("\n");
            
            // Write line items header
            writer.write("Line Items\n");
            writer.write("Description,Quantity,Unit Price,Line Total\n");
            
            // Write each line item
            for (LineItem item : invoice.getItems()) {
                writer.write(String.format("%s,%d,%.2f,%.2f\n",
                    escapeCsv(item.getDescription()),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal()
                ));
            }
        }
    }

    /**
     * Convert invoice data to JSON string
     */
    public String toJsonString(InvoiceData invoice) {
        return gson.toJson(invoice);
    }

    /**
     * Convert invoice data to CSV string
     */
    public String toCsvString(InvoiceData invoice) {
        StringBuilder sb = new StringBuilder();
        
        // Metadata
        sb.append("Invoice Metadata\n");
        sb.append("Invoice Number,").append(invoice.getInvoiceNumber()).append("\n");
        sb.append("Date,").append(invoice.getInvoiceDate()).append("\n");
        sb.append("Vendor,").append(invoice.getVendorName()).append("\n");
        sb.append("Subtotal,").append(invoice.getSubtotal()).append("\n");
        sb.append("Tax,").append(invoice.getTaxAmount()).append("\n");
        sb.append("Total,").append(invoice.getTotalAmount()).append("\n\n");
        
        // Line items
        sb.append("Line Items\n");
        sb.append("Description,Quantity,Unit Price,Line Total\n");
        
        for (LineItem item : invoice.getItems()) {
            sb.append(escapeCsv(item.getDescription())).append(",")
              .append(item.getQuantity()).append(",")
              .append(String.format("%.2f", item.getUnitPrice())).append(",")
              .append(String.format("%.2f", item.getLineTotal())).append("\n");
        }
        
        return sb.toString();
    }

    /**
     * Escape CSV special characters
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        
        // If contains comma, quote, or newline, wrap in quotes and escape quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}
