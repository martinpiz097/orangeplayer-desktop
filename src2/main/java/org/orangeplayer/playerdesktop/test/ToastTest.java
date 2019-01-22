///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.orangeplayer.playerdesktop.test;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
///**
// *
// * @author martin
// */
//public class Test {
//    private JPanel contentPane;
//
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    Test frame = new Test();
//                    frame.setVisible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
//                ToastMessage toastMessage = new ToastMessage("Sample text to toast ",3000);
//                toastMessage.setVisible(true);
//            }
//        });
//        contentPane.add(btnTestToast, BorderLayout.SOUTH);
//    }
//
//}
//
//public class ToastMessage extends JDialog {
//    int miliseconds;
//    public ToastMessage(String toastString, int time) {
//        this.miliseconds = time;
//        setUndecorated(true);
//        getContentPane().setLayout(new BorderLayout(0, 0));
//
//        JPanel panel = new JPanel();
//        panel.setBackground(Color.GRAY);
//        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
//        getContentPane().add(panel, BorderLayout.CENTER);
//
//        JLabel toastLabel = new JLabel("");
//        toastLabel.setText(toastString);
//        toastLabel.setFont(new Font("Dialog", Font.BOLD, 12));
//        toastLabel.setForeground(Color.WHITE);
//
//        setBounds(100, 100, toastLabel.getPreferredSize().width+20, 31);
//
//
//        setAlwaysOnTop(true);
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        int y = dim.height/2-getSize().height/2;
//        int half = y/2;
//        setLocation(dim.width/2-getSize().width/2, y+half);
//        panel.add(toastLabel);
//        setVisible(false);
//
//        new Thread(){
//            public void run() {
//                try {
//                    Thread.sleep(miliseconds);
//                    dispose();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//}
//
//
//
