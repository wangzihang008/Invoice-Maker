/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invoicemaker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class connection {
    
    final private String url = "your database url";
    final private String username = "username";
    final private String password = "password";
    private Connection con;
    private Statement stmt;
    
    
    public connection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            //stmt = con.createStatement();
            ScriptRunner runner = new ScriptRunner(con, false, false);
            String file = "src/source/ini.sql";
            runner.runScript(new BufferedReader(new FileReader(file)));
            //stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet getResult(String sql){
        ResultSet re;
        try {
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
            re = this.stmt.executeQuery(sql);
            //stmt.close();
            //con.close();
            return re;
        } catch (SQLException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void excute(String sql){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
            stmt = con.createStatement();
            this.stmt.execute(sql);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
