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
import invoicemaker.schoolRecord;
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
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author root
 */
public class ClientInovicePanel extends javax.swing.JPanel {
    
    private clientRecord cRecord;
    private ArrayList<client> clients;
    private final NumberFormat formatter = new DecimalFormat("#0.00"); 

    /**
     * Creates new form schoolPanel
     */
    public ClientInovicePanel() {
        cRecord = new clientRecord();
        clients = new ArrayList();
        initComponents();
        iniTable();
        getStudentBox();
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
                this.clients.add(student);
                students.add(r.getString("first_name") + " " + r.getString("secord_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientInvoiceClientNameSelector = new JComboBox(students.toArray());
        JComboBox student = new JComboBox(students.toArray());
        return student;
    }
    
    private void iniTable(){
        clientInvoiceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!clientInvoiceTable.getSelectionModel().isSelectionEmpty() && clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1) != null){
                    //studentIdText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 0).toString());
                    clientInvoiceDescriptionText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1).toString());
                    clientInvoiceAmountText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 2).toString());
                    clientInvoicePaidText.setText(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 3).toString());
                    calculate();
                }
            }
        });
    }
    
    private void calculate(){
        double total = 0;
        double paid = 0;
        if(clientInvoiceTable.getRowCount() > 1){
            for(int i = 0; i < clientInvoiceTable.getRowCount() - 1; i++){
                total += Double.parseDouble(clientInvoiceTable.getValueAt(i, 2).toString());
                paid += Double.parseDouble(clientInvoiceTable.getValueAt(i, 3).toString());
            }
        }
        clientInvoiceTotalAmountText.setText(String.valueOf(formatter.format(total)));
        clientInvoiceTotalPaidText.setText(String.valueOf(formatter.format(paid)));
        clientInvoiceTotalDueText.setText(String.valueOf(formatter.format(total - paid)));
    }
    
    private void cleanField(){
        clientInvoiceDescriptionText.setText("");
        clientInvoicePaidText.setText("");
        clientInvoiceAmountText.setText("");
    }
    
    private int getRecordsCount(){
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select count(*) from client_record where client_id = " + clients.get(clientInvoiceClientNameSelector.getSelectedIndex()).getId());
            if(r.next()){
                return r.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
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
        cRecord.setId(String.valueOf(clientInvoiceClientNameSelector.getSelectedIndex() + 1));
        cRecord.setInvoiceNo(this.clients.get(clientInvoiceClientNameSelector.getSelectedIndex()).getFullName()+ dtf.format(localDate) + String.format("%04d", num));
        double total = 0;
        double paid = 0;
        double due = 0;
        for(int i = 0; i < cRecord.getPaid().size(); i++){
            total += cRecord.getAmount().get(i);
            paid += cRecord.getPaid().get(i);
        }
        due = total - paid;
        String data = "(" + cRecord.getId() + ",'" + cRecord.getInvoiceNo() + "'," + (clientInvoiceTable.getRowCount() - 1) + ",'" + cRecord.getDescriptionString() + "','" + cRecord.getAmountString()
                + "','" + cRecord.getPaidString() + "'," + total + "," + paid + "," + due + ")";
        connection con = new connection();
        con.excute("insert into client_record (client_id, invoice_num, rows_count, description, sigle_amount, sigle_paid, total_amount, total_paid, total_due) values " + data);
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

        setMinimumSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.GridBagLayout());

        clientInvoiceTitle.setFont(new java.awt.Font("Cantarell", 1, 24)); // NOI18N
        clientInvoiceTitle.setText("New Client Invoice");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 6.0;
        gridBagConstraints.weighty = 1.0;
        add(clientInvoiceTitle, gridBagConstraints);

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
        jScrollPane1.setViewportView(clientInvoiceTable);
        if (clientInvoiceTable.getColumnModel().getColumnCount() > 0) {
            clientInvoiceTable.getColumnModel().getColumn(1).setMinWidth(200);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = -200;
        gridBagConstraints.weightx = 6.0;
        gridBagConstraints.weighty = 8.0;
        add(jScrollPane1, gridBagConstraints);

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
        add(clientInvoiceTotalPaidLebal, gridBagConstraints);

        clientInvoiceTotalPaidText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 200;
        add(clientInvoiceTotalPaidText, gridBagConstraints);

        clientInvoiceTotalDueLebal.setText("Total Due Amount:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        add(clientInvoiceTotalDueLebal, gridBagConstraints);

        clientInvoiceTotalDueText.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 200;
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
            int row = clientInvoiceTable.getRowCount();
            DefaultTableModel model = (DefaultTableModel) clientInvoiceTable.getModel();
            model.removeRow(clientInvoiceTable.getRowCount() - 1);
            model.addRow(new Object[]{row, clientInvoiceDescriptionText.getText(), clientInvoiceAmountText.getText(), clientInvoicePaidText.getText()});
            model.addRow(new Object[]{});
            cRecord.addDescription(clientInvoiceDescriptionText.getText());
            cRecord.addAmount(Double.parseDouble(clientInvoiceAmountText.getText()));
            cRecord.addPaid(Double.parseDouble(clientInvoicePaidText.getText()));
        }
        cleanField();
        calculate();
    }//GEN-LAST:event_clientInvoiceAddButtonActionPerformed

    private void clientInvoiceEditButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceEditButtonActionPerformed
        // TODO add your handling code here:
        if(clientInvoiceTable.getSelectionModel().isSelectionEmpty()){
            JOptionPane.showMessageDialog(this, "There is no row has been selected!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else if(clientInvoiceTable.getValueAt(clientInvoiceTable.getSelectedRow(), 1) == null){
            JOptionPane.showMessageDialog(this, "You should selected a row with content!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            DefaultTableModel model = (DefaultTableModel) clientInvoiceTable.getModel();
            model.setValueAt(clientInvoiceDescriptionText.getText(), clientInvoiceTable.getSelectedRow(), 1);
            model.setValueAt(clientInvoiceAmountText.getText(), clientInvoiceTable.getSelectedRow(), 2);
            model.setValueAt(clientInvoicePaidText.getText(), clientInvoiceTable.getSelectedRow(), 3);
            cRecord.setDescriptionIndex(clientInvoiceDescriptionText.getText(), clientInvoiceTable.getSelectedRow());
            cRecord.setAmountIndex(Double.parseDouble(clientInvoiceAmountText.getText()), clientInvoiceTable.getSelectedRow());
            cRecord.setPaidIndex(Double.parseDouble(clientInvoicePaidText.getText()), clientInvoiceTable.getSelectedRow());
        }
        cleanField();
        calculate();
    }//GEN-LAST:event_clientInvoiceEditButtonActionPerformed

    private void clientInvoiceCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceCreateButtonActionPerformed
        // TODO add your handling code here:
        if(clientInvoiceTable.getValueAt(0, 1) != null){
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
            newPdf.setNewFile(new File(path + "/" + this.clients.get(clientInvoiceClientNameSelector.getSelectedIndex()).getFullName()+ dtf.format(localDate) + String.format("%04d", num)  + ".pdf"));
            newPdf.createClientInvoice(cRecord);
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(newPdf.getNewFile());
            } catch (IOException ex) {
                Logger.getLogger(SchoolInvoicePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_clientInvoiceCreateButtonActionPerformed

    private void clientInvoiceSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientInvoiceSaveButtonActionPerformed
        // TODO add your handling code here:
        if(this.clientInvoiceTable.getRowCount() <= 1){
             JOptionPane.showMessageDialog(this, "There is no record to save!", "Warning", JOptionPane.WARNING_MESSAGE);
        }else{
            saveData();
            JOptionPane.showMessageDialog(this, "The invoice record has been saved!", "Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_clientInvoiceSaveButtonActionPerformed


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
    private javax.swing.JTable clientInvoiceTable;
    private javax.swing.JLabel clientInvoiceTitle;
    private javax.swing.JLabel clientInvoiceTotalAmountLabel;
    private javax.swing.JTextField clientInvoiceTotalAmountText;
    private javax.swing.JLabel clientInvoiceTotalDueLebal;
    private javax.swing.JTextField clientInvoiceTotalDueText;
    private javax.swing.JLabel clientInvoiceTotalPaidLebal;
    private javax.swing.JTextField clientInvoiceTotalPaidText;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
