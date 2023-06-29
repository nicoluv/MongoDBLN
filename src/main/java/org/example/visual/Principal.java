package org.example.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

public class Principal extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton movimientoInventarioButton;
    private JTable table1;
    private JButton ordenarButton;
    private JButton componentesButton;
    private JButton registrarComponenteButton;
    private JTextField XYZComponentsTextField;

    public Principal() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        movimientoInventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverInventario movimiento = new moverInventario();
                movimiento.pack();
                movimiento.setSize(new Dimension(800,600));
                movimiento.setVisible(true);


            }
        });

        componentesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadoComponentes componentes = new ListadoComponentes();
                componentes.pack();
                componentes.setSize(new Dimension(800,600));
                componentes.setVisible(true);


            }
        });

        registrarComponenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarComponente reg = null;
                reg = new RegistrarComponente();
                reg.pack();
                reg.setSize(new Dimension(800,600));
                reg.setVisible(true);
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ordenarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarOrden generar = null;
                try {
                    generar = new generarOrden();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                generar.pack();
                generar.setSize(new Dimension(800,600));
                generar.setVisible(true);
            }
        });
    }



    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Principal dialog = new Principal();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);

        //  JButton movimientoInventarioButton = new JButton();


    }

}
