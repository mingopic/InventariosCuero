/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Domingo Luna
 */
public class ConexionMariaDB
{
    private Connection conn;
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    private final String imagen="/Reportes/logo_esmar.png";
    
    public ConexionMariaDB()
    {
        try
        {
            Class.forName("org.mariadb.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    public void conectar() throws Exception
    {
        String usuario = "root";
//        String contrasenia = "adminesmar";
        String contrasenia = "root";
        
        //Generamos una conexión solo si no existe alguna ó, si existe y ya está cerrada.
        if (conn == null || conn.isClosed())
//              conn = DriverManager.getConnection("jdbc:mariadb://192.168.0.3:3307/esmart", usuario, contrasenia);        
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/esmart", usuario, contrasenia);
    }
    
    public void desconectar() throws Exception 
    {
        conn.close(); 
    }
    
    public Connection getConexion()
    {
        return conn;
    }
}

