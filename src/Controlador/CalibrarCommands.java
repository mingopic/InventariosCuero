/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.CueroTrabajarCommands.c;
import Modelo.Calibrar;
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
public class CalibrarCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    private final String imagen="/Reportes/logo_esmar.png";
    
    public static String[][] obtenerListaCalibrar(Calibrar ca) throws Exception {
        String query= "SELECT * FROM calibrar"
                 + " WHERE tipoProducto LIKE '"+ca.getTipoProducto()
                 + "' AND calibre LIKE '"+ca.getCalibre()
                 + "' AND noPiezasActuales>0 "
                 + ca.getDescripcion()
                 + " AND fecha BETWEEN '"+ca.getFecha()+"' AND '"+ca.getFecha1()+"';";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 8;
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
                datos[i][0] = rs.getString("idCalibrar");
                datos[i][1] = rs.getString("noPartida");
                datos[i][2] = rs.getString("tipoProducto");
                datos[i][3] = rs.getString("calibre");
                datos[i][4] = rs.getString("noPiezas");
                datos[i][5] = rs.getString("noPiezasActuales");
                datos[i][6] = rs.getString("descripcion");
                datos[i][7] = rs.getString("fecha");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    
    //Método para llenar el combobox con los datos de los calibres existentes
    public static String[] llenarComboboxCalibres() throws Exception
    {
        String[] calibres=null;
        
        String query="SELECT descripcion FROM tipoCalibre;";
        
        Statement stmt = null;
        ResultSet rs = null;
        int renglones =1;
        int i = 0;
        
        c.conectar();
        stmt = c.getConexion().createStatement();
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) {
            renglones = rs.getRow();
            calibres = new String[renglones];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                calibres[i]= rs.getString("descripcion");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return calibres;
    }
    
    
    //    Método para realizar una entrada de producto calibrado
    public static void agregarProductoCalibrar(Calibrar ca)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into calibrar "
                    + "values (null,?,?,?,?,?,?,?)");
            
            st.setInt(1, ca.getNoPartida());
            st.setString(2, ca.getTipoProducto());
            st.setString(3, ca.getCalibre());
            st.setInt(4, ca.getNoPiezas());
            st.setInt(5, ca.getNoPiezasActuales());
            st.setString(6, ca.getDescripcion());
            st.setString(7, ca.getFecha());
            
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
    
    
    //    Método para realizar una entrada de producto seleccionado, reduce existencias del producto por trabajar
    public static void actualizarProductoCalibrado (int noPiezas, int idRegistro)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("update calibrar "
                    + "set noPiezasActuales=noPiezasActuales-? where idCalibrar=?");
            
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
    
    
    //Método para generar el reporte de los inventarios de cuero calibrado
    public void mostrarReporteInventarioProuctoCalibrado(Calibrar ca) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/CueroCalibrado.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoProducto", ca.getTipoProducto());
            parametros.put("calibre", ca.getCalibre());
            parametros.put("fecha", ca.getFecha());
            parametros.put("fecha2", ca.getFecha1());
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
