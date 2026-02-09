package com.invoiceparser.models;

/**
 * Represents a line item on an invoice
 */
public class LineItem {
    private String description;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public LineItem() {
    }

    public LineItem(String description, int quantity, double unitPrice, double lineTotal) {
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = lineTotal;
    }

    // Getters and Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "description='" + description + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", lineTotal=" + lineTotal +
                '}';
    }
}
