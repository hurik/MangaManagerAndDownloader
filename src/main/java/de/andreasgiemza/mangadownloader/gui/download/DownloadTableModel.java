package de.andreasgiemza.mangadownloader.gui.download;

import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.andreasgiemza.mangadownloader.data.Download;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class DownloadTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final List<Download> downloads;
	private final List<String> columnNames = Arrays.asList("ID", "Site",
			"Manga", "Chapter", "Status");

	public DownloadTableModel(List<Download> downloads) {
		this.downloads = downloads;
	}

	@Override
	public int getRowCount() {
		return downloads.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return downloads.get(row).getId();
		case 1:
			return downloads.get(row).getSite().getName();
		case 2:
			return downloads.get(row).getManga().getTitle();
		case 3:
			return downloads.get(row).getChapter().getTitle();
		case 4:
			return downloads.get(row).getMessage();
		default:
			return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Integer.class;
		}

		return super.getColumnClass(columnIndex);
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}
}
