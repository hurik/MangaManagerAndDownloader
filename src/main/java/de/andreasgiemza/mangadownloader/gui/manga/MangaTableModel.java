package de.andreasgiemza.mangadownloader.gui.manga;

import de.andreasgiemza.mangadownloader.data.Manga;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final List<Manga> mangas;
	private final List<String> columnNames = Arrays.asList("Title");

	public MangaTableModel(List<Manga> mangas) {
		this.mangas = mangas;
	}

	@Override
	public int getRowCount() {
		return mangas.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return String.class;
		}

		return super.getColumnClass(columnIndex);
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return mangas.get(row).getTitle();
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.get(col);
	}

	public Manga getMangaAt(int row) {
		return mangas.get(row);
	}
}
