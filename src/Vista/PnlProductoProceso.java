/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.CalibrarCommands;
import Controlador.CueroPesarCommands;
import Controlador.CueroTrabajarCommands;
import Controlador.InvDispTrabajarCommands;
import Controlador.SeleccionCommands;
import Modelo.Calibrar;
import Modelo.CueroPesar;
import Modelo.CueroSeleccionado;
import Modelo.CueroTrabajar;
import Modelo.InvDispTrabajar;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Domingo Luna
 */
public class PnlProductoProceso extends javax.swing.JPanel {
    CueroSeleccionado cs;
    Calibrar calibrar;
    CueroTrabajar ct;
    CueroPesar cp;
    InvDispTrabajar idt;
    
    CueroTrabajarCommands ctc;
    CalibrarCommands cc;
    SeleccionCommands sc;
    CueroPesarCommands cpc;
    InvDispTrabajarCommands idtc;
    
    String[] colsPesar = new String[]
    {
        "ID Registro","No. Partida","Tipo del producto","No. piezas iniciales","No. Piezas Actuales","Peso","Peso prom. X pieza","Selección","Calibre","Descripción","Fecha de entrada"
    };
    
    String[] colsSeleccionar = new String[]
    {
        "ID Registro","No. Partida","Tipo del producto","Calibre","selección","No. piezas iniciales","No. Piezas Actuales","Descripción","Fecha de entrada"
    };
    
