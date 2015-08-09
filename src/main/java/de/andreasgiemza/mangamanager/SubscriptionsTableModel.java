package de.andreasgiemza.mangamanager;

import de.andreasgiemza.mangamanager.data.Subscription;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SubscriptionsTableModel extends AbstractTableModel {

    private final List<Subscription> subscriptions;
    private final List<String> columnNames = Arrays.asList(
            "Site",
            "Manga",
            "Filter",
            "Chapter",
            "Unread chapters",
            "Last read chapter",
            "Latest chapter",
            "Last update");

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
        Subscription subscription = subscriptions.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return subscription.getSite().getName();
            case 1:
                return subscription.getManga().getTitle();
            case 2:
                return subscription.getFilter();
            case 3:
                return subscription.getChapters().size();
            case 4:
                return subscription.getUnreadChapters();
            case 5:
                return subscription.getLastReadChapter();
            case 6:
                return subscription.getChapters().get(0).getTitle();
            case 7:
                return new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(subscription.getLastUpdate());
            default:
                return null;
        }
    }

    public Subscription getSubscription(int row) {
        return subscriptions.get(row);
    }
}
