/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    String[] datosBD = null;
    
    public ConexionMariaDB()
    {
        try
        {
            datosBD = buscaDatos();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ConexionSQLServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            if (datosBD[0].equals("mariadb"))
            {
                Class.forName("org.mariadb.jdbc.Driver");
            }
            else
            {
                Class.forName("com.mysql.jdbc.Driver");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    
    public void conectar() throws Exception
    {   
        //Generamos una conexión solo si no existe alguna ó, si existe y ya está cerrada.
        if (conn == null || conn.isClosed())
              conn = DriverManager.getConnection("jdbc:"+datosBD[0]+"://"+datosBD[1]+":"+datosBD[2]+"/"+datosBD[3], datosBD[4], datosBD[5]);        
    }
    
    public void desconectar() throws Exception 
    {
        conn.close(); 
    }
    
    public Connection getConexion()
    {
        return conn;
    }
    
    public String[] buscaDatos() throws FileNotFoundException, IOException {
        String cadena;
        String[] datos = new String[6];
        int j=0;
        
        FileReader f = new FileReader("ConexionSQL.txt");
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) 
        {
            String[] palabra = cadena.split(":");
            
            for (int i = 0; i < palabra.length; i++) 
            {
                datos[j]=palabra[1].replaceAll("^\\s*","");
            }
            //System.out.println(datos[j]);
            j++;
        }
        b.close();
        
        return datos;
    }
}