    String[] colsCalibrar = new String[]
    {
        "ID Registro", "No. Partida","Tipo del producto","Calibre","No. piezas iniciales","No. Piezas Actuales","Descripción","Fecha de entrada"
    };
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "ID Registro","No. Partida","Tipo del producto","No. piezas iniciales","No. Piezas Actuales","Descripción","Fecha de entrada"
    };

    /**
     * Creates new form PnlProductoProceso
     */
    public PnlProductoProceso() throws Exception {
        initComponents();
        inicializar();
    }
    
    public void inicializar() throws Exception
    {
        calibrar=new Calibrar();
        cs=new CueroSeleccionado();
        cc=new CalibrarCommands();
        sc=new SeleccionCommands();
        cp=new CueroPesar();
        cpc=new CueroPesarCommands();
        
        actualizarTablaCalibrar();
        actualizarTablaSeleccionar();
        actualizarTablaPesar();
        
        llenarComboTipoProducto();
        llenarComboTipoCalibre();
        llenarComboSeleccion();
        
        dcFecha1.setEnabled(false);
        dcFecha2.setEnabled(false);
        dcFecha1Seleccionar.setEnabled(false);
        dcFecha2Seleccionar.setEnabled(false);
        dcFecha1Pesar.setEnabled(false);
        dcFecha2Pesar.setEnabled(false);
        
        jrFiltroFechas.setSelected(false);
        jrFiltroFechasSeleccionar.setSelected(false);
        jrFiltroFechasSeleccionar1.setSelected(false);
    }

    
    //Método para actualizar la tabla de las entradas de cuero por trabajar, se inicializa al llamar la clase
    public void actualizarTablaCalibrar() 
    {   
        buscaFiltrosCalibrar();
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try 
        {
            datos = cc.obtenerListaCalibrar(calibrar);
            
            dtm = new DefaultTableModel(datos, colsCalibrar){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblCalibrar.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    //Método para actualizar la tabla de las entradas de cuero seleccionado, se inicializa al llamar la clase
    public void actualizarTablaSeleccionar() 
    {        
        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
        if (jrFiltroFechasSeleccionar.isSelected())
        {
            try {
                    String fechaAux="";
                    String fecha=dcFecha1Seleccionar.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    cs.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    cs.setFecha("0");
                }
            
            try {
                    String fechaAux="";
                    String fecha=dcFecha2Seleccionar.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    //obtiene año
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene mes
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene día
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    cs.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    cs.setFecha1("0");
                }
        }
        else
        {
            cs.setFecha("1900-01-01");
            cs.setFecha1("2040-01-01");
        }
        
        
        //validamos si esta seleccionado algún producto para hacer filtro
        if (cmbTipoEntradaProductoSeleccionar.getSelectedItem().toString().equals("..."))
        {
            cs.setTipoProducto("%%");
        }
        else
        {
            cs.setTipoProducto(cmbTipoEntradaProductoSeleccionar.getSelectedItem().toString());
        }
        
        //validamos si esta seleccionado algún calibre para hacer filtro
        if (cmbCalibreSeleccionar.getSelectedItem().toString().equals("..."))
        {
            cs.setCalibre("%%");
        }
        else
        {
            cs.setCalibre(cmbCalibreSeleccionar.getSelectedItem().toString());
        }
        
         //validamos si esta seleccionado alguna seección para hacer filtro
        if (cmbSeleccion.getSelectedItem().toString().equals("..."))
        {
            cs.setSeleccion("%%");
        }
        else
        {
            cs.setSeleccion(cmbSeleccion.getSelectedItem().toString());
        }
        
        //validar descripcion
        cs.setDescripcion("");
        
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try 
        {
            datos = sc.obtenerListaCueroSeleccionado(cs);
            
            dtm = new DefaultTableModel(datos, colsSeleccionar){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblSeleccionar.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    
    //Método para actualizar la tabla de las entradas de cuero pesado, se inicializa al llamar la clase
    public void actualizarTablaPesar() 
    {        
        buscaFiltrosPesar();
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try 
        {
            datos = cpc.obtenerListaCueroPesado(cp);
            
            dtm = new DefaultTableModel(datos, colsPesar){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblPesar.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    
    public void buscaFiltrosCalibrar()
    {
        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
        if (jrFiltroFechas.isSelected())
        {
            try {
                    String fechaAux="";
                    String fecha=dcFecha1.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    calibrar.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    calibrar.setFecha("0");
                }
            
            try {
                    String fechaAux="";
                    String fecha=dcFecha2.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    //obtiene año
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene mes
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene día
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    calibrar.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    calibrar.setFecha1("0");
                }
        }
        else
        {
            calibrar.setFecha("1900-01-01");
            calibrar.setFecha1("2040-01-01");
        }
        
        
        //validamos si esta seleccionado algún producto para hacer filtro
        if (cmbTipoEntradaProducto.getSelectedItem().toString().equals("..."))
        {
            calibrar.setTipoProducto("%%");
        }
        else
        {
            calibrar.setTipoProducto(cmbTipoEntradaProducto.getSelectedItem().toString());
        }
        
        //validamos si esta seleccionado algún calibre para hacer filtro
        if (cmbCalibre.getSelectedItem().toString().equals("..."))
        {
            calibrar.setCalibre("%%");
        }
        else
        {
            calibrar.setCalibre(cmbCalibre.getSelectedItem().toString());
        }
        
        //validar descripcion
        calibrar.setDescripcion("");
    }
    
    
    public void buscaFiltrosPesar()
    {
        if (txtNoPartidaPesado.getText().equals(""))
        {
            cp.setNoPartida("");
        }
        else
        {
            cp.setNoPartida("AND noPartida="+txtNoPartidaPesado.getText());
        }
        
        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
        if (jrFiltroFechasSeleccionar1.isSelected())
        {
            try {
                    String fechaAux="";
                    String fecha=dcFecha1Pesar.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    cp.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    cp.setFecha("0");
                }
            
            try {
                    String fechaAux="";
                    String fecha=dcFecha2Pesar.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    //obtiene año
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene mes
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene día
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    cp.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    cp.setFecha1("0");
                }
        }
        else
        {
            cp.setFecha("1900-01-01");
            cp.setFecha1("2040-01-01");
        }
        
        
        //validamos si esta seleccionado algún producto para hacer filtro
        if (cmbTipoEntradaProductoPesar.getSelectedItem().toString().equals("..."))
        {
            cp.setTipoProducto("%%");
        }
        else
        {
            cp.setTipoProducto(cmbTipoEntradaProductoPesar.getSelectedItem().toString());
        }
        
        //validamos si esta seleccionado algún calibre para hacer filtro
        if (cmbCalibrePesar.getSelectedItem().toString().equals("..."))
        {
            cp.setCalibre("%%");
        }
        else
        {
            cp.setCalibre(cmbCalibrePesar.getSelectedItem().toString());
        }
        
         //validamos si esta seleccionado alguna seección para hacer filtro
        if (cmbSeleccionPesar.getSelectedItem().toString().equals("..."))
        {
            cp.setSeleccion("%%");
        }
        else
        {
            cp.setSeleccion(cmbSeleccionPesar.getSelectedItem().toString());
        }
        
        //
        cp.setDescripcion("");
    }
    
    
    public void buscaFiltrosSeleccionar()
    {
        //validamos si esta seleccionada lo opción de rango de fechas para tomar el valor seleccionado,
        //si no esta seleccionado se ponen automáticamente los valores 1900-01-01 y 2040-01-01
        if (jrFiltroFechas.isSelected())
        {
            try {
                    String fechaAux="";
                    String fecha=jrFiltroFechas.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    calibrar.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    calibrar.setFecha("0");
                }
            
            try {
                    String fechaAux="";
                    String fecha=dcFecha2.getText();
                    
                    if (fecha.length()<10)
                    {
                        fecha="0"+fecha;
                    }
                    
                    //obtiene año
                    for (int i=6; i<fecha.length(); i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene mes
                    for (int i=3; i<5; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                    fechaAux=fechaAux+"-";
                    
                    //obtiene día
                    for (int i=0; i<2; i++)
                    {
                        fechaAux=fechaAux+fecha.charAt(i);
                    }
                            
                    calibrar.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    calibrar.setFecha1("0");
                }
        }
        else
        {
            calibrar.setFecha("1900-01-01");
            calibrar.setFecha1("2040-01-01");
        }
        
        
        //validamos si esta seleccionado algún producto para hacer filtro
        if (cmbTipoEntradaProducto.getSelectedItem().toString().equals("..."))
        {
            calibrar.setTipoProducto("'%%'");
        }
        else
        {
            calibrar.setTipoProducto("'"+cmbTipoEntradaProducto.getSelectedItem().toString()+"'");
        }
        
        //validamos si esta seleccionado algún calibre para hacer filtro
        if (cmbCalibre.getSelectedItem().toString().equals("..."))
        {
            calibrar.setCalibre("'%%'");
        }
        else
        {
            calibrar.setCalibre("'"+cmbCalibre.getSelectedItem().toString()+"'");
        }
        
        //validar descripcion
        calibrar.setDescripcion("");
    }
    
    //Método para actualizar la tabla de productos a agregar a la entrada de cuero a calibrar
    public void actualizarTablaProductosPorTrabajar(boolean descripcion) 
    {  
        ct=new CueroTrabajar();
        ctc=new CueroTrabajarCommands();
        
        ct.setFecha("1900-01-01");
        ct.setFecha1("2040-01-01");     
        ct.setTipoProducto("'%%'");
        
        if (descripcion==true)
        {
            ct.setDescripcion("AND (descripcion>='A') AND (descripcion<='Z')");
        }
        if (descripcion==false)
        {
            ct.setDescripcion("AND descripcion LIKE ''");
        }
        
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try {
            
            datos = ctc.obtenerListaCueroTrabajar(ct);
            
            dtm = new DefaultTableModel(datos, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblBuscarPartidaCalibrar.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
   
    
    //Método para actualizar la tabla de productos a agregar a la entrada de cuero a seleccionar
    public void actualizarTablaProductosASeleccionar(boolean descripcion) 
    {  
        cc=new CalibrarCommands();
        calibrar=new Calibrar();
        
        calibrar.setFecha("1900-01-01");
        calibrar.setFecha1("2040-01-01");     
        calibrar.setTipoProducto("%%");
        calibrar.setCalibre("%%");
        
        if (descripcion==true)
        {
            calibrar.setDescripcion("AND (descripcion>='A') AND (descripcion<='Z')");
        }
        if (descripcion==false)
        {
            calibrar.setDescripcion("AND descripcion LIKE ''");
        }
        
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try {
            
            datos = cc.obtenerListaCalibrar(calibrar);
            
            dtm = new DefaultTableModel(datos, colsCalibrar){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblBuscarPartidaSeleccionar.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    
    //Método para actualizar la tabla de productos a agregar a la entrada de cuero a pesar
    public void actualizarTablaProductosAPesar(boolean descripcion) 
    {  
        sc=new SeleccionCommands();
        cs=new CueroSeleccionado();
        
        cs.setFecha("1900-01-01");
        cs.setFecha1("2040-01-01");     
        cs.setTipoProducto("%%");
        cs.setCalibre("%%");
        cs.setSeleccion("%%");
        
        if (descripcion==true)
        {
            cs.setDescripcion("AND (descripcion>='A') AND (descripcion<='Z')");
        }
        if (descripcion==false)
        {
            cs.setDescripcion("AND descripcion LIKE ''");
        }
        
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try {
            
            datos = sc.obtenerListaCueroSeleccionado(cs);
            
            dtm = new DefaultTableModel(datos, colsSeleccionar){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblBuscarPartidaPesar.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    
    //Método para actualizar la tabla de productos a agregar a la entrada de invDisponible
    public void actualizarTablaProductosAInvDisp(boolean descripcion) 
    {  
        cp=new CueroPesar();
        cpc=new CueroPesarCommands();
        
        cp.setNoPartida("");
        
        if (descripcion==true)
        {
            cp.setDescripcion("AND (descripcion>='A') AND (descripcion<='Z')");
        }
        if (descripcion==false)
        {
            cp.setDescripcion("AND descripcion LIKE ''");
        }
        
        cp.setFecha("1900-01-01");
        cp.setFecha1("2040-01-01");
        cp.setTipoProducto("%%");
        cp.setCalibre("%%");
        cp.setSeleccion("%%");
        
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try {
            
            datos = cpc.obtenerListaCueroPesado(cp);
            
            dtm = new DefaultTableModel(datos, colsPesar){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblBuscarPartidaProdPesado.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    
    //método que llena los combobox de los tipos de producto en la base de datos
    public void llenarComboTipoProducto() throws Exception
    {
        ctc=new CueroTrabajarCommands();
//        cmbTipoEntradaProducto.removeAllItems();
        String[] productos=ctc.llenarComboboxProductos();
        
        int i=0;
        while (i<productos.length)
        {
            cmbTipoEntradaProducto.addItem(productos[i]);
            cmbTipoEntradaProductoSeleccionar.addItem(productos[i]);
            cmbTipoEntradaProductoPesar.addItem(productos[i]);
            i++;
        }
    }
    
    
    //método que llena los combobox de los tipos de calibre en la base de datos
    public void llenarComboTipoCalibre() throws Exception
    {
        cc=new CalibrarCommands();
//        cmbCalibre.removeAllItems();
        String[] calibres=cc.llenarComboboxCalibres();
        
        int i=0;
        while (i<calibres.length)
        {
            cmbCalibre.addItem(calibres[i]);
            cmbCalibreSeleccionar.addItem(calibres[i]);
            cmbCalibrePesar.addItem(calibres[i]);
            i++;
        }
    }
    
    
    //método que llena los combobox de los tipos de selección en la base de datos
    public void llenarComboSeleccion() throws Exception
    {
        sc=new SeleccionCommands();
        String[] seleccion=sc.llenarComboboxSeleccion();
        
        int i=0;
        while (i<seleccion.length)
        {
            cmbSeleccion.addItem(seleccion[i]);
            cmbSeleccionPesar.addItem(seleccion[i]);
            i++;
        }
    }
    
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inicializarCamposAgregar() throws Exception
    {
        bgGrupo1.add(jrNoPartida);
        bgGrupo1.add(jrDescripcion);
        
        jrNoPartida.setSelected(true);
        txtNoPartidaAgregar.setEnabled(true);
        txtNoPartidaAgregar.setText("");
        txtDescripcion.setText("");
        txtDescripcion.setEnabled(false);
        
        calibrar=new Calibrar();
        txtTipoProducto.setText("");
        lblErrorAgregar.setText("");
        lblIdRegistroCalibrar.setText("");
        lblIdRegistroCalibrar.setVisible(false);
        txtNoPiezasCueroAgregar.setText("");
        
        //Llenar comboBox con los tipos de calibre
        //----------------------------------------------------------------------
        cc=new CalibrarCommands();
        cmbCalibreAgregar.removeAllItems();
        String[] productos=cc.llenarComboboxCalibres();
        int i=0;
        while (i<productos.length)
        {
            cmbCalibreAgregar.addItem(productos[i]);
            i++;
        }
        //----------------------------------------------------------------------
    }   
    
    
    //Metodo para inicializar los campos de dlgAgregarSelecciar
    public void inicializarCamposAgregarSeleccionar() throws Exception
    {
        bgGrupo2.add(jrNoPartidaSeleccion);
        bgGrupo2.add(jrDescripcionSeleccion);
        jrNoPartidaSeleccion.setSelected(true);
        
        cs=new CueroSeleccionado();
        txtNoPartidaAgregarSeleccionar.setText("");
        txtTipoProductoSeleccionar.setText("");
        lblErrorAgregarSeleccionar.setText("");
        lblidRegistroSeleccionar.setText("");
        lblidRegistroSeleccionar.setVisible(false);
        txtCalibreSeleccionar.setText("");
        txtNoPiezasCueroSeleccionar.setText("");
        txtDescripcionSeleccionar.setText("");
        
        //Llenar comboBox con los tipos de seleccion
        //----------------------------------------------------------------------
        sc=new SeleccionCommands();
        cmbSeleccionAgregar.removeAllItems();
        String[] seleccion=sc.llenarComboboxSeleccion();
        int i=0;
        while (i<seleccion.length)
        {
            cmbSeleccionAgregar.addItem(seleccion[i]);
            i++;
        }
        //----------------------------------------------------------------------
    }
    
    
    //Metodo para inicializar los campos de dlgAgregarSelecciar
    public void inicializarCamposAgregarPesar() throws Exception
    {
        bgGrupo3.add(jrNoPartidaPesar);
        bgGrupo3.add(jrDescripcionPesar);
        jrNoPartidaPesar.setSelected(true);
        
        cp=new CueroPesar();
        txtNoPartidaAgregarPesar.setText("");
        txtTipoProductoPesar.setText("");
        txtCalibrePesar.setText("");
        txtSeleccionPesar.setText("");
        lblErrorAgregarPesar.setText("");
        lblIdRegistroPesar.setText("");
        lblIdRegistroPesar.setVisible(false);
        txtNoPiezasCueroPesar.setText("");
        txtDescripcionPesar.setText("");
        txtKgPesar.setText("");
    }
    
    
    //Metodo para inicializar los campos de dlgAgregarInvDisp
    public void inicializarCamposAgregarInvDisp() throws Exception
    {
        bgGrupo4.add(jrNoPartidaInvDisp);
        bgGrupo4.add(jrDescripcionInvDisp);
        jrNoPartidaInvDisp.setSelected(true);
        
        idt=new InvDispTrabajar();
        txtNoPartidaAgregarInvDisp.setText("");
        txtTipoProductoInvDisp.setText("");
        txtCalibreInvDisp.setText("");
        txtSeleccionInvDisp.setText("");
        lblErrorAgregarInvDisp.setText("");
        lblIdRegistroInvDisp.setText("");
        lblIdRegistroInvDisp.setVisible(false);
        txtNoPiezasCueroInvDisp.setText("");
        txtKgInvDisp.setText("");
        txtDescripcionInvDisp.setText("");
    }
    
    
    //Método que abre el dialogo de agregar entrada de producto a calibrar
    public void abrirDialogoAgregar() throws Exception
    {
        inicializarCamposAgregar();
        
        dlgAgregar.setSize(400, 380);
        dlgAgregar.setPreferredSize(dlgAgregar.getSize());
        dlgAgregar.setLocationRelativeTo(null);
        dlgAgregar.setAlwaysOnTop(true);
        dlgAgregar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de agregar entrada de producto seleccionado
    public void abrirDialogoAgregarSeleccionar() throws Exception
    {
        inicializarCamposAgregarSeleccionar();
        
        dlgAgregarSeleccionar.setSize(450, 410);
        dlgAgregarSeleccionar.setPreferredSize(dlgBuscarSeleccionar.getSize());
        dlgAgregarSeleccionar.setLocationRelativeTo(null);
        dlgAgregarSeleccionar.setAlwaysOnTop(true);
        dlgAgregarSeleccionar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de agregar entrada de producto pesado
    public void abrirDialogoAgregarPesar() throws Exception
    {
        inicializarCamposAgregarPesar();
        
        dlgAgregarPesar.setSize(450, 460);
        dlgAgregarPesar.setPreferredSize(dlgAgregarPesar.getSize());
        dlgAgregarPesar.setLocationRelativeTo(null);
        dlgAgregarPesar.setAlwaysOnTop(true);
        dlgAgregarPesar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de agregar entrada a invDisp
    public void abrirDialogoAgregarInvDisp() throws Exception
    {
        inicializarCamposAgregarInvDisp();
        
        dlgAgregarInvDisponible.setSize(470, 460);
        dlgAgregarInvDisponible.setPreferredSize(dlgAgregarInvDisponible.getSize());
        dlgAgregarInvDisponible.setLocationRelativeTo(null);
        dlgAgregarInvDisponible.setAlwaysOnTop(true);
        dlgAgregarInvDisponible.setVisible(true);
    }
    
    
    //Método que abre el dialogo de buscar partida de producto a calibrar
    public void abrirDialogoBuscarPartida() throws Exception
    {   
        boolean descripcion=false;
        if (jrNoPartida.isSelected()==true)
        {
            descripcion=false;
        }
        if (jrDescripcion.isSelected()==true)
        {
            descripcion=true;
        }
        
        dlgAgregar.setVisible(false);
        actualizarTablaProductosPorTrabajar(descripcion);
        
        dlgBuscar.setSize(600,320);
        dlgBuscar.setPreferredSize(dlgBuscar.getSize());
        dlgBuscar.setLocationRelativeTo(null);
        dlgBuscar.setAlwaysOnTop(true);
        dlgBuscar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de buscar partida de producto a seleccionar
    public void abrirDialogoBuscarPartidaSeleccionar() throws Exception
    {   
        boolean descripcion=false;
        if (jrNoPartidaSeleccion.isSelected()==true)
        {
            descripcion=false;
        }
        if (jrDescripcionSeleccion.isSelected()==true)
        {
            descripcion=true;
        }
        
        dlgAgregarSeleccionar.setVisible(false);
        actualizarTablaProductosASeleccionar(descripcion);
        
        dlgBuscarSeleccionar.setSize(600,335);
        dlgBuscarSeleccionar.setPreferredSize(dlgBuscarSeleccionar.getSize());
        dlgBuscarSeleccionar.setLocationRelativeTo(null);
        dlgBuscarSeleccionar.setAlwaysOnTop(true);
        dlgBuscarSeleccionar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de buscar partida de producto a pesar
    public void abrirDialogoBuscarPartidaPesar() throws Exception
    {   
        dlgAgregarPesar.setVisible(false);
        
        boolean descripcion=false;
        if (jrNoPartidaPesar.isSelected()==true)
        {
            descripcion=false;
        }
        if (jrDescripcionPesar.isSelected()==true)
        {
            descripcion=true;
        }
        actualizarTablaProductosAPesar(descripcion);
        
        dlgBuscarPesar.setSize(670,340);
        dlgBuscarPesar.setPreferredSize(dlgBuscarPesar.getSize());
        dlgBuscarPesar.setLocationRelativeTo(null);
        dlgBuscarPesar.setAlwaysOnTop(true);
        dlgBuscarPesar.setVisible(true);
    }
    
    
    //Método que abre el dialogo de buscar partida de invDisp
    public void abrirDialogoBuscarPartidaInvDisp() throws Exception
    {   
        dlgAgregarInvDisponible.setVisible(false);
        
        boolean descripcion=false;
        if (jrNoPartidaInvDisp.isSelected()==true)
        {
            descripcion=false;
        }
        if (jrNoPartidaInvDisp.isSelected()==false)
        {
            descripcion=true;
        }
        
        actualizarTablaProductosAInvDisp(descripcion);
        
        dlgBuscarInvDisp.setSize(720,340);
        dlgBuscarInvDisp.setPreferredSize(dlgBuscarInvDisp.getSize());
        dlgBuscarInvDisp.setLocationRelativeTo(null);
        dlgBuscarInvDisp.setAlwaysOnTop(true);
        dlgBuscarInvDisp.setVisible(true);
    }
    
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldBuscar
    public void SeleccionarEntrada() throws Exception
     {
        int renglonSeleccionado = tblBuscarPartidaCalibrar.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            String[] datos=null;
            ct=new CueroTrabajar();
            
            ct.setIdCueroTrabajar(Integer.parseInt(tblBuscarPartidaCalibrar.getValueAt(renglonSeleccionado, 0).toString()));
            ct.setNoPartida(Integer.parseInt(tblBuscarPartidaCalibrar.getValueAt(renglonSeleccionado, 1).toString()));
            ct.setTipoProducto(tblBuscarPartidaCalibrar.getValueAt(renglonSeleccionado, 2).toString());
            ct.setNoPiezasActuales(Integer.parseInt(tblBuscarPartidaCalibrar.getValueAt(renglonSeleccionado, 4).toString()));
            ct.setDescripcion(tblBuscarPartidaCalibrar.getValueAt(renglonSeleccionado, 5).toString());
            
            dlgBuscar.setVisible(false);
            
            abrirDialogoAgregar();
            
            if (!ct.getDescripcion().equals(""))
            {
                jrDescripcion.setSelected(true);
                jrNoPartida.setSelected(false);
                txtDescripcion.setEnabled(true);
                txtNoPartidaAgregar.setEnabled(false);
            }
            
            txtNoPartidaAgregar.setText(String.valueOf(ct.getNoPartida()));
            txtTipoProducto.setText(ct.getTipoProducto());
            lblIdRegistroCalibrar.setText(String.valueOf(ct.getIdCueroTrabajar()));
            lblErrorAgregar.setText("");
            txtNoPiezasCueroAgregar.setText(String.valueOf(ct.getNoPiezasActuales()));
            txtDescripcion.setText(ct.getDescripcion());
        }
        else 
        {
            dlgBuscar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgBuscar.setVisible(true);
        }
     }
    
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldBuscarSeleccionar
    public void SeleccionarEntradaSeleccionar() throws Exception
    {
        int renglonSeleccionado = tblBuscarPartidaSeleccionar.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            String[] datos=null;
            calibrar=new Calibrar();
            
            calibrar.setIdCalibrar(Integer.parseInt(tblBuscarPartidaSeleccionar.getValueAt(renglonSeleccionado, 0).toString()));
            calibrar.setNoPartida(Integer.parseInt(tblBuscarPartidaSeleccionar.getValueAt(renglonSeleccionado, 1).toString()));
            calibrar.setTipoProducto(tblBuscarPartidaSeleccionar.getValueAt(renglonSeleccionado, 2).toString());
            calibrar.setCalibre(tblBuscarPartidaSeleccionar.getValueAt(renglonSeleccionado, 3).toString());
            calibrar.setNoPiezasActuales(Integer.parseInt(tblBuscarPartidaSeleccionar.getValueAt(renglonSeleccionado, 5).toString()));
            calibrar.setDescripcion(tblBuscarPartidaSeleccionar.getValueAt(renglonSeleccionado, 6).toString());
            dlgBuscarSeleccionar.setVisible(false);
            
            abrirDialogoAgregarSeleccionar();
            
            if (!calibrar.getDescripcion().equals(""))
            {
                jrDescripcionSeleccion.setSelected(true);
            }
            
            txtNoPartidaAgregarSeleccionar.setText(String.valueOf(calibrar.getNoPartida()));
            txtTipoProductoSeleccionar.setText(calibrar.getTipoProducto());
            txtCalibreSeleccionar.setText(calibrar.getCalibre());
            lblidRegistroSeleccionar.setText(String.valueOf(calibrar.getIdCalibrar()));
            lblErrorAgregar.setText("");
            txtNoPiezasCueroSeleccionar.setText(String.valueOf(calibrar.getNoPiezasActuales()));
            txtDescripcionSeleccionar.setText(calibrar.getDescripcion());
        }
        else 
        {
            dlgBuscarSeleccionar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgBuscarSeleccionar.setVisible(true);
        }
    }
    
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldBuscarPesar
    public void SeleccionarEntradaPesar() throws Exception
    {
        int renglonSeleccionado = tblBuscarPartidaPesar.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            String[] datos=null;
            cs=new CueroSeleccionado();
            
            cs.setIdCueroSeleccionado(Integer.parseInt(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 0).toString()));
            cs.setNoPartida(Integer.parseInt(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 1).toString()));
            cs.setTipoProducto(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 2).toString());
            cs.setCalibre(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 3).toString());
            cs.setSeleccion(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 4).toString());
            cs.setNoPiezasActuales(Integer.parseInt(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 6).toString()));
            cs.setDescripcion(tblBuscarPartidaPesar.getValueAt(renglonSeleccionado, 7).toString());
            dlgBuscarPesar.setVisible(false);
            
            abrirDialogoAgregarPesar();
            
            if (!cs.getDescripcion().equals(""))
            {
                jrDescripcionPesar.setSelected(true);
            }
            
            txtNoPartidaAgregarPesar.setText(String.valueOf(cs.getNoPartida()));
            txtTipoProductoPesar.setText(cs.getTipoProducto());
            txtCalibrePesar.setText(cs.getCalibre());
            txtSeleccionPesar.setText(cs.getSeleccion());
            lblIdRegistroPesar.setText(String.valueOf(cs.getIdCueroSeleccionado()));
            lblErrorAgregarPesar.setText("");
            txtNoPiezasCueroPesar.setText(String.valueOf(cs.getNoPiezasActuales()));
            txtDescripcionPesar.setText(cs.getDescripcion());
            txtKgPesar.setText("");
        }
        else 
        {
            dlgBuscarPesar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgBuscarPesar.setVisible(true);
        }
    }
    
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldBuscarInvDisp
    public void SeleccionarEntradaInvDisp() throws Exception
    {
        int renglonSeleccionado = tblBuscarPartidaProdPesado.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            String[] datos=null;
            cp=new CueroPesar();
            
            cp.setIdCueroSeleccionado(Integer.parseInt(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 0).toString()));
            cp.setNoPartida(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 1).toString());
            cp.setTipoProducto(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 2).toString());
            cp.setNoPiezasActuales(Integer.parseInt(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 4).toString()));
            cp.setPeso(Double.parseDouble(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 5).toString()));
            cp.setSeleccion(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 7).toString());
            cp.setCalibre(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 8).toString());
            cp.setDescripcion(tblBuscarPartidaProdPesado.getValueAt(renglonSeleccionado, 9).toString());
            dlgBuscarInvDisp.setVisible(false);
            
            abrirDialogoAgregarInvDisp();
            txtNoPartidaAgregarInvDisp.setText(String.valueOf(cp.getNoPartida()));
            txtTipoProductoInvDisp.setText(cp.getTipoProducto());
            txtCalibreInvDisp.setText(cp.getCalibre());
            txtSeleccionInvDisp.setText(cp.getSeleccion());
            lblIdRegistroInvDisp.setText(String.valueOf(cp.getIdCueroSeleccionado()));
            lblErrorAgregarInvDisp.setText("");
            txtNoPiezasCueroInvDisp.setText(String.valueOf(cp.getNoPiezasActuales()));
            txtKgInvDisp.setText(String.valueOf(cp.getPeso()));
            txtDescripcionInvDisp.setText(cp.getDescripcion());
        }
        else 
        {
            dlgBuscarInvDisp.setVisible(false);
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgBuscarInvDisp.setVisible(true);
        }
    }
    
    
    public void realizarEntradaCalibrar()
    {
        if (!txtNoPartidaAgregar.getText().equals("") || !txtTipoProducto.getText().equals("") || !txtNoPiezasCueroAgregar.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasCueroAgregar.getText())>=1)
            {
                if (Integer.parseInt(txtNoPiezasCueroAgregar.getText())<=ct.getNoPiezasActuales())
                {
                    try 
                    {
                        calibrar.setNoPartida(Integer.parseInt(txtNoPartidaAgregar.getText()));
                        calibrar.setTipoProducto(txtTipoProducto.getText());
                        calibrar.setCalibre(cmbCalibreAgregar.getSelectedItem().toString());
                        calibrar.setNoPiezas(Integer.parseInt(txtNoPiezasCueroAgregar.getText()));
                        calibrar.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroAgregar.getText()));
                        calibrar.setDescripcion(txtDescripcion.getText());
                        calibrar.setFecha(FrmPrincipal.lblFecha.getText());
                        
                        cc.agregarProductoCalibrar(calibrar);
                        ctc.actualizarProductoTrabajar(Integer.parseInt(txtNoPiezasCueroAgregar.getText()), Integer.parseInt(lblIdRegistroCalibrar.getText()));
                        dlgAgregar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                        actualizarTablaCalibrar();
                    } 
                    catch (Exception e) 
                    {
                        System.out.println(e);
                        dlgAgregar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Error de conexión en la base de datos", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                        dlgAgregar.setVisible(true);
                    }
                }
                else
                {
                    dlgAgregar.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad en inventario insuficiente", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                    dlgAgregar.setVisible(true);
                }
            }   
            else
            {
                dlgAgregar.setVisible(false);
                JOptionPane.showMessageDialog(null, "El número de piezas debe de ser mayor a 0", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                dlgAgregar.setVisible(true);
            }
        }
        else
        {
            dlgAgregar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgAgregar.setVisible(true);
        }
    }
    
    
    public void realizarEntradaSeleccionar()
    {
        if (!txtNoPartidaAgregarSeleccionar.getText().equals("") || !txtTipoProductoSeleccionar.getText().equals("") || !txtCalibreSeleccionar.getText().equals("") || !txtNoPiezasCueroSeleccionar.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasCueroSeleccionar.getText())>=1)
            {
                if (Integer.parseInt(txtNoPiezasCueroSeleccionar.getText())<=calibrar.getNoPiezasActuales())
                {
                    try 
                    {
                        cs.setNoPartida(Integer.parseInt(txtNoPartidaAgregarSeleccionar.getText()));
                        cs.setTipoProducto(txtTipoProductoSeleccionar.getText());
                        cs.setCalibre(txtCalibreSeleccionar.getText());
                        cs.setSeleccion(cmbSeleccionAgregar.getSelectedItem().toString());
                        cs.setNoPiezas(Integer.parseInt(txtNoPiezasCueroSeleccionar.getText()));
                        cs.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroSeleccionar.getText()));
                        cs.setFecha(FrmPrincipal.lblFecha.getText());
                        cs.setDescripcion(txtDescripcionSeleccionar.getText());
                        
                        sc.agregarProductoSeleccionar(cs);
                        cc.actualizarProductoCalibrado(Integer.parseInt(txtNoPiezasCueroSeleccionar.getText()), Integer.parseInt(lblidRegistroSeleccionar.getText()));
                        dlgAgregarSeleccionar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                        actualizarTablaCalibrar();
                        actualizarTablaSeleccionar();
                    } 
                    catch (Exception e) 
                    {
                        System.out.println(e);
                        dlgAgregarSeleccionar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Error de conexión en la base de datos", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                        dlgAgregarSeleccionar.setVisible(true);
                    }
                }
                else
                {
                    dlgAgregarSeleccionar.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad en inventario insuficiente", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                    dlgAgregarSeleccionar.setVisible(true);
                }
            }   
            else
            {
                dlgAgregarSeleccionar.setVisible(false);
                JOptionPane.showMessageDialog(null, "El número de piezas debe de ser mayor a 0", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                dlgAgregarSeleccionar.setVisible(true);
            }
        }
        else
        {
            dlgAgregarSeleccionar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgAgregarSeleccionar.setVisible(true);
        }
    }
    
    
    public void realizarEntradaPesar()
    {
        if (!txtNoPartidaAgregarPesar.getText().equals("") || !txtTipoProductoPesar.getText().equals("") || !txtCalibrePesar.getText().equals("") || !txtNoPiezasCueroPesar.getText().equals("") || !txtKgPesar.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasCueroPesar.getText())>=1 && Double.parseDouble(txtKgPesar.getText())>=1.0)
            {
                if (Integer.parseInt(txtNoPiezasCueroPesar.getText())<=cs.getNoPiezasActuales())
                {
                    try 
                    {
                        cp.setNoPartida(txtNoPartidaAgregarPesar.getText());
                        cp.setTipoProducto(txtTipoProductoPesar.getText());
                        cp.setCalibre(txtCalibrePesar.getText());
                        cp.setSeleccion(txtSeleccionPesar.getText());
                        cp.setNoPiezas(Integer.parseInt(txtNoPiezasCueroPesar.getText()));
                        cp.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroPesar.getText()));
                        cp.setPeso(Double.parseDouble(txtKgPesar.getText()));
                        cp.setFecha(FrmPrincipal.lblFecha.getText());
                        cp.setDescripcion(txtDescripcionPesar.getText());
                        
                        cpc.agregarProductoPesar(cp);
                        int idInventario = cpc.obtenerUltimoIdCueroPesado();
                        cpc.insertarProductoPesar(idInventario, cp);
                        sc.actualizarProductoSeleccionado(Integer.parseInt(txtNoPiezasCueroPesar.getText()), Integer.parseInt(lblIdRegistroPesar.getText()));
                        dlgAgregarPesar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                        actualizarTablaSeleccionar();
                        actualizarTablaPesar();
                    } 
                    catch (Exception e) 
                    {
                        System.err.println(e);
                        dlgAgregarPesar.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Error de conexión en la base de datos", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                        dlgAgregarPesar.setVisible(true);
                    }
                }
                else
                {
                    dlgAgregarPesar.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad en inventario insuficiente", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                    dlgAgregarPesar.setVisible(true);
                }
            }   
            else
            {
                dlgAgregarPesar.setVisible(false);
                JOptionPane.showMessageDialog(null, "El número de piezas y Kg debe de ser mayor a 0", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                dlgAgregarPesar.setVisible(true);
            }
        }
        else
        {
            dlgAgregarPesar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgAgregarPesar.setVisible(true);
        }
    }
    
    
    public void realizarEntradaInvDisp()
    {
        if (!txtNoPartidaAgregarInvDisp.getText().equals("") || !txtTipoProductoInvDisp.getText().equals("") || !txtCalibreInvDisp.getText().equals("") || !txtNoPiezasCueroInvDisp.getText().equals("") || !txtKgInvDisp.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasCueroInvDisp.getText())>=1 && Double.parseDouble(txtKgInvDisp.getText())>=1.0)
            {
                if (Integer.parseInt(txtNoPiezasCueroInvDisp.getText())<=cp.getNoPiezasActuales())
                {
                    try 
                    {
                        idt.setNoPartida(Integer.parseInt(txtNoPartidaAgregarInvDisp.getText()));
                        idt.setTipoProducto(txtTipoProductoInvDisp.getText());
                        idt.setCalibre(txtCalibreInvDisp.getText());
                        idt.setSeleccion(txtSeleccionInvDisp.getText());
                        idt.setNoPiezas(Integer.parseInt(txtNoPiezasCueroInvDisp.getText()));
                        idt.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroInvDisp.getText()));
                        idt.setPeso(Double.parseDouble(txtKgInvDisp.getText()));
                        idt.setFecha(FrmPrincipal.lblFecha.getText());
                        idt.setDescripcion(txtDescripcionInvDisp.getText());
                        
                        idtc.agregarIdinvDispTrabajar(idt);
                        cpc.modificarProductoPesar(Integer.parseInt(txtNoPiezasCueroInvDisp.getText()), Integer.parseInt(lblIdRegistroInvDisp.getText()), Double.parseDouble(txtKgInvDisp.getText()));
                        dlgAgregarInvDisponible.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                        actualizarTablaPesar();
                    } 
                    catch (Exception e) 
                    {
                        System.err.println(e);
                        dlgAgregarInvDisponible.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Error de conexión en la base de datos", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                        dlgAgregarInvDisponible.setVisible(true);
                    }
                }
                else
                {
                    dlgAgregarInvDisponible.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad en inventario insuficiente", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                    dlgAgregarInvDisponible.setVisible(true);
                }
            }   
            else
            {
                dlgAgregarInvDisponible.setVisible(false);
                JOptionPane.showMessageDialog(null, "El número de piezas y Kg debe de ser mayor a 0", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
                dlgAgregarInvDisponible.setVisible(true);
            }
        }
        else
        {
            dlgAgregarInvDisponible.setVisible(false);
            JOptionPane.showMessageDialog(null, "Llene todos los campos", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
            dlgAgregarInvDisponible.setVisible(true);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgAgregar = new javax.swing.JDialog();
        jLabel13 = new javax.swing.JLabel();
        btnRealizarEntrada = new javax.swing.JButton();
        btnCancelarAgregar = new javax.swing.JButton();
        lblErrorAgregar = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtNoPartidaAgregar = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        txtTipoProducto = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        txtNoPiezasCueroAgregar = new javax.swing.JTextField();
        cmbCalibreAgregar = new javax.swing.JComboBox();
        lblIdRegistroCalibrar = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jrNoPartida = new javax.swing.JRadioButton();
        jrDescripcion = new javax.swing.JRadioButton();
        dlgBuscar = new javax.swing.JDialog();
        jLabel14 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBuscarPartidaCalibrar = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        dlgAgregarSeleccionar = new javax.swing.JDialog();
        jLabel18 = new javax.swing.JLabel();
        btnRealizarEntrada1 = new javax.swing.JButton();
        btnCancelarAgregar1 = new javax.swing.JButton();
        lblErrorAgregarSeleccionar = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        txtNoPartidaAgregarSeleccionar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        txtCalibreSeleccionar = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        txtNoPiezasCueroSeleccionar = new javax.swing.JTextField();
        txtTipoProductoSeleccionar = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        cmbSeleccionAgregar = new javax.swing.JComboBox();
        lblidRegistroSeleccionar = new javax.swing.JLabel();
        jrNoPartidaSeleccion = new javax.swing.JRadioButton();
        jrDescripcionSeleccion = new javax.swing.JRadioButton();
        txtDescripcionSeleccionar = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        dlgBuscarSeleccionar = new javax.swing.JDialog();
        jLabel19 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBuscarPartidaSeleccionar = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        dlgAgregarPesar = new javax.swing.JDialog();
        jLabel22 = new javax.swing.JLabel();
        btnRealizarEntrada2 = new javax.swing.JButton();
        btnCancelarAgregar2 = new javax.swing.JButton();
        lblErrorAgregarPesar = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel78 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        txtNoPartidaAgregarPesar = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        txtCalibrePesar = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        txtNoPiezasCueroPesar = new javax.swing.JTextField();
        txtTipoProductoPesar = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        lblIdRegistroPesar = new javax.swing.JLabel();
        txtSeleccionPesar = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        txtKgPesar = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        txtDescripcionPesar = new javax.swing.JTextField();
        jrNoPartidaPesar = new javax.swing.JRadioButton();
        jrDescripcionPesar = new javax.swing.JRadioButton();
        dlgBuscarPesar = new javax.swing.JDialog();
        jLabel23 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblBuscarPartidaPesar = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        dlgAgregarInvDisponible = new javax.swing.JDialog();
        jLabel84 = new javax.swing.JLabel();
        btnRealizarEntradaInvDisp = new javax.swing.JButton();
        btnCancelarAgregarInvDisp = new javax.swing.JButton();
        lblErrorAgregarInvDisp = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtNoPartidaAgregarInvDisp = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        txtCalibreInvDisp = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        txtNoPiezasCueroInvDisp = new javax.swing.JTextField();
        txtTipoProductoInvDisp = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        lblIdRegistroInvDisp = new javax.swing.JLabel();
        txtSeleccionInvDisp = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        txtKgInvDisp = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        txtDescripcionInvDisp = new javax.swing.JTextField();
        jrNoPartidaInvDisp = new javax.swing.JRadioButton();
        jrDescripcionInvDisp = new javax.swing.JRadioButton();
        dlgBuscarInvDisp = new javax.swing.JDialog();
        jLabel91 = new javax.swing.JLabel();
        jSeparator21 = new javax.swing.JSeparator();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblBuscarPartidaProdPesado = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        bgGrupo1 = new javax.swing.ButtonGroup();
        bgGrupo2 = new javax.swing.ButtonGroup();
        bgGrupo3 = new javax.swing.ButtonGroup();
        bgGrupo4 = new javax.swing.ButtonGroup();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        Calibrar = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnAgregarEntrada1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        cmbTipoEntradaProducto = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        cmbCalibre = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jrFiltroFechas = new javax.swing.JRadioButton();
        jLabel33 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jLabel53 = new javax.swing.JLabel();
        dcFecha1 = new datechooser.beans.DateChooserCombo();
        lbl1 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        dcFecha2 = new datechooser.beans.DateChooserCombo();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        btnBuscarEntrada1 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        btnReporteEntrada1 = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCalibrar = new javax.swing.JTable();
        Seleccionar = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        btnAgregarSeleccionar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        cmbTipoEntradaProductoSeleccionar = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        cmbCalibreSeleccionar = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        cmbSeleccion = new javax.swing.JComboBox();
        jLabel62 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblSeleccionar = new javax.swing.JTable();
        jToolBar5 = new javax.swing.JToolBar();
        jLabel17 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jrFiltroFechasSeleccionar = new javax.swing.JRadioButton();
        jLabel47 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jLabel65 = new javax.swing.JLabel();
        dcFecha1Seleccionar = new datechooser.beans.DateChooserCombo();
        lbl3 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        dcFecha2Seleccionar = new datechooser.beans.DateChooserCombo();
        lbl4 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JToolBar.Separator();
        btnBuscarSeleccionar = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JToolBar.Separator();
        btnReporteSeleccionar = new javax.swing.JButton();
        jLabel67 = new javax.swing.JLabel();
        Pesar = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        btnAgregarPesar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnBaja = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        cmbTipoEntradaProductoPesar = new javax.swing.JComboBox();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        cmbCalibrePesar = new javax.swing.JComboBox();
        jLabel43 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        cmbSeleccionPesar = new javax.swing.JComboBox();
        jLabel44 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        txtNoPartidaPesado = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblPesar = new javax.swing.JTable();
        jToolBar7 = new javax.swing.JToolBar();
        jLabel21 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jrFiltroFechasSeleccionar1 = new javax.swing.JRadioButton();
        jLabel74 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JToolBar.Separator();
        jLabel75 = new javax.swing.JLabel();
        dcFecha1Pesar = new datechooser.beans.DateChooserCombo();
        lbl5 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        dcFecha2Pesar = new datechooser.beans.DateChooserCombo();
        lbl6 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JToolBar.Separator();
        btnBuscarPesar = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JToolBar.Separator();
        btnReporteSeleccionar1 = new javax.swing.JButton();
        jLabel96 = new javax.swing.JLabel();
        btnReporteEntradasSeleccionar = new javax.swing.JButton();
        jLabel98 = new javax.swing.JLabel();
        btnAgregarPesar1 = new javax.swing.JButton();
        jLabel77 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        jLabel13.setText("Agregar entrada");
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnRealizarEntrada.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        btnRealizarEntrada.setText("Aceptar");
        btnRealizarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaActionPerformed(evt);
            }
        });

        btnCancelarAgregar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        btnCancelarAgregar.setText("Cancelar");
        btnCancelarAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarActionPerformed(evt);
            }
        });

        lblErrorAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorAgregar.setForeground(new java.awt.Color(204, 0, 0));
        lblErrorAgregar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorAgregar.setText("error");

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel31.setText("No. Partida:");

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel50.setText("Tipo de producto:");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setText("Calibre:");

        txtNoPartidaAgregar.setEditable(false);
        txtNoPartidaAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaAgregarKeyTyped(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtTipoProducto.setEditable(false);
        txtTipoProducto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoProductoKeyTyped(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel56.setText("No. Piezas:");

        txtNoPiezasCueroAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroAgregarKeyTyped(evt);
            }
        });

        cmbCalibreAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblIdRegistroCalibrar.setText("id Registro");

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel93.setText("Descripción:");

        txtDescripcion.setEditable(false);
        txtDescripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });
        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyTyped(evt);
            }
        });

        jrNoPartida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrNoPartida.setText("No. Partida");
        jrNoPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrNoPartidaActionPerformed(evt);
            }
        });

        jrDescripcion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrDescripcion.setText("Descripción");
        jrDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrDescripcionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgAgregarLayout = new javax.swing.GroupLayout(dlgAgregar.getContentPane());
        dlgAgregar.getContentPane().setLayout(dlgAgregarLayout);
        dlgAgregarLayout.setHorizontalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator5)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIdRegistroCalibrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 159, Short.MAX_VALUE)
                .addComponent(btnRealizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregar)
                .addContainerGap())
            .addComponent(lblErrorAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel56)
                            .addComponent(jLabel50)
                            .addComponent(jLabel31)
                            .addComponent(jLabel51)
                            .addComponent(jLabel93))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDescripcion)
                            .addComponent(txtNoPiezasCueroAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoPartidaAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTipoProducto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.Alignment.LEADING, 0, 122, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jrNoPartida)
                        .addGap(18, 18, 18)
                        .addComponent(jrDescripcion)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dlgAgregarLayout.setVerticalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jrNoPartida)
                            .addComponent(jrDescripcion))
                        .addGap(16, 16, 16)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(txtNoPartidaAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel93)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel50)
                    .addComponent(txtTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(txtNoPiezasCueroAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarLayout.createSequentialGroup()
                        .addComponent(lblErrorAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRealizarEntrada)
                            .addComponent(btnCancelarAgregar)))
                    .addComponent(lblIdRegistroCalibrar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jLabel14.setText("Buscar partida");
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        tblBuscarPartidaCalibrar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBuscarPartidaCalibrar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblBuscarPartidaCalibrar);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        jButton2.setText("Seleccionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        jButton3.setText("Cancelar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgBuscarLayout = new javax.swing.GroupLayout(dlgBuscar.getContentPane());
        dlgBuscar.getContentPane().setLayout(dlgBuscarLayout);
        dlgBuscarLayout.setHorizontalGroup(
            dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(dlgBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgBuscarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );
        dlgBuscarLayout.setVerticalGroup(
            dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(dlgBuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        jLabel18.setText("Agregar entrada seleccionado");
        jLabel18.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnRealizarEntrada1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        btnRealizarEntrada1.setText("Aceptar");
        btnRealizarEntrada1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntrada1ActionPerformed(evt);
            }
        });

        btnCancelarAgregar1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        btnCancelarAgregar1.setText("Cancelar");
        btnCancelarAgregar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregar1ActionPerformed(evt);
            }
        });

        lblErrorAgregarSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorAgregarSeleccionar.setForeground(new java.awt.Color(204, 0, 0));
        lblErrorAgregarSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorAgregarSeleccionar.setText("error");

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel38.setText("No. Partida:");

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setText("Tipo de producto:");

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel61.setText("Calibre:");

        txtNoPartidaAgregarSeleccionar.setEditable(false);
        txtNoPartidaAgregarSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaAgregarSeleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaAgregarSeleccionarKeyTyped(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        txtCalibreSeleccionar.setEditable(false);
        txtCalibreSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCalibreSeleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCalibreSeleccionarKeyTyped(evt);
            }
        });

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel63.setText("No. Piezas:");

        txtNoPiezasCueroSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoPiezasCueroSeleccionarActionPerformed(evt);
            }
        });
        txtNoPiezasCueroSeleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroSeleccionarKeyTyped(evt);
            }
        });

        txtTipoProductoSeleccionar.setEditable(false);
        txtTipoProductoSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoProductoSeleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoProductoSeleccionarKeyTyped(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel64.setText("Selección:");

        cmbSeleccionAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        lblidRegistroSeleccionar.setText("idRegistro");

        jrNoPartidaSeleccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrNoPartidaSeleccion.setText("No. Partida");
        jrNoPartidaSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrNoPartidaSeleccionActionPerformed(evt);
            }
        });

        jrDescripcionSeleccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrDescripcionSeleccion.setText("Descripción");
        jrDescripcionSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrDescripcionSeleccionActionPerformed(evt);
            }
        });

        txtDescripcionSeleccionar.setEditable(false);
        txtDescripcionSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionSeleccionar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionSeleccionarKeyTyped(evt);
            }
        });

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel94.setText("Descripción:");

        javax.swing.GroupLayout dlgAgregarSeleccionarLayout = new javax.swing.GroupLayout(dlgAgregarSeleccionar.getContentPane());
        dlgAgregarSeleccionar.getContentPane().setLayout(dlgAgregarSeleccionarLayout);
        dlgAgregarSeleccionarLayout.setHorizontalGroup(
            dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator10)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarSeleccionarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblidRegistroSeleccionar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                .addComponent(btnRealizarEntrada1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregar1)
                .addContainerGap())
            .addComponent(lblErrorAgregarSeleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                        .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel63)
                            .addComponent(jLabel60)
                            .addComponent(jLabel38)
                            .addComponent(jLabel61)
                            .addComponent(jLabel64))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNoPiezasCueroSeleccionar)
                            .addComponent(txtNoPartidaAgregarSeleccionar)
                            .addComponent(txtCalibreSeleccionar)
                            .addComponent(txtTipoProductoSeleccionar)
                            .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                        .addComponent(jLabel94)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDescripcionSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jrNoPartidaSeleccion)
                .addGap(18, 18, 18)
                .addComponent(jrDescripcionSeleccion)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        dlgAgregarSeleccionarLayout.setVerticalGroup(
            dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrNoPartidaSeleccion)
                    .addComponent(jrDescripcionSeleccion))
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(txtNoPartidaAgregarSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDescripcionSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel94))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(dlgAgregarSeleccionarLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60)
                    .addComponent(txtTipoProductoSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel61)
                    .addComponent(txtCalibreSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel63)
                    .addComponent(txtNoPiezasCueroSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarSeleccionarLayout.createSequentialGroup()
                        .addComponent(lblErrorAgregarSeleccionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRealizarEntrada1)
                            .addComponent(btnCancelarAgregar1)))
                    .addComponent(lblidRegistroSeleccionar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jLabel19.setText("Buscar partida");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        tblBuscarPartidaSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBuscarPartidaSeleccionar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblBuscarPartidaSeleccionar);

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        jButton5.setText("Seleccionar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        jButton6.setText("Cancelar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgBuscarSeleccionarLayout = new javax.swing.GroupLayout(dlgBuscarSeleccionar.getContentPane());
        dlgBuscarSeleccionar.getContentPane().setLayout(dlgBuscarSeleccionarLayout);
        dlgBuscarSeleccionarLayout.setHorizontalGroup(
            dlgBuscarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarSeleccionarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addComponent(jSeparator11, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(dlgBuscarSeleccionarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgBuscarSeleccionarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addContainerGap())
        );
        dlgBuscarSeleccionarLayout.setVerticalGroup(
            dlgBuscarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarSeleccionarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(dlgBuscarSeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        jLabel22.setText("Agregar entrada pesado");
        jLabel22.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnRealizarEntrada2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntrada2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        btnRealizarEntrada2.setText("Aceptar");
        btnRealizarEntrada2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntrada2ActionPerformed(evt);
            }
        });

        btnCancelarAgregar2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        btnCancelarAgregar2.setText("Cancelar");
        btnCancelarAgregar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregar2ActionPerformed(evt);
            }
        });

        lblErrorAgregarPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorAgregarPesar.setForeground(new java.awt.Color(204, 0, 0));
        lblErrorAgregarPesar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorAgregarPesar.setText("error");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel45.setText("Descripción:");

        jLabel78.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel78.setText("Tipo de producto:");

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel80.setText("Calibre:");

        txtNoPartidaAgregarPesar.setEditable(false);
        txtNoPartidaAgregarPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaAgregarPesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoPartidaAgregarPesarActionPerformed(evt);
            }
        });
        txtNoPartidaAgregarPesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaAgregarPesarKeyTyped(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jButton7.setText("Buscar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        txtCalibrePesar.setEditable(false);
        txtCalibrePesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCalibrePesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCalibrePesarKeyTyped(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel81.setText("No. Piezas:");

        txtNoPiezasCueroPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroPesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroPesarKeyTyped(evt);
            }
        });

        txtTipoProductoPesar.setEditable(false);
        txtTipoProductoPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoProductoPesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoProductoPesarKeyTyped(evt);
            }
        });

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel82.setText("Selección:");

        lblIdRegistroPesar.setText("idRegistro");

        txtSeleccionPesar.setEditable(false);
        txtSeleccionPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSeleccionPesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSeleccionPesarKeyTyped(evt);
            }
        });

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel83.setText("Kg:");

        txtKgPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgPesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgPesarKeyTyped(evt);
            }
        });

        jLabel92.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel92.setText("No. Partida:");

        txtDescripcionPesar.setEditable(false);
        txtDescripcionPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionPesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionPesarKeyTyped(evt);
            }
        });

        jrNoPartidaPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrNoPartidaPesar.setText("No. Partida");
        jrNoPartidaPesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrNoPartidaPesarActionPerformed(evt);
            }
        });

        jrDescripcionPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrDescripcionPesar.setText("Descripción");
        jrDescripcionPesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrDescripcionPesarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgAgregarPesarLayout = new javax.swing.GroupLayout(dlgAgregarPesar.getContentPane());
        dlgAgregarPesar.getContentPane().setLayout(dlgAgregarPesarLayout);
        dlgAgregarPesarLayout.setHorizontalGroup(
            dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator12)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarPesarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIdRegistroPesar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRealizarEntrada2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregar2)
                .addContainerGap())
            .addComponent(lblErrorAgregarPesar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel81)
                            .addComponent(jLabel78)
                            .addComponent(jLabel45)
                            .addComponent(jLabel80)
                            .addComponent(jLabel82)
                            .addComponent(jLabel83)
                            .addComponent(jLabel92))
                        .addGap(18, 18, 18)
                        .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtKgPesar)
                                .addComponent(txtNoPiezasCueroPesar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCalibrePesar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTipoProductoPesar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSeleccionPesar, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtDescripcionPesar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNoPartidaAgregarPesar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7))
                    .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jrNoPartidaPesar)
                        .addGap(18, 18, 18)
                        .addComponent(jrDescripcionPesar)))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        dlgAgregarPesarLayout.setVerticalGroup(
            dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jrNoPartidaPesar)
                    .addComponent(jrDescripcionPesar))
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel92, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNoPartidaAgregarPesar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(txtDescripcionPesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dlgAgregarPesarLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(txtTipoProductoPesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(txtCalibrePesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(txtSeleccionPesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(txtKgPesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(txtNoPiezasCueroPesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblErrorAgregarPesar)
                .addGap(11, 11, 11)
                .addGroup(dlgAgregarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntrada2)
                    .addComponent(btnCancelarAgregar2)
                    .addComponent(lblIdRegistroPesar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jLabel23.setText("Buscar partida");
        jLabel23.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        tblBuscarPartidaPesar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBuscarPartidaPesar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tblBuscarPartidaPesar);

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        jButton8.setText("Seleccionar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        jButton9.setText("Cancelar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgBuscarPesarLayout = new javax.swing.GroupLayout(dlgBuscarPesar.getContentPane());
        dlgBuscarPesar.getContentPane().setLayout(dlgBuscarPesarLayout);
        dlgBuscarPesarLayout.setHorizontalGroup(
            dlgBuscarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarPesarLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addComponent(jSeparator19, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(dlgBuscarPesarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgBuscarPesarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap())
        );
        dlgBuscarPesarLayout.setVerticalGroup(
            dlgBuscarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarPesarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(dlgBuscarPesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        jLabel84.setText("Agregar entrada inv. Disponible");
        jLabel84.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnRealizarEntradaInvDisp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarEntradaInvDisp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        btnRealizarEntradaInvDisp.setText("Aceptar");
        btnRealizarEntradaInvDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarEntradaInvDispActionPerformed(evt);
            }
        });

        btnCancelarAgregarInvDisp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarAgregarInvDisp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        btnCancelarAgregarInvDisp.setText("Cancelar");
        btnCancelarAgregarInvDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarAgregarInvDispActionPerformed(evt);
            }
        });

        lblErrorAgregarInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorAgregarInvDisp.setForeground(new java.awt.Color(204, 0, 0));
        lblErrorAgregarInvDisp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorAgregarInvDisp.setText("error");

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel85.setText("Descripción:");

        jLabel86.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel86.setText("Tipo de producto:");

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel87.setText("Calibre:");

        txtNoPartidaAgregarInvDisp.setEditable(false);
        txtNoPartidaAgregarInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPartidaAgregarInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPartidaAgregarInvDispKeyTyped(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jButton10.setText("Buscar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        txtCalibreInvDisp.setEditable(false);
        txtCalibreInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCalibreInvDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalibreInvDispActionPerformed(evt);
            }
        });
        txtCalibreInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCalibreInvDispKeyTyped(evt);
            }
        });

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel88.setText("No. Piezas:");

        txtNoPiezasCueroInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroInvDispKeyTyped(evt);
            }
        });

        txtTipoProductoInvDisp.setEditable(false);
        txtTipoProductoInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoProductoInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoProductoInvDispKeyTyped(evt);
            }
        });

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel89.setText("Selección:");

        lblIdRegistroInvDisp.setText("idRegistro");

        txtSeleccionInvDisp.setEditable(false);
        txtSeleccionInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSeleccionInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSeleccionInvDispKeyTyped(evt);
            }
        });

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel90.setText("Kg:");

        txtKgInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgInvDispKeyTyped(evt);
            }
        });

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel95.setText("No. Partida:");

        txtDescripcionInvDisp.setEditable(false);
        txtDescripcionInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionInvDisp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionInvDispKeyTyped(evt);
            }
        });

        jrNoPartidaInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrNoPartidaInvDisp.setText("No. Partida");
        jrNoPartidaInvDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrNoPartidaInvDispActionPerformed(evt);
            }
        });

        jrDescripcionInvDisp.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrDescripcionInvDisp.setText("Descripción");
        jrDescripcionInvDisp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrDescripcionInvDispActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgAgregarInvDisponibleLayout = new javax.swing.GroupLayout(dlgAgregarInvDisponible.getContentPane());
        dlgAgregarInvDisponible.getContentPane().setLayout(dlgAgregarInvDisponibleLayout);
        dlgAgregarInvDisponibleLayout.setHorizontalGroup(
            dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator20)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarInvDisponibleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIdRegistroInvDisp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                .addComponent(btnRealizarEntradaInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregarInvDisp)
                .addContainerGap())
            .addComponent(lblErrorAgregarInvDisp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel88)
                            .addComponent(jLabel86)
                            .addComponent(jLabel85)
                            .addComponent(jLabel87)
                            .addComponent(jLabel89)
                            .addComponent(jLabel90)
                            .addComponent(jLabel95))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtKgInvDisp)
                                .addComponent(txtNoPiezasCueroInvDisp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addComponent(txtNoPartidaAgregarInvDisp, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCalibreInvDisp, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTipoProductoInvDisp, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSeleccionInvDisp, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(txtDescripcionInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10))
                    .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jrNoPartidaInvDisp)
                        .addGap(18, 18, 18)
                        .addComponent(jrDescripcionInvDisp)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dlgAgregarInvDisponibleLayout.setVerticalGroup(
            dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jrNoPartidaInvDisp)
                            .addComponent(jrDescripcionInvDisp))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel95)
                            .addComponent(txtNoPartidaAgregarInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel85)
                            .addComponent(txtDescripcionInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(dlgAgregarInvDisponibleLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(txtTipoProductoInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(txtCalibreInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(txtSeleccionInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(txtKgInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(txtNoPiezasCueroInvDisp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarInvDisponibleLayout.createSequentialGroup()
                        .addComponent(lblErrorAgregarInvDisp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgAgregarInvDisponibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRealizarEntradaInvDisp)
                            .addComponent(btnCancelarAgregarInvDisp)))
                    .addComponent(lblIdRegistroInvDisp, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jLabel91.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
        jLabel91.setText("Buscar partida");
        jLabel91.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        tblBuscarPartidaProdPesado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBuscarPartidaProdPesado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tblBuscarPartidaProdPesado);

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        jButton11.setText("Seleccionar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        jButton12.setText("Cancelar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dlgBuscarInvDispLayout = new javax.swing.GroupLayout(dlgBuscarInvDisp.getContentPane());
        dlgBuscarInvDisp.getContentPane().setLayout(dlgBuscarInvDispLayout);
        dlgBuscarInvDispLayout.setHorizontalGroup(
            dlgBuscarInvDispLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarInvDispLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel91, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addComponent(jSeparator21, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(dlgBuscarInvDispLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgBuscarInvDispLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton12)
                .addContainerGap())
        );
        dlgBuscarInvDispLayout.setVerticalGroup(
            dlgBuscarInvDispLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgBuscarInvDispLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel91)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator21, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addGroup(dlgBuscarInvDispLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setLayout(new java.awt.BorderLayout());

        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jToolBar3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnAgregarEntrada1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregarEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        btnAgregarEntrada1.setText("Agregar");
        btnAgregarEntrada1.setFocusable(false);
        btnAgregarEntrada1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregarEntrada1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregarEntrada1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregarEntrada1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEntrada1ActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAgregarEntrada1);

        jLabel2.setText("   ");
        jToolBar3.add(jLabel2);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(227, 222, 222));
        jLabel15.setText(" ");
        jToolBar3.add(jLabel15);

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Tipo producto:");
        jToolBar3.add(jLabel24);

        jLabel52.setText("   ");
        jToolBar3.add(jLabel52);

        cmbTipoEntradaProducto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        cmbTipoEntradaProducto.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbTipoEntradaProducto.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbTipoEntradaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoEntradaProductoActionPerformed(evt);
            }
        });
        jToolBar3.add(cmbTipoEntradaProducto);

        jLabel30.setText("   ");
        jToolBar3.add(jLabel30);

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(227, 222, 222));
        jLabel26.setText("     ");
        jToolBar3.add(jLabel26);

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Calibre:");
        jToolBar3.add(jLabel25);

        jLabel57.setText("   ");
        jToolBar3.add(jLabel57);

        cmbCalibre.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        cmbCalibre.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbCalibre.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbCalibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCalibreActionPerformed(evt);
            }
        });
        jToolBar3.add(cmbCalibre);

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(227, 222, 222));
        jLabel28.setText("     ");
        jToolBar3.add(jLabel28);

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/calendar.png"))); // NOI18N
        jToolBar3.add(jLabel32);

        jrFiltroFechas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrFiltroFechas.setFocusable(false);
        jrFiltroFechas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jrFiltroFechas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jrFiltroFechas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrFiltroFechasActionPerformed(evt);
            }
        });
        jToolBar3.add(jrFiltroFechas);

        jLabel33.setText("   ");
        jToolBar3.add(jLabel33);
        jToolBar3.add(jSeparator7);

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel53.setText("De:");
        jToolBar3.add(jLabel53);

        dcFecha1.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    dcFecha1.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
    dcFecha1.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        dcFecha1.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha1.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar3.add(dcFecha1);

    lbl1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl1.setForeground(new java.awt.Color(227, 222, 222));
    lbl1.setText("     ");
    jToolBar3.add(lbl1);

    jLabel54.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel54.setText("Hasta:");
    jToolBar3.add(jLabel54);

    dcFecha2.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha2.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
