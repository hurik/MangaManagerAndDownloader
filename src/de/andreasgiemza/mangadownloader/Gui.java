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

import de.andreasgiemza.mangadownloader.chapters.ChapteCheckBoxItemListener;
import de.andreasgiemza.mangadownloader.chapters.Chapter;
import de.andreasgiemza.mangadownloader.chapters.ChapterListSearchDocumentListener;
import de.andreasgiemza.mangadownloader.chapters.ChapterTableModel;
import de.andreasgiemza.mangadownloader.download.Download;
import de.andreasgiemza.mangadownloader.mangas.Manga;
import de.andreasgiemza.mangadownloader.mangas.MangaListSearchDocumentListener;
import de.andreasgiemza.mangadownloader.mangas.MangaListSelectionListener;
import de.andreasgiemza.mangadownloader.mangas.MangaTableModel;
import de.andreasgiemza.mangadownloader.sites.Batoto;
import de.andreasgiemza.mangadownloader.sites.Mangacow;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Gui extends javax.swing.JFrame {

    private final Path currentDirectory = Paths.get("").toAbsolutePath();
    private Site site;
    private final List<Manga> mangas = new LinkedList<>();
    private final MangaTableModel mangasTableModel = new MangaTableModel(mangas);
    private Manga selectedManga;
    private final List<Chapter> chapters = new LinkedList<>();
    private final ChapterTableModel chaptersTableModel = new ChapterTableModel(chapters);

    /**
     * Creates new form Gui
     */
    public Gui() {
        initComponents();

        // Setup Manga List Table
        final TableRowSorter<MangaTableModel> mangaTableRowSorter
                = new TableRowSorter<>(mangasTableModel);
        mangaListTable.setRowSorter(mangaTableRowSorter);
        mangaListSearchTextField.getDocument().addDocumentListener(
                new MangaListSearchDocumentListener(
                        mangaListSearchTextField,
                        mangaTableRowSorter));
        mangaListTable.getSelectionModel().addListSelectionListener(
                new MangaListSelectionListener(this, mangaListTable));

        // Setup Chapter List Table
        final TableRowSorter<ChapterTableModel> chapterTableRowSorter
                = new TableRowSorter<>(chaptersTableModel);
        chapterListTable.setRowSorter(chapterTableRowSorter);
        chapterListSearchTextField.getDocument().addDocumentListener(
                new ChapterListSearchDocumentListener(
                        chapterListSearchTextField,
                        chapterTableRowSorter,
                        chapterDeSelectAllCheckBox));
        chapterListTable.getColumnModel().getColumn(1).setMaxWidth(34);
        chapterListTable.getColumnModel().getColumn(1).setMinWidth(34);

        // Setup DeSelectAll CheckBox
        chapterDeSelectAllCheckBox.addItemListener(
                new ChapteCheckBoxItemListener(chapterListTable));

        // Setup Source ComboBox
        sourceComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadMangaList();
                }
            }
        });

        // Load Manga List
        loadMangaList();
    }

    @SuppressWarnings("unchecked")
    private void loadMangaList() {
        mangas.clear();
        chapters.clear();

        mangaListSearchTextField.setText("");
        chapterListSearchTextField.setText("");

        String source = (String) sourceComboBox.getSelectedItem();

        switch (source) {
            case "Batoto":
                site = new Batoto();
                break;
            case "Mangacow":
                site = new Mangacow();
                break;
        }

        Path sourceFile = currentDirectory.resolve("sources").resolve(source + ".txt");

        if (Files.exists(sourceFile)) {
            try (FileInputStream fin = new FileInputStream(sourceFile.toFile())) {
                ObjectInputStream ois = new ObjectInputStream(fin);
                mangas.addAll((LinkedList<Manga>) ois.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        mangasTableModel.fireTableDataChanged();
    }

    public void getChapters(Manga selectedManga) {
        chapters.clear();
        chaptersTableModel.fireTableDataChanged();
        chapterListSearchTextField.setText("");

        this.selectedManga = selectedManga;
        chapters.addAll(site.getChapterList(selectedManga));
        chaptersTableModel.fireTableDataChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourceLabel = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manga Downloader");

        sourceLabel.setText("Source:");

        sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Batoto", "Mangacow" }));

        sourceButton.setText("Update");
        sourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceButtonActionPerformed(evt);
            }
        });

        MangaChapterPanel.setLayout(new javax.swing.BoxLayout(MangaChapterPanel, javax.swing.BoxLayout.X_AXIS));

        mangaListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Manga List"));

        mangaListSearchLabel.setText("Search:");

        mangaListTable.setModel(mangasTableModel);
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
                .addComponent(mangaListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
        );

        MangaChapterPanel.add(mangaListPanel);

        chapterListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Chapter List"));

        chapterListSearchLabel.setText("Search:");

        chapterDeSelectAllCheckBox.setToolTipText("");

        chapterListTable.setModel(chaptersTableModel);
        chapterListTable.setAutoscrolls(false);
        chapterListTable.setRowSelectionAllowed(false);
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
                .addComponent(chapterListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
        );

        MangaChapterPanel.add(chapterListPanel);

        downloadButton.setText("Download");
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
                    .addComponent(MangaChapterPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sourceLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sourceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sourceButton))
                    .addComponent(downloadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceButton)
                    .addComponent(sourceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MangaChapterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downloadButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceButtonActionPerformed
        mangas.clear();

        String source = (String) sourceComboBox.getSelectedItem();

        mangas.addAll(site.getMangaList());
        Collections.sort(mangas);
        mangasTableModel.fireTableDataChanged();

        // Save data to file
        try {
            Path sourceFile = currentDirectory.resolve("sources").resolve(source + ".txt");

            if (!Files.exists(sourceFile.getParent())) {
                Files.createDirectory(sourceFile.getParent());
            }

            if (Files.exists(sourceFile)) {
                Files.delete(sourceFile);
            }

            try (FileOutputStream fout = new FileOutputStream(sourceFile.toFile())) {
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(mangas);
            }
        } catch (IOException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_sourceButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
        JDialog dialog = new JDialog(this, "Downloading ...", true);
        dialog.getContentPane().add(new Download(currentDirectory, site, selectedManga, chapters));
        dialog.pack();
        dialog.setLocation(
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (dialog.getWidth() / 2)).intValue(),
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (dialog.getHeight() / 2)).intValue());
        dialog.setResizable(true);
        dialog.setVisible(true);
    }//GEN-LAST:event_downloadButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui().setVisible(true);
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
    private javax.swing.JButton sourceButton;
    private javax.swing.JComboBox sourceComboBox;
    private javax.swing.JLabel sourceLabel;
    // End of variables declaration//GEN-END:variables

}
