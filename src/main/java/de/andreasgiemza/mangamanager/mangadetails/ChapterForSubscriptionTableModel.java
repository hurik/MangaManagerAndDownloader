package de.andreasgiemza.mangamanager.mangadetails;

import de.andreasgiemza.mangamanager.data.ChapterForSubscription;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterForSubscriptionTableModel extends AbstractTableModel {

    private final List<ChapterForSubscription> chapters;
    private final List<String> columnNames = Arrays.asList(
            "Title",
            "Added",
            "Status",
            "Read date");

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
        ChapterForSubscription chapter = chapters.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return chapter.getTitle();
            case 1:
                if (chapter.getAddedDate() != null) {
                    return new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(chapter.getAddedDate());
                } else {
                    return null;
                }
            case 2:
                return (chapter.getRead() ? "READ" : "UNREAD");
            case 3:
                if (chapter.getReadDate() != null) {
                    return new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(chapter.getReadDate());
                } else {
                    return null;
                }
            default:
                return null;
        }
    }

    public ChapterForSubscription getChapter(int rowIndex) {
        return chapters.get(rowIndex);
    }

}
