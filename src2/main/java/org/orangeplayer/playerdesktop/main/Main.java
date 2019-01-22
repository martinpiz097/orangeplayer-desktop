//package org.orangeplayer.playerdesktop.main;
//
//import org.orangeplayer.playerdesktop.gui.PlayerForm;
//
//import java.awt.*;
//import java.util.concurrent.atomic.AtomicReference;
//
//public class Main {
//    public static void main(String[] args) throws InterruptedException {
//        //startApp();
//        EventQueue.invokeLater(()->
//                new org.orangeplayer.playerdesktop.gui.PlayerForm().setVisible(true)
//        );
//    }
//
//    private static void startApp() throws InterruptedException {
//        AtomicReference<PlayerForm> formReference = new AtomicReference<>();
//        SplashScreen screen = new SplashScreen(new PlayerForm());
//        EventQueue.invokeLater(()-> screen.setVisible(true));
//        EventQueue.invokeLater(()-> formReference.set(new PlayerForm()));
//        EventQueue.invokeLater(()-> formReference.get().setVisible(true));
//        Thread.sleep(500);
//        EventQueue.invokeLater(()-> screen.setVisible(false));
//
//    }
//}
