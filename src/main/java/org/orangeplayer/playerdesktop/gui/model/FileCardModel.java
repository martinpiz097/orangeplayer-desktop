/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orangeplayer.playerdesktop.gui.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.muplayer.audio.Player;
import org.orangeplayer.playerdesktop.base.PlayerController;
import org.orangeplayer.playerdesktop.gui.PlayerForm;
import org.orangeplayer.playerdesktop.gui.components.FileCard;
import org.orangeplayer.playerdesktop.gui.util.ComponentUtil;
import org.orangeplayer.playerdesktop.sys.Session;
import org.orangeplayer.playerdesktop.sys.SessionKey;

/**
 *
 * @author martin
 */
public class FileCardModel extends CardModel<File, FileCard>{

    public FileCardModel() {
    }

    public FileCardModel(ArrayList<File> listObjects, JPanel panelList) {
        super(listObjects, panelList);
    }

    public FileCardModel(Player player, JPanel panelList) {
        listObjects = new ArrayList<>();
        loadListData(player.getRootFolder());
        configButtons();
        this.panelList = panelList;
    }
    
    public FileCardModel(File rootDir, JPanel panelList) {
        listObjects = new ArrayList<>();
        loadListData(rootDir);
        configButtons();
        this.panelList = panelList;
    }
    
    private void playFolder(File rootDir) {
        if (rootDir != null) {
            // mas adelante reproducir SOLO las canciones de esta carpeta
            // actualizando la lista inicial
            Player player = Session.getInstance().get(SessionKey.CONTROLLER, PlayerController.class)
                    .getPlayer();

            // filtrar por si la carpeta seleccionada no esta vacia
            String[] list = rootDir.list();
            if (list != null) {
                File[] listFiles = rootDir.listFiles((File pathname) -> !pathname.isDirectory());

                // si solo tiene carpetas dentro reproducir la primera, sino reproducir la misma
                if (listFiles == null || listFiles.length == 0)
                    playFolder(new File(list[0]));
                else
                    player.playFolder(rootDir.getPath());
            }
        }
    }
    
    private void configButtons() {
        if (!listObjects.isEmpty()) {
            PlayerForm form = Session.getInstance().get(SessionKey.CONTROLLER, PlayerController.class)
                    .getForm();
            form.getPanelFolderOptions().setVisible(true);
            JButton btnGoBack = form.getBtnGoBack();
            JButton btnPlayFolder = form.getBtnPlayFolder();
            
            ComponentUtil.deleteAllActionListeners(btnGoBack);
            ComponentUtil.deleteAllActionListeners(btnPlayFolder);
            
            btnGoBack.addActionListener((ActionEvent e) -> {
                File rootDir = Session.getInstance().get(SessionKey.FILE_CARD_MODEL_ROOT, File.class);
                if (rootDir != null) {
                    rootDir = rootDir.getParentFile();
                    new FileCardModel(rootDir, panelList).loadSongs();
                }
            });
            
            btnPlayFolder.addActionListener((ActionEvent e) -> {
                File rootDir = Session.getInstance().get(SessionKey.FILE_CARD_MODEL_ROOT, File.class);
                playFolder(rootDir);
            });
            
            form.getTabFolders().updateUI();
        }
    }
    
    private void loadListData(File rootDir) {
        Session.getInstance().set(SessionKey.FILE_CARD_MODEL_ROOT, rootDir);
        final File[] files = rootDir.listFiles();
        
        if (files != null) {
            File file;
            for (int i = 0; i < files.length; i++) {
                file = files[i];
                if (file.isDirectory())
                    listObjects.add(file);
            }
            listObjects.sort(Comparator.naturalOrder());
        }
    }
    
    @Override
    protected FileCard getCardPanel(File element) {
        return new FileCard(element);
    }

    @Override
    protected void configCardEvents(FileCard card) {
        card.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    new FileCardModel(card.getElement(), panelList).loadSongs();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
    
}
