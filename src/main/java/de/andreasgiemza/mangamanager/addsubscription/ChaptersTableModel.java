package de.andreasgiemza.mangamanager.addsubscription;

import de.andreasgiemza.mangadownloader.data.Chapter;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChaptersTableModel extends AbstractTableModel {

    private final List<Chapter> chapters;
    private final List<String> columnNames = Arrays.asList("Title");

    public ChaptersTableModel(List<Chapter> chapters) {
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
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        }

        return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return chapters.get(rowIndex).getTitle();
            default:
                return null;
        }
    }
}
