package de.andreasgiemza.mangamanager;

import de.andreasgiemza.mangamanager.data.Subscription;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SubscriptionsTableModel extends AbstractTableModel {

    private final List<Subscription> subscriptions;
    private final List<String> columnNames = Arrays.asList("Site", "Manga", "Filter", "Chapter", "Unread chapters");

    public SubscriptionsTableModel(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public int getRowCount() {
        return subscriptions.size();
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
        switch (columnIndex) {
            case 3:
            case 4:
                return Integer.class;
            default:
                return super.getColumnClass(columnIndex);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return subscriptions.get(rowIndex).getSite().getName();
            case 1:
                return subscriptions.get(rowIndex).getManga().getTitle();
            case 2:
                return subscriptions.get(rowIndex).getFilter();
            case 3:
                return subscriptions.get(rowIndex).getChapters().size();
            case 4:
                return subscriptions.get(rowIndex).getUnreadChapters();
            default:
                return null;
        }
    }

    public Subscription getSubscription(int row) {
        return subscriptions.get(row);
    }
}
