package de.andreasgiemza.mangamanager.addsubscription;

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
public class FilterDocumentListener implements DocumentListener {

    private final JTextField chapterListSearchTextField;
    private final TableRowSorter<ChaptersTableModel> chapterTableRowSorter;

    @SuppressWarnings("unchecked")
    public FilterDocumentListener(JTextField chapterListSearchTextField, JTable chapterListTable) {
        this.chapterListSearchTextField = chapterListSearchTextField;
        chapterTableRowSorter = (TableRowSorter<ChaptersTableModel>) chapterListTable
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
        final String searchText = chapterListSearchTextField.getText();

        if (searchText.length() == 0) {
            chapterTableRowSorter.setRowFilter(null);
        } else if (searchText.length() > 0) {
            chapterTableRowSorter.setRowFilter(RowFilter
                    .regexFilter(RegexHelper.build(searchText)));
        }
    }
}
