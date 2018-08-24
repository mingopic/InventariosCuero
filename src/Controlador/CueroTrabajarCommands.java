/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;


import Modelo.CueroTrabajar;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Domingo Luna
 */
public class CueroTrabajarCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    private final String imagen="/Reportes/logo_esmar.png";
    
    //Método que se llama para obtener la lista de los cueros por trabajar
    public static String[][] obtenerListaCueroTrabajar(CueroTrabajar ct) throws Exception {
        String query;
        String piezas;
        
        query= "SELECT * FROM cueroTrabajar"
                 + " WHERE tipoProducto LIKE "+ct.getTipoProducto()
                 + " "+ct.getDescripcion()
                 + " AND fecha BETWEEN '"+ct.getFecha()+"' AND '"+ct.getFecha1()+"'"
                +  " AND noPiezasActuales>0;";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 7;
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
                datos[i][0] = rs.getString("idCueroTrabajar");
                datos[i][1] = rs.getString("noPartida");
                datos[i][2] = rs.getString("tipoProducto");
                datos[i][3] = rs.getString("noPiezas");
                datos[i][4] = rs.getString("noPiezasActuales");
                datos[i][5] = rs.getString("descripcion");
                datos[i][6] = rs.getString("fecha");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para llenar el combobox con los datos de los productos existentes
    public static String[] llenarComboboxProductos() throws Exception
    {
        String[] productos=null;
        
        String query="SELECT tipoProducto FROM tipoProductoCuero "
                + "GROUP BY tipoProducto;";
        
        Statement stmt = null;
        ResultSet rs = null;
        int renglones = 0;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement();
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            productos = new String[renglones];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                productos[i]= rs.getString("tipoProducto");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return productos;
    }
    
    
//    Método para realizar una entrada de producto por trabajar
    public static void agregarEntradaProductoTrabajar (CueroTrabajar ct)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into cueroTrabajar "
                    + "values (null,?,?,?,?,?,?)");
            
            st.setInt(1, ct.getNoPartida());
            st.setString(2, ct.getTipoProducto());
            st.setInt(3, ct.getNoPiezas());
            st.setInt(4, ct.getNoPiezasActuales());
            st.setString(5, ct.getDescripcion());
            st.setString(6, ct.getFecha());
            
            st.executeUpdate();
        
        } 
        catch (Exception e) 
        {
            System.err.println(e);
        }
        finally
        {
            c.desconectar();
        }
    }
    
    
    //    Método para realizar una entrada de producto calibrado, reduce existencias del producto por trabajar
    public static void actualizarProductoTrabajar (int noPiezas, int idRegistro)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("update cueroTrabajar "
                    + "set noPiezasActuales=noPiezasActuales-? where idCueroTrabajar=?");
            
            st.setInt(1, noPiezas);
            st.setInt(2, idRegistro);
            
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

    
    //Método para generar el reporte de llos inventarios por trabajar
    public void mostrarReporteInventarioTrabajar(CueroTrabajar ct) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/InventarioTrabajar.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
