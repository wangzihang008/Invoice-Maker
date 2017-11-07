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
public class client {
    
    private int id;
    private String studentId;
    private String firstName;
    private String secordName;
    private ArrayList<String> programme;
    private ArrayList<String> startDate;
    
    public boolean addNewprogramme(String programme, String startDate){
        boolean result = false;
        if(this.startDate.contains(startDate)){
        }else{
            this.programme.add(programme);
            this.startDate.add(startDate);
            result = true;
        }
        return result;
    }
    
    public String getProgrammeString(){
        String result = "";
        if(!this.programme.isEmpty()){
            for(String r : this.programme){
                result += r + "|";
            }
        }
        return result;
    }
    
    public String getStartDateString(){
        String result = "";
        if(!this.startDate.isEmpty()){
            for(String r : this.startDate){
                result += r + "|";
            }
        }
        return result;
    }
    
    public client(){
        programme = new ArrayList();
        startDate = new ArrayList();
    }
    
    public String getFullName(){
        return firstName + " " + secordName;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param name the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the programme
     */
    public ArrayList<String> getProgramme() {
        return programme;
    }

    /**
     * @param programme the programme to set
     */
    public void setProgramme(ArrayList<String> programme) {
        this.programme = programme;
    }
    
    public void addProgramme(String programme){
        this.programme.add(programme);
    }

    /**
     * @return the startDate
     */
    public ArrayList<String> getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(ArrayList<String> startDate) {
        this.startDate = startDate;
    }
    
    public void addStartDate(String startDate){
        this.startDate.add(startDate);
    }

    /**
     * @return the secordName
     */
    public String getSecordName() {
        return secordName;
    }

    /**
     * @param secordName the secordName to set
     */
    public void setSecordName(String secordName) {
        this.secordName = secordName;
    }

    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    
}
