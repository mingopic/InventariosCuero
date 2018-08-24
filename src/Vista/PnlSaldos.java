/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.CalibrarCommands;
import Controlador.CueroTrabajarCommands;
import Controlador.ProductoSaldoCommands;
import Controlador.SaldoTerminadoTrabajarCommands;
import Controlador.SeleccionCommands;
import Modelo.ProductoSaldo;
import Modelo.SaldoTerminadoTrabajar;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SISTEMAS
 */
public class PnlSaldos extends javax.swing.JPanel {
    ProductoSaldo ps;
    ProductoSaldoCommands psc;
    CueroTrabajarCommands ctc;
    CalibrarCommands cc;
    SeleccionCommands sc;
    SaldoTerminadoTrabajar stt;
    SaldoTerminadoTrabajarCommands sttc;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "ID Registro","Tipo del producto","No. piezas iniciales","No. Piezas Actuales","Peso","Peso prom.","Selección","Calibre","Descripción","Fecha de entrada"
    };
    /**
     * Creates new form PnlProductosNoLinea
     */
    public PnlSaldos() throws Exception {
        initComponents();
        inicializar();
    }

    public void inicializar() throws Exception
    {
        ps=new ProductoSaldo();
        psc=new ProductoSaldoCommands();
        ctc=new CueroTrabajarCommands();
        cc=new CalibrarCommands();
        sc=new SeleccionCommands();
        
        actualizarTablaProductoSaldo();
        
        llenarComboTipoProducto();
        llenarComboTipoCalibre();
        llenarComboSeleccion();
        
        dcFecha1.setEnabled(false);
        dcFecha2.setEnabled(false);

        
        jrFiltroFechas.setSelected(false);
    }
    
    
    //Método para actualizar la tabla de las entradas de saldos de producto, se inicializa al llamar la clase
    public void actualizarTablaProductoSaldo() 
    {        
        buscaFiltros();
        String[][] datos = null;
       
        DefaultTableModel dtm = null;
        
        try 
        {
            datos = psc.obtenerListaProductoSaldo(ps);
            
            dtm = new DefaultTableModel(datos, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblProductoSaldo.setModel(dtm);

        } catch (Exception e) {
           
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD");
        }
    }
    
    
    public void buscaFiltros()
    {
        //validamos si esta seleccionada la opción de rango de fechas para tomar el valor seleccionado,
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
                            
                    ps.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    ps.setFecha("0");
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
                            
                    ps.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    ps.setFecha1("0");
                }
        }
        else
        {
            ps.setFecha("1900-01-01");
            ps.setFecha1("2040-01-01");
        }
        
        
        //validamos si esta seleccionado algún producto para hacer filtro
        if (cmbTipoEntradaProducto.getSelectedItem().toString().equals("..."))
        {
            ps.setTipoProducto("%%");
        }
        else
        {
            ps.setTipoProducto(cmbTipoEntradaProducto.getSelectedItem().toString());
        }
        
        //validamos si esta seleccionado algún calibre para hacer filtro
        if (cmbCalibre.getSelectedItem().toString().equals("..."))
        {
            ps.setCalibre("%%");
        }
        else
        {
            ps.setCalibre(cmbCalibre.getSelectedItem().toString());
        }
        
         //validamos si esta seleccionado alguna seección para hacer filtro
        if (cmbSeleccion.getSelectedItem().toString().equals("..."))
        {
            ps.setSeleccion("%%");
        }
        else
        {
            ps.setSeleccion(cmbSeleccion.getSelectedItem().toString());
        }
        
        ps.setDescripcion("");
    }
    
    
    //método que llena los combobox de los tipos de producto en la base de datos
    public void llenarComboTipoProducto() throws Exception
    {
        ctc=new CueroTrabajarCommands();
        String[] productos=ctc.llenarComboboxProductos();
        
        int i=0;
        while (i<productos.length)
        {
            cmbTipoEntradaProducto.addItem(productos[i]);
            cmbTipoProductoAgregar.addItem(productos[i]);
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
            cmbCalibreAgregar.addItem(calibres[i]);
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
            cmbSeleccionAgregar.addItem(seleccion[i]);
            i++;
        }
    }
    
    
    //Método que abre el dialogo de agregar entrada de producto de saldo
    public void abrirDialogoAgregar() throws Exception
    {
        inicializarCamposAgregar();
        
        dlgAgregar.setSize(450, 380);
        dlgAgregar.setPreferredSize(dlgAgregar.getSize());
        dlgAgregar.setLocationRelativeTo(null);
        dlgAgregar.setAlwaysOnTop(true);
        dlgAgregar.setVisible(true);
    }
    
    
     //Método para validar que se selecciono un producto de la lista, se usa en dldSalidaSaldos
    public void abrirDialogoSalida() throws Exception
    {
        int renglonSeleccionado = tblProductoSaldo.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            String[] datos=null;
            ps=new ProductoSaldo();
            
            ps.setIdProductoSaldo(Integer.parseInt(tblProductoSaldo.getValueAt(renglonSeleccionado, 0).toString()));
            ps.setTipoProducto(tblProductoSaldo.getValueAt(renglonSeleccionado, 1).toString());
            ps.setNoPiezas(Integer.parseInt(tblProductoSaldo.getValueAt(renglonSeleccionado, 2).toString()));
            ps.setNoPiezasActuales(Integer.parseInt(tblProductoSaldo.getValueAt(renglonSeleccionado, 3).toString()));
            ps.setPeso(Double.parseDouble(tblProductoSaldo.getValueAt(renglonSeleccionado, 4).toString()));
            ps.setSeleccion(tblProductoSaldo.getValueAt(renglonSeleccionado, 6).toString());
            ps.setCalibre(tblProductoSaldo.getValueAt(renglonSeleccionado, 7).toString());
            ps.setDescripcion(tblProductoSaldo.getValueAt(renglonSeleccionado, 8).toString());
            
            txtTipoProductoSalida.setText(ps.getTipoProducto());
            txtCalibreSalida.setText(ps.getCalibre());
            txtSeleccionSalida.setText(ps.getSeleccion());
            txtKgSalida.setText(String.valueOf(ps.getPeso()));
            txtNoPiezasCueroSalida.setText(String.valueOf(ps.getNoPiezasActuales()));
            txtDescripcionSalida.setText(ps.getDescripcion());
            lblErrorSalida.setText("");
            
            dlgSalidaSaldos.setSize(430, 375);
            dlgSalidaSaldos.setPreferredSize(dlgSalidaSaldos.getSize());
            dlgSalidaSaldos.setLocationRelativeTo(null);
            dlgSalidaSaldos.setAlwaysOnTop(true);
            dlgSalidaSaldos.setVisible(true);
        }
        else 
        {
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    //Metodo para inicializar los campos de dlgAgregar
    public void inicializarCamposAgregar() throws Exception
    {
        ps=new ProductoSaldo();
        lblErrorAgregar.setText("");
        txtNoPiezasCueroAgregar.setText("");
        txtDescripcionAgregar.setText("");
        txtKgAgregar.setText("");
    }
    
    
    //Método que realiza una entrada de producto de saldo al inventario
    public void relizarEntradaSaldo()
    {
        if (!txtNoPiezasCueroAgregar.getText().equals("") || !txtKgAgregar.getText().equals(""))
        {
            if (Integer.parseInt(txtNoPiezasCueroAgregar.getText())>=1 && Double.parseDouble(txtKgAgregar.getText())>=1.0)
            {
                try 
                {
                    ps.setTipoProducto(cmbTipoProductoAgregar.getSelectedItem().toString());
                    ps.setCalibre(cmbCalibreAgregar.getSelectedItem().toString());
                    ps.setSeleccion(cmbSeleccionAgregar.getSelectedItem().toString());
                    ps.setNoPiezas(Integer.parseInt(txtNoPiezasCueroAgregar.getText()));
                    ps.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroAgregar.getText()));
                    ps.setPeso(Double.parseDouble(txtKgAgregar.getText()));
                    ps.setFecha(FrmPrincipal.lblFecha.getText());
                    ps.setDescripcion(txtDescripcionAgregar.getText());

                    psc.agregarProductoSaldo(ps);
                    dlgAgregar.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Entrada realizada con éxito");
                    actualizarTablaProductoSaldo();
                } 
                catch (Exception e) 
                {
                    System.err.println(e);
                    dlgAgregar.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Error de conexión en la base de datos", "Mensaje de error", JOptionPane.ERROR_MESSAGE);
                    dlgAgregar.setVisible(true);
                }
            }   
            else
            {
                dlgAgregar.setVisible(false);
                JOptionPane.showMessageDialog(null, "El número de piezas y Kg debe de ser mayor a 0", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
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
    
    
    //Método qque realiza una baja de producto de saldo y da un alta de producto de saldo para trabajar
    public void realizarSalida() throws Exception
    {
        try 
        {
            if (Integer.parseInt(txtNoPiezasCueroSalida.getText())<=ps.getNoPiezasActuales())
            {
                if (Integer.parseInt(txtNoPiezasCueroSalida.getText())==0)
                {
                    dlgSalidaSaldos.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad debe ser mayor a 0","Error",JOptionPane.WARNING_MESSAGE);
                    dlgSalidaSaldos.setVisible(true);
                }
                else
                {
                    if (Double.parseDouble(txtKgSalida.getText())<=ps.getPeso())
                    {
                        if (Double.parseDouble(txtKgSalida.getText())==0 || Double.parseDouble(txtKgSalida.getText())<1)
                        {
                            dlgSalidaSaldos.setVisible(false);
                            JOptionPane.showMessageDialog(null, "Cantidad debe piezas debe ser mayor a 0","Error",JOptionPane.WARNING_MESSAGE);
                            dlgSalidaSaldos.setVisible(true);
                        }
                        else
                        {
                            ps.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroSalida.getText()));
                            ps.setPeso(Double.parseDouble(txtKgSalida.getText()));

                            psc.salirProductoSaldo(ps);

                            sttc=new SaldoTerminadoTrabajarCommands();
                            stt=new SaldoTerminadoTrabajar();

                            stt.setTipoProducto(txtTipoProductoSalida.getText());
                            stt.setCalibre(txtCalibreSalida.getText());
                            stt.setSeleccion(txtSeleccionSalida.getText());
                            stt.setPeso(Double.parseDouble(txtKgSalida.getText()));
                            stt.setNoPiezas(Integer.parseInt(txtNoPiezasCueroSalida.getText()));
                            stt.setNoPiezasActuales(Integer.parseInt(txtNoPiezasCueroSalida.getText()));
                            stt.setDescripcion(txtDescripcionSalida.getText());
                            stt.setFecha(FrmPrincipal.lblFecha.getText());

                            sttc.agregarSaldoTrabajar(stt);
                            dlgSalidaSaldos.setVisible(false);
                            JOptionPane.showMessageDialog(null, "Salida realizada con éxito");
                            actualizarTablaProductoSaldo();
                        }
                    }
                    else
                    {
                        dlgSalidaSaldos.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Cantidad de KG insuficiente en inventario","Error",JOptionPane.WARNING_MESSAGE);
                        dlgSalidaSaldos.setVisible(true);
                    }
                }
            }
            else
            {
                dlgSalidaSaldos.setVisible(false);
                JOptionPane.showMessageDialog(null, "Cantidad insuficiente de piezas en inventario","Error",JOptionPane.WARNING_MESSAGE);
                dlgSalidaSaldos.setVisible(true);
            }
        }
        catch (Exception e) 
        {
            dlgSalidaSaldos.setVisible(false);
            JOptionPane.showMessageDialog(null, "Ingrese cantidad de No. piezas y Kg","Error",JOptionPane.WARNING_MESSAGE);
            dlgSalidaSaldos.setVisible(true);
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
        jSeparator5 = new javax.swing.JSeparator();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtNoPiezasCueroAgregar = new javax.swing.JTextField();
        cmbCalibreAgregar = new javax.swing.JComboBox();
        jLabel93 = new javax.swing.JLabel();
        txtDescripcionAgregar = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtKgAgregar = new javax.swing.JTextField();
        cmbTipoProductoAgregar = new javax.swing.JComboBox();
        jLabel52 = new javax.swing.JLabel();
        cmbSeleccionAgregar = new javax.swing.JComboBox();
        dlgSalidaSaldos = new javax.swing.JDialog();
        jLabel84 = new javax.swing.JLabel();
        btnRealizarsalida = new javax.swing.JButton();
        btnCancelarSalida = new javax.swing.JButton();
        lblErrorSalida = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtCalibreSalida = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        txtNoPiezasCueroSalida = new javax.swing.JTextField();
        txtTipoProductoSalida = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        txtSeleccionSalida = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        txtKgSalida = new javax.swing.JTextField();
        txtDescripcionSalida = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductoSaldo = new javax.swing.JTable();
        jToolBar6 = new javax.swing.JToolBar();
        btnAgregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnBaja = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        cmbTipoEntradaProducto = new javax.swing.JComboBox();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        cmbCalibre = new javax.swing.JComboBox();
        jLabel72 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        cmbSeleccion = new javax.swing.JComboBox();
        jLabel73 = new javax.swing.JLabel();
        jToolBar7 = new javax.swing.JToolBar();
        jLabel21 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jrFiltroFechas = new javax.swing.JRadioButton();
        jLabel74 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JToolBar.Separator();
        jLabel75 = new javax.swing.JLabel();
        dcFecha1 = new datechooser.beans.DateChooserCombo();
        lbl5 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        dcFecha2 = new datechooser.beans.DateChooserCombo();
        lbl6 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JToolBar.Separator();
        btnBuscar = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JToolBar.Separator();
        btnReporteInventario = new javax.swing.JButton();
        jLabel96 = new javax.swing.JLabel();
        btnReporteEntradas = new javax.swing.JButton();
        jLabel98 = new javax.swing.JLabel();
        btnReporteSalidas = new javax.swing.JButton();
        jLabel77 = new javax.swing.JLabel();

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

        jLabel50.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel50.setText("Tipo de producto:");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setText("Calibre:");

        jLabel56.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel56.setText("No. Piezas:");

        txtNoPiezasCueroAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroAgregarKeyTyped(evt);
            }
        });

        cmbCalibreAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel93.setText("Descripción:");

        txtDescripcionAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionAgregarActionPerformed(evt);
            }
        });
        txtDescripcionAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionAgregarKeyTyped(evt);
            }
        });

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setText("Kg:");

        txtKgAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgAgregar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgAgregarKeyTyped(evt);
            }
        });

        cmbTipoProductoAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel52.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel52.setText("Selección:");

        cmbSeleccionAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbSeleccionAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSeleccionAgregarActionPerformed(evt);
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
                .addContainerGap(160, Short.MAX_VALUE)
                .addComponent(btnRealizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregar)
                .addContainerGap())
            .addComponent(lblErrorAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel50)
                            .addComponent(jLabel51)
                            .addComponent(jLabel93)
                            .addComponent(jLabel52))
                        .addGap(18, 18, 18)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDescripcionAgregar)
                            .addComponent(cmbCalibreAgregar, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbTipoProductoAgregar, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dlgAgregarLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel56)
                            .addComponent(jLabel57))
                        .addGap(18, 18, 18)
                        .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNoPiezasCueroAgregar)
                            .addComponent(txtKgAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dlgAgregarLayout.setVerticalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbTipoProductoAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(cmbCalibreAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSeleccionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(txtNoPiezasCueroAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtKgAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(txtDescripcionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblErrorAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarEntrada)
                    .addComponent(btnCancelarAgregar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        jLabel84.setText("Salida de saldos");
        jLabel84.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnRealizarsalida.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnRealizarsalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/accept_1.png"))); // NOI18N
        btnRealizarsalida.setText("Aceptar");
        btnRealizarsalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarsalidaActionPerformed(evt);
            }
        });

        btnCancelarSalida.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancelarSalida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/cross.png"))); // NOI18N
        btnCancelarSalida.setText("Cancelar");
        btnCancelarSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarSalidaActionPerformed(evt);
            }
        });

        lblErrorSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblErrorSalida.setForeground(new java.awt.Color(204, 0, 0));
        lblErrorSalida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblErrorSalida.setText("error");

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel85.setText("Descripción:");

        jLabel86.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel86.setText("Tipo de producto:");

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel87.setText("Calibre:");

        txtCalibreSalida.setEditable(false);
        txtCalibreSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCalibreSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCalibreSalidaActionPerformed(evt);
            }
        });
        txtCalibreSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCalibreSalidaKeyTyped(evt);
            }
        });

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel88.setText("No. Piezas:");

        txtNoPiezasCueroSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtNoPiezasCueroSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNoPiezasCueroSalidaKeyTyped(evt);
            }
        });

        txtTipoProductoSalida.setEditable(false);
        txtTipoProductoSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtTipoProductoSalida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipoProductoSalidaActionPerformed(evt);
            }
        });
        txtTipoProductoSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTipoProductoSalidaKeyTyped(evt);
            }
        });

        jLabel89.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel89.setText("Selección:");

        txtSeleccionSalida.setEditable(false);
        txtSeleccionSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtSeleccionSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSeleccionSalidaKeyTyped(evt);
            }
        });

        jLabel90.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel90.setText("Kg:");

        txtKgSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtKgSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKgSalidaKeyTyped(evt);
            }
        });

        txtDescripcionSalida.setEditable(false);
        txtDescripcionSalida.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDescripcionSalida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionSalidaKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout dlgSalidaSaldosLayout = new javax.swing.GroupLayout(dlgSalidaSaldos.getContentPane());
        dlgSalidaSaldos.getContentPane().setLayout(dlgSalidaSaldosLayout);
        dlgSalidaSaldosLayout.setHorizontalGroup(
            dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSalidaSaldosLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator20)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgSalidaSaldosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRealizarsalida, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarSalida)
                .addContainerGap())
            .addComponent(lblErrorSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgSalidaSaldosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(dlgSalidaSaldosLayout.createSequentialGroup()
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDescripcionSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dlgSalidaSaldosLayout.createSequentialGroup()
                        .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel88)
                            .addComponent(jLabel86)
                            .addComponent(jLabel87)
                            .addComponent(jLabel89)
                            .addComponent(jLabel90))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtKgSalida)
                            .addComponent(txtNoPiezasCueroSalida, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCalibreSalida, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTipoProductoSalida, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSeleccionSalida, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(117, 117, 117))
        );
        dlgSalidaSaldosLayout.setVerticalGroup(
            dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSalidaSaldosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(txtTipoProductoSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(txtCalibreSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(txtSeleccionSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(txtKgSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel88)
                    .addComponent(txtNoPiezasCueroSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(txtDescripcionSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblErrorSalida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaSaldosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarsalida)
                    .addComponent(btnCancelarSalida))
                .addContainerGap())
        );

        tblProductoSaldo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblProductoSaldo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblProductoSaldo);

        jToolBar6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar6.setFloatable(false);
        jToolBar6.setRollover(true);

        btnAgregar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/add.png"))); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.setFocusable(false);
        btnAgregar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAgregar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jToolBar6.add(btnAgregar);

        jLabel7.setText("   ");
        jToolBar6.add(jLabel7);

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

        jLabel9.setText("   ");
        jToolBar6.add(jLabel9);

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel39.setText("Tipo producto:");
        jToolBar6.add(jLabel39);

        jLabel69.setText("   ");
        jToolBar6.add(jLabel69);

        cmbTipoEntradaProducto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        cmbTipoEntradaProducto.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbTipoEntradaProducto.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbTipoEntradaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoEntradaProductoActionPerformed(evt);
            }
        });
        jToolBar6.add(cmbTipoEntradaProducto);

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

        cmbCalibre.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        cmbCalibre.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbCalibre.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbCalibre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCalibreActionPerformed(evt);
            }
        });
        jToolBar6.add(cmbCalibre);

        jLabel72.setText("   ");
        jToolBar6.add(jLabel72);

        jLabel79.setText("   ");
        jToolBar6.add(jLabel79);

        jLabel80.setText("   ");
        jToolBar6.add(jLabel80);

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel45.setText("Selección:");
        jToolBar6.add(jLabel45);

        jLabel97.setText("   ");
        jToolBar6.add(jLabel97);

        cmbSeleccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "..." }));
        cmbSeleccion.setMinimumSize(new java.awt.Dimension(100, 20));
        cmbSeleccion.setPreferredSize(new java.awt.Dimension(120, 25));
        cmbSeleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSeleccionActionPerformed(evt);
            }
        });
        jToolBar6.add(cmbSeleccion);

        jLabel73.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(227, 222, 222));
        jLabel73.setText("                                             ");
        jToolBar6.add(jLabel73);

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
        jToolBar7.add(jrFiltroFechas);

        jLabel74.setText("   ");
        jToolBar7.add(jLabel74);
        jToolBar7.add(jSeparator16);

        jLabel75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel75.setText("De:");
        jToolBar7.add(jLabel75);

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
    jToolBar7.add(dcFecha1);

    lbl5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl5.setForeground(new java.awt.Color(227, 222, 222));
    lbl5.setText("     ");
    jToolBar7.add(lbl5);

    jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel76.setText("Hasta:");
    jToolBar7.add(jLabel76);

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
    jToolBar7.add(dcFecha2);

    lbl6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl6.setForeground(new java.awt.Color(227, 222, 222));
    lbl6.setText("     ");
    jToolBar7.add(lbl6);
    jToolBar7.add(jSeparator17);

    btnBuscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/magnifier.png"))); // NOI18N
    btnBuscar.setText("Buscar");
    btnBuscar.setFocusable(false);
    btnBuscar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnBuscar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnBuscar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnBuscar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnBuscarActionPerformed(evt);
        }
    });
    jToolBar7.add(btnBuscar);
    jToolBar7.add(jSeparator18);

    btnReporteInventario.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteInventario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteInventario.setText("Reporte Inventario");
    btnReporteInventario.setFocusable(false);
    btnReporteInventario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteInventario.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteInventario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteInventario.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteInventarioActionPerformed(evt);
        }
    });
    jToolBar7.add(btnReporteInventario);

    jLabel96.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel96.setForeground(new java.awt.Color(227, 222, 222));
    jLabel96.setText("  ");
    jToolBar7.add(jLabel96);

    btnReporteEntradas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteEntradas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteEntradas.setText("Reporte entradas");
    btnReporteEntradas.setFocusable(false);
    btnReporteEntradas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    btnReporteEntradas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteEntradas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteEntradas.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteEntradasActionPerformed(evt);
        }
    });
    jToolBar7.add(btnReporteEntradas);

    jLabel98.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel98.setForeground(new java.awt.Color(227, 222, 222));
    jLabel98.setText("  ");
    jToolBar7.add(jLabel98);

    btnReporteSalidas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    btnReporteSalidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/printer.png"))); // NOI18N
    btnReporteSalidas.setText("Reporte salidas");
    btnReporteSalidas.setFocusable(false);
    btnReporteSalidas.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
    btnReporteSalidas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnReporteSalidas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    btnReporteSalidas.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnReporteSalidasActionPerformed(evt);
        }
    });
    jToolBar7.add(btnReporteSalidas);

    jLabel77.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel77.setForeground(new java.awt.Color(227, 222, 222));
    jLabel77.setText("                                                                                                            ");
    jToolBar7.add(jLabel77);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar6, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
        .addComponent(jToolBar7, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
        .addComponent(jScrollPane1)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jToolBar7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
    );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        try {
            abrirDialogoAgregar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error de conexión a base de datos","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        try {
            abrirDialogoSalida();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error de conexión a base de datos","Error",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(PnlProductoProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBajaActionPerformed

    private void cmbTipoEntradaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoEntradaProductoActionPerformed
        actualizarTablaProductoSaldo();
    }//GEN-LAST:event_cmbTipoEntradaProductoActionPerformed

    private void cmbCalibreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCalibreActionPerformed
        actualizarTablaProductoSaldo();
    }//GEN-LAST:event_cmbCalibreActionPerformed

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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        actualizarTablaProductoSaldo();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnReporteInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteInventarioActionPerformed
        actualizarTablaProductoSaldo();
        try {
            psc.mostrarReporteInventarioSaldo(ps);
        } catch (Exception ex) {
            Logger.getLogger(PnlSaldos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteInventarioActionPerformed

    private void btnReporteEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradasActionPerformed
        
    }//GEN-LAST:event_btnReporteEntradasActionPerformed

    private void btnReporteSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteSalidasActionPerformed
        
    }//GEN-LAST:event_btnReporteSalidasActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        relizarEntradaSaldo();
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void btnCancelarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarActionPerformed
        dlgAgregar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarActionPerformed

    private void txtNoPiezasCueroAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroAgregarKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasCueroAgregarKeyTyped

    private void txtDescripcionAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionAgregarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionAgregarKeyTyped

    private void txtDescripcionAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionAgregarActionPerformed

    private void txtKgAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgAgregarKeyTyped
        char c;
        c=evt.getKeyChar();    
        int punto=txtKgAgregar.getText().indexOf(".")+1;
        
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
    }//GEN-LAST:event_txtKgAgregarKeyTyped

    private void btnRealizarsalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarsalidaActionPerformed
        try {
            realizarSalida();
        } catch (Exception ex) {
            Logger.getLogger(PnlSaldos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarsalidaActionPerformed

    private void btnCancelarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarSalidaActionPerformed
        dlgSalidaSaldos.setVisible(false);
    }//GEN-LAST:event_btnCancelarSalidaActionPerformed

    private void txtCalibreSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCalibreSalidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibreSalidaActionPerformed

    private void txtCalibreSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCalibreSalidaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCalibreSalidaKeyTyped

    private void txtNoPiezasCueroSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoPiezasCueroSalidaKeyTyped
        char c;
        c=evt.getKeyChar();

        if (!Character.isDigit(c)  && c!=KeyEvent.VK_BACK_SPACE)
        {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_txtNoPiezasCueroSalidaKeyTyped

    private void txtSeleccionSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeleccionSalidaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSeleccionSalidaKeyTyped

    private void txtKgSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKgSalidaKeyTyped
        char c;
        c=evt.getKeyChar();    
        int punto=txtKgSalida.getText().indexOf(".")+1;
        
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
    }//GEN-LAST:event_txtKgSalidaKeyTyped

    private void txtDescripcionSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionSalidaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionSalidaKeyTyped

    private void txtTipoProductoSalidaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTipoProductoSalidaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoProductoSalidaKeyTyped

    private void txtTipoProductoSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipoProductoSalidaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoProductoSalidaActionPerformed

    private void cmbSeleccionAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbSeleccionAgregarActionPerformed

    private void cmbSeleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSeleccionActionPerformed
        actualizarTablaProductoSaldo();
    }//GEN-LAST:event_cmbSeleccionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAgregar;
    public javax.swing.JButton btnBaja;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelarAgregar;
    private javax.swing.JButton btnCancelarSalida;
    private javax.swing.JButton btnRealizarEntrada;
    private javax.swing.JButton btnRealizarsalida;
    private javax.swing.JButton btnReporteEntradas;
    private javax.swing.JButton btnReporteInventario;
    private javax.swing.JButton btnReporteSalidas;
    private javax.swing.JComboBox cmbCalibre;
    private javax.swing.JComboBox cmbCalibreAgregar;
    private javax.swing.JComboBox cmbSeleccion;
    private javax.swing.JComboBox cmbSeleccionAgregar;
    private javax.swing.JComboBox cmbTipoEntradaProducto;
    private javax.swing.JComboBox cmbTipoProductoAgregar;
    private datechooser.beans.DateChooserCombo dcFecha1;
    private datechooser.beans.DateChooserCombo dcFecha2;
    private javax.swing.JDialog dlgAgregar;
    private javax.swing.JDialog dlgSalidaSaldos;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator17;
    private javax.swing.JToolBar.Separator jSeparator18;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JRadioButton jrFiltroFechas;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lblErrorAgregar;
    private javax.swing.JLabel lblErrorSalida;
    private javax.swing.JTable tblProductoSaldo;
    private javax.swing.JTextField txtCalibreSalida;
    private javax.swing.JTextField txtDescripcionAgregar;
    private javax.swing.JTextField txtDescripcionSalida;
    private javax.swing.JTextField txtKgAgregar;
    private javax.swing.JTextField txtKgSalida;
    private javax.swing.JTextField txtNoPiezasCueroAgregar;
    private javax.swing.JTextField txtNoPiezasCueroSalida;
    private javax.swing.JTextField txtSeleccionSalida;
    private javax.swing.JTextField txtTipoProductoSalida;
    // End of variables declaration//GEN-END:variables
}
