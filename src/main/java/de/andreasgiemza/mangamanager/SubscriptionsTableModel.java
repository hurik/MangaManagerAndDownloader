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
    private final List<String> columnNames = Arrays.asList("Site", "Manga", "Filter");

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
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return subscriptions.get(rowIndex).getSite().getName();
            case 1:
                return subscriptions.get(rowIndex).getManga().getTitle();
            case 2:
                return subscriptions.get(rowIndex).getFilter();
            default:
                return null;
        }
    }
}
