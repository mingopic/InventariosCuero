/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import static Controlador.CueroPesarCommands.c;
import Modelo.CueroPesar;
import Modelo.ProductoSaldo;
import Modelo.SaldoTerminadoTrabajar;
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
 * @author SISTEMAS
 */
public class SaldoTerminadoTrabajarCommands {
    static Statement stmt = null;
    static ResultSet rs = null;
    static ConexionMariaDB c=new ConexionMariaDB();
    private final String imagen="/Reportes/logo_esmar.png";
    
    //    Método para realizar una entrada de producto de saldo
    public static void agregarSaldoTrabajar(SaldoTerminadoTrabajar stt)throws Exception {
        try 
        {
            c.conectar();
            PreparedStatement st=c.getConexion().prepareStatement("insert into SaldoTerminadoTrabajar "
                    + "values (null,?,?,?,?,?,?,?,?)");
            
            st.setString(1, stt.getTipoProducto());
            st.setString(2, stt.getCalibre());
            st.setString(3, stt.getSeleccion());
            st.setDouble(4, stt.getPeso());
            st.setInt(5, stt.getNoPiezas());
            st.setInt(6, stt.getNoPiezasActuales());
            st.setString(7, stt.getDescripcion());
            st.setString(8, stt.getFecha());
            
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
