package com.invoiceparser.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a complete invoice with all extracted data
 */
public class InvoiceData {
    private String invoiceNumber;
    private String invoiceDate;
    private String vendorName;
    private String vendorAddress;
    private String customerName;
    private String customerAddress;
    private double subtotal;
    private double taxAmount;
    private double totalAmount;
    private List<LineItem> items;

    public InvoiceData() {
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public void addItem(LineItem item) {
        this.items.add(item);
    }

    /**
     * Validates that subtotal + tax = total (within small margin for rounding)
     */
    public boolean isValid() {
        double calculatedTotal = subtotal + taxAmount;
        return Math.abs(calculatedTotal - totalAmount) < 0.01;
    }

    @Override
    public String toString() {
        return "InvoiceData{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", vendorName='" + vendorName + '\'' +
                ", vendorAddress='" + vendorAddress + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", subtotal=" + subtotal +
                ", taxAmount=" + taxAmount +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                '}';
    }
}
