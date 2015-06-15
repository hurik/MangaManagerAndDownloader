package de.andreasgiemza.mangadownloader.gui.chapter;

import de.andreasgiemza.mangadownloader.data.Chapter;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final List<Chapter> chapters;
	private final List<String> columnNames = Arrays.asList("Title", "");

	public ChapterTableModel(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	@Override
	public int getRowCount() {
		return chapters.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 1;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return String.class;
		}

		if (columnIndex == 1) {
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return chapters.get(rowIndex).getTitle();
		case 1:
			return chapters.get(rowIndex).isDownload();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		getChapterAt(rowIndex).setDownload((boolean) aValue);
		super.setValueAt(aValue, rowIndex, columnIndex);
	}

	public Chapter getChapterAt(int row) {
		return chapters.get(row);
	}

	public List<Chapter> getChapters() {
		return chapters;
	}
}
