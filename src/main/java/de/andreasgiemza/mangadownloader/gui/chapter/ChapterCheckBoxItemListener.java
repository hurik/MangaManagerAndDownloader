package de.andreasgiemza.mangadownloader.gui.chapter;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JTable;

import de.andreasgiemza.mangadownloader.MangaDownloader;
import de.andreasgiemza.mangadownloader.data.Chapter;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class ChapterCheckBoxItemListener implements ItemListener {

	private final MangaDownloader mangaDownloader;
	private final JTable chapterListTable;
	private final ChapterTableModel chapterTableModel;

	public ChapterCheckBoxItemListener(MangaDownloader mangaDownloader,
			JTable chapterListTable) {
		this.mangaDownloader = mangaDownloader;
		this.chapterListTable = chapterListTable;
		chapterTableModel = (ChapterTableModel) chapterListTable.getModel();

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			for (int i = 0; i < chapterListTable.getRowCount(); i++) {
				Chapter chapter = chapterTableModel
						.getChapterAt(chapterListTable
								.convertRowIndexToModel(i));

				if (!chapter.isAlreadyDownloaded()) {
					chapter.setDownload(true);
				}
			}
			chapterTableModel.fireTableDataChanged();
		} else if (e.getStateChange() == ItemEvent.DESELECTED) {
			mangaDownloader.deactivateDownloads();
		}
	}
}
