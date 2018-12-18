/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.CueroPesarCommands.c;
import Modelo.CueroPesar;
import Modelo.InvDispTrabajar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Domingo Luna
 */
public class InvDispTrabajarCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    static ConexionSQLServer cs=new ConexionSQLServer();
    private final String imagen="/Reportes/logo_esmar.png";
    
    
    //Método que se llama para obtener la lista del inventario disponible
    public static String[][] obtenerInvDispTrabajar(InvDispTrabajar idt) throws Exception {
        String query;        
        query= "SELECT * FROM invDispTrabajar"
                 + " WHERE tipoProducto LIKE "+idt.getTipoProducto()
                 + " AND calibre LIKE "+idt.getCalibre()
                 + " AND seleccion LIKE "+idt.getSeleccion()
                 + " AND fecha BETWEEN '"+idt.getFecha()+"' AND '"+idt.getFecha1()+"' "
                 +idt.getDescripcion()
                 +  " AND noPiezasActuales>0;";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 10;
        int i = 0;

        c.conectar();
        stmt = c.getConexion().createStatement();
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            renglones = rs.getRow();
            datos = new String[renglones][columnas];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("IdinvDispTrabajar");
                datos[i][1] = rs.getString("noPartida");
                datos[i][2] = rs.getString("tipoProducto");
                datos[i][3] = rs.getString("calibre");
                datos[i][4] = rs.getString("seleccion");
                datos[i][5] = rs.getString("peso");
                datos[i][6] = rs.getString("noPiezas");
                datos[i][7] = rs.getString("noPiezasActuales");
                datos[i][8] = rs.getString("descripcion");
                datos[i][9] = rs.getString("fecha");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    
    //    Método para realizar una entrada de producto a IdinvDispTrabajar
    public static void agregarIdinvDispTrabajar(InvDispTrabajar idt)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into InvDispTrabajar "
                    + "values (null,?,?,?,?,?,?,?,?,?)");
            
            st.setInt(1, idt.getNoPartida());
            st.setString(2, idt.getTipoProducto());
            st.setString(3, idt.getCalibre());
            st.setString(4, idt.getSeleccion());
            st.setDouble(5, idt.getPeso());
            st.setInt(6, idt.getNoPiezas());
            st.setInt(7, idt.getNoPiezasActuales());
            st.setString(8, idt.getDescripcion());
            st.setString(9, idt.getFecha());
            
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
