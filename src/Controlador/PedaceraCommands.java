/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.CalibrarCommands.rs;
import Modelo.Pedacera;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author SISTEMAS
 */
public class PedaceraCommands extends ConexionMariaDB{
    
    //Método que consulta y regresa los registros de la tabla pedacera
    public String[][] obtenerDatosPedacera(Pedacera p) throws Exception
    {
        String[][] datos=null;
        
        String query= "SELECT * FROM pedacera"
                 + " WHERE pesoActual>0 AND fecha BETWEEN '"+p.getFecha()+"' AND '"+p.getFecha1()+"';";
        
        int renglones=0, columnas=5, i=0;
        
        conectar();
        stmt=getConexion().createStatement();
        rs=stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("idPedacera");
                datos[i][1] = rs.getString("peso");
                datos[i][2] = rs.getString("pesoActual");
                datos[i][3] = rs.getString("descripcion");
                datos[i][4] = rs.getString("fecha");
                i++; 
            }
        }
        rs.close();
        return datos;
    }
    
    
    //    Método para realizar una entrada de pedacera
    public static void agregarProductoSaldo(Pedacera p)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into pedacera "
                    + "values (null,?,?,?,?)");
            
            st.setDouble(1, p.getPeso());
            st.setDouble(2, p.getPesoActual());
            st.setString(3, p.getDescripcion());
            st.setString(4, p.getFecha());
            
            System.out.println(st);
            st.executeUpdate();
        
        } 
        catch (Exception e) 
        {
            throw e;
        }
        finally
        {
            c.desconectar();
        }
    }
    
    
    //    Método para realizar una salida de pedacera
    public static void salirPedacera(Pedacera p)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("update pedacera "
                    + "set pesoActual=pesoActual-? where idPedacera=?");
            
            st.setDouble(1, p.getPesoActual());
            st.setInt(2, p.getIdPedacera());
            
            System.out.println(st);
            st.executeUpdate();
        
        } 
        catch (Exception e) 
        {
            throw e;
        }
        finally
        {
            c.desconectar();
        }
    }
}
