/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.CalibrarCommands.c;
import static Controlador.CueroTrabajarCommands.c;
import Modelo.Calibrar;
import Modelo.CueroSeleccionado;
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
public class SeleccionCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    private final String imagen="/Reportes/logo_esmar.png";
    
    
    //Método que se llama para obtener la lista de los cueros seleccionados
    public static String[][] obtenerListaCueroSeleccionado(CueroSeleccionado cs) throws Exception {
        String query;        
        query= "SELECT * FROM CueroSeleccionado"
                 + " WHERE tipoProducto LIKE '"+cs.getTipoProducto()
                 + "' AND calibre LIKE '"+cs.getCalibre()
                 + "' AND seleccion LIKE '"+cs.getSeleccion()
                 + "' AND fecha BETWEEN '"+cs.getFecha()+"' AND '"+cs.getFecha1()+"'"
                 +  " AND noPiezasActuales>0 "+cs.getDescripcion();

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 9;
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
                datos[i][0] = rs.getString("idcueroSeleccionado");
                datos[i][1] = rs.getString("noPartida");
                datos[i][2] = rs.getString("tipoProducto");
                datos[i][3] = rs.getString("calibre");
                datos[i][4] = rs.getString("seleccion");
                datos[i][5] = rs.getString("noPiezas");
                datos[i][6] = rs.getString("noPiezasActuales");
                datos[i][7] = rs.getString("descripcion");
                datos[i][8] = rs.getString("fecha");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    
    //Método para llenar el combobox con los datos de las selecciones en catálogo 
    public static String[] llenarComboboxSeleccion() throws Exception
    {
        String[] seleccion=null;
        
        String query="SELECT seleccion FROM seleccionCuero;";
        
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
            seleccion = new String[renglones];
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            while (rs.next()) {
                seleccion[i]= rs.getString("seleccion");
                i++;
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return seleccion;
    }
    
    
    //    Método para realizar una entrada de producto seleccionado
    public static void agregarProductoSeleccionar(CueroSeleccionado cs)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into CueroSeleccionado "
                    + "values (null,?,?,?,?,?,?,?,?)");
            
            st.setInt(1, cs.getNoPartida());
            st.setString(2, cs.getTipoProducto());
            st.setString(3, cs.getCalibre());
            st.setString(4, cs.getSeleccion());
            st.setInt(5, cs.getNoPiezas());
            st.setInt(6, cs.getNoPiezasActuales());
            st.setString(7, cs.getDescripcion());
            st.setString(8, cs.getFecha());
            
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
    
    
    //    Método para realizar una entrada de producto pesado, reduce existencias del producto seleccionado
    public static void actualizarProductoSeleccionado (int noPiezas, int idRegistro)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("update cueroSeleccionado "
                    + "set noPiezasActuales=noPiezasActuales-? where idcueroSeleccionado=?");
            
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
    
    
    //Método para generar el reporte de los inventarios de cuero seleccionado
    public void mostrarReporteInventarioProuctoSeleccionado(CueroSeleccionado cs) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/InventarioSeleccionado.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoProducto", cs.getTipoProducto());
            parametros.put("calibre", cs.getCalibre());
            parametros.put("seleccion", cs.getSeleccion());
            parametros.put("fecha", cs.getFecha());
            parametros.put("fecha2", cs.getFecha1());
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
