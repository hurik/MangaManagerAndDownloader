package de.andreasgiemza.mangamanager.mangadetails;

import de.andreasgiemza.mangamanager.data.ChapterForSubscription;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterForSubscriptionTableModel extends AbstractTableModel {

    private final List<ChapterForSubscription> chapters;
    private final List<String> columnNames = Arrays.asList("Title", "Read");

    public ChapterForSubscriptionTableModel(List<ChapterForSubscription> chapters) {
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return chapters.get(rowIndex).getTitle();
            case 1:
                return (chapters.get(rowIndex).getRead() ? "READ" : "UNREAD");
            default:
                return null;
        }
    }

    public ChapterForSubscription getChapter(int rowIndex) {
        return chapters.get(rowIndex);
    }

}
