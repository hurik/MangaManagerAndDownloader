package de.andreasgiemza.mangamanager.addsubscription;

import de.andreasgiemza.mangadownloader.gui.manga.MangaTableModel;
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
public class MangaDocumentListener implements DocumentListener {

    private final AddSubscription addSubscription;
    private final JTextField mangaListSearchTextField;
    private final TableRowSorter<MangaTableModel> mangaTableRowSorter;

    @SuppressWarnings("unchecked")
    public MangaDocumentListener(AddSubscription addSubscription,
            JTextField mangaListSearchTextField, JTable mangaListTable) {
        this.mangaListSearchTextField = mangaListSearchTextField;
        this.mangaTableRowSorter = (TableRowSorter<MangaTableModel>) mangaListTable.getRowSorter();
        this.addSubscription = addSubscription;
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
        addSubscription.mangaSearchChanged();

        final String searchText = mangaListSearchTextField.getText();
        if (searchText.length() == 0) {
            mangaTableRowSorter.setRowFilter(null);
        } else if (searchText.length() > 0) {
            mangaTableRowSorter.setRowFilter(RowFilter.regexFilter(RegexHelper
                    .build(searchText)));
        }
    }
}
