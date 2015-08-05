package de.andreasgiemza.mangadownloader.gui.site;

import de.andreasgiemza.mangadownloader.data.MangaList;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class SiteTableModel extends AbstractTableModel {

    private final List<Site> sites = SiteHelper.getSites();
    private final List<String> columnNames = Arrays.asList(
            "Name",
            "Manga Count",
            "Language",
            "Watermarks",
            "Last List update");

    @Override
    public int getRowCount() {
        return sites.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 1:
                return Integer.class;
            case 3:
                return Boolean.class;
            default:
                return super.getColumnClass(columnIndex);
        }
    }

    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return sites.get(row).getName();
            case 1:
                int count = MangaList.getMangaCount(sites.get(row));

                if (count == 0) {
                    return null;
                } else {
                    return MangaList.getMangaCount(sites.get(row));
                }
            case 2:
                return sites.get(row).getLanguage().toString().replace("[", "").replace("]", "");
            case 3:
                return sites.get(row).hasWatermarks();
            case 4:
                String time = MangaList.getLastListUpdate(sites.get(row));

                if (time != null) {
                    return time;
                } else {
                    return "no list present";
                }
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames.get(col);
    }

    public Site getSite(int row) {
        return sites.get(row);
    }

    public int getIndexOf(Site site) {
        return sites.indexOf(site);
    }

    public List<Site> getSites() {
        return sites;
    }
}
