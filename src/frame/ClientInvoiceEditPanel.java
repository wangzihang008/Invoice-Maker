/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frame;

import invoicemaker.PdfMaker;
import invoicemaker.client;
import invoicemaker.clientRecord;
import invoicemaker.connection;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
public class ClientInvoiceEditPanel extends javax.swing.JPanel {
    
    private ArrayList<client> students;
    private clientRecord cRecord;
    private JTable jTable;
    private JList jList;
    private final NumberFormat formatter = new DecimalFormat("#0.00"); 

    /**
     * Creates new form ClientInvoiceEditPanel
     */
    public ClientInvoiceEditPanel() {
        students = new ArrayList();
        initComponents();
    }
    
    private void cleanField(){
        clientInvoiceDescriptionText.setText("");
        clientInvoicePaidText.setText("");
        clientInvoiceAmountText.setText("");
    }
    
    public void calculate(JTable jTable){
        double total = 0;
        double paid = 0;
        if(jTable.getRowCount() > 1){
            for(int i = 0; i < jTable.getRowCount(); i++){
                total += Double.parseDouble(jTable.getValueAt(i, 2).toString());
                paid += Double.parseDouble(jTable.getValueAt(i, 3).toString());
            }
        }
        clientInvoiceTotalAmountText.setText(String.valueOf(formatter.format(total)));
        clientInvoiceTotalPaidText.setText(String.valueOf(formatter.format(paid)));
        clientInvoiceTotalDueText.setText(String.valueOf(formatter.format(total - paid)));
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
        clientInvoiceClientNameSelector = new JComboBox(students.toArray());
        JComboBox student = new JComboBox(students.toArray());
        return student;
    }
    
    public void setTexts(String description, String amount, String paid){
        clientInvoiceDescriptionText.setText(description);
        clientInvoiceAmountText.setText(amount);
        clientInvoicePaidText.setText(paid);
        calculate(this.jTable);
    }
    
    public void saveData(){
        String sql = "rows_count = " + cRecord.getDescription().size() + ", description = '" + cRecord.getDescriptionString()+ "', sigle_amount = '" + cRecord.getAmountString()
                + "', sigle_paid = '" + cRecord.getPaidString()+ "', total_amount = " + cRecord.getTotal()+ ", total_paid = " + cRecord.getPaidAmount()
                + ", total_due = '" + (cRecord.getTotal() - cRecord.getPaidAmount());
        connection con = new connection();
        con.excute("update school_record set " + sql + " where invoice_num = '" + cRecord.getInvoiceNo() + "'");
    }
    
