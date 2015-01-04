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
package de.andreasgiemza.mangadownloader.gui;

import de.andreasgiemza.mangadownloader.MangaDownloader;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterTableModel;
import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.gui.manga.MangaTableModel;
import de.andreasgiemza.mangadownloader.gui.panels.Download;
import de.andreasgiemza.mangadownloader.gui.panels.Loading;
import de.andreasgiemza.mangadownloader.helpers.FilenameHelper;
import de.andreasgiemza.mangadownloader.sites.Site;
import java.awt.Toolkit;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.reflections.Reflections;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class Controller {

    // Current directory
    private final Path currentDirectory = Paths.get("").toAbsolutePath();
    // Gui elements
    private final MangaDownloader mangaDownloader;
    private final JComboBox sourceComboBox;
    private final JTextField mangaListSearchTextField;
    private final JTable mangaListTable;
    private final JTextField chapterListSearchTextField;
    private final JCheckBox chapterDeSelectAllCheckBox;
    private final JTable chapterListTable;
    private final JButton downloadButton;
    // Site
    private Site site;
    // Table data
    private final List<Manga> mangas;
    private final List<Chapter> chapters;
    // Selected manga
    private Manga selectedManga;
    private Manga lastSelectedManga;

    public Controller(
            MangaDownloader mangaDownloader,
            JComboBox sourceComboBox,
            JTextField mangaListSearchTextField,
            JTable mangaListTable,
            JTextField chapterListSearchTextField,
            JCheckBox chapterDeSelectAllCheckBox,
            JTable chapterListTable,
            JButton downloadButton,
            List<Manga> mangas,
            List<Chapter> chapters) {
        this.mangaDownloader = mangaDownloader;
        this.sourceComboBox = sourceComboBox;
        this.mangaListSearchTextField = mangaListSearchTextField;
        this.mangaListTable = mangaListTable;
        this.chapterListSearchTextField = chapterListSearchTextField;
        this.chapterDeSelectAllCheckBox = chapterDeSelectAllCheckBox;
        this.chapterListTable = chapterListTable;
        this.downloadButton = downloadButton;
        this.mangas = mangas;
        this.chapters = chapters;
    }

    public void loadMangaList() {
        resetMangaPanel();

        String source = (String) sourceComboBox.getSelectedItem();

        for (Class<? extends Site> siteClasse : new Reflections("de.andreasgiemza.mangadownloader.sites").getSubTypesOf(Site.class)) {
            if (siteClasse.getName().contains(source)) {
                try {
                    site = siteClasse.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                }
            }
        }

        if (site == null) {
            return;
        }

        Loading loading = new Loading(mangaDownloader, true, this, Loading.LoadingJob.SiteSelected);
        loading.setLocation(
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (loading.getWidth() / 2)).intValue(),
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (loading.getHeight() / 2)).intValue());
        loading.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    public void loadMangaListWorker() {
        String source = (String) sourceComboBox.getSelectedItem();

        Path sourceFile = currentDirectory.resolve("sources").resolve(source + ".txt");

        if (Files.exists(sourceFile)) {
            try (FileInputStream fin = new FileInputStream(sourceFile.toFile())) {
                ObjectInputStream ois = new ObjectInputStream(fin);
                mangas.addAll((LinkedList<Manga>) ois.readObject());
            } catch (IOException | ClassNotFoundException ex) {
            }
        }

        ((MangaTableModel) mangaListTable.getModel()).fireTableDataChanged();
    }

    public void updateMangaList() {
        resetMangaPanel();

        Loading loading = new Loading(mangaDownloader, true, this, Loading.LoadingJob.MangaListUpdate);
        loading.setLocation(
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (loading.getWidth() / 2)).intValue(),
                new Double((Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (loading.getHeight() / 2)).intValue());
        loading.setVisible(true);
    }

    public void updateMangaListWorker() {
        String source = (String) sourceComboBox.getSelectedItem();

        try {
            mangas.addAll(site.getMangaList());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    mangaDownloader,
                    "Cant't connect to " + source + "!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Collections.sort(mangas);
        ((MangaTableModel) mangaListTable.getModel()).fireTableDataChanged();

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
        }
    }

    public void mangaSearchChanged() {
        mangaListTable.clearSelection();
        downloadButton.setEnabled(false);
        lastSelectedManga = null;
        resetChapterPanel();
    }

    public void mangaSelected(Manga selectedManga) {
        if (selectedManga != lastSelectedManga) {
            this.selectedManga = selectedManga;
            resetChapterPanel();

            Loading loading = new Loading(mangaDownloader, true, this, Loading.LoadingJob.MangaSelected);
            loading.setLocation(
                    new Double((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (loading.getWidth() / 2)).intValue(),
                    new Double((Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (loading.getHeight() / 2)).intValue());
            loading.setVisible(true);
        }
    }

    public void mangaSelectedWorker() {
        try {
            chapters.addAll(site.getChapterList(selectedManga));

            for (Chapter chapter : chapters) {
                String mangaTitle = FilenameHelper.checkForIllegalCharacters(selectedManga.getTitle());
                String chapterTitle = FilenameHelper.checkForIllegalCharacters(chapter.getTitle());

                Path file = currentDirectory.resolve("mangas")
                        .resolve(mangaTitle)
                        .resolve(chapterTitle + ".cbz");

                if (Files.exists(file)) {
                    chapter.setAlreadyDownloaded(true);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    mangaDownloader,
                    "Cant't connect to " + site.getClass().getSimpleName() + "!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            mangaListTable.clearSelection();
            lastSelectedManga = null;
            return;
        }
        ((ChapterTableModel) chapterListTable.getModel()).fireTableDataChanged();

        downloadButton.setEnabled(true);

        lastSelectedManga = selectedManga;
    }

    public void chapterSearchChanged() {
        chapterDeSelectAllCheckBox.setSelected(false);
        deactivateDownloads();
    }

    public void deactivateDownloads() {
        for (Chapter chapter : chapters) {
            chapter.setDownload(false);
        }
        ((ChapterTableModel) chapterListTable.getModel()).fireTableDataChanged();
    }

    public void resetMangaPanel() {
        mangas.clear();
        ((MangaTableModel) mangaListTable.getModel()).fireTableDataChanged();
        mangaListSearchTextField.setText("");
        mangaListTable.clearSelection();
        downloadButton.setEnabled(false);
        selectedManga = null;

        resetChapterPanel();
    }

    public void resetChapterPanel() {
        chapters.clear();
        ((ChapterTableModel) chapterListTable.getModel()).fireTableDataChanged();
        chapterListSearchTextField.setText("");
        chapterDeSelectAllCheckBox.setSelected(false);
    }

    public void download() {
        boolean oneSelected = false;

        for (Chapter chapter : chapters) {
            if (chapter.isDownload()) {
                oneSelected = true;
                break;
            }
        }

        if (oneSelected) {
            Download download = new Download(mangaDownloader, true, currentDirectory, site, selectedManga, chapters);
            download.setLocation(
                    new Double((Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2) - (download.getWidth() / 2)).intValue(),
                    new Double((Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2) - (download.getHeight() / 2)).intValue());
            download.setVisible(true);
            
            ((ChapterTableModel) chapterListTable.getModel()).fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(
                    mangaDownloader,
                    "Please select one or more chapters!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
