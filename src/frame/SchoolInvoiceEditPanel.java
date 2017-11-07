/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import invoicemaker.PdfMaker;
import invoicemaker.client;
import invoicemaker.connection;
import invoicemaker.school;
import invoicemaker.schoolRecord;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author root
 */
public class SchoolInvoiceEditPanel extends javax.swing.JPanel {
    
    private schoolRecord sRecord;
    private ArrayList<client> students;
    private ArrayList<school> schools;
    private school s;
    private JTable jTable;
    private JList jList;
    private final NumberFormat formatter = new DecimalFormat("#0.00");

    /**
     * Creates new form SchoolInvoiceEditPanel
     */
    public SchoolInvoiceEditPanel() {
        students = new ArrayList();
        schools = new ArrayList();
        initComponents();
        startDateText.setCalendarPreferredSize(new Dimension(350, 280));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        startDateText.setDateFormat(sdf);
    }
    
    private void addNewRow(JTable jTable, String studentId, String studentName, String programme, String startDate, String fee, String rate, String commission){
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.addRow(new Object[]{studentId, studentName, programme, startDate, fee, rate, commission});
        client c = getStudent(studentName);
        client cn = new client();
        cn.setId(c.getId());
        cn.setFirstName(c.getFirstName());
        cn.setSecordName(c.getSecordName());
        cn.addNewprogramme(programme, startDate);
        cn.setStudentId(studentId);
        getsRecord().addProgramme(programme);
        getsRecord().addStudent(cn);
        getsRecord().addFee(fee);
        getsRecord().addRate(rate);
        getsRecord().addCommission(commission);
    }
    
