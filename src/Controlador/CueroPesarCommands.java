/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.SeleccionCommands.c;
import Modelo.Calibrar;
import Modelo.CueroPesar;
import Modelo.CueroSeleccionado;
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
public class CueroPesarCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    static ConexionSQLServer cs=new ConexionSQLServer();
    private final String imagen="/Reportes/logo_esmar.png";
    
    
    //Método que se llama para obtener la lista de los cueros pesado
    public static String[][] obtenerListaCueroPesado(CueroPesar cp) throws Exception {
        String query;        
        query= "SELECT idcueroPesado, noPartida, tipoProducto, calibre, seleccion, peso, noPiezas, noPiezasActuales, AVG(peso/noPiezasActuales) as 'Peso_promedio', descripcion, fecha"
                 + " FROM cueroPesado"
                 + " WHERE tipoProducto LIKE '"+cp.getTipoProducto()
                 + "' AND calibre LIKE '"+cp.getCalibre()
                 + "' AND seleccion LIKE '"+cp.getSeleccion()
                 + "' AND fecha BETWEEN '"+cp.getFecha()+"' AND '"+cp.getFecha1()+"'"
                 + " AND noPiezasActuales>0 "
                 + cp.getDescripcion()
                 + cp.getNoPartida()
                 + " GROUP BY idCueroPesado;";

        
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
                datos[i][0] = rs.getString("idcueroPesado");
                datos[i][1] = rs.getString("noPartida");
                datos[i][2] = rs.getString("tipoProducto");
                datos[i][3] = rs.getString("noPiezas");
                datos[i][4] = rs.getString("noPiezasActuales");
                datos[i][5] = rs.getString("peso");
                datos[i][6] = df.format(Double.parseDouble(rs.getString("Peso_promedio")));
                datos[i][7] = rs.getString("seleccion");
                datos[i][8] = rs.getString("calibre");
                datos[i][9] = rs.getString("descripcion");
                datos[i][10] = rs.getString("fecha");
                i++; 
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    
    //    Método para realizar una entrada de producto pesado
    public static void agregarProductoPesar(CueroPesar cp)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into cueroPesado "
                    + "values (null,?,?,?,?,?,?,?,?,?)");
            
            st.setString(1, cp.getNoPartida());
            st.setString(2, cp.getTipoProducto());
            st.setString(3, cp.getCalibre());
            st.setString(4, cp.getSeleccion());
            st.setDouble(5, cp.getPeso());
            st.setInt(6, cp.getNoPiezas());
            st.setInt(7, cp.getNoPiezasActuales());
            st.setString(8, cp.getDescripcion());
            st.setString(9, cp.getFecha());
            
            System.out.println(st);
            st.executeUpdate();
            
            // DLUNA 19-08-2018
            // Se agrega funcionalidad para actualizar inventarios
        
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
    
    
    //    Método para actualizar una entrada de producto pesado
    public static void modificarProductoPesar(int noPiezas, int idCueroPesar, Double peso)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("UPDATE cueroPesado SET "
                    + "noPiezasActuales=noPiezasActuales-?, peso=peso-? WHERE idcueroPesado=?");
            
            st.setInt(1, noPiezas);
            st.setDouble(2, peso);
            st.setInt(3, idCueroPesar);
            
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
    
    
    //Método para generar el reporte de las entradas de cuero seleccionado
    public void mostrarReporteEntradasProuctoPesado(CueroPesar cp) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/CueroPesado.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoProducto", cp.getTipoProducto());
            parametros.put("calibre", cp.getCalibre());
            parametros.put("seleccion", cp.getSeleccion());
            parametros.put("fecha1", cp.getFecha());
            parametros.put("fecha2", cp.getFecha1());
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Método para generar el reporte de los inventarios de cuero seleccionado
    public void mostrarReporteInventarioProuctoPesado(CueroPesar cp) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/InventarioPesado.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoproducto", cp.getTipoProducto());
            parametros.put("calibre", cp.getCalibre());
            parametros.put("seleccion", cp.getSeleccion());
            parametros.put("fecha", cp.getFecha());
            parametros.put("fecha2", cp.getFecha1());
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Método para generar el reporte de las salidas de producto semiterminado
    public void mostrarReporteSalidasSemiterminado(CueroPesar cp) throws Exception {
        try 
        {
            URL dir=this.getClass().getResource("/Reportes/ReporteSalidas.jasper");
            
            JasperReport reporte=(JasperReport) JRLoader.loadObject(dir);
            
            c=new ConexionMariaDB();
            c.conectar();
            Map parametros=new HashMap();
            
            parametros.put("logo", this.getClass().getResourceAsStream(imagen));
            parametros.put("tipoProducto", cp.getTipoProducto());
            parametros.put("calibre", cp.getCalibre());
            parametros.put("seleccion", cp.getSeleccion());
            parametros.put("fecha", cp.getFecha());
            parametros.put("fecha2", cp.getFecha1());
            
            JasperPrint p = JasperFillManager.fillReport(reporte, parametros, c.getConexion());
            JasperViewer.viewReport(p,false);
            c.desconectar();
        }
        catch (Exception ex) {
            Logger.getLogger(CueroTrabajarCommands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Método para obtener el ultimo id de la tabla CueroPesado
    public static int obtenerUltimoIdCueroPesado() throws Exception {
        String query;        
        query= "SELECT MAX (idcueroPesado) AS idcueroPesado"
                 + " FROM cueroPesado;";

        
        int datos = 0;

        c.conectar();
        stmt = c.getConexion().createStatement();
        rs = stmt.executeQuery(query);
        System.out.println(query);
        
        if (rs.last()) 
        {
            rs.beforeFirst();

            //Recorremos el ResultSet registro a registro
            DecimalFormat df = new DecimalFormat("0.00"); 
            while (rs.next()) 
            {
                datos = Integer.parseInt(rs.getString("idcueroPesado"));
            }
        }
        
        rs.close();
        stmt.close();
        c.desconectar();
        return datos;
    }
    
    //Método para insertar el registro de cuero pesado en la base de datos esmarProd
    public static void insertarProductoPesar(int idInventario, CueroPesar cp) throws Exception {
        String query = "exec sp_insCuePesado "
                + idInventario
                + ", " + cp.getNoPartida()
                + ", '" + cp.getTipoProducto()
                + "', '" + cp.getCalibre()
                + "', '" + cp.getSeleccion()
                + "', " + cp.getPeso()
                + ", " + cp.getNoPiezas()
                + ", " + cp.getNoPiezasActuales()
                + ", '" + cp.getDescripcion()
                +"'";
        
        PreparedStatement pstmt = null;
        cs.conectar();
        pstmt = cs.getConexion().prepareStatement(query);
        System.out.println(query);
        pstmt.executeUpdate();
        cs.desconectar();
    }
}
