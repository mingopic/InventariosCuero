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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mingo
 */
public class ConexionSQLServer
{
    private Connection conn;
    String[] datosBD = null;
    
    public ConexionSQLServer()
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
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void conectar() throws Exception
    {
        //String url = "jdbc:sqlserver://MINGO-LAP:1433;databaseName=esmarProd";
        String url = "jdbc:sqlserver://"+datosBD[0]+":"+datosBD[5]+";databaseName="+datosBD[1];
        String usuario = datosBD[2];
        String password = datosBD[3];
        
        //Generamos una conexión solo si no existe alguna ó, si existe y ya está cerrada.
        if (conn == null || conn.isClosed())
        {
            conn = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conectado por SQL Server");
        } 
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
        String[] datos = new String[7];
        int j=0;
        
        FileReader f = new FileReader("datosBD.txt");
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