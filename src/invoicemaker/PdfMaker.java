/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicemaker;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RadioCheckField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class PdfMaker {
    
    private File newFile;
    private company company;
    private static final Font Title_Font = new Font(FontFactory.getFont("Times-Roman", 18, Font.BOLD));
    private static final Font Subtitle_Font = new Font(FontFactory.getFont("Times-Roman", 14, Font.BOLD));
    
    public PdfMaker(){
        company = new company();
        this.getCompanyInfo();
    }
    
    public void createFile(String name) throws IOException{
        this.setNewFile(new File(name));
        if(!this.newFile.exists()){
            this.getNewFile().createNewFile();
        }else{
            this.getNewFile().delete();
        }
    }
    
    public void getCompanyInfo(){
        try {
            connection con = new connection();
            ResultSet r = con.getResult("select * from company");
            while(r.next()){
                company.setName(r.getString("company_name"));
                company.setAddress(r.getString("address"));
                company.setPost(r.getString("post_address"));
                company.setPhone(r.getString("phone"));
                company.setEmail(r.getString("email"));
                company.setGst(r.getString("gst"));
                company.setAccountName(r.getString("account_name"));
                company.setAccountNo(r.getString("account_number"));
                company.setBankName(r.getString("bank"));
                company.setSwiftCode(r.getString("swift_code"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createSchoolInvoice(schoolRecord sRecord, school school){
        try {
            Document document = new Document();
            OutputStream outputStream = new FileOutputStream(this.getNewFile());
            PdfWriter.getInstance(document, outputStream);
            document.open();
            this.addSchoolInvoiceDetails(sRecord, school, company, document);
            document.close();
            outputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createClientInvoice(clientRecord cRecord){
        try {
            Document document = new Document();
            OutputStream outputStream = new FileOutputStream(this.getNewFile());
            PdfWriter.getInstance(document, outputStream);
            document.open();
            this.addStudentInvoiceDetails(cRecord, company, document);
            document.close();
            outputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addSchoolInvoiceDetails(schoolRecord sRecord, school school, company company, Document document){
        try {
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(this.getNewFile());
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(20, "TAX INVOICE\n\n\n", Subtitle_Font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            PdfPTable pdfPTable1 = new PdfPTable(2);
            pdfPTable1.getDefaultCell().setBorder(0);
            pdfPTable1.setWidths(new int[]{1, 1} );
            pdfPTable1.addCell("To:  " + school.getShortName());
            pdfPTable1.addCell("Tax Invoice No.  " );
            pdfPTable1.addCell("Add:  " + school.getAddress());
            pdfPTable1.addCell("GST Registration No.  " + company.getGst());
            pdfPTable1.addCell("Tell:   " + school.getPhone());
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            pdfPTable1.addCell("Date:  " + dtf.format(localDate));
            
            PdfPTable pdfPTable2 = new PdfPTable(7);
            pdfPTable2.setTotalWidth(520);
            pdfPTable2.setLockedWidth(true);
            pdfPTable2.setWidths(new int[]{1, 1, 2, 1, 1, 1, 1});
            pdfPTable2.addCell("STUDENT ID NUMBER");
            pdfPTable2.addCell("STUDENT NAME");
            pdfPTable2.addCell("PROGRAMME");
            pdfPTable2.addCell("START DATE");
            pdfPTable2.addCell("FEES PAID");
            pdfPTable2.addCell("RATE");
            pdfPTable2.addCell("COMMISSION DUE");
            double total = 0;
            for(int i = 0; i < sRecord.getStudents().size(); i++){
                this.addSchoolRecordIntoTable(sRecord.getStudents().get(i).getStudentId(), sRecord.getStudents().get(i).getFirstName() + " " + sRecord.getStudents().get(i).getSecordName(),
                        sRecord.getStudents().get(i).getProgramme().get(0), sRecord.getStudents().get(i).getStartDate().get(0), String.valueOf(sRecord.getFee().get(i)), 
                        String.valueOf(sRecord.getRate().get(i)), String.valueOf(sRecord.getCommission().get(i)), pdfPTable2);
                total += sRecord.getCommission().get(i);
            }
            PdfPTable pdfPTable3 = new PdfPTable(2);
            pdfPTable3.getDefaultCell().setBorder(0);
            pdfPTable3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPTable3.setTotalWidth(170);
            pdfPTable3.setLockedWidth(true);
            pdfPTable3.setWidths(new int[]{2, 1});
            pdfPTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPTable3.addCell("GST included");
            NumberFormat formatter = new DecimalFormat("#0.00");
            pdfPTable3.addCell("$" + formatter.format(total - total / 1.15));
            pdfPTable3.addCell("TOTAL");
            pdfPTable3.addCell("$" + formatter.format(total));
            document.add(this.addCompanyInformation());
            document.add(paragraph);
            document.add(pdfPTable1);
            document.add(new Paragraph("\n\n"));
            document.add(pdfPTable2);
            document.add(new Paragraph("\n\n"));
            document.add(pdfPTable3);
            document.add(this.addPaymentTable());
            document.add(this.addPaymentDetail(company));
            document.close();
            outputStream.close();
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addStudentInvoiceDetails(clientRecord cRecord, company company, Document document){
        try {
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(this.getNewFile());
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(20, "TAX INVOICE\n\n\n", Subtitle_Font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            PdfPTable pdfPTable1 = new PdfPTable(2);
            pdfPTable1.getDefaultCell().setBorder(0);
            pdfPTable1.setWidths(new int[]{1, 1} );
            pdfPTable1.addCell("To:  " + cRecord.getClientName());
            pdfPTable1.addCell("Tax Invoice No.  " );
            pdfPTable1.addCell("Add:  ");
            pdfPTable1.addCell("GST Registration No.  " + company.getGst());
            pdfPTable1.addCell("Tell:   ");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            pdfPTable1.addCell("Date:  " + dtf.format(localDate));
            
            PdfPTable pdfPTable2 = new PdfPTable(4);
            pdfPTable2.setTotalWidth(520);
            pdfPTable2.setLockedWidth(true);
            pdfPTable2.setWidths(new int[]{ 1, 2, 1, 1});
            pdfPTable2.addCell("NO.");
            pdfPTable2.addCell("DESCRIPTION");
            pdfPTable2.addCell("AMOUNT");
            pdfPTable2.addCell("PAID");
            double total = 0;
            double paid = 0;
            for(int i = 1; i <= cRecord.getDescription().size(); i++){
                this.addClientRecordIntoTable(i, cRecord, pdfPTable2);
                total += cRecord.getAmount().get(i - 1);
                paid += cRecord.getPaid().get(i - 1);
            }
            PdfPTable pdfPTable3 = new PdfPTable(2);
            pdfPTable3.getDefaultCell().setBorder(0);
            pdfPTable3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPTable3.setTotalWidth(170);
            pdfPTable3.setLockedWidth(true);
            pdfPTable3.setWidths(new int[]{2, 1});
            pdfPTable3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            //pdfPTable3.addCell("GST included");
            NumberFormat formatter = new DecimalFormat("#0.00");
            //pdfPTable3.addCell("$" + formatter.format(total - total / 1.15));
            pdfPTable3.addCell("TOTAL");
            pdfPTable3.addCell("$" + formatter.format(total));
            pdfPTable3.addCell("TOTAL PAID");
            pdfPTable3.addCell("$" + formatter.format(paid));
            pdfPTable3.addCell("TOTAL AMOUNT DUE");
            pdfPTable3.addCell("$" + formatter.format(total - paid));
            
            document.add(this.addCompanyInformation());
            document.add(paragraph);
            document.add(pdfPTable1);
            document.add(new Paragraph("\n\n"));
            document.add(pdfPTable2);
            document.add(new Paragraph("\n\n"));
            document.add(pdfPTable3);
            document.add(this.addPaymentTable());
            document.add(this.addPaymentDetail(company));
            document.close();
            outputStream.close();
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PdfPTable addCompanyInformation() throws SQLException{
        PdfPTable pdfPTable = null;
        connection con = new connection();
        ResultSet r = con.getResult("select * from company");
        company information = new company();
        if(r.next()){
            information.setName(r.getString("company_name"));
            information.setAddress(r.getString("address"));
            information.setPost(r.getString("post_address"));
            information.setPhone(r.getString("phone"));
            information.setEmail(r.getString("email"));
            information.setGst(r.getString("gst"));
            information.setAccountName(r.getString("account_name"));
            information.setAccountNo(r.getString("account_number"));
            information.setBankName(r.getString("bank"));
            information.setSwiftCode(r.getString("swift_code"));
        }
        try {
            pdfPTable = new PdfPTable(2);
            pdfPTable.getDefaultCell().setBorder(0);
            pdfPTable.setWidths(new int[]{1, 3} );
            Image image = Image.getInstance("./src/source/logo.jpg");
            image.scaleAbsolute(100, 100);
            String content = "";
            String name = information.getName();
            content += "\n" + information.getAddress() + "\n" + information.getPost()
                    + "\n" + information.getPhone() + information.getEmail();
            Phrase title = new Paragraph(name, Title_Font);
            Phrase paragraph = new Paragraph(content);
            PdfPCell p1 = new PdfPCell(title);
            PdfPCell p2 = new PdfPCell(paragraph);
            p1.setPaddingLeft(20);
            p2.setPaddingLeft(20);
            p1.setPaddingTop(25);
            p2.setPaddingTop(0);
            p1.setBorder(0);
            p2.setBorder(0);
            p1.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            p2.setHorizontalAlignment(Element.ALIGN_TOP);
            PdfPTable pdfPCell2 = new PdfPTable(1);
            pdfPCell2.getDefaultCell().setBorder(0);
            /*pdfPCell2.setPaddingLeft(40);
            pdfPCell2.setBorder(0);
            pdfPCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);*/
            pdfPCell2.addCell(p1);
            pdfPCell2.addCell(p2);
            pdfPTable.addCell(image);
            pdfPTable.addCell(pdfPCell2);
            
            //document.add(pdfPTable);
            //document.close();
            //outputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfMaker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
        }
        return pdfPTable;
    }
    
    public void addSchoolRecordIntoTable(String studentId, String studentName, String programme, String startDate, String fee, String rate, String commission, PdfPTable pdfPTable){
        pdfPTable.addCell(studentId);
        pdfPTable.addCell(studentName);
        pdfPTable.addCell(programme);
        pdfPTable.addCell(startDate);
        pdfPTable.addCell("$" + fee);
        pdfPTable.addCell("" + rate + "%");
        pdfPTable.addCell("$" + commission);
    }
    
    public void addClientRecordIntoTable(int index, clientRecord cRecord, PdfPTable pdfPTable){
        pdfPTable.addCell(String.valueOf(index));
        pdfPTable.addCell(cRecord.getDescription().get(index - 1));
        pdfPTable.addCell("$" + cRecord.getAmount().get(index - 1));
        pdfPTable.addCell("$" + cRecord.getPaid().get(index - 1));
    }
    
    public PdfPTable addPaymentTable() throws DocumentException{
        PdfPTable pdfPTable = null;
        pdfPTable = new PdfPTable(1);
        pdfPTable.getDefaultCell().setBorder(0);
        pdfPTable.setTotalWidth(200);
        
        pdfPTable.setLockedWidth(true);
        pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        Phrase phrase = new Phrase("\n\nMode of Payment", Subtitle_Font);
        pdfPTable.addCell(phrase);
        
        PdfPTable p = new PdfPTable(2);
        p.getDefaultCell().setBorder(0);
        p.setTotalWidth(100);
        p.setWidths(new int[]{1, 4} );
        PdfPCell c1 =new PdfPCell();
        c1.setCellEvent(new CheckboxCellEvent("Cash", 0));
        //c1.setMinimumHeight(10);
        c1.setPadding(10);
        c1.setBorder(0);
        p.addCell(c1);
        PdfPCell p1 = new PdfPCell(new Phrase("Cash"));
        p1.setPadding(10);
        p1.setBorder(0);
        p.addCell(p1);
        PdfPCell c2 =new PdfPCell();
        c2.setCellEvent(new CheckboxCellEvent("Cheque", 0));
        //c2.setMinimumHeight(10);
        c2.setPadding(10);
        c2.setBorder(0);
        p.addCell(c2);
        PdfPCell p2 = new PdfPCell(new Phrase("Cheque"));
        p2.setPaddingLeft(10);
        p2.setBorder(0);
        p2.setPadding(10);
        p.addCell(p2);
        PdfPCell c3 =new PdfPCell();
        c3.setCellEvent(new CheckboxCellEvent("EFTPOS", 0));
        //c3.setMinimumHeight(10);
        c3.setPadding(10);
        c3.setBorder(0);
        p.addCell(c3);
        PdfPCell p3 = new PdfPCell(new Phrase("EFTPOS"));
        
        p3.setPaddingLeft(10);
        p3.setBorder(0);
        p3.setPadding(10);
        p.addCell(p3);
        pdfPTable.addCell(p);
        
        return pdfPTable;
    }
    
    public PdfPTable addPaymentDetail(company company){
        PdfPTable pdfPTable = null;
        pdfPTable = new PdfPTable(1);
        pdfPTable.getDefaultCell().setBorder(0);
        pdfPTable.setWidthPercentage(100);
        Phrase p1 = new Phrase("\n*** Please ensure the Total Amount Payable is paid within 10 working days. ***\n");
        PdfPCell p3 = new PdfPCell(p1);
        p3.setBorder(0);
        p3.setPaddingBottom(10);
        p3.setBorderWidthBottom(1);
        p3.setHorizontalAlignment(Element.ALIGN_CENTER);
        Paragraph p2 = new Paragraph("\nFor easy payment, please direct credit to:\n\nAccount Name:  " + company.getAccountName() + "\nAccount N0.:  " 
                + company.getAccountNo()+ "\nBank:  " + company.getBankName()+ "\nSwift Code:  " + company.getSwiftCode());
        pdfPTable.addCell(p3);
        pdfPTable.addCell(p2);
         return pdfPTable;
    }
    
    class CheckboxCellEvent implements PdfPCellEvent {
        // The name of the check box field
        protected String name;
        protected int i;
        // We create a cell event
        public CheckboxCellEvent(String name, int i) {
            this.name = name;
            this.i = i;
        }
        // We create and add the check box field
        public void cellLayout(PdfPCell cell, Rectangle position,
            PdfContentByte[] canvases) {
            PdfWriter writer = canvases[0].getPdfWriter(); 
            // define the coordinates of the middle
            float x = (position.getLeft() + position.getRight()) / 2;
            float y = (position.getTop() + position.getBottom()) / 2;
            // define the position of a check box that measures 20 by 20
            Rectangle rect = new Rectangle(x - 10, y - 10, x + 10, y + 10);
            // define the check box
            RadioCheckField checkbox = new RadioCheckField(
                    writer, rect, name, "Yes");
            checkbox.setBorderWidth(3);
            switch(i) {
                case 0:
                    checkbox.setCheckType(RadioCheckField.READ_ONLY);
                    break;
                case 1:
                    checkbox.setCheckType(RadioCheckField.TYPE_CIRCLE);
                    break;
                case 2:
                    checkbox.setCheckType(RadioCheckField.TYPE_CROSS);
                    break;
                case 3:
                    checkbox.setCheckType(RadioCheckField.TYPE_DIAMOND);
                    break;
                case 4:
                    checkbox.setCheckType(RadioCheckField.TYPE_SQUARE);
                    break;
                case 5:
                    checkbox.setCheckType(RadioCheckField.TYPE_STAR);
                    break;
            }
            checkbox.setChecked(true);
            // add the check box as a field
            try {
                writer.addAnnotation(checkbox.getCheckField());
            } catch (Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }

    /**
     * @return the newFile
     */
    public File getNewFile() {
        return newFile;
    }

    /**
     * @param newFile the newFile to set
     */
    public void setNewFile(File newFile) {
        this.newFile = newFile;
    }
}