try {
    dcFecha2.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha2.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar3.add(dcFecha2);
    jToolBar3.add(jSeparator8);

    btnBuscarEntrada1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBuscarEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
    btnBuscarEntrada1.setText("Buscar");
    btnBuscarEntrada1.setFocusable(false);
    btnBuscarEntrada1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnBuscarEntrada1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBuscarEntrada1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBuscarEntrada1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBuscarEntrada1ActionPerformed(evt);
        }
    });
    jToolBar3.add(btnBuscarEntrada1);
    jToolBar3.add(jSeparator9);

    btnReporteEntrada1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntrada1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteEntrada1.setText("Reporte");
    btnReporteEntrada1.setFocusable(false);
    btnReporteEntrada1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntrada1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntrada1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntrada1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntrada1ActionPerformed(evt);
        }
    });
    jToolBar3.add(btnReporteEntrada1);

    jLabel55.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel55.setForeground(new java.awt.Color(227, 222, 222));
    jLabel55.setText("     ");
    jToolBar3.add(jLabel55);

    jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel27.setForeground(new java.awt.Color(227, 222, 222));
    jLabel27.setText("     ");
    jToolBar3.add(jLabel27);

    tblCalibrar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    tblCalibrar.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ));
    jScrollPane2.setViewportView(tblCalibrar);

    javax.swing.GroupLayout CalibrarLayout = new javax.swing.GroupLayout(Calibrar);
    Calibrar.setLayout(CalibrarLayout);
    CalibrarLayout.setHorizontalGroup(
        CalibrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
        .addComponent(jScrollPane2)
    );
    CalibrarLayout.setVerticalGroup(
        CalibrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(CalibrarLayout.createSequentialGroup()
            .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
    );

    jTabbedPane2.addTab("Calibrar", Calibrar);

    jToolBar4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar4.setFloatable(false);
    jToolBar4.setRollover(true);

    btnAgregarSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
    btnAgregarSeleccionar.setText("Agregar");
    btnAgregarSeleccionar.setFocusable(false);
    btnAgregarSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnAgregarSeleccionar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarSeleccionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarSeleccionarActionPerformed(evt);
        }
    });
    jToolBar4.add(btnAgregarSeleccionar);

    jLabel3.setText("   ");
    jToolBar4.add(jLabel3);

    jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel16.setForeground(new java.awt.Color(227, 222, 222));
    jLabel16.setText(" ");
    jToolBar4.add(jLabel16);

    jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel29.setText("Tipo producto:");
    jToolBar4.add(jLabel29);

    jLabel58.setText("   ");
    jToolBar4.add(jLabel58);

    cmbTipoEntradaProductoSeleccionar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbTipoEntradaProductoSeleccionar.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbTipoEntradaProductoSeleccionar.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbTipoEntradaProductoSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbTipoEntradaProductoSeleccionarActionPerformed(evt);
        }
    });
    jToolBar4.add(cmbTipoEntradaProductoSeleccionar);

    jLabel34.setText("   ");
    jToolBar4.add(jLabel34);

    jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel35.setForeground(new java.awt.Color(227, 222, 222));
    jLabel35.setText("     ");
    jToolBar4.add(jLabel35);

    jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel36.setText("Calibre:");
    jToolBar4.add(jLabel36);

    jLabel59.setText("   ");
    jToolBar4.add(jLabel59);

    cmbCalibreSeleccionar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbCalibreSeleccionar.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbCalibreSeleccionar.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbCalibreSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbCalibreSeleccionarActionPerformed(evt);
        }
    });
    jToolBar4.add(cmbCalibreSeleccionar);

    jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel37.setForeground(new java.awt.Color(227, 222, 222));
    jLabel37.setText("     ");
    jToolBar4.add(jLabel37);

    jLabel49.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel49.setText("Selección:");
    jToolBar4.add(jLabel49);

    jLabel68.setText("   ");
    jToolBar4.add(jLabel68);

    cmbSeleccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbSeleccion.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbSeleccion.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbSeleccion.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbSeleccionActionPerformed(evt);
        }
    });
    jToolBar4.add(cmbSeleccion);

    jLabel62.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel62.setForeground(new java.awt.Color(227, 222, 222));
    jLabel62.setText("                                                                                      ");
    jToolBar4.add(jLabel62);

    tblSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    tblSeleccionar.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ));
    jScrollPane3.setViewportView(tblSeleccionar);

    jToolBar5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar5.setFloatable(false);
    jToolBar5.setRollover(true);

    jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel17.setForeground(new java.awt.Color(227, 222, 222));
    jLabel17.setText(" ");
    jToolBar5.add(jLabel17);

    jLabel46.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/calendar.png"))); // NOI18N
    jToolBar5.add(jLabel46);

    jrFiltroFechasSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jrFiltroFechasSeleccionar.setFocusable(false);
    jrFiltroFechasSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jrFiltroFechasSeleccionar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jrFiltroFechasSeleccionar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechasSeleccionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechasSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jrFiltroFechasSeleccionarActionPerformed(evt);
        }
    });
    jToolBar5.add(jrFiltroFechasSeleccionar);

    jLabel47.setText("   ");
    jToolBar5.add(jLabel47);
    jToolBar5.add(jSeparator13);

    jLabel65.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel65.setText("De:");
    jToolBar5.add(jLabel65);

    dcFecha1Seleccionar.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha1Seleccionar.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
