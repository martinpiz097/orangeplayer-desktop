//package org.orangeplayer.playerdesktop.gui2.components;
//
//public class JToastObserver extends Thread {
//
//    private JToast toast;
//    private int duration;
//
//    public JToastObserver(JToast toast, int duration) {
//        this.toast = toast;
//        this.duration = duration;
//        setName(getClass().getSimpleName()+" "+getId());
//    }
//
//    @Override
//    public void run() {
//        while (!toast.isVisible());
//        try {
//            Thread.sleep(duration*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        toast.dispose();
//    }
//}
