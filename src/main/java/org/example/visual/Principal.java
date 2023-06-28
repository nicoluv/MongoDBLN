package org.example.visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Principal extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton movimientoInventarioButton;
    private JTable table1;
    private JButton ordenarButton;
    private JButton componentesButton;
    private JButton registrarComponenteButton;
    private JLabel jpic;

    public Principal() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);



        movimientoInventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moverInventario movimiento = new moverInventario();
                movimiento.setVisible(true);
                movimiento.setSize(500,400);


            }
        });

        componentesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListadoComponentes componentes = new ListadoComponentes();
                componentes.setVisible(true);
                componentes.pack();

            }
        });

        registrarComponenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrarComponente aux = new RegistrarComponente();
                aux.setVisible(true);
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

    }



    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Principal dialog = new Principal();
        dialog.pack();
        dialog.setVisible(true);
        dialog.setSize(500,400);
        System.exit(0);

      //  JButton movimientoInventarioButton = new JButton();


    }

}
