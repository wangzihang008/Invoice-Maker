/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import invoicemaker.client;
import invoicemaker.clientRecord;
import invoicemaker.connection;
import invoicemaker.school;
import invoicemaker.schoolRecord;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author root
 */
public class SearchPanel extends javax.swing.JPanel {

    private ArrayList<client> students;
    private ArrayList<school> schools;
    private ArrayList<clientRecord> cRecords;
    private ArrayList<schoolRecord> sRecords;
    private ArrayList listRecord;
    private SchoolInvoiceEditPanel schoolInvoiceEditPanel;
    private ClientInvoiceEditPanel clientInvoiceEditPanel;
    private final NumberFormat formatter = new DecimalFormat("#0.00"); 
    
    /**
     * Creates new form SearchPanel
     */
    public SearchPanel() {
        students = new ArrayList();
        schools = new ArrayList();
        cRecords = new ArrayList();
        sRecords = new ArrayList();
        listRecord = new ArrayList();
        getClients();
        initComponents();
        iniSelector();
        setTable("school");
        getClientRecords();
    }
    
    private void getClientRecords(){
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select * from school_record");
            while(r.next()){
                schoolRecord sRecord = new schoolRecord();
                sRecord.setId(String.valueOf(r.getInt("id")));
                sRecord.setSchoolId(r.getString("school_id"));
                sRecord.setInvoiceNo(r.getString("invoice_num"));
                for(int i = 0; i < getArrayData(r.getString("student_ids")).size(); i++){
                    client student = new client();
                    student.setId(Integer.parseInt(getArrayData(r.getString("client_ids")).get(i).toString()));
                    student.setStudentId(getArrayData(r.getString("student_ids")).get(i).toString());
                    String [] name = getArrayData(r.getString("student_names")).get(i).toString().split(" ");
                    student.setSecordName(name[name.length - 1]);
                    student.setFirstName(getArrayData(r.getString("student_names")).get(i).toString().replace(name[name.length - 1], ""));
                    student.addNewprogramme(getArrayData(r.getString("programmes")).get(i).toString(), 
                            getArrayData(r.getString("start_dates")).get(i).toString());
                    sRecord.addStudent(student);
                }
                sRecord.setProgrammes(getArrayData(r.getString("programmes")));
                sRecord.setFee(getArrayDoubleData(r.getString("fees")));
                sRecord.setRate(getArrayDoubleData(r.getString("rate")));
                sRecord.setCommission(getArrayDoubleData(r.getString("commission")));
                sRecord.setTotal(Double.parseDouble(r.getString("total_amount")));
                sRecord.setDate(getDate(sRecord.getInvoiceNo()));
                sRecords.add(sRecord);
            }
            ResultSet r1 = con.getResult("select * from client_record");
            while(r1.next()){
                clientRecord cRecord = new clientRecord();
                cRecord.setClientId(String.valueOf(r1.getInt("client_id")));
                cRecord.setInvoiceNo(r1.getString("invoice_num"));
                cRecord.setDescription(getArrayData(r1.getString("description")));
                cRecord.setAmount(getArrayDoubleData(r1.getString("sigle_amount")));
                cRecord.setPaid(getArrayDoubleData(r1.getString("sigle_paid")));
                cRecord.setTotal(r1.getDouble("total_amount"));
                cRecord.setDate(getDate(cRecord.getInvoiceNo()));
                cRecords.add(cRecord);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getDate(String data){
        String result = "";
        String r = data.substring(0, data.length() - 5);
        result = r.replaceAll("[^\\d.]", "");
        result = result.substring(0,1) + "/" + result.substring(2,3) + "/" + result.substring(4, result.length() - 1);
        return result;
    }
    
    private ArrayList getArrayData(String data){
        ArrayList<String> result = new ArrayList();
        char [] datas = data.toCharArray();
        String cResult = "";
        for(char c : datas){
            if(c != '|'){
                cResult += c;
            }else{
                result.add(cResult);
                cResult = "";
            }
        }
        return result;
    }
    
    private ArrayList getArrayDoubleData(String data){
        ArrayList<Double> result = new ArrayList();
        char [] datas = data.toCharArray();
        String cResult = "";
        for(char c : datas){
            if(c != '|'){
                cResult += c;
            }else{
                result.add(Double.parseDouble(cResult));
                cResult = "";
            }
        }
        return result;
    }
    
    private void iniSelector(){
        clientTypeSeletor.addItemListener (new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    if(clientTypeSeletor.getSelectedIndex() == 0){
                        clientNameSeletor.setModel(new DefaultComboBoxModel(getSchoolNameSeletor().toArray()));
                        setTable("school");
                        //setEditPanel();
                        if(searchRecordList.getModel().getSize() > 0){
                            DefaultListModel listModel = (DefaultListModel) searchRecordList.getModel();
                            listModel.removeAllElements();
                        }
                    }else if(clientTypeSeletor.getSelectedIndex() == 1){
                        clientNameSeletor.setModel(new DefaultComboBoxModel(getClientNameSeletor().toArray()));
                        setTable("student");
                        if(searchRecordList.getModel().getSize() > 0){
                            DefaultListModel listModel = (DefaultListModel) searchRecordList.getModel();
                            listModel.removeAllElements();
                        }
                    }
                }
                
            }
        });
    }
    
    private ArrayList<String> getClientNameSeletor(){
        ArrayList student = new ArrayList();
        for(client s: students){
            student.add(s.getFullName());
        }
        return student;
    }
    
    private ArrayList<String> getSchoolNameSeletor(){
        ArrayList school = new ArrayList();
        for(school s: schools){
            school.add(s.getName());
        }
        return school;
    }
    
    private void setTable(String type){
        if(type.equals("school")){
            //clientInvoiceTable = new javax.swing.JTable();
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
            if (clientInvoiceTable.getColumnModel().getColumnCount() > 0) {
                clientInvoiceTable.getColumnModel().getColumn(0).setPreferredWidth(100);
                clientInvoiceTable.getColumnModel().getColumn(1).setPreferredWidth(100);
                clientInvoiceTable.getColumnModel().getColumn(2).setPreferredWidth(100);
                clientInvoiceTable.getColumnModel().getColumn(3).setPreferredWidth(100);
                clientInvoiceTable.getColumnModel().getColumn(4).setPreferredWidth(100);
                clientInvoiceTable.getColumnModel().getColumn(5).setPreferredWidth(50);
                clientInvoiceTable.getColumnModel().getColumn(6).setPreferredWidth(150);
            }
        }else if(type.equals("student")){
            //clientInvoiceTable = new javax.swing.JTable();
            clientInvoiceTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null}
                },
                new String [] {
                    "No.", "Description", "Amount", "Paid"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            if (clientInvoiceTable.getColumnModel().getColumnCount() > 0) {
                clientInvoiceTable.getColumnModel().getColumn(1).setMinWidth(200);
            }
        }
    }
    
    private void setList(){
        searchRecordList = new javax.swing.JList();
    }
    
    private void setEditPanel(){
        //schoolInvoiceEditPanel = null;
        //clientInvoiceEditPanel = null;
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 5;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 4;
        GridBagLayout layout = (GridBagLayout) this.getLayout();
        if(schoolInvoiceEditPanel != null){
            if(schoolInvoiceEditPanel.getParent() == this){
                this.remove(schoolInvoiceEditPanel);
            }
        }else if(clientInvoiceEditPanel != null){
            if(clientInvoiceEditPanel.getParent() == this){
                this.remove(clientInvoiceEditPanel);
            }
        }
        if(clientTypeSeletor.getSelectedIndex() == 0){
            schoolInvoiceEditPanel = new SchoolInvoiceEditPanel();
            schoolInvoiceEditPanel.setStudents(this.students);
            schoolInvoiceEditPanel.setS(this.schools.get(clientNameSeletor.getSelectedIndex()));
            schoolInvoiceEditPanel.setjList(searchRecordList);
            schoolInvoiceEditPanel.setjTable(clientInvoiceTable);
            if(!searchRecordList.isSelectionEmpty()){
                schoolInvoiceEditPanel.setsRecord((schoolRecord)listRecord.get(searchRecordList.getSelectedIndex()));
            }
            this.add(schoolInvoiceEditPanel, c);
        }else{
            clientInvoiceEditPanel = new ClientInvoiceEditPanel();
            clientInvoiceEditPanel.setStudents(students);
            clientInvoiceEditPanel.setjList(searchRecordList);
            clientInvoiceEditPanel.setjTable(clientInvoiceTable);
            if(!searchRecordList.isSelectionEmpty()){
                clientInvoiceEditPanel.setcRecord((clientRecord)listRecord.get(searchRecordList.getSelectedIndex()));
            }
            this.add(clientInvoiceEditPanel, c);
        }
    }
    
    private void getClients(){
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select * from client");
            while(r.next()){
                client student = new client();
                student.setId(r.getInt("id"));
                student.setFirstName(r.getString("first_name"));
                student.setSecordName(r.getString("secord_name"));
                students.add(student);
            }
            ResultSet r1 = con.getResult("select * from school");
            while(r1.next()){
                school s = new school();
                s.setId(Integer.parseInt(r1.getString("id")));
                s.setName(r1.getString("school_name"));
                s.setShortName(r1.getString("school_short_name"));
                s.setPhone(r1.getString("phone"));
                s.setAddress(r1.getString("address"));
                schools.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ArrayList search(String key){
        ArrayList<schoolRecord> sr = new ArrayList();
        ArrayList<clientRecord> cr = new ArrayList();
        if(clientTypeSeletor.getSelectedIndex() == 0){
            for(schoolRecord s : this.sRecords){
                if(s.getSchoolId().equals(String.valueOf(clientNameSeletor.getSelectedIndex() + 1))){
                    sr.add(s);
                }
            }
            if(searchTypeSelector.getSelectedIndex() == 0){
                for(int i = 0; i < sr.size(); i++){
                    schoolRecord s1 = sr.get(i);
                    if(!(clientNameSeletor.getSelectedItem().toString() + s1.getInvoiceNo() + s1.getProgrammeString() + s1.getCommissionString() + s1.getRateString() + 
                            s1.getStudentNameString() + s1.getFeeString()).toLowerCase().contains(key.toLowerCase())){
                        sr.remove(s1);
                    }
                }
            }else if(searchTypeSelector.getSelectedIndex() == 1){
                for(int i = 0; i < sr.size(); i++){
                    schoolRecord s1 = sr.get(i);
                    if(!(s1.getDate()).contains(key)){
                        sr.remove(s1);
                    }
                }
            }else{
                for(int i = 0; i < sr.size(); i++){
                    schoolRecord s1 = sr.get(i);
                    if(!(s1.getSchoolName()).toLowerCase().contains(key.toLowerCase()) && 
                            this.schools.get(clientNameSeletor.getSelectedIndex()).getShortName().toLowerCase().contains(key.toLowerCase())){
                        sr.remove(s1);
                    }
                }
            }
            DefaultListModel<String> listModel = new DefaultListModel<>();
            searchRecordList.setModel(listModel);
            for(schoolRecord s : sr){
                listModel.addElement(s.getInvoiceNo());
            }
            return sr;
        }else{
            for(clientRecord c : this.cRecords){
                if(c.getClientId().equals(String.valueOf(clientNameSeletor.getSelectedIndex() + 1))){
                    cr.add(c);
                }
            }
            
            if(searchTypeSelector.getSelectedIndex() == 0){
                for(int i = 0; i < cr.size(); i++){
                    clientRecord c1 = cr.get(i);
                    if(!(clientNameSeletor.getSelectedItem().toString() + c1.getInvoiceNo() + c1.getDescriptionString() + c1.getAmountString() +
                            c1.getPaidString()).toLowerCase().contains(key.toLowerCase())){
                        cr.remove(c1);
                    }
                }
            }else if(searchTypeSelector.getSelectedIndex() == 1){
                for(int i = 0; i < cr.size(); i++){
                    clientRecord c1 = cr.get(i);
                    if(!(c1.getDate()).contains(key)){
                        cr.remove(c1);
                    }
                }
            }else{
                for(int i = 0; i < cr.size(); i++){
                    clientRecord c1 = cr.get(i);
                    if(!(c1.getClientName()).toLowerCase().contains(key.toLowerCase()) && 
                            this.students.get(clientNameSeletor.getSelectedIndex()).getFullName().toLowerCase().contains(key.toLowerCase())){
                        cr.remove(c1);
                    }
                }
            }
            
            DefaultListModel<String> listModel = new DefaultListModel<>();
            searchRecordList.setModel(listModel);
            for(clientRecord s : cr){
                listModel.addElement(s.getInvoiceNo());
            }
            return cr;
        }
    }
    
    private void getSchoolRecordTableContent(schoolRecord s){
        DefaultTableModel model = (DefaultTableModel) clientInvoiceTable.getModel();
        model.setRowCount(0);
        for(int i = 0; i < s.getProgrammes().size(); i++){
            model.addRow(new Object [] {s.getStudents().get(i).getStudentId(), s.getStudents().get(i).getFullName(), s.getProgrammes().get(i), 
            s.getStudents().get(i).getStartDate().get(0), s.getFee().get(i), s.getRate().get(i) * 100 + "%", s.getCommission().get(i)});
        }
    }
    
    private void getClientRecordTableContent(clientRecord c){
        DefaultTableModel model = (DefaultTableModel) clientInvoiceTable.getModel();
        model.setRowCount(0);
        for(int i = 0; i < c.getDescription().size(); i++){
            model.addRow(new Object [] {i + 1, c.getDescription().get(i), formatter.format(c.getAmount().get(i)), formatter.format(c.getPaid().get(i))});
        }
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

        clientTypeLabel = new javax.swing.JLabel();
        clientTypeSeletor = new javax.swing.JComboBox();
        clientNameLabel = new javax.swing.JLabel();
        clientNameSeletor = new javax.swing.JComboBox(getSchoolNameSeletor().toArray());
        searchTypeLabel = new javax.swing.JLabel();
        searchTypeSelector = new javax.swing.JComboBox();
        searchKeyWordLabel = new javax.swing.JLabel();
        searchKeyWordText = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchRecordList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        clientInvoiceTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.GridBagLayout());

        clientTypeLabel.setText("Client Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        add(clientTypeLabel, gridBagConstraints);

        clientTypeSeletor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "School", "Client" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(clientTypeSeletor, gridBagConstraints);

        clientNameLabel.setText("Client Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        add(clientNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(clientNameSeletor, gridBagConstraints);

        searchTypeLabel.setText("Search Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        add(searchTypeLabel, gridBagConstraints);

        searchTypeSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Key Word", "Date", "Name" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(searchTypeSelector, gridBagConstraints);

        searchKeyWordLabel.setText("Key Word:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.weightx = 1.0;
        add(searchKeyWordLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(searchKeyWordText, gridBagConstraints);

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        add(searchButton, gridBagConstraints);

        searchRecordList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchRecordListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(searchRecordList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        add(jScrollPane1, gridBagConstraints);

        clientInvoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        clientInvoiceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clientInvoiceTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(clientInvoiceTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = -200;
        gridBagConstraints.weightx = 4.0;
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        listRecord = search(searchKeyWordText.getText());
    }//GEN-LAST:event_searchButtonActionPerformed

    private void searchRecordListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchRecordListMouseClicked
        // TODO add your handling code here:
        for (Object listRecord1 : listRecord) {
            if (clientTypeSeletor.getSelectedIndex() == 0 && ((schoolRecord) listRecord1).getInvoiceNo().equals(searchRecordList.getSelectedValue().toString())) {
                getSchoolRecordTableContent((schoolRecord) listRecord1);
            } else if (clientTypeSeletor.getSelectedIndex() == 1 && ((clientRecord) listRecord1).getInvoiceNo().equals(searchRecordList.getSelectedValue().toString())) {
                getClientRecordTableContent((clientRecord) listRecord1);
            }
        }
        setEditPanel();
        if(schoolInvoiceEditPanel != null){
            schoolInvoiceEditPanel.calculate(clientInvoiceTable);
        }else if(clientInvoiceEditPanel != null){
            clientInvoiceEditPanel.calculate(clientInvoiceTable);
        }
    }//GEN-LAST:event_searchRecordListMouseClicked

    private void clientInvoiceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientInvoiceTableMouseClicked
        // TODO add your handling code here:
        if(!clientInvoiceTable.getSelectionModel().isSelectionEmpty() && clientInvoiceTable.getSelectedRowCount() == 1){
            if(clientTypeSeletor.getSelectedIndex() == 0){
                this.schoolInvoiceEditPanel.setTexts(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 0).toString(), clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1).toString(),
                        clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 2).toString(), clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 3).toString(), 
                        clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 4).toString(), String.valueOf(Double.parseDouble(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 5).toString().replace("%", "")) / 100), 
                        clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 6).toString());
            }else if(clientTypeSeletor.getSelectedIndex() == 1){
                clientInvoiceEditPanel.setTexts(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1).toString(), clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 2).toString(),
                        clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 3).toString());
            }
        }
    }//GEN-LAST:event_clientInvoiceTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable clientInvoiceTable;
    private javax.swing.JLabel clientNameLabel;
    private javax.swing.JComboBox clientNameSeletor;
    private javax.swing.JLabel clientTypeLabel;
    private javax.swing.JComboBox clientTypeSeletor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchKeyWordLabel;
    private javax.swing.JTextField searchKeyWordText;
    private javax.swing.JList searchRecordList;
    private javax.swing.JLabel searchTypeLabel;
    private javax.swing.JComboBox searchTypeSelector;
    // End of variables declaration//GEN-END:variables
}
