/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import invoicemaker.PdfMaker;
import invoicemaker.client;
import invoicemaker.company;
import invoicemaker.connection;
import invoicemaker.school;
import invoicemaker.schoolRecord;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.CheckBox;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author root
 */
public class SchoolInvoicePanel extends javax.swing.JPanel {
    
    private ArrayList<client> students;
    private ArrayList<school> schools;
    private schoolRecord sRecord;
    private final NumberFormat formatter = new DecimalFormat("#0.00"); 

    /**
     * Creates new form clintPanel
     */
    public SchoolInvoicePanel() {
        students = new ArrayList();
        sRecord = new schoolRecord();
        schools= new ArrayList();
        initComponents();
        getStudentBox();
        calculate();
        iniTable();
        startDateText.setCalendarPreferredSize(new Dimension(350, 280));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        startDateText.setDateFormat(sdf);
    }
    
    public void calculate(){
        double total = 0;
        double gst = 0;
        if(clientInvoiceTable.getRowCount() > 0 && clientInvoiceTable.getModel().getValueAt(0, 0) != null){
            double fee = 0;
            double rate = 0;
            for(int i = 0; i < clientInvoiceTable.getRowCount() - 1; i++){
                for(int j = 0; j < clientInvoiceTable.getColumnCount(); j++){
                    if(j == 4){
                        fee = Double.parseDouble(clientInvoiceTable.getModel().getValueAt(i, j).toString());
                    }else if(j == 5){
                        rate = Double.parseDouble(clientInvoiceTable.getModel().getValueAt(i, j).toString().replace("%", "")) / 100.0;
                    }
                }
                total += fee * rate;
                gst += fee * rate - fee * rate /1.15;
            }
        }
        gstText.setText(String.valueOf(formatter.format(gst)));
        totalText.setText(String.valueOf(formatter.format(total)));
    }
    
