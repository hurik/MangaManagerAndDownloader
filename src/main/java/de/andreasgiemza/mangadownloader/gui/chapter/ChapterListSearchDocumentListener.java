package de.andreasgiemza.mangadownloader.gui.chapter;

import de.andreasgiemza.mangadownloader.MangaDownloader;
import de.andreasgiemza.mangadownloader.helpers.RegexHelper;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterListSearchDocumentListener implements DocumentListener {

    private final MangaDownloader mangaDownloader;
    private final JTextField chapterListSearchTextField;
    private final TableRowSorter<ChapterTableModel> chapterTableRowSorter;

    @SuppressWarnings("unchecked")
    public ChapterListSearchDocumentListener(MangaDownloader mangaDownloader,
            JTextField chapterListSearchTextField, JTable chapterListTable) {
        this.mangaDownloader = mangaDownloader;
        this.chapterListSearchTextField = chapterListSearchTextField;
        chapterTableRowSorter = (TableRowSorter<ChapterTableModel>) chapterListTable
                .getRowSorter();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changed();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changed();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changed();

    }

    private void changed() {
        mangaDownloader.chapterSearchChanged();

        final String searchText = chapterListSearchTextField.getText();

        if (searchText.length() == 0) {
            chapterTableRowSorter.setRowFilter(null);
        } else if (searchText.length() > 0) {
            chapterTableRowSorter.setRowFilter(RowFilter
                    .regexFilter(RegexHelper.build(searchText)));
        }
    }
}