    private void editSelectedRow(JTable jTable, String studentId, String studentName, String programme, String startDate, String fee, String rate, String commission){
        if(jTable.getSelectionModel().isSelectionEmpty()){
            JOptionPane.showMessageDialog(this, "There must be a row has been selected!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            model.setValueAt(studentId, jTable.getSelectedRow(), 0);
            model.setValueAt(studentName, jTable.getSelectedRow(), 1);
            model.setValueAt(programme, jTable.getSelectedRow(), 2);
            model.setValueAt(startDate, jTable.getSelectedRow(), 3);
            model.setValueAt(fee, jTable.getSelectedRow(), 4);
            model.setValueAt(rate, jTable.getSelectedRow(), 5);
            model.setValueAt(commission, jTable.getSelectedRow(), 6);
        }
    }
    
    private client getStudent(String studentName){
        for(client c : getStudents()){
            if(studentName.equals(c.getFirstName() + " " + c.getSecordName())){
                return c;
            }
        }
        return null;
    }
    
    private void cleanTexts(){
        studentIdText.setText("");
        programmeText.setText("");
        feeText.setText("");
        rateText.setText("");
    }
    
    private boolean isParasableAsDouble(String s){
        try{
            Double.valueOf(s);
            return true;
        }catch(NumberFormatException numberFormatException){
            return false;
        }
    }
    
    public void calculate(JTable jTable){
        double total = 0;
        double gst = 0;
        if(jTable.getRowCount() > 0 && jTable.getModel().getValueAt(0, 0) != null){
            double fee = 0;
            double rate = 0;
            for(int i = 0; i < jTable.getRowCount(); i++){
                for(int j = 0; j < jTable.getColumnCount(); j++){
                    if(j == 4){
                        fee = Double.parseDouble(jTable.getModel().getValueAt(i, j).toString());
                    }else if(j == 5){
                        rate = Double.parseDouble(jTable.getModel().getValueAt(i, j).toString().replace("%", "")) / 100.0;
                    }
                }
                total += fee * rate;
                gst += fee * rate - fee * rate /1.15;
            }
        }
        gstText.setText(String.valueOf(formatter.format(gst)));
        totalText.setText(String.valueOf(formatter.format(total)));
    }
    
    public JComboBox getStudentBox(){
        ArrayList students = new ArrayList();
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select * from client");
            while(r.next()){
                client student = new client();
                student.setId(r.getInt("id"));
                student.setFirstName(r.getString("first_name"));
                student.setSecordName(r.getString("secord_name"));
                this.students.add(student);
                students.add(r.getString("first_name") + " " + r.getString("secord_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        studentNameComboBox = new JComboBox(students.toArray());
        JComboBox student = new JComboBox(students.toArray());
        return student;
    }
    
    public JComboBox getSchoolBox(){
        ArrayList schools = new ArrayList();
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select * from school");
            while(r.next()){
                school s = new school();
                s.setId(r.getInt("id"));
                s.setName(r.getString("school_name"));
                s.setShortName(r.getString("school_short_name"));
                s.setPhone(r.getString("phone"));
                s.setAddress(r.getString("address"));
                this.getSchools().add(s);
                schools.add(r.getString("school_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        schoolNameComboBox = new JComboBox(schools.toArray());
        JComboBox school = new JComboBox(schools.toArray());
        return school;
    }
    
    private void deleteSeletedRow(JTable jTable){
        this.sRecord.removeByIndex(jTable.getSelectedRow());
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.removeRow(jTable.getSelectedRow());
        calculate(jTable);
    }
    
    private void deleteInvoice(){
        
    }
    
    public void setTexts(String studentId, String studentName, String programme, String startDate, String fee, String rate, String commission){
        studentIdText.setText(studentId);
        studentNameComboBox.setSelectedItem(studentName);
        programmeText.setText(programme);
        startDateText.setText(startDate);
        feeText.setText(fee);
        rateText.setText(rate);
        calculate(jTable);
    }
    
    private void saveData(){
        String sql = "rows_count = " + sRecord.getStudents().size() + ", client_ids = '" + sRecord.getStudentIdString()+ "', student_ids = '" + sRecord.getStudentStudentIdString()
                + "', student_names = '" + sRecord.getStudentNameString() + "', programmes = '" + sRecord.getProgrammeString() + "', start_dates = '" + sRecord.getStudentStartDateString()
                + "', fees = '" + sRecord.getFeeString() + "', rate = '" + sRecord.getRateString() + "', commission = '" + sRecord.getCommissionString() + "', gst_amount = "
                + gstText.getText() + ", total_amount = " + totalText.getText();
        connection con = new connection();
        con.excute("update school_record set " + sql + " where invoice_num = '" + sRecord.getInvoiceNo() + "'");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        createButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        addRowButton = new javax.swing.JButton();
        studentIdLabel = new javax.swing.JLabel();
        studentIdText = new javax.swing.JTextField();
        studentNameLabel = new javax.swing.JLabel();
        programmeText = new javax.swing.JTextField();
        programmeLabel = new javax.swing.JLabel();
        startDateLabel = new javax.swing.JLabel();
        feeLabel = new javax.swing.JLabel();
        feeText = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        rateLabel = new javax.swing.JLabel();
        rateText = new javax.swing.JTextField();
        gstLabel = new javax.swing.JLabel();
        gstText = new javax.swing.JTextField();
        totalLabel = new javax.swing.JLabel();
        totalText = new javax.swing.JTextField();
        studentNameComboBox = getStudentBox();
        startDateText = new datechooser.beans.DateChooserCombo();
        schoolNameLabel = new javax.swing.JLabel();
        schoolNameComboBox = getSchoolBox();
        deleteRowButton = new javax.swing.JButton();
        deleteInvoiceButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(800, 300));
        setPreferredSize(new java.awt.Dimension(800, 300));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.GridBagLayout());

        createButton.setText("Create Invoice");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(createButton, gridBagConstraints);

        editButton.setText("Edit Selected Row");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(editButton, gridBagConstraints);

        addRowButton.setText("Add New Row");
        addRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(addRowButton, gridBagConstraints);

        studentIdLabel.setText("Student ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(studentIdLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(studentIdText, gridBagConstraints);

        studentNameLabel.setText("Student Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(studentNameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(programmeText, gridBagConstraints);

        programmeLabel.setText("Programme :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(programmeLabel, gridBagConstraints);

        startDateLabel.setText("Start Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(startDateLabel, gridBagConstraints);

        feeLabel.setText("Fee Paid :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(feeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(feeText, gridBagConstraints);

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(saveButton, gridBagConstraints);

        rateLabel.setText("Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(rateLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(rateText, gridBagConstraints);

        gstLabel.setText("GST included :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(gstLabel, gridBagConstraints);

        gstText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(gstText, gridBagConstraints);

        totalLabel.setText("Total :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(totalLabel, gridBagConstraints);

        totalText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(totalText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(studentNameComboBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(startDateText, gridBagConstraints);

        schoolNameLabel.setText("School Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(schoolNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(schoolNameComboBox, gridBagConstraints);

        deleteRowButton.setText("Delete Selected Row");
        deleteRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(deleteRowButton, gridBagConstraints);

        deleteInvoiceButton.setText("Delete This Invoice");
        deleteInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInvoiceButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(deleteInvoiceButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        // TODO add your handling code here:
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        //LocalDate localDate = LocalDate.now();
        String path = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose the path you want to save the invoice.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            path = chooser.getCurrentDirectory().getAbsolutePath();
        }
        PdfMaker newPdf = new PdfMaker();
        /*int num = getRecordsCount();
        if(num != -1){
            num++;
        }else{
            num = 1;
        }*/
        newPdf.setNewFile(new File(path + "/" + this.sRecord.getInvoiceNo() + ".pdf"));
        //getsRecord().setInvoiceNo(this.schools.get(schoolNameComboBox.getSelectedIndex()).getShortName() + dtf.format(localDate) + String.format("%04d", num));
        newPdf.createSchoolInvoice(getsRecord(), this.getS());
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(newPdf.getNewFile());
        } catch (IOException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        int row = this.jTable.getSelectedRow();
        if(this.jTable.getSelectionModel().isSelectionEmpty()){
            JOptionPane.showMessageDialog(this, "There must be a row has been selected!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(studentNameComboBox.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(this, "The student name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(programmeText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The programme cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(startDateText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The start date cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(feeText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The fee paid cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(rateText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The rate cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(!isParasableAsDouble(rateText.getText())){
            JOptionPane.showMessageDialog(this, "The rate must be numbers!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(!isParasableAsDouble(feeText.getText())){
            JOptionPane.showMessageDialog(this, "The fees paid must be numbers!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            String studentId = studentIdText.getText();
            String studentName = studentNameComboBox.getSelectedItem().toString();
            String programme = programmeText.getText();
            String startDate = startDateText.getText();
            double fee = Double.parseDouble(feeText.getText());
            double rate = Double.parseDouble(rateText.getText());
            String commission = String.valueOf(formatter.format(fee * rate));
            editSelectedRow(this.jTable, studentId, studentName, programme, startDate, String.valueOf(formatter.format(fee)), rate * 100 + "%", commission);
            
            /*((DefaultTableModel)clientInvoiceTable.getModel()).removeRow(row);
            ((DefaultTableModel)clientInvoiceTable.getModel()).insertRow(row, new Object[]{studentId, studentName, programme, startDate, String.valueOf(formatter.format(fee)), rate * 100 + "%", commission});*/
            client c = getStudent(studentName);
            client cn = new client();
            cn.setId(c.getId());
            cn.setFirstName(c.getFirstName());
            cn.setSecordName(c.getSecordName());
            cn.addNewprogramme(programme, startDate);
            cn.setStudentId(studentId);
            getsRecord().setProgrammeIndex(row, programme);
            getsRecord().setStudentIndex(row, cn);
            getsRecord().setFeeIndex(row, fee);
            getsRecord().setRateIndex(row, rate);
            getsRecord().setCommissionIndex(row, Double.parseDouble(formatter.format(fee * rate)));
        }
        calculate(this.jTable);
        cleanTexts();
    }//GEN-LAST:event_editButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        // TODO add your handling code here:

        if(studentNameComboBox.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(this, "The student name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(programmeText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The programme cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(startDateText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The start date cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(feeText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The fee paid cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(rateText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The rate cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(!isParasableAsDouble(rateText.getText())){
            JOptionPane.showMessageDialog(this, "The rate must be numbers!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(!isParasableAsDouble(feeText.getText())){
            JOptionPane.showMessageDialog(this, "The fees paid must be numbers!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            String studentId = studentIdText.getText();
            String studentName = studentNameComboBox.getSelectedItem().toString();
            String programme = programmeText.getText();
            String startDate = startDateText.getText();
            double fee = Double.parseDouble(feeText.getText());
            double rate = Double.parseDouble(rateText.getText());
            String commission = String.valueOf(formatter.format(fee * rate));
            addNewRow(this.jTable, studentId, studentName, programme, startDate, String.valueOf(formatter.format(fee)), ""+rate * 100 + "%", commission);
        }
        calculate(this.jTable);
        cleanTexts();
    }//GEN-LAST:event_addRowButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        if(this.jTable.getRowCount() <= 1){
            JOptionPane.showMessageDialog(this, "There is no record to save!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            saveData();
            cleanTexts();
            JOptionPane.showMessageDialog(this, "The invoice record has been saved!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void deleteRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRowButtonActionPerformed
        // TODO add your handling code here:
        deleteSeletedRow(this.jTable);
    }//GEN-LAST:event_deleteRowButtonActionPerformed

    private void deleteInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInvoiceButtonActionPerformed
        // TODO add your handling code here:
        connection con = new connection();
        con.excute("delete from school_record where invoice_num = '" + this.sRecord.getInvoiceNo() + "'");
        jList.remove(jList.getSelectedIndex());
    }//GEN-LAST:event_deleteInvoiceButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowButton;
    private javax.swing.JButton createButton;
    private javax.swing.JButton deleteInvoiceButton;
    private javax.swing.JButton deleteRowButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel feeLabel;
    private javax.swing.JTextField feeText;
    private javax.swing.JLabel gstLabel;
    private javax.swing.JTextField gstText;
    private javax.swing.JLabel programmeLabel;
    private javax.swing.JTextField programmeText;
    private javax.swing.JLabel rateLabel;
    private javax.swing.JTextField rateText;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox schoolNameComboBox;
    private javax.swing.JLabel schoolNameLabel;
    private javax.swing.JLabel startDateLabel;
    private datechooser.beans.DateChooserCombo startDateText;
    private javax.swing.JLabel studentIdLabel;
    private javax.swing.JTextField studentIdText;
    private javax.swing.JComboBox studentNameComboBox;
    private javax.swing.JLabel studentNameLabel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField totalText;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the sRecord
     */
    public schoolRecord getsRecord() {
        return sRecord;
    }

    /**
     * @param sRecord the sRecord to set
     */
    public void setsRecord(schoolRecord sRecord) {
        this.sRecord = sRecord;
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
     * @return the s
     */
    public school getS() {
        return s;
    }

    /**
     * @param s the s to set
     */
    public void setS(school s) {
        this.s = s;
    }

    /**
     * @return the jTable
     */
    public JTable getjTable() {
        return jTable;
    }

    /**
     * @param jTable the jTable to set
     */
    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

    /**
     * @return the schools
     */
    public ArrayList<school> getSchools() {
        return schools;
    }

    /**
     * @param schools the schools to set
     */
    public void setSchools(ArrayList<school> schools) {
        this.schools = schools;
    }

    /**
     * @return the jList
     */
    public JList getjList() {
        return jList;
    }

    /**
     * @param jList the jList to set
     */
    public void setjList(JList jList) {
        this.jList = jList;
    }
}