dcFecha1Seleccionar.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
try {
    dcFecha1Seleccionar.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha1Seleccionar.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar5.add(dcFecha1Seleccionar);

    lbl3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl3.setForeground(new java.awt.Color(227, 222, 222));
    lbl3.setText("     ");
    jToolBar5.add(lbl3);

    jLabel66.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel66.setText("Hasta:");
    jToolBar5.add(jLabel66);

    dcFecha2Seleccionar.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha2Seleccionar.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
try {
    dcFecha2Seleccionar.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha2Seleccionar.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar5.add(dcFecha2Seleccionar);

    lbl4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl4.setForeground(new java.awt.Color(227, 222, 222));
    lbl4.setText("     ");
    jToolBar5.add(lbl4);
    jToolBar5.add(jSeparator14);

    btnBuscarSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBuscarSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
    btnBuscarSeleccionar.setText("Buscar");
    btnBuscarSeleccionar.setFocusable(false);
    btnBuscarSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnBuscarSeleccionar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBuscarSeleccionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBuscarSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBuscarSeleccionarActionPerformed(evt);
        }
    });
    jToolBar5.add(btnBuscarSeleccionar);
    jToolBar5.add(jSeparator15);

    btnReporteSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteSeleccionar.setText("Reporte");
    btnReporteSeleccionar.setFocusable(false);
    btnReporteSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteSeleccionar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteSeleccionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteSeleccionarActionPerformed(evt);
        }
    });
    jToolBar5.add(btnReporteSeleccionar);

    jLabel67.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel67.setForeground(new java.awt.Color(227, 222, 222));
    jLabel67.setText("                                                                                                                       ");
    jToolBar5.add(jLabel67);

    javax.swing.GroupLayout SeleccionarLayout = new javax.swing.GroupLayout(Seleccionar);
    Seleccionar.setLayout(SeleccionarLayout);
    SeleccionarLayout.setHorizontalGroup(
        SeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
        .addComponent(jScrollPane3)
        .addComponent(jToolBar5, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
    );
    SeleccionarLayout.setVerticalGroup(
        SeleccionarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(SeleccionarLayout.createSequentialGroup()
            .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))
    );

    jTabbedPane2.addTab("Seleccionar", Seleccionar);

    jToolBar6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar6.setFloatable(false);
    jToolBar6.setRollover(true);

    btnAgregarPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarPesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
    btnAgregarPesar.setText("Agregar");
    btnAgregarPesar.setFocusable(false);
    btnAgregarPesar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnAgregarPesar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarPesar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarPesar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarPesarActionPerformed(evt);
        }
    });
    jToolBar6.add(btnAgregarPesar);

    jLabel4.setText("   ");
    jToolBar6.add(jLabel4);

    jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel20.setForeground(new java.awt.Color(227, 222, 222));
    jLabel20.setText(" ");
    jToolBar6.add(jLabel20);

    btnBaja.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/Flecha_abajo16x16.png"))); // NOI18N
    btnBaja.setText("Baja");
    btnBaja.setFocusable(false);
    btnBaja.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
    btnBaja.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBaja.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBaja.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBajaActionPerformed(evt);
        }
    });
    jToolBar6.add(btnBaja);

    jLabel12.setText("   ");
    jToolBar6.add(jLabel12);

    jLabel5.setText("   ");
    jToolBar6.add(jLabel5);

    jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel39.setText("Tipo producto:");
    jToolBar6.add(jLabel39);

    jLabel69.setText("   ");
    jToolBar6.add(jLabel69);

    cmbTipoEntradaProductoPesar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbTipoEntradaProductoPesar.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbTipoEntradaProductoPesar.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbTipoEntradaProductoPesar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbTipoEntradaProductoPesarActionPerformed(evt);
        }
    });
    jToolBar6.add(cmbTipoEntradaProductoPesar);

    jLabel40.setText("   ");
    jToolBar6.add(jLabel40);

    jLabel41.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel41.setForeground(new java.awt.Color(227, 222, 222));
    jLabel41.setText("     ");
    jToolBar6.add(jLabel41);

    jLabel42.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel42.setText("Calibre:");
    jToolBar6.add(jLabel42);

    jLabel70.setText("   ");
    jToolBar6.add(jLabel70);

    cmbCalibrePesar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbCalibrePesar.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbCalibrePesar.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbCalibrePesar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbCalibrePesarActionPerformed(evt);
        }
    });
    jToolBar6.add(cmbCalibrePesar);

    jLabel43.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel43.setForeground(new java.awt.Color(227, 222, 222));
    jLabel43.setText("     ");
    jToolBar6.add(jLabel43);

    jLabel71.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel71.setText("Selección:");
    jToolBar6.add(jLabel71);

    jLabel72.setText("   ");
    jToolBar6.add(jLabel72);

    cmbSeleccionPesar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
    cmbSeleccionPesar.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbSeleccionPesar.setPreferredSize(new java.awt.Dimension(120, 25));
    cmbSeleccionPesar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cmbSeleccionPesarActionPerformed(evt);
        }
    });
    jToolBar6.add(cmbSeleccionPesar);

    jLabel44.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel44.setForeground(new java.awt.Color(227, 222, 222));
    jLabel44.setText("     ");
    jToolBar6.add(jLabel44);

    jLabel79.setText("   ");
    jToolBar6.add(jLabel79);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel1.setText("No. Partida:");
    jToolBar6.add(jLabel1);

    jLabel97.setText("   ");
    jToolBar6.add(jLabel97);

    txtNoPartidaPesado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    txtNoPartidaPesado.setMinimumSize(new java.awt.Dimension(18, 21));
    txtNoPartidaPesado.setPreferredSize(new java.awt.Dimension(45, 25));
    txtNoPartidaPesado.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txtNoPartidaPesadoActionPerformed(evt);
        }
    });
    jToolBar6.add(txtNoPartidaPesado);

    jLabel73.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel73.setForeground(new java.awt.Color(227, 222, 222));
    jLabel73.setText("                                             ");
    jToolBar6.add(jLabel73);

    tblPesar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    tblPesar.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ));
    jScrollPane5.setViewportView(tblPesar);

    jToolBar7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jToolBar7.setFloatable(false);
    jToolBar7.setRollover(true);

    jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel21.setForeground(new java.awt.Color(227, 222, 222));
    jLabel21.setText(" ");
    jToolBar7.add(jLabel21);

    jLabel48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/calendar.png"))); // NOI18N
    jToolBar7.add(jLabel48);

    jrFiltroFechasSeleccionar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jrFiltroFechasSeleccionar1.setFocusable(false);
    jrFiltroFechasSeleccionar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jrFiltroFechasSeleccionar1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jrFiltroFechasSeleccionar1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechasSeleccionar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jrFiltroFechasSeleccionar1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jrFiltroFechasSeleccionar1ActionPerformed(evt);
        }
    });
    jToolBar7.add(jrFiltroFechasSeleccionar1);

    jLabel74.setText("   ");
    jToolBar7.add(jLabel74);
    jToolBar7.add(jSeparator16);

    jLabel75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel75.setText("De:");
    jToolBar7.add(jLabel75);

    dcFecha1Pesar.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha1Pesar.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