    private void deleteSeletedRow(JTable jTable){
        this.cRecord.removeByIndex(jTable.getSelectedRow());
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.removeRow(jTable.getSelectedRow());
        calculate(jTable);
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

        clientDescriptionLabel = new javax.swing.JLabel();
        clientInvoiceAmountLabel = new javax.swing.JLabel();
        clientInvoiceAmountText = new javax.swing.JTextField();
        clientInvoicePaidLabel = new javax.swing.JLabel();
        clientInvoicePaidText = new javax.swing.JTextField();
        clientInvoiceAddButton = new javax.swing.JButton();
        clientInvoiceEditButton = new javax.swing.JButton();
        clientInvoiceSaveButton = new javax.swing.JButton();
        clientInvoiceCreateButton = new javax.swing.JButton();
        clientInvoiceTotalAmountLabel = new javax.swing.JLabel();
        clientInvoiceTotalAmountText = new javax.swing.JTextField();
        clientInvoiceTotalPaidLebal = new javax.swing.JLabel();
        clientInvoiceTotalPaidText = new javax.swing.JTextField();
        clientInvoiceTotalDueLebal = new javax.swing.JLabel();
        clientInvoiceTotalDueText = new javax.swing.JTextField();
        clientInvoiceClientNameLabel = new javax.swing.JLabel();
        clientInvoiceClientNameSelector = getStudentBox();
        clientInvoiceDescriptionTextScollPane = new javax.swing.JScrollPane();
        clientInvoiceDescriptionText = new javax.swing.JTextArea();
        deleteSelectedRow = new javax.swing.JButton();
        deleteInvoiceButton = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(800, 300));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(800, 300));
        setLayout(new java.awt.GridBagLayout());

        clientDescriptionLabel.setText("Description:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientDescriptionLabel, gridBagConstraints);

        clientInvoiceAmountLabel.setText("Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoiceAmountLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoiceAmountText, gridBagConstraints);

        clientInvoicePaidLabel.setText("Paid Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoicePaidLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoicePaidText, gridBagConstraints);

        clientInvoiceAddButton.setText("Add New Row");
        clientInvoiceAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientInvoiceAddButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoiceAddButton, gridBagConstraints);

        clientInvoiceEditButton.setText("Edit Selected Row");
        clientInvoiceEditButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientInvoiceEditButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceEditButton, gridBagConstraints);

        clientInvoiceSaveButton.setText("Save");
        clientInvoiceSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientInvoiceSaveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceSaveButton, gridBagConstraints);

        clientInvoiceCreateButton.setText("Create Invoice");
        clientInvoiceCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientInvoiceCreateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceCreateButton, gridBagConstraints);

        clientInvoiceTotalAmountLabel.setText("Total Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceTotalAmountLabel, gridBagConstraints);

        clientInvoiceTotalAmountText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceTotalAmountText, gridBagConstraints);

        clientInvoiceTotalPaidLebal.setText("Total Paid Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceTotalPaidLebal, gridBagConstraints);

        clientInvoiceTotalPaidText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceTotalPaidText, gridBagConstraints);

        clientInvoiceTotalDueLebal.setText("Total Due Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceTotalDueLebal, gridBagConstraints);

        clientInvoiceTotalDueText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceTotalDueText, gridBagConstraints);

        clientInvoiceClientNameLabel.setText("Client Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceClientNameLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 170;
        gridBagConstraints.weightx = 1.0;
        add(clientInvoiceClientNameSelector, gridBagConstraints);

        clientInvoiceDescriptionText.setColumns(20);
        clientInvoiceDescriptionText.setRows(5);
        clientInvoiceDescriptionText.setPreferredSize(new java.awt.Dimension(200, 80));
        clientInvoiceDescriptionTextScollPane.setViewportView(clientInvoiceDescriptionText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoiceDescriptionTextScollPane, gridBagConstraints);

        deleteSelectedRow.setText("Delete Selected Row");
        deleteSelectedRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedRowActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(deleteSelectedRow, gridBagConstraints);

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

    private void clientInvoiceAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceAddButtonActionPerformed
        // TODO add your handling code here:
        if(clientInvoiceDescriptionText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The description cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(clientInvoiceAmountText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The total amount cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(clientInvoicePaidText.getText().equals("")){
            JOptionPane.showMessageDialog(this, "The paid amount cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else{
            int row = jTable.getRowCount();
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            model.removeRow(jTable.getRowCount() - 1);
            model.addRow(new Object[]{row, clientInvoiceDescriptionText.getText(), clientInvoiceAmountText.getText(), clientInvoicePaidText.getText()});
            model.addRow(new Object[]{});
            getcRecord().addDescription(clientInvoiceDescriptionText.getText());
            getcRecord().addAmount(Double.parseDouble(clientInvoiceAmountText.getText()));
            getcRecord().addPaid(Double.parseDouble(clientInvoicePaidText.getText()));
        }
        cleanField();
        calculate(this.jTable);
    }//GEN-LAST:event_clientInvoiceAddButtonActionPerformed

    private void clientInvoiceEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceEditButtonActionPerformed
        // TODO add your handling code here:
        if(jTable.getSelectionModel().isSelectionEmpty()){
            JOptionPane.showMessageDialog(this, "There is no row has been selected!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(jTable.getValueAt(jTable.getSelectedRow(), 1) == null){
            JOptionPane.showMessageDialog(this, "You should selected a row with content!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            model.setValueAt(clientInvoiceDescriptionText.getText(), jTable.getSelectedRow(), 1);
            model.setValueAt(formatter.format(clientInvoiceAmountText.getText()), jTable.getSelectedRow(), 2);
            model.setValueAt(formatter.format(clientInvoicePaidText.getText()), jTable.getSelectedRow(), 3);
            getcRecord().setDescriptionIndex(clientInvoiceDescriptionText.getText(), jTable.getSelectedRow());
            getcRecord().setAmountIndex(Double.parseDouble(clientInvoiceAmountText.getText()), jTable.getSelectedRow());
            getcRecord().setPaidIndex(Double.parseDouble(clientInvoicePaidText.getText()), jTable.getSelectedRow());
        }
        calculate(this.jTable);
        cleanField();
    }//GEN-LAST:event_clientInvoiceEditButtonActionPerformed

    private void clientInvoiceSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceSaveButtonActionPerformed
        // TODO add your handling code here:
        if(this.jTable.getRowCount() <= 1){
            JOptionPane.showMessageDialog(this, "There is no record to save!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            saveData();
            cleanField();
            JOptionPane.showMessageDialog(this, "The invoice record has been saved!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_clientInvoiceSaveButtonActionPerformed

    private void clientInvoiceCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceCreateButtonActionPerformed
        // TODO add your handling code here:
        if(jTable.getValueAt(0, 1) != null){
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
            newPdf.setNewFile(new File(path + "/" + cRecord.getInvoiceNo() + ".pdf"));
            newPdf.createClientInvoice(getcRecord());
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(newPdf.getNewFile());
            } catch (IOException ex) {
                Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_clientInvoiceCreateButtonActionPerformed

    private void deleteInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInvoiceButtonActionPerformed
        // TODO add your handling code here:
        connection con = new connection();
        con.excute("delete from school_record where invoice_num = '" + this.cRecord.getInvoiceNo() + "'");
        jList.remove(jList.getSelectedIndex());
    }//GEN-LAST:event_deleteInvoiceButtonActionPerformed

    private void deleteSelectedRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSelectedRowActionPerformed
        // TODO add your handling code here:
        deleteSeletedRow(jTable);
    }//GEN-LAST:event_deleteSelectedRowActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clientDescriptionLabel;
    private javax.swing.JButton clientInvoiceAddButton;
    private javax.swing.JLabel clientInvoiceAmountLabel;
    private javax.swing.JTextField clientInvoiceAmountText;
    private javax.swing.JLabel clientInvoiceClientNameLabel;
    private javax.swing.JComboBox clientInvoiceClientNameSelector;
    private javax.swing.JButton clientInvoiceCreateButton;
    private javax.swing.JTextArea clientInvoiceDescriptionText;
    private javax.swing.JScrollPane clientInvoiceDescriptionTextScollPane;
    private javax.swing.JButton clientInvoiceEditButton;
    private javax.swing.JLabel clientInvoicePaidLabel;
    private javax.swing.JTextField clientInvoicePaidText;
    private javax.swing.JButton clientInvoiceSaveButton;
    private javax.swing.JLabel clientInvoiceTotalAmountLabel;
    private javax.swing.JTextField clientInvoiceTotalAmountText;
    private javax.swing.JLabel clientInvoiceTotalDueLebal;
    private javax.swing.JTextField clientInvoiceTotalDueText;
    private javax.swing.JLabel clientInvoiceTotalPaidLebal;
    private javax.swing.JTextField clientInvoiceTotalPaidText;
    private javax.swing.JButton deleteInvoiceButton;
    private javax.swing.JButton deleteSelectedRow;
    // End of variables declaration//GEN-END:variables

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
     * @return the cRecord
     */
    public clientRecord getcRecord() {
        return cRecord;
    }

    /**
     * @param cRecord the cRecord to set
     */
    public void setcRecord(clientRecord cRecord) {
        this.cRecord = cRecord;
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