    public void addRow(String id, String studentName, String programme, String startDate, String fee, String rate, String commission){
        DefaultTableModel model = (DefaultTableModel) clientInvoiceTable.getModel();
        model.removeRow(clientInvoiceTable.getRowCount() - 1);
        model.addRow(new Object[]{id, studentName, programme, startDate, fee, rate, commission});
        //model.removeRow(clientInvoiceTable.getRowCount() - 1);
        model.addRow(new Object[]{});
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
                this.schools.add(s);
                schools.add(r.getString("school_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        schoolNameComboBox = new JComboBox(schools.toArray());
        JComboBox school = new JComboBox(schools.toArray());
        return school;
    }
    
    public void iniTable(){
        clientInvoiceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!clientInvoiceTable.getSelectionModel().isSelectionEmpty() && clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1) != null){
                    studentIdText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 0).toString());
                    studentNameComboBox.setSelectedItem(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1).toString());
                    programmeText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 2).toString());
                    startDateText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 3).toString());
                    feeText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 4).toString());
                    rateText.setText(String.valueOf(Double.parseDouble(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 5).toString().replace("%", "")) / 100.0));
                    calculate();
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    private int getRecordsCount(){
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select count(*) from school_record where school_id = " + schools.get(schoolNameComboBox.getSelectedIndex()).getId() );
            if(r.next()){
                return r.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    private void cleanTexts(){
        studentIdText.setText("");
        programmeText.setText("");
        feeText.setText("");
        rateText.setText("");
    }
    
    public void addNewSchoolInvoiceRecord(String studentId, String studentName, String programme, String startDate, String fee, String rate, String commission){
        client c = getStudent(studentName);
        client cn = new client();
        cn.setId(c.getId());
        cn.setFirstName(c.getFirstName());
        cn.setSecordName(c.getSecordName());
        cn.addNewprogramme(programme, startDate);
        cn.setStudentId(studentId);
        sRecord.addProgramme(programme);
        sRecord.addStudent(cn);
        sRecord.addFee(fee);
        sRecord.addRate(rate);
        sRecord.addCommission(commission);
    }
    
    private client getStudent(String studentName){
        for(client c : students){
            if(studentName.equals(c.getFirstName() + " " + c.getSecordName())){
                return c;
            }
        }
        return null;
    }
    
    private boolean isParasableAsDouble(String s){
        try{
            Double.valueOf(s);
            return true;
        }catch(NumberFormatException numberFormatException){
            return false;
        }
    }
    
    private void saveData(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate localDate = LocalDate.now();
        int num = getRecordsCount();
        if(num != -1){
            num++;
        }else{
            num = 1;
        }
        sRecord.setInvoiceNo(this.schools.get(schoolNameComboBox.getSelectedIndex()).getShortName() + dtf.format(localDate) + String.format("%04d", num));
        String startDates = "";
        String ids = "";
        String client_ids = "";
        for(client c: sRecord.getStudents()){
            startDates += c.getStartDate().get(0) + "|";
            ids += "" + c.getStudentId()+ "|";
            client_ids += "" + c.getId() + "|";
        }
        String data = "(" + schools.get(schoolNameComboBox.getSelectedIndex()).getId() + ",'" + sRecord.getInvoiceNo() + "'," + (clientInvoiceTable.getRowCount() - 1) + ",'" + client_ids + "','"+ ids + "','" +
                sRecord.getStudentNameString() + "','" + sRecord.getProgrammeString() + "','" + startDates + "','"+ sRecord.getFeeString() + "','" + sRecord.getRateString() + "','" + sRecord.getCommissionString()
                + "'," + gstText.getText() + "," + totalText.getText() + ")";
        connection con = new connection();
        con.excute("insert into school_record (school_id, invoice_num, rows_count, client_ids, student_ids, student_names, programmes, start_dates, fees, rate, commission, gst_amount, total_amount) values " + data);
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

        clientInvoiceTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        clientInvoiceTable = new javax.swing.JTable();
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

        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.GridBagLayout());

        clientInvoiceTitle.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        clientInvoiceTitle.setText("New Client Invoice");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoiceTitle, gridBagConstraints);

        clientInvoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Student Name", "Programme", "Start Date", "Fees Paid", "Rate", "Commission Due"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        clientInvoiceTable.setMinimumSize(new java.awt.Dimension(750, 200));
        clientInvoiceTable.setName(""); // NOI18N
        clientInvoiceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clientInvoiceTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(clientInvoiceTable);
        if (clientInvoiceTable.getColumnModel().getColumnCount() > 0) {
            clientInvoiceTable.getColumnModel().getColumn(0).setPreferredWidth(100);
            clientInvoiceTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            clientInvoiceTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            clientInvoiceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            clientInvoiceTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            clientInvoiceTable.getColumnModel().getColumn(5).setPreferredWidth(50);
            clientInvoiceTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 750;
        gridBagConstraints.ipady = 150;
        gridBagConstraints.weighty = 5.0;
        add(jScrollPane1, gridBagConstraints);

        createButton.setText("Create Invoice");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
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
        gridBagConstraints.gridy = 8;
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
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(addRowButton, gridBagConstraints);

        studentIdLabel.setText("Student ID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(studentIdLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(studentIdText, gridBagConstraints);

        studentNameLabel.setText("Student Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(studentNameLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(programmeText, gridBagConstraints);

        programmeLabel.setText("Programme :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(programmeLabel, gridBagConstraints);

        startDateLabel.setText("Start Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(startDateLabel, gridBagConstraints);

        feeLabel.setText("Fee Paid :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(feeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
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
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(saveButton, gridBagConstraints);

        rateLabel.setText("Rate :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(rateLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(rateText, gridBagConstraints);

        gstLabel.setText("GST included :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(gstLabel, gridBagConstraints);

        gstText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(gstText, gridBagConstraints);

        totalLabel.setText("Total :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(totalLabel, gridBagConstraints);

        totalText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(totalText, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(studentNameComboBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(startDateText, gridBagConstraints);

        schoolNameLabel.setText("School Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(schoolNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 70;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        add(schoolNameComboBox, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        // TODO add your handling code here:
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyy");
        LocalDate localDate = LocalDate.now();
        String path = "";
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose the path you want to save the invoice.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            path = chooser.getCurrentDirectory().getAbsolutePath();
        }
        PdfMaker newPdf = new PdfMaker();
        int num = getRecordsCount();
        if(num != -1){
            num++;
        }else{
            num = 1;
        }
        newPdf.setNewFile(new File(path + "/" + this.schools.get(schoolNameComboBox.getSelectedIndex()).getShortName() + dtf.format(localDate) + String.format("%04d", num) + ".pdf"));
        sRecord.setInvoiceNo(this.schools.get(schoolNameComboBox.getSelectedIndex()).getShortName() + dtf.format(localDate) + String.format("%04d", num));
        newPdf.createSchoolInvoice(sRecord, this.schools.get(schoolNameComboBox.getSelectedIndex()));
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(newPdf.getNewFile());
        } catch (IOException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        int row = clientInvoiceTable.getSelectedRow();
        if(clientInvoiceTable.getSelectionModel().isSelectionEmpty()){
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
            ((DefaultTableModel)clientInvoiceTable.getModel()).removeRow(row);
            ((DefaultTableModel)clientInvoiceTable.getModel()).insertRow(row, new Object[]{studentId, studentName, programme, startDate, String.valueOf(formatter.format(fee)), rate * 100 + "%", commission});
            client c = getStudent(studentName);
            client cn = new client();
            cn.setId(c.getId());
            cn.setFirstName(c.getFirstName());
            cn.setSecordName(c.getSecordName());
            cn.addNewprogramme(programme, startDate);
            cn.setStudentId(studentId);
            sRecord.setProgrammeIndex(row, programme);
            sRecord.setStudentIndex(row, cn);
            sRecord.setFeeIndex(row, fee);
            sRecord.setRateIndex(row, rate);
            sRecord.setCommissionIndex(row, Double.parseDouble(formatter.format(fee * rate)));
        }
        calculate();
        cleanTexts();
    }//GEN-LAST:event_editButtonActionPerformed

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        // TODO add your handling code here:
        
        int row = this.clientInvoiceTable.getRowCount() - 1;
        if(row < 0){
            JOptionPane.showMessageDialog(this, "The last record has not been finished!", "Warning", JOptionPane.WARNING_MESSAGE);
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
            addNewSchoolInvoiceRecord(studentId, studentName, programme, startDate, String.valueOf(formatter.format(fee)), String.valueOf(formatter.format(rate)), commission);
            addRow(studentId, studentName, programme, startDate, String.valueOf(formatter.format(fee)), ""+rate * 100 + "%", commission);
        }
        calculate();
        cleanTexts();
    }//GEN-LAST:event_addRowButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        if(this.clientInvoiceTable.getRowCount() <= 1){
             JOptionPane.showMessageDialog(this, "There is no record to save!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            saveData();
            JOptionPane.showMessageDialog(this, "The invoice record has been saved!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void clientInvoiceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientInvoiceTableMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_clientInvoiceTableMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowButton;
    private javax.swing.JTable clientInvoiceTable;
    private javax.swing.JLabel clientInvoiceTitle;
    private javax.swing.JButton createButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel feeLabel;
    private javax.swing.JTextField feeText;
    private javax.swing.JLabel gstLabel;
    private javax.swing.JTextField gstText;
    private javax.swing.JScrollPane jScrollPane1;
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

    
}
