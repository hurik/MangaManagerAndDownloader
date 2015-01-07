/*
 * The MIT License
 *
 * Copyright 2014 Andreas Giemza <andreas@giemza.net>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.andreasgiemza.mangadownloader;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.gui.Controller;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterCheckBoxItemListener;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterListSearchDocumentListener;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterTableCellRenderer;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterTableModel;
import de.andreasgiemza.mangadownloader.gui.manga.MangaListSearchDocumentListener;
import de.andreasgiemza.mangadownloader.gui.manga.MangaListSelectionListener;
import de.andreasgiemza.mangadownloader.gui.manga.MangaTableCellRenderer;
import de.andreasgiemza.mangadownloader.gui.manga.MangaTableModel;
import de.andreasgiemza.mangadownloader.options.Options;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaDownloader extends javax.swing.JFrame {

    private final List<Manga> mangas = new LinkedList<>();
    private final MangaTableModel mangasTableModel = new MangaTableModel(mangas);
    private final List<Chapter> chapters = new LinkedList<>();
    private final ChapterTableModel chaptersTableModel = new ChapterTableModel(chapters);
    private final Controller controller;

    /**
     * Creates new form Gui
     */
    @SuppressWarnings("unchecked")
    public MangaDownloader() {
        initComponents();

        setLocation(new Double((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (this.getWidth() / 2)).intValue(),
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (this.getHeight() / 2)).intValue());

        // Setup controller
        controller = new Controller(
                this,
                sourceComboBox,
                mangaListSearchTextField,
                mangaListTable,
                chapterListSearchTextField,
                chapterDeSelectAllCheckBox,
                chapterListTable,
                downloadButton,
                mangas,
                chapters);

        // Setup Source ComboBox
        List<String> supportedSites = SiteHelper.getSites();
        sourceComboBox.setModel(new DefaultComboBoxModel<>(supportedSites.toArray()));

        if (supportedSites.indexOf(Options.INSTANCE.getSelectedSource()) != -1) {
            sourceComboBox.setSelectedIndex(supportedSites.indexOf(Options.INSTANCE.getSelectedSource()));
        }

        sourceComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controller.loadMangaList();
                }
            }
        });

        // Setup Manga List Table
        mangaListSearchTextField.getDocument().addDocumentListener(
                new MangaListSearchDocumentListener(
                        mangaListSearchTextField,
                        mangaListTable,
                        controller));
        mangaListTable.getSelectionModel().addListSelectionListener(
                new MangaListSelectionListener(controller, mangaListTable));
        mangaListTable.setDefaultRenderer(String.class, new MangaTableCellRenderer());

        // Setup Chapter List Table
        chapterListSearchTextField.getDocument().addDocumentListener(
                new ChapterListSearchDocumentListener(
                        chapterListSearchTextField,
                        chapterListTable,
                        controller));
        chapterListTable.getColumnModel().getColumn(1).setMaxWidth(34);
        chapterListTable.getColumnModel().getColumn(1).setMinWidth(34);
        chapterListTable.setDefaultRenderer(String.class, new ChapterTableCellRenderer());

        // Setup DeSelectAll CheckBox
        chapterDeSelectAllCheckBox.addItemListener(new ChapterCheckBoxItemListener(chapterListTable, controller));

        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                Options.INSTANCE.saveOptions();
            }

        });

        mangasDirTextField.setText(Options.INSTANCE.getMangaDir());

        // Load Manga List
        controller.loadMangaList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mangasDirFileChooser = new javax.swing.JFileChooser();
        mangasDirPanel = new javax.swing.JPanel();
        mangasDirTextField = new javax.swing.JTextField();
        mangasDirButton = new javax.swing.JButton();
        sourcePanel = new javax.swing.JPanel();
        sourceComboBox = new javax.swing.JComboBox();
        sourceButton = new javax.swing.JButton();
        MangaChapterPanel = new javax.swing.JPanel();
        mangaListPanel = new javax.swing.JPanel();
        mangaListSearchLabel = new javax.swing.JLabel();
        mangaListSearchTextField = new javax.swing.JTextField();
        mangaListScrollPane = new javax.swing.JScrollPane();
        mangaListTable = new javax.swing.JTable();
        chapterListPanel = new javax.swing.JPanel();
        chapterListSearchLabel = new javax.swing.JLabel();
        chapterListSearchTextField = new javax.swing.JTextField();
        chapterDeSelectAllCheckBox = new javax.swing.JCheckBox();
        chapterListScrollPane = new javax.swing.JScrollPane();
        chapterListTable = new javax.swing.JTable();
        downloadButton = new javax.swing.JButton();

        mangasDirFileChooser.setDialogTitle("Select a manga directory ...");
        mangasDirFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manga Downloader");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("de/andreasgiemza/mangadownloader/gui/icons/mangadownloader.png")));

        mangasDirPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Mangas Directory"));

        mangasDirTextField.setEditable(false);

        mangasDirButton.setText("Select");
        mangasDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mangasDirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mangasDirPanelLayout = new javax.swing.GroupLayout(mangasDirPanel);
        mangasDirPanel.setLayout(mangasDirPanelLayout);
        mangasDirPanelLayout.setHorizontalGroup(
            mangasDirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mangasDirPanelLayout.createSequentialGroup()
                .addComponent(mangasDirTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mangasDirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mangasDirPanelLayout.setVerticalGroup(
            mangasDirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mangasDirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(mangasDirTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(mangasDirButton))
        );

        sourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Source"));

        sourceComboBox.setMaximumRowCount(25);

        sourceButton.setText("Update");
        sourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout sourcePanelLayout = new javax.swing.GroupLayout(sourcePanel);
        sourcePanel.setLayout(sourcePanelLayout);
        sourcePanelLayout.setHorizontalGroup(
            sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sourcePanelLayout.createSequentialGroup()
                .addComponent(sourceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        sourcePanelLayout.setVerticalGroup(
            sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(sourceButton)
                .addComponent(sourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        MangaChapterPanel.setLayout(new javax.swing.BoxLayout(MangaChapterPanel, javax.swing.BoxLayout.X_AXIS));

        mangaListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Manga List"));

        mangaListSearchLabel.setText("Search:");

        mangaListTable.setModel(mangasTableModel);
        mangaListTable.setRowSorter(new TableRowSorter<>(mangasTableModel));
        mangaListTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        mangaListScrollPane.setViewportView(mangaListTable);

        javax.swing.GroupLayout mangaListPanelLayout = new javax.swing.GroupLayout(mangaListPanel);
        mangaListPanel.setLayout(mangaListPanelLayout);
        mangaListPanelLayout.setHorizontalGroup(
            mangaListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mangaListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
            .addGroup(mangaListPanelLayout.createSequentialGroup()
                .addComponent(mangaListSearchLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mangaListSearchTextField))
        );
        mangaListPanelLayout.setVerticalGroup(
            mangaListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mangaListPanelLayout.createSequentialGroup()
                .addGroup(mangaListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mangaListSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mangaListSearchLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mangaListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
        );

        MangaChapterPanel.add(mangaListPanel);

        chapterListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Chapter List"));

        chapterListSearchLabel.setText("Search:");

        chapterDeSelectAllCheckBox.setToolTipText("");

        chapterListTable.setModel(chaptersTableModel);
        chapterListTable.setAutoscrolls(false);
        chapterListTable.setRowSelectionAllowed(false);
        chapterListTable.setRowSorter(new TableRowSorter<>(chaptersTableModel));
        chapterListScrollPane.setViewportView(chapterListTable);

        javax.swing.GroupLayout chapterListPanelLayout = new javax.swing.GroupLayout(chapterListPanel);
        chapterListPanel.setLayout(chapterListPanelLayout);
        chapterListPanelLayout.setHorizontalGroup(
            chapterListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chapterListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
            .addGroup(chapterListPanelLayout.createSequentialGroup()
                .addComponent(chapterListSearchLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chapterListSearchTextField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chapterDeSelectAllCheckBox))
        );
        chapterListPanelLayout.setVerticalGroup(
            chapterListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chapterListPanelLayout.createSequentialGroup()
                .addGroup(chapterListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chapterListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chapterListSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chapterListSearchLabel))
                    .addComponent(chapterDeSelectAllCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chapterListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
        );

        MangaChapterPanel.add(chapterListPanel);

        downloadButton.setText("Download");
        downloadButton.setEnabled(false);
        downloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourcePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MangaChapterPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downloadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mangasDirPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mangasDirPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourcePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MangaChapterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceButtonActionPerformed
        controller.updateMangaList();
    }//GEN-LAST:event_sourceButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        controller.download();
    }//GEN-LAST:event_downloadButtonActionPerformed

    private void mangasDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mangasDirButtonActionPerformed
        if (mangasDirFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String mangaDir = mangasDirFileChooser.getSelectedFile().toString();
            mangasDirTextField.setText(mangaDir);
            Options.INSTANCE.setMangaDir(mangaDir);
        }
    }//GEN-LAST:event_mangasDirButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MangaDownloader.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MangaDownloader().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MangaChapterPanel;
    private javax.swing.JCheckBox chapterDeSelectAllCheckBox;
    private javax.swing.JPanel chapterListPanel;
    private javax.swing.JScrollPane chapterListScrollPane;
    private javax.swing.JLabel chapterListSearchLabel;
    private javax.swing.JTextField chapterListSearchTextField;
    private javax.swing.JTable chapterListTable;
    private javax.swing.JButton downloadButton;
    private javax.swing.JPanel mangaListPanel;
    private javax.swing.JScrollPane mangaListScrollPane;
    private javax.swing.JLabel mangaListSearchLabel;
    private javax.swing.JTextField mangaListSearchTextField;
    private javax.swing.JTable mangaListTable;
    private javax.swing.JButton mangasDirButton;
    private javax.swing.JFileChooser mangasDirFileChooser;
    private javax.swing.JPanel mangasDirPanel;
    private javax.swing.JTextField mangasDirTextField;
    private javax.swing.JButton sourceButton;
    private javax.swing.JComboBox sourceComboBox;
    private javax.swing.JPanel sourcePanel;
    // End of variables declaration//GEN-END:variables
}
