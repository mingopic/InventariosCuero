/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.ConexionMariaDB.stmt;
import java.sql.ResultSet;

/**
 *
 * @author Domingo Luna
 */
public class ObtenerFechaHora extends ConexionMariaDB{
    
    public String ObtenerFechaHora()
    {               
        String fecha_y_hora=getDate_Full();         
        String fecha="";        
        String hora="";         
        String x[]=fecha_y_hora.split(" ");        
        String x1[]=x[0].split("-");         
        fecha =x1[0]+"-"+x1[1]+"-"+x1[2];        
        hora= x[1];         
        hora=hora.substring(0,hora.length()-2);                   
//        System.out.println(fecha);                
//        System.out.println("Hora: "+hora);
        return fecha;
    }           
    
    public String getDate_Full()     
    {         
        String sql="SELECT NOW() AS Hora_Fecha";               
        String fx="2010-02-01 00:00:00";         
        try         
        {             
            conectar();
            stmt=getConexion().createStatement();
            rs=stmt.executeQuery(sql); 
            while(rs.next())             
            {                 
                fx=rs.getString("Hora_Fecha");             
            }         
        }
        catch(Exception e )        
        {             
            System.out.print(e);         
        }               
        return fx;     
    }             
}

