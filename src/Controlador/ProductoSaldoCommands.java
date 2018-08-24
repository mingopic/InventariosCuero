/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.SaldoTerminadoTrabajarCommands.c;
import Modelo.ProductoSaldo;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
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
public class ProductoSaldoCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    private final String imagen="/Reportes/logo_esmar.png";
    
    
    //Método que se llama para obtener la lista del inventario disponible
    public static String[][] obtenerListaProductoSaldo(ProductoSaldo ps) throws Exception {
        String query;        
        query= "SELECT idProductoSaldo, tipoProducto, calibre, seleccion, peso, noPiezas, noPiezasActuales, AVG(peso/noPiezasActuales) as 'Peso_promedio', descripcion, fecha"
                 + " FROM ProductoSaldo"
                 + " WHERE tipoProducto LIKE '"+ps.getTipoProducto()
                 + "' AND calibre LIKE '"+ps.getCalibre()
                 + "' AND seleccion LIKE '"+ps.getSeleccion()
                 + "' AND fecha BETWEEN '"+ps.getFecha()+"' AND '"+ps.getFecha1()+"'"
                 + " AND noPiezasActuales>0 "
                 + ps.getDescripcion()
                 + " GROUP BY idProductoSaldo;";

        
        String[][] datos = null;
        int renglones = 0;
        int columnas = 11;
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
            DecimalFormat df = new DecimalFormat("0.00"); 
            while (rs.next()) 
            {
                datos[i][0] = rs.getString("idProductoSaldo");
                datos[i][1] = rs.getString("tipoProducto");
                datos[i][2] = rs.getString("noPiezas");
                datos[i][3] = rs.getString("noPiezasActuales");
                datos[i][4] = rs.getString("peso");
                datos[i][5] = df.format(Double.parseDouble(rs.getString("Peso_promedio")));
                datos[i][6] = rs.getString("seleccion");
                datos[i][7] = rs.getString("calibre");
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
    
    
    //    Método para realizar una entrada de producto saldo
    public static void agregarProductoSaldo(ProductoSaldo ps)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into ProductoSaldo "
                    + "values (null,?,?,?,?,?,?,?,?)");
            
            st.setString(1, ps.getTipoProducto());
            st.setString(2, ps.getCalibre());
            st.setString(3, ps.getSeleccion());
            st.setDouble(4, ps.getPeso());
            st.setInt(5, ps.getNoPiezas());
            st.setInt(6, ps.getNoPiezasActuales());
            st.setString(7, ps.getDescripcion());
            st.setString(8, ps.getFecha());
            
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
   
    
    //    Método para realizar una salida de producto saldo
    public static void salirProductoSaldo(ProductoSaldo ps)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("update ProductoSaldo "
                    + "set noPiezasActuales=noPiezasActuales-?, peso=peso-? "
                    + "where idProductoSaldo=?");
            
            st.setInt(1, ps.getNoPiezasActuales());
            st.setDouble(2, ps.getPeso());
            st.setInt(3, ps.getIdProductoSaldo());
            
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
    
    
    //Método para generar el reporte de inventario de producto de saldo
    public void mostrarReporteInventarioSaldo(ProductoSaldo ps) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/InventarioSaldo.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoProducto", ps.getTipoProducto());
            parametros.put("calibre", ps.getCalibre());
            parametros.put("seleccion", ps.getSeleccion());
            parametros.put("fecha1", ps.getFecha());
            parametros.put("fecha2", ps.getFecha1());
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
