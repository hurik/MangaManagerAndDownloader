package de.andreasgiemza.mangadownloader.gui.manga;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import de.andreasgiemza.mangadownloader.MangaDownloader;
import de.andreasgiemza.mangadownloader.helpers.RegexHelper;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaListSearchDocumentListener implements DocumentListener {

	private final MangaDownloader mangaDownloader;
	private final JTextField mangaListSearchTextField;
	private final TableRowSorter<MangaTableModel> mangaTableRowSorter;

	@SuppressWarnings("unchecked")
	public MangaListSearchDocumentListener(MangaDownloader mangaDownloader,
			JTextField mangaListSearchTextField, JTable mangaListTable) {
		this.mangaListSearchTextField = mangaListSearchTextField;
		mangaTableRowSorter = (TableRowSorter<MangaTableModel>) mangaListTable
				.getRowSorter();
		this.mangaDownloader = mangaDownloader;
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
		final String searchText = mangaListSearchTextField.getText();
		if (searchText.length() == 0) {
			mangaTableRowSorter.setRowFilter(null);
		} else if (searchText.length() > 0) {
			mangaTableRowSorter.setRowFilter(RowFilter.regexFilter(RegexHelper
					.build(searchText)));
		}

		mangaDownloader.mangaSearchChanged();
	}
}
