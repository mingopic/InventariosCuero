/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Controlador.CueroTrabajarCommands;
import Controlador.PedaceraCommands;
import Controlador.SaldoTerminadoTrabajarCommands;
import Modelo.Pedacera;
import Modelo.ProductoSaldo;
import Modelo.SaldoTerminadoTrabajar;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Domingo Luna
 */
public class PnlPedacera extends javax.swing.JPanel {
    Pedacera p=new Pedacera();
    PedaceraCommands pc=new PedaceraCommands();
    FrmPrincipal frmPrincipal;
    
    //Variable para nombrar las columnas de la tabla que carga el listado de las entradas realizadas
    String[] cols = new String[]
    {
        "ID Pedacera","Peso inicial (KG)","Peso actual (KG)","Descripción","Fecha de entrada"
    };
    /**
     * Creates new form PnlPedacera
     */
    public PnlPedacera() throws Exception {
        initComponents();
        llenarTabla();
        dcFecha1.setEnabled(false);
        dcFecha2.setEnabled(false);
        jrFiltroFechas.setSelected(false);
    }
    
    
    //Método utilizado para llenar la tabla tblPedacera
    public void llenarTabla() throws Exception
    {
        String[][] datos;
        DefaultTableModel dtm;
        
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
                            
                    p.setFecha(fechaAux);
                }
            catch (Exception ex) 
                {
                    p.setFecha("0");
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
                            
                    p.setFecha1(fechaAux);
                }
            catch (Exception ex) 
                {
                    p.setFecha1("0");
                }
        }
        else
        {
            p.setFecha("1900-01-01");
            p.setFecha1("2040-01-01");
        }
        
        p.setDescripcion("");
        
        try
        {
            datos=pc.obtenerDatosPedacera(p);
            dtm=new DefaultTableModel(datos, cols){
            public boolean isCellEditable(int row, int column) {
            return false;
            }
            };
            tblPedacera.setModel(dtm);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al recuperar datos de la BD","Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    //Método que abre el dialogo para agregar un entrada de Pedacera
    public void abrirDialogoAgregar()
    {
        txtKgAgregar.setText("");
        txtDescripcionAgregar.setText("");
        lblErrorAgregar.setText("");
        
        dlgAgregar.setSize(450, 220);
        dlgAgregar.setPreferredSize(dlgAgregar.getSize());
        dlgAgregar.setLocationRelativeTo(null);
        dlgAgregar.setAlwaysOnTop(true);
        dlgAgregar.setVisible(true);
    }
    
    
    //Método que realiza una entrada de pedacera y cierra el dialogo dlgAgregar
    public void realizarEntrada() throws Exception
    {
        if (!txtKgAgregar.getText().equals("") && Double.parseDouble(txtKgAgregar.getText())>0)
        {
            p.setPeso(Double.parseDouble(txtKgAgregar.getText()));
            p.setPesoActual(Double.parseDouble(txtKgAgregar.getText()));
            p.setDescripcion(txtDescripcionAgregar.getText());
            p.setFecha(frmPrincipal.lblFecha.getText());
            
            pc.agregarProductoSaldo(p);
            dlgAgregar.setVisible(false);
        }
        else
        {
            dlgAgregar.setVisible(false);
            JOptionPane.showMessageDialog(null, "Peso debe de sar mayor a 0","Error", JOptionPane.ERROR_MESSAGE);
            dlgAgregar.setVisible(true);
        }
    }
    
    
    //Método para validar que se selecciono un producto de la lista, se usa en dldSalidaPedacera
    public void abrirDialogoSalida() throws Exception
    {
        int renglonSeleccionado = tblPedacera.getSelectedRow();
        if (renglonSeleccionado!=-1) 
        {
            String[] datos=null;
            p=new Pedacera();
            
            p.setIdPedacera(Integer.parseInt(tblPedacera.getValueAt(renglonSeleccionado, 0).toString()));
            p.setPesoActual(Double.parseDouble(tblPedacera.getValueAt(renglonSeleccionado, 2).toString()));
            p.setDescripcion(tblPedacera.getValueAt(renglonSeleccionado, 3).toString());
            
            txtIdPedacera.setText(String.valueOf(p.getIdPedacera()));
            txtKgSalida.setText(String.valueOf(p.getPesoActual()));
            txtDescripcionSalida.setText(p.getDescripcion());
            lblErrorSalida.setText("");
            
            dlgSalidaPedacera.setSize(430, 270);
            dlgSalidaPedacera.setPreferredSize(dlgSalidaPedacera.getSize());
            dlgSalidaPedacera.setLocationRelativeTo(null);
            dlgSalidaPedacera.setAlwaysOnTop(true);
            dlgSalidaPedacera.setVisible(true);
        }
        else 
        {
            JOptionPane.showMessageDialog(null, "Seleccione una entrada de la lista", "Mensaje de advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    //Método qque realiza una baja de pedacera y da un alta de pedacera para trabajar
    public void realizarSalida() throws Exception
    {
        try
        {
            if (Double.parseDouble(txtKgSalida.getText())<=p.getPesoActual())
            {
                if (Double.parseDouble(txtKgSalida.getText())==0)
                {
                    dlgSalidaPedacera.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Cantidad debe ser mayor a 0","Error",JOptionPane.WARNING_MESSAGE);
                    dlgSalidaPedacera.setVisible(true);
                }
                else
                {
                    p.setIdPedacera(Integer.parseInt(txtIdPedacera.getText()));
                    p.setPesoActual(Double.parseDouble(txtKgSalida.getText()));
                    pc.salirPedacera(p);
                    dlgSalidaPedacera.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Salida realizada con éxito");
                    llenarTabla();
                }
            }
            else
            {
                dlgSalidaPedacera.setVisible(false);
                JOptionPane.showMessageDialog(null, "Cantidad insuficiente de Kg en inventario","Error",JOptionPane.WARNING_MESSAGE);
                dlgSalidaPedacera.setVisible(true);
            }
        }
        catch (Exception e) 
        {
            dlgSalidaPedacera.setVisible(false);
            JOptionPane.showMessageDialog(null, "Ingrese cantidad Kg","Error",JOptionPane.WARNING_MESSAGE);
            dlgSalidaPedacera.setVisible(true);
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
        jLabel93 = new javax.swing.JLabel();
        txtDescripcionAgregar = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtKgAgregar = new javax.swing.JTextField();
        dlgSalidaPedacera = new javax.swing.JDialog();
        jLabel84 = new javax.swing.JLabel();
        btnRealizarsalida = new javax.swing.JButton();
        btnCancelarSalida = new javax.swing.JButton();
        lblErrorSalida = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        jLabel86 = new javax.swing.JLabel();
        txtIdPedacera = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        txtKgSalida = new javax.swing.JTextField();
        txtDescripcionSalida = new javax.swing.JTextField();
        jToolBar6 = new javax.swing.JToolBar();
        btnAgregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnBaja = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jrFiltroFechas = new javax.swing.JRadioButton();
        jLabel48 = new javax.swing.JLabel();
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
        jLabel98 = new javax.swing.JLabel();
        btnReporteInventario = new javax.swing.JButton();
        jLabel96 = new javax.swing.JLabel();
        btnReporteEntradas = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        btnReporteSalidas = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPedacera = new javax.swing.JTable();

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

        javax.swing.GroupLayout dlgAgregarLayout = new javax.swing.GroupLayout(dlgAgregar.getContentPane());
        dlgAgregar.getContentPane().setLayout(dlgAgregarLayout);
        dlgAgregarLayout.setHorizontalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator5)
            .addComponent(lblErrorAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel57)
                    .addComponent(jLabel93))
                .addGap(18, 18, 18)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtDescripcionAgregar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKgAgregar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addContainerGap(86, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgAgregarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRealizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelarAgregar)
                .addContainerGap())
        );
        dlgAgregarLayout.setVerticalGroup(
            dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgAgregarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(txtKgAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgAgregarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel93)
                    .addComponent(txtDescripcionAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblErrorAgregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        jLabel86.setText("ID Pedacera:");

        txtIdPedacera.setEditable(false);
        txtIdPedacera.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtIdPedacera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdPedaceraActionPerformed(evt);
            }
        });
        txtIdPedacera.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdPedaceraKeyTyped(evt);
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

        javax.swing.GroupLayout dlgSalidaPedaceraLayout = new javax.swing.GroupLayout(dlgSalidaPedacera.getContentPane());
        dlgSalidaPedacera.getContentPane().setLayout(dlgSalidaPedaceraLayout);
        dlgSalidaPedaceraLayout.setHorizontalGroup(
            dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSalidaPedaceraLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addComponent(jSeparator20)
            .addComponent(lblErrorSalida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgSalidaPedaceraLayout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgSalidaPedaceraLayout.createSequentialGroup()
                        .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(dlgSalidaPedaceraLayout.createSequentialGroup()
                                .addComponent(jLabel85)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDescripcionSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dlgSalidaPedaceraLayout.createSequentialGroup()
                                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel86)
                                    .addComponent(jLabel90))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtKgSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtIdPedacera, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(117, 117, 117))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dlgSalidaPedaceraLayout.createSequentialGroup()
                        .addComponent(btnRealizarsalida, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelarSalida)
                        .addContainerGap())))
        );
        dlgSalidaPedaceraLayout.setVerticalGroup(
            dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgSalidaPedaceraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(txtIdPedacera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(txtKgSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(txtDescripcionSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblErrorSalida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dlgSalidaPedaceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarsalida)
                    .addComponent(btnCancelarSalida))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        jrFiltroFechas.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrFiltroFechas.setFocusable(false);
        jrFiltroFechas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jrFiltroFechas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jrFiltroFechas.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechas.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jrFiltroFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrFiltroFechasActionPerformed(evt);
            }
        });
        jToolBar6.add(jrFiltroFechas);

        jLabel48.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ima/calendar.png"))); // NOI18N
        jToolBar6.add(jLabel48);

        jLabel74.setText("   ");
        jToolBar6.add(jLabel74);
        jToolBar6.add(jSeparator16);

        jLabel75.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel75.setText("De:");
        jToolBar6.add(jLabel75);

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
    jToolBar6.add(dcFecha1);

    lbl5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl5.setForeground(new java.awt.Color(227, 222, 222));
    lbl5.setText("     ");
    jToolBar6.add(lbl5);

    jLabel76.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel76.setText("Hasta:");
    jToolBar6.add(jLabel76);

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
    jToolBar6.add(dcFecha2);

    lbl6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    lbl6.setForeground(new java.awt.Color(227, 222, 222));
    lbl6.setText("  ");
    jToolBar6.add(lbl6);
    jToolBar6.add(jSeparator17);

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
    jToolBar6.add(btnBuscar);

    jLabel98.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel98.setForeground(new java.awt.Color(227, 222, 222));
    jLabel98.setText("  ");
    jToolBar6.add(jLabel98);

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
    jToolBar6.add(btnReporteInventario);

    jLabel96.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel96.setForeground(new java.awt.Color(227, 222, 222));
    jLabel96.setText("  ");
    jToolBar6.add(jLabel96);

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
    jToolBar6.add(btnReporteEntradas);

    jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
    jLabel21.setForeground(new java.awt.Color(227, 222, 222));
    jLabel21.setText("  ");
    jToolBar6.add(jLabel21);

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
    jToolBar6.add(btnReporteSalidas);

    tblPedacera.setModel(new javax.swing.table.DefaultTableModel(
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
    jScrollPane1.setViewportView(tblPedacera);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar6, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
        .addComponent(jScrollPane1)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
            .addContainerGap())
    );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        abrirDialogoAgregar();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnBajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBajaActionPerformed
        try {
            abrirDialogoSalida();
        } catch (Exception ex) {
            Logger.getLogger(PnlPedacera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBajaActionPerformed

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
        
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnReporteInventarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteInventarioActionPerformed
        
    }//GEN-LAST:event_btnReporteInventarioActionPerformed

    private void btnReporteEntradasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteEntradasActionPerformed

    }//GEN-LAST:event_btnReporteEntradasActionPerformed

    private void btnReporteSalidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteSalidasActionPerformed

    }//GEN-LAST:event_btnReporteSalidasActionPerformed

    private void btnRealizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarEntradaActionPerformed
        try {
            realizarEntrada();
            llenarTabla();
        } catch (Exception ex) {
            Logger.getLogger(PnlPedacera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarEntradaActionPerformed

    private void btnCancelarAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarAgregarActionPerformed
        dlgAgregar.setVisible(false);
    }//GEN-LAST:event_btnCancelarAgregarActionPerformed

    private void txtDescripcionAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionAgregarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionAgregarActionPerformed

    private void txtDescripcionAgregarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionAgregarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionAgregarKeyTyped

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
            Logger.getLogger(PnlPedacera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRealizarsalidaActionPerformed

    private void btnCancelarSalidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarSalidaActionPerformed
        dlgSalidaPedacera.setVisible(false);
    }//GEN-LAST:event_btnCancelarSalidaActionPerformed

    private void txtIdPedaceraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdPedaceraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdPedaceraActionPerformed

    private void txtIdPedaceraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdPedaceraKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdPedaceraKeyTyped

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
    private datechooser.beans.DateChooserCombo dcFecha1;
    private datechooser.beans.DateChooserCombo dcFecha2;
    private javax.swing.JDialog dlgAgregar;
    private javax.swing.JDialog dlgSalidaPedacera;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator17;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JRadioButton jrFiltroFechas;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lblErrorAgregar;
    private javax.swing.JLabel lblErrorSalida;
    private javax.swing.JTable tblPedacera;
    private javax.swing.JTextField txtDescripcionAgregar;
    private javax.swing.JTextField txtDescripcionSalida;
    private javax.swing.JTextField txtIdPedacera;
    private javax.swing.JTextField txtKgAgregar;
    private javax.swing.JTextField txtKgSalida;
    // End of variables declaration//GEN-END:variables
}
