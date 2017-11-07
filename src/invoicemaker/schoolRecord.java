/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicemaker;

import java.util.ArrayList;

/**
 *
 * @author root
 */
public class schoolRecord {
    private String id;
    private String schoolId;
    private String schoolName;
    private ArrayList<client> students;
    private ArrayList<Double> fee;
    private ArrayList<Double> rate;
    private ArrayList<Double> commission;
    private ArrayList<String> programmes;
    private double total;
    private String date;
    private String invoiceNo;
    
    public schoolRecord(){
        students = new ArrayList();
        fee = new ArrayList();
        rate = new ArrayList();
        commission = new ArrayList();
        programmes = new ArrayList();
    }
    
    public void setStudentIndex(int index, client student){
        students.set(index, student);
    }
    
    public void setFeeIndex(int index, double fee){
        this.fee.set(index, fee);
    }
    
    public void setRateIndex(int index, double rate){
        this.rate.set(index, rate);
    }
    
    public void setCommissionIndex(int index, double commission){
        this.commission.set(index, commission);
    }
    
    public void setProgrammeIndex(int index, String programme){
        this.programmes.set(index, programme);
    }
    
    public void addStudent(client student){
        this.students.add(student);
    }
    
    public void addFee(String fee){
        this.fee.add(Double.parseDouble(fee));
    }
    
    public void addRate(String rate){
        this.rate.add(Double.parseDouble(rate.replace("%", "")));
    }
    
    public void addCommission(String commission){
        this.commission.add(Double.parseDouble(commission));
    }
    
    public void addProgramme(String programme){
        this.programmes.add(programme);
    } 
    
    public String getFeeString(){
        String result = "";
        if(!this.fee.isEmpty()){
            for(double r : this.fee){
                result += String.valueOf(r) + "|";
            }
        }
        return result;
    }
    
    public String getProgrammeString(){
        String result = "";
        if(!this.programmes.isEmpty()){
            for(String s : this.programmes){
                result += String.valueOf(s) + "|";
            }
        }
        return result;
    }
    
    public String getRateString(){
        String result = "";
        if(!this.rate.isEmpty()){
            for(double r : this.rate){
                result += String.valueOf(r) + "|";
            }
        }
        return result;
    }
    
    public String getCommissionString(){
        String result = "";
        if(!this.commission.isEmpty()){
            for(double r : this.commission){
                result += String.valueOf(r) + "|";
            }
        }
        return result;
    }
    
    public String getStudentNameString(){
        String result = "";
        if(!this.students.isEmpty()){
            for(client c : this.students){
                result += c.getFirstName() + " " + c.getSecordName() + "|";
            }
        }
        return result;
    }
    
    public String getStudentStudentIdString(){
        String result = "";
        if(!this.students.isEmpty()){
            for(client c : this.students){
                result += c.getStudentId()+ "|";
            }
        }
        return result;
    }
    
    public String getStudentIdString(){
        String result = "";
        if(!this.students.isEmpty()){
            for(client c : this.students){
                result += c.getId() + "|";
            }
        }
        return result;
    }
    
    public String getStudentStartDateString(){
        String result = "";
        if(!this.students.isEmpty()){
            for(client c : this.students){
                result += c.getStartDateString();
            }
        }
        return result;
    }
    
    public void removeStudent(int index){
        this.students.remove(index);
    }
    
    public void removeProgramme(int index){
        this.programmes.remove(index);
    }
    
    public void removeFee(int index){
        this.fee.remove(index);
    }
    
    public void removeRate(int index){
        this.rate.remove(index);
    }
    
    public void removeCommission(int index){
        this.commission.remove(index);
    }
    
    public void removeByIndex(int index){
        this.removeCommission(index);
        this.removeFee(index);
        this.removeProgramme(index);
        this.removeRate(index);
        this.removeStudent(index);
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
     * @return the schoolId
     */
    public String getSchoolId() {
        return schoolId;
    }

    /**
     * @param schoolId the schoolId to set
     */
    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    /**
     * @return the schoolName
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * @param schoolName the schoolName to set
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * @return the students
     */
    public ArrayList<client> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(ArrayList<client> students) {
        this.students = students;
    }

    /**
     * @return the fee
     */
    public ArrayList<Double> getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(ArrayList<Double> fee) {
        this.fee = fee;
    }

    /**
     * @return the rate
     */
    public ArrayList<Double> getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(ArrayList<Double> rate) {
        this.rate = rate;
    }

    /**
     * @return the commission
     */
    public ArrayList<Double> getCommission() {
        return commission;
    }

    /**
     * @param commission the commission to set
     */
    public void setCommission(ArrayList<Double> commission) {
        this.commission = commission;
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
     * @return the programmes
     */
    public ArrayList<String> getProgrammes() {
        return programmes;
    }

    /**
     * @param programmes the programmes to set
     */
    public void setProgrammes(ArrayList<String> programmes) {
        this.programmes = programmes;
    }

}