dcFecha1Pesar.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
try {
    dcFecha1Pesar.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha1Pesar.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar7.add(dcFecha1Pesar);

    lbl5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl5.setForeground(new java.awt.Color(227, 222, 222));
    lbl5.setText("     ");
    jToolBar7.add(lbl5);

    jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel76.setText("Hasta:");
    jToolBar7.add(jLabel76);

    dcFecha2Pesar.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
        new datechooser.view.appearance.ViewAppearance("custom",
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                true,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 255),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.ButtonPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(128, 128, 128),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(0, 0, 255),
                false,
                true,
                new datechooser.view.appearance.swing.LabelPainter()),
            new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                new java.awt.Color(0, 0, 0),
                new java.awt.Color(255, 0, 0),
                false,
                false,
                new datechooser.view.appearance.swing.ButtonPainter()),
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dcFecha2Pesar.setCalendarPreferredSize(new java.awt.Dimension(260, 195));
try {
    dcFecha2Pesar.setDefaultPeriods(new datechooser.model.multiple.PeriodSet());
    } catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
        e1.printStackTrace();
    }
    dcFecha2Pesar.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 12));
    jToolBar7.add(dcFecha2Pesar);

    lbl6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl6.setForeground(new java.awt.Color(227, 222, 222));
    lbl6.setText("     ");
    jToolBar7.add(lbl6);
    jToolBar7.add(jSeparator17);

    btnBuscarPesar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBuscarPesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
    btnBuscarPesar.setText("Buscar");
    btnBuscarPesar.setFocusable(false);
    btnBuscarPesar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnBuscarPesar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBuscarPesar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBuscarPesar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBuscarPesarActionPerformed(evt);
        }
    });
    jToolBar7.add(btnBuscarPesar);
    jToolBar7.add(jSeparator18);

    btnReporteSeleccionar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteSeleccionar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteSeleccionar1.setText("Reporte Inventario");
    btnReporteSeleccionar1.setFocusable(false);
    btnReporteSeleccionar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteSeleccionar1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteSeleccionar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteSeleccionar1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteSeleccionar1ActionPerformed(evt);
        }
    });
    jToolBar7.add(btnReporteSeleccionar1);

    jLabel96.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel96.setForeground(new java.awt.Color(227, 222, 222));
    jLabel96.setText("  ");
    jToolBar7.add(jLabel96);

    btnReporteEntradasSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntradasSeleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteEntradasSeleccionar.setText("Reporte entradas");
    btnReporteEntradasSeleccionar.setFocusable(false);
    btnReporteEntradasSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntradasSeleccionar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntradasSeleccionar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntradasSeleccionar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntradasSeleccionarActionPerformed(evt);
        }
    });
    jToolBar7.add(btnReporteEntradasSeleccionar);

    jLabel98.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel98.setForeground(new java.awt.Color(227, 222, 222));
    jLabel98.setText("  ");
    jToolBar7.add(jLabel98);

    btnAgregarPesar1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnAgregarPesar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnAgregarPesar1.setText("Reporte salidas");
    btnAgregarPesar1.setFocusable(false);
    btnAgregarPesar1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
    btnAgregarPesar1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnAgregarPesar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnAgregarPesar1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnAgregarPesar1ActionPerformed(evt);
        }
    });
    jToolBar7.add(btnAgregarPesar1);

    jLabel77.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel77.setForeground(new java.awt.Color(227, 222, 222));
    jLabel77.setText("                                                                                                            ");
    jToolBar7.add(jLabel77);

    javax.swing.GroupLayout PesarLayout = new javax.swing.GroupLayout(Pesar);
    Pesar.setLayout(PesarLayout);
    PesarLayout.setHorizontalGroup(
        PesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar6, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
        .addComponent(jScrollPane5)
        .addComponent(jToolBar7, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
    );
    PesarLayout.setVerticalGroup(
        PesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(PesarLayout.createSequentialGroup()
            .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))
    );

    jTabbedPane2.addTab("Pesar", Pesar);

    add(jTabbedPane2, java.awt.BorderLayout.CENTER);

    jPanel1.setLayout(new java.awt.BorderLayout());
    add(jPanel1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntrada1ActionPerformed
        try {
            abrirDialogoAgregar();
        } catch (Exception ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }//GEN-LAST:event_btnAgregarEntrada1ActionPerformed

    private void cmbTipoEntradaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoEntradaProductoActionPerformed
        actualizarTablaCalibrar();
    }//GEN-LAST:event_cmbTipoEntradaProductoActionPerformed

    private void jrFiltroFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasActionPerformed
        if (dcFecha1.isEnabled() && dcFecha2.isEnabled())
        {
            dcFecha1.setEnabled(false);
            dcFecha1.setCurrent(null);
            dcFecha2.setEnabled(false);
            dcFecha2.setCurrent(null);
        }
        else
        {
            dcFecha1.setEnabled(true);
            dcFecha2.setEnabled(true);
        }
    }//GEN-LAST:event_jrFiltroFechasActionPerformed

    private void btnBuscarEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarEntrada1ActionPerformed
        actualizarTablaCalibrar();
    }//GEN-LAST:event_btnBuscarEntrada1ActionPerformed

    private void btnReporteEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntrada1ActionPerformed
        try {
            cc.mostrarReporteInventarioProuctoCalibrado(calibrar);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteEntrada1ActionPerformed

    private void cmbCalibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreActionPerformed
        actualizarTablaCalibrar();
    }//GEN-LAST:event_cmbCalibreActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        realizarEntradaCalibrar();
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void btnCancelarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarActionPerformed
        dlgAgregar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarActionPerformed

    private void txtNoPartidaAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaAgregarKeyTyped
        
    }//GEN-LAST:event_txtNoPartidaAgregarKeyTyped

    private void txtTipoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoProductoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoProductoKeyTyped

    private void txtNoPiezasCueroAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroAgregarKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasCueroAgregarKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            abrirDialogoBuscarPartida();
        } catch (Exception ex) {
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            SeleccionarEntrada();
        } catch (Exception ex) {
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        dlgBuscar.setVisible(false);
        dlgAgregar.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnAgregarSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarSeleccionarActionPerformed
        try {
            abrirDialogoAgregarSeleccionar();
        } catch (Exception ex) {
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error en base de datos", "Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarSeleccionarActionPerformed

    private void cmbTipoEntradaProductoSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoEntradaProductoSeleccionarActionPerformed
        actualizarTablaSeleccionar();
    }//GEN-LAST:event_cmbTipoEntradaProductoSeleccionarActionPerformed

    private void cmbCalibreSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreSeleccionarActionPerformed
        actualizarTablaSeleccionar();
    }//GEN-LAST:event_cmbCalibreSeleccionarActionPerformed

    private void jrFiltroFechasSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasSeleccionarActionPerformed
        if (dcFecha1Seleccionar.isEnabled() && dcFecha2Seleccionar.isEnabled())
        {
            dcFecha1Seleccionar.setEnabled(false);
            dcFecha1Seleccionar.setCurrent(null);
            dcFecha2Seleccionar.setEnabled(false);
            dcFecha2Seleccionar.setCurrent(null);
        }
        else
        {
            dcFecha1Seleccionar.setEnabled(true);
            dcFecha2Seleccionar.setEnabled(true);
        }
    }//GEN-LAST:event_jrFiltroFechasSeleccionarActionPerformed

    private void btnBuscarSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarSeleccionarActionPerformed
        actualizarTablaSeleccionar();
    }//GEN-LAST:event_btnBuscarSeleccionarActionPerformed

    private void btnReporteSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteSeleccionarActionPerformed
        try {
            actualizarTablaSeleccionar();
            sc.mostrarReporteInventarioProuctoSeleccionado(cs);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteSeleccionarActionPerformed

    private void cmbSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionActionPerformed
        actualizarTablaSeleccionar();
    }//GEN-LAST:event_cmbSeleccionActionPerformed

    private void btnRealizarEntrada1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntrada1ActionPerformed
        realizarEntradaSeleccionar();
    }//GEN-LAST:event_btnRealizarEntrada1ActionPerformed

    private void btnCancelarAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregar1ActionPerformed
        dlgAgregarSeleccionar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregar1ActionPerformed

    private void txtNoPartidaAgregarSeleccionarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaAgregarSeleccionarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaAgregarSeleccionarKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            abrirDialogoBuscarPartidaSeleccionar();
        } catch (Exception ex) {
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtCalibreSeleccionarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCalibreSeleccionarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibreSeleccionarKeyTyped

    private void txtNoPiezasCueroSeleccionarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroSeleccionarKeyTyped
        char c;
        c=evt.getKeyChar();    
        
        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();           
            evt.consume(); 
        }
    }//GEN-LAST:event_txtNoPiezasCueroSeleccionarKeyTyped

    private void txtTipoProductoSeleccionarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoProductoSeleccionarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoProductoSeleccionarKeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            SeleccionarEntradaSeleccionar();
        } catch (Exception ex) {
            dlgBuscarSeleccionar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error de conexión a base de datos","Error",JOptionPane.ERROR_MESSAGE);
            dlgBuscarSeleccionar.setVisible(true);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        dlgBuscarSeleccionar.setVisible(false);
        dlgAgregarSeleccionar.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnAgregarPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPesarActionPerformed
        try {
            abrirDialogoAgregarPesar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error de conexión a base de datos","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarPesarActionPerformed

    private void cmbTipoEntradaProductoPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoEntradaProductoPesarActionPerformed
        actualizarTablaPesar();
    }//GEN-LAST:event_cmbTipoEntradaProductoPesarActionPerformed

    private void cmbCalibrePesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibrePesarActionPerformed
        actualizarTablaPesar();
    }//GEN-LAST:event_cmbCalibrePesarActionPerformed

    private void cmbSeleccionPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionPesarActionPerformed
        actualizarTablaPesar();
    }//GEN-LAST:event_cmbSeleccionPesarActionPerformed

    private void jrFiltroFechasSeleccionar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrFiltroFechasSeleccionar1ActionPerformed
        if (dcFecha1Pesar.isEnabled() && dcFecha2Pesar.isEnabled())
        {
            dcFecha1Pesar.setEnabled(false);
            dcFecha1Pesar.setCurrent(null);
            dcFecha2Pesar.setEnabled(false);
            dcFecha2Pesar.setCurrent(null);
        }
        else
        {
            dcFecha1Pesar.setEnabled(true);
            dcFecha2Pesar.setEnabled(true);
        }
    }//GEN-LAST:event_jrFiltroFechasSeleccionar1ActionPerformed

    private void btnBuscarPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPesarActionPerformed
        actualizarTablaPesar();
    }//GEN-LAST:event_btnBuscarPesarActionPerformed

    private void btnReporteSeleccionar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteSeleccionar1ActionPerformed
        try {
            actualizarTablaPesar();
            cpc.mostrarReporteInventarioProuctoPesado(cp);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteSeleccionar1ActionPerformed

    private void btnRealizarEntrada2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntrada2ActionPerformed
        realizarEntradaPesar();
    }//GEN-LAST:event_btnRealizarEntrada2ActionPerformed

    private void btnCancelarAgregar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregar2ActionPerformed
        dlgAgregarPesar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregar2ActionPerformed

    private void txtNoPartidaAgregarPesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaAgregarPesarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaAgregarPesarKeyTyped

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            abrirDialogoBuscarPartidaPesar();
        } catch (Exception ex) {
            dlgAgregarSeleccionar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error de conexión a base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtCalibrePesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCalibrePesarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibrePesarKeyTyped

    private void txtNoPiezasCueroPesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroPesarKeyTyped
        char c;
        c=evt.getKeyChar();    
        
        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();           
            evt.consume(); 
        }
    }//GEN-LAST:event_txtNoPiezasCueroPesarKeyTyped

    private void txtTipoProductoPesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoProductoPesarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoProductoPesarKeyTyped

    private void txtSeleccionPesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeleccionPesarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSeleccionPesarKeyTyped

    private void txtKgPesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgPesarKeyTyped
        char c;
        c=evt.getKeyChar();    
        int punto=txtKgPesar.getText().indexOf(".")+1;
        
        if (punto==0)
        {
            if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE && c!=KeyEvent.VK_PERIOD)
            {
                getToolkit().beep();           
                evt.consume(); 
            }
        }
        
        else
        {
            if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
            {
                getToolkit().beep();           
                evt.consume(); 
            }
        }
    }//GEN-LAST:event_txtKgPesarKeyTyped

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            SeleccionarEntradaPesar();
        } catch (Exception ex) {
            dlgBuscarPesar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error de conexión a base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            dlgBuscarPesar.setVisible(true);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        dlgBuscarPesar.setVisible(false);
        dlgAgregarPesar.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        try {
            abrirDialogoAgregarInvDisp();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error de conexión a base de datos","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBajaActionPerformed

    private void btnRealizarEntradaInvDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaInvDispActionPerformed
        realizarEntradaInvDisp();
    }//GEN-LAST:event_btnRealizarEntradaInvDispActionPerformed

    private void btnCancelarAgregarInvDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarInvDispActionPerformed
        dlgAgregarInvDisponible.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarInvDispActionPerformed

    private void txtNoPartidaAgregarInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPartidaAgregarInvDispKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaAgregarInvDispKeyTyped

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            abrirDialogoBuscarPartidaInvDisp();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error de conexión a base de datos","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void txtCalibreInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCalibreInvDispKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibreInvDispKeyTyped

    private void txtNoPiezasCueroInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroInvDispKeyTyped
        char c;
        c=evt.getKeyChar();    
        
        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();           
            evt.consume(); 
        }
    }//GEN-LAST:event_txtNoPiezasCueroInvDispKeyTyped

    private void txtTipoProductoInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoProductoInvDispKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoProductoInvDispKeyTyped

    private void txtSeleccionInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeleccionInvDispKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSeleccionInvDispKeyTyped

    private void txtKgInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgInvDispKeyTyped
        char c;
        c=evt.getKeyChar();    
        int punto=txtKgPesar.getText().indexOf(".")+1;
        
        if (punto==0)
        {
            if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE && c!=KeyEvent.VK_PERIOD)
            {
                getToolkit().beep();           
                evt.consume(); 
            }
        }
        
        else
        {
            if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
            {
                getToolkit().beep();           
                evt.consume(); 
            }
        }
    }//GEN-LAST:event_txtKgInvDispKeyTyped

    private void txtCalibreInvDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalibreInvDispActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibreInvDispActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            SeleccionarEntradaInvDisp();
        } catch (Exception ex) {
            dlgBuscarInvDisp.setVisible(false);
            JOptionPane.showMessageDialog(null, "Error de conexión a base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            dlgBuscarInvDisp.setVisible(true);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        dlgBuscarInvDisp.setVisible(false);
        dlgAgregarInvDisponible.setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void txtDescripcionPesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionPesarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionPesarKeyTyped

    private void jrNoPartidaPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrNoPartidaPesarActionPerformed
        txtNoPartidaAgregarPesar.setText("");
        txtTipoProductoPesar.setText("");
        txtCalibrePesar.setText("");
        txtSeleccionPesar.setText("");
        lblIdRegistroPesar.setText("");
        lblErrorAgregarPesar.setText("");
        txtNoPiezasCueroPesar.setText("");
        txtDescripcionPesar.setText("");
        txtKgPesar.setText("");
    }//GEN-LAST:event_jrNoPartidaPesarActionPerformed

    private void jrDescripcionPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrDescripcionPesarActionPerformed
        txtNoPartidaAgregarPesar.setText("");
        txtTipoProductoPesar.setText("");
        txtCalibrePesar.setText("");
        txtSeleccionPesar.setText("");
        lblIdRegistroPesar.setText("");
        lblErrorAgregarPesar.setText("");
        txtNoPiezasCueroPesar.setText("");
        txtDescripcionPesar.setText("");
        txtKgPesar.setText("");
    }//GEN-LAST:event_jrDescripcionPesarActionPerformed

    private void txtDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionKeyTyped

    private void jrNoPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrNoPartidaActionPerformed
        txtDescripcion.setEnabled(false);
        txtDescripcion.setText("");
        txtNoPartidaAgregar.setText("");
        txtNoPartidaAgregar.setEnabled(true);
        txtTipoProducto.setText("");
        txtNoPiezasCueroAgregar.setText("");
    }//GEN-LAST:event_jrNoPartidaActionPerformed

    private void jrDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrDescripcionActionPerformed
        txtDescripcion.setEnabled(true);
        txtDescripcion.setText("");
        txtNoPartidaAgregar.setText("");
        txtNoPartidaAgregar.setEnabled(false);
    }//GEN-LAST:event_jrDescripcionActionPerformed

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void jrNoPartidaSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrNoPartidaSeleccionActionPerformed
        txtNoPartidaAgregarSeleccionar.setText("");
        txtTipoProductoSeleccionar.setText("");
        lblErrorAgregarSeleccionar.setText("");
        lblidRegistroSeleccionar.setText("");
        lblidRegistroSeleccionar.setVisible(false);
        txtCalibreSeleccionar.setText("");
        txtNoPiezasCueroSeleccionar.setText("");
        txtDescripcionSeleccionar.setText("");
    }//GEN-LAST:event_jrNoPartidaSeleccionActionPerformed

    private void jrDescripcionSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrDescripcionSeleccionActionPerformed
        txtNoPartidaAgregarSeleccionar.setText("");
        txtTipoProductoSeleccionar.setText("");
        txtCalibreSeleccionar.setText("");
        lblErrorAgregarSeleccionar.setText("");
        lblidRegistroSeleccionar.setText("");
        lblidRegistroSeleccionar.setVisible(false);
        txtNoPiezasCueroSeleccionar.setText("");
        txtDescripcionSeleccionar.setText("");
    }//GEN-LAST:event_jrDescripcionSeleccionActionPerformed

    private void txtDescripcionSeleccionarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionSeleccionarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionSeleccionarKeyTyped

    private void txtNoPiezasCueroSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroSeleccionarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPiezasCueroSeleccionarActionPerformed

    private void txtNoPartidaAgregarPesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPartidaAgregarPesarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoPartidaAgregarPesarActionPerformed

    private void txtDescripcionInvDispKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionInvDispKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionInvDispKeyTyped

    private void jrNoPartidaInvDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrNoPartidaInvDispActionPerformed
        txtNoPartidaAgregarInvDisp.setText("");
        txtTipoProductoInvDisp.setText("");
        txtCalibreInvDisp.setText("");
        txtSeleccionInvDisp.setText("");
        lblIdRegistroInvDisp.setText("");
        lblErrorAgregarInvDisp.setText("");
        txtNoPiezasCueroInvDisp.setText("");
        txtDescripcionInvDisp.setText("");
        txtKgInvDisp.setText("");
    }//GEN-LAST:event_jrNoPartidaInvDispActionPerformed

    private void jrDescripcionInvDispActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrDescripcionInvDispActionPerformed
        txtNoPartidaAgregarInvDisp.setText("");
        txtTipoProductoInvDisp.setText("");
        txtCalibreInvDisp.setText("");
        txtSeleccionInvDisp.setText("");
        lblIdRegistroInvDisp.setText("");
        lblErrorAgregarInvDisp.setText("");
        txtNoPiezasCueroInvDisp.setText("");
        txtDescripcionInvDisp.setText("");
        txtKgInvDisp.setText("");
    }//GEN-LAST:event_jrDescripcionInvDispActionPerformed

    private void btnAgregarPesar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPesar1ActionPerformed
        try {
            actualizarTablaPesar();
            cpc.mostrarReporteSalidasSemiterminado(cp);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarPesar1ActionPerformed

    private void txtNoPartidaPesadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoPartidaPesadoActionPerformed
        actualizarTablaPesar();
    }//GEN-LAST:event_txtNoPartidaPesadoActionPerformed

    private void btnReporteEntradasSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradasSeleccionarActionPerformed
        try {
            actualizarTablaPesar();
            cpc.mostrarReporteEntradasProuctoPesado(cp);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteEntradasSeleccionarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Calibrar;
    private javax.swing.JPanel Pesar;
    private javax.swing.JPanel Seleccionar;
    private javax.swing.ButtonGroup bgGrupo1;
    private javax.swing.ButtonGroup bgGrupo2;
    private javax.swing.ButtonGroup bgGrupo3;
    private javax.swing.ButtonGroup bgGrupo4;
    public javax.swing.JButton btnAgregarEntrada1;
    public javax.swing.JButton btnAgregarPesar;
    private javax.swing.JButton btnAgregarPesar1;
    public javax.swing.JButton btnAgregarSeleccionar;
    public javax.swing.JButton btnBaja;
    private javax.swing.JButton btnBuscarEntrada1;
    private javax.swing.JButton btnBuscarPesar;
    private javax.swing.JButton btnBuscarSeleccionar;
    private javax.swing.JButton btnCancelarAgregar;
    private javax.swing.JButton btnCancelarAgregar1;
    private javax.swing.JButton btnCancelarAgregar2;
    private javax.swing.JButton btnCancelarAgregarInvDisp;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarEntrada1;
    private javax.swing.JButton btnRealizarEntrada2;
    private javax.swing.JButton btnRealizarEntradaInvDisp;
    private javax.swing.JButton btnReporteEntrada1;
    private javax.swing.JButton btnReporteEntradasSeleccionar;
    private javax.swing.JButton btnReporteSeleccionar;
    private javax.swing.JButton btnReporteSeleccionar1;
    private javax.swing.JComboBox cmbCalibre;
    private javax.swing.JComboBox cmbCalibreAgregar;
    private javax.swing.JComboBox cmbCalibrePesar;
    private javax.swing.JComboBox cmbCalibreSeleccionar;
    private javax.swing.JComboBox cmbSeleccion;
    private javax.swing.JComboBox cmbSeleccionAgregar;
    private javax.swing.JComboBox cmbSeleccionPesar;
    private javax.swing.JComboBox cmbTipoEntradaProducto;
    private javax.swing.JComboBox cmbTipoEntradaProductoPesar;
    private javax.swing.JComboBox cmbTipoEntradaProductoSeleccionar;
    private datechooser.beans.DateChooserCombo dcFecha1;
    private datechooser.beans.DateChooserCombo dcFecha1Pesar;
    private datechooser.beans.DateChooserCombo dcFecha1Seleccionar;
    private datechooser.beans.DateChooserCombo dcFecha2;
    private datechooser.beans.DateChooserCombo dcFecha2Pesar;
    private datechooser.beans.DateChooserCombo dcFecha2Seleccionar;
    private javax.swing.JDialog dlgAgregar;
    private javax.swing.JDialog dlgAgregarInvDisponible;
    private javax.swing.JDialog dlgAgregarPesar;
    private javax.swing.JDialog dlgAgregarSeleccionar;
    private javax.swing.JDialog dlgBuscar;
    private javax.swing.JDialog dlgBuscarInvDisp;
    private javax.swing.JDialog dlgBuscarPesar;
    private javax.swing.JDialog dlgBuscarSeleccionar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator14;
    private javax.swing.JToolBar.Separator jSeparator15;
    private javax.swing.JToolBar.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator17;
    private javax.swing.JToolBar.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JRadioButton jrDescripcion;
    private javax.swing.JRadioButton jrDescripcionInvDisp;
    private javax.swing.JRadioButton jrDescripcionPesar;
    private javax.swing.JRadioButton jrDescripcionSeleccion;
    private javax.swing.JRadioButton jrFiltroFechas;
    private javax.swing.JRadioButton jrFiltroFechasSeleccionar;
    private javax.swing.JRadioButton jrFiltroFechasSeleccionar1;
    private javax.swing.JRadioButton jrNoPartida;
    private javax.swing.JRadioButton jrNoPartidaInvDisp;
    private javax.swing.JRadioButton jrNoPartidaPesar;
    private javax.swing.JRadioButton jrNoPartidaSeleccion;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lblErrorAgregar;
    private javax.swing.JLabel lblErrorAgregarInvDisp;
    private javax.swing.JLabel lblErrorAgregarPesar;
    private javax.swing.JLabel lblErrorAgregarSeleccionar;
    private javax.swing.JLabel lblIdRegistroCalibrar;
    private javax.swing.JLabel lblIdRegistroInvDisp;
    private javax.swing.JLabel lblIdRegistroPesar;
    private javax.swing.JLabel lblidRegistroSeleccionar;
    private javax.swing.JTable tblBuscarPartidaCalibrar;
    private javax.swing.JTable tblBuscarPartidaPesar;
    private javax.swing.JTable tblBuscarPartidaProdPesado;
    private javax.swing.JTable tblBuscarPartidaSeleccionar;
    private javax.swing.JTable tblCalibrar;
    private javax.swing.JTable tblPesar;
    private javax.swing.JTable tblSeleccionar;
    private javax.swing.JTextField txtCalibreInvDisp;
    private javax.swing.JTextField txtCalibrePesar;
    private javax.swing.JTextField txtCalibreSeleccionar;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDescripcionInvDisp;
    private javax.swing.JTextField txtDescripcionPesar;
    private javax.swing.JTextField txtDescripcionSeleccionar;
    private javax.swing.JTextField txtKgInvDisp;
    private javax.swing.JTextField txtKgPesar;
    private javax.swing.JTextField txtNoPartidaAgregar;
    private javax.swing.JTextField txtNoPartidaAgregarInvDisp;
    private javax.swing.JTextField txtNoPartidaAgregarPesar;
    private javax.swing.JTextField txtNoPartidaAgregarSeleccionar;
    private javax.swing.JTextField txtNoPartidaPesado;
    private javax.swing.JTextField txtNoPiezasCueroAgregar;
    private javax.swing.JTextField txtNoPiezasCueroInvDisp;
    private javax.swing.JTextField txtNoPiezasCueroPesar;
    private javax.swing.JTextField txtNoPiezasCueroSeleccionar;
    private javax.swing.JTextField txtSeleccionInvDisp;
    private javax.swing.JTextField txtSeleccionPesar;
    private javax.swing.JTextField txtTipoProducto;
    private javax.swing.JTextField txtTipoProductoInvDisp;
    private javax.swing.JTextField txtTipoProductoPesar;
    private javax.swing.JTextField txtTipoProductoSeleccionar;
    // End of variables declaration//GEN-END:variables
}
