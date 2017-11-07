/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicemaker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 *
 * @author root
 */
public class clientRecord {
    private String id;
    private String clientId;
    private String clientName;
    private ArrayList<String> description;
    private ArrayList<Double> amount;
    private ArrayList<Double> paid;
    private double total;
    private String date;
    private String invoiceNo;
    private final NumberFormat formatter = new DecimalFormat("#0.00"); 
    
    public clientRecord(){
        description = new ArrayList();
        amount = new ArrayList();
        paid = new ArrayList();
    }
    
    public double getPaidAmount(){
        double r = 0;
        for(double d : paid){
            r += d;
        }
        return r;
    }
    
    public void calculate(){
        double r = 0;
        for(double d : amount){
            r += d;
        }
        total = r;
    }
    
    public void removeByIndex(int index){
        description.remove(index);
        amount.remove(index);
        paid.remove(index);
        calculate();
    }
    
    public void addDescription(String description){
        this.description.add(description);
    }
    
    public void addAmount(double amount){
        this.amount.add(amount);
    }
    
    public void addPaid(double paid){
        this.paid.add(paid);
    }
    
    public void setDescriptionIndex(String description, int index){
        this.description.set(index, description);
    }
    
    public void setAmountIndex(double amount, int index){
        this.amount.set(index, amount);
    }
    
    public void setPaidIndex(double paid, int index){
        this.paid.set(index, paid);
    }
    
    public String getDescriptionString(){
        String result = "";
        if(!this.description.isEmpty()){
            for(String r : this.description){
                result += r + "|";
            }
        }
        return result;
    }
    
    public String getAmountString(){
        String result = "";
        if(!this.amount.isEmpty()){
            for(double r : this.amount){
                result += String.valueOf(formatter.format(r)) + "|";
            }
        }
        return result;
    }
    
    public String getPaidString(){
        String result = "";
        if(!this.paid.isEmpty()){
            for(double r : this.paid){
                result += String.valueOf(formatter.format(r)) + "|";
            }
        }
        return result;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public ArrayList<String> getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    /**
     * @return the amount
     */
    public ArrayList<Double> getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(ArrayList<Double> amount) {
        this.amount = amount;
    }

    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * @return the paid
     */
    public ArrayList<Double> getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(ArrayList<Double> paid) {
        this.paid = paid;
    }
    
    
    
}
