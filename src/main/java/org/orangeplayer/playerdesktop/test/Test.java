///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.orangeplayer.playerdesktop.test;
//
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.border.LineBorder;
//import org.orangeplayer.playerdesktop.gui.util.ToastMessage;
//
///**
// *
// * @author martin
// */
//public class Test extends JFrame {
//
//    private JPanel contentPane;
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(() -> {
//            try {
//                Test frame = new Test();
//                frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public Test() {
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setBounds(100, 100, 450, 300);
//        contentPane = new JPanel();
//        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//        contentPane.setLayout(new BorderLayout(0, 0));
//        setContentPane(contentPane);
//
//        JButton btnTestToast = new JButton("Test Toast");
//        btnTestToast.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                ToastMessage toastMessage = new ToastMessage("Sample text to toast ", 3000);
//                toastMessage.setVisible(true);
//            }
//        });
//        contentPane.add(btnTestToast, BorderLayout.SOUTH);
//    }
//
//    
//
//}
