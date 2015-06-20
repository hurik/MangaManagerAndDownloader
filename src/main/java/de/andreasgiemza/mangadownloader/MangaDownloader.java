package de.andreasgiemza.mangadownloader;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import de.andreasgiemza.mangadownloader.data.Chapter;
import de.andreasgiemza.mangadownloader.data.Download;
import de.andreasgiemza.mangadownloader.data.Image;
import de.andreasgiemza.mangadownloader.data.Manga;
import de.andreasgiemza.mangadownloader.data.MangaList;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterCheckBoxItemListener;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterListSearchDocumentListener;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterTableCellRenderer;
import de.andreasgiemza.mangadownloader.gui.chapter.ChapterTableModel;
import de.andreasgiemza.mangadownloader.gui.dialogs.Loading;
import de.andreasgiemza.mangadownloader.gui.dialogs.SelectSite;
import de.andreasgiemza.mangadownloader.gui.download.DownloadTableModel;
import de.andreasgiemza.mangadownloader.gui.manga.MangaListSearchDocumentListener;
import de.andreasgiemza.mangadownloader.gui.manga.MangaListSelectionListener;
import de.andreasgiemza.mangadownloader.gui.manga.MangaTableCellRenderer;
import de.andreasgiemza.mangadownloader.gui.manga.MangaTableModel;
import de.andreasgiemza.mangadownloader.helpers.FilenameHelper;
import de.andreasgiemza.mangadownloader.helpers.JsoupHelper;
import de.andreasgiemza.mangadownloader.options.Options;
import de.andreasgiemza.mangadownloader.sites.Site;
import de.andreasgiemza.mangadownloader.sites.SiteHelper;

/**
 *
 * @author Andreas Giemza <andreas@giemza.net>
 */
public class MangaDownloader extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	// Tables
	private final List<Manga> mangas = new LinkedList<>();
	private final MangaTableModel mangasTableModel = new MangaTableModel(mangas);
	private final List<Chapter> chapters = new LinkedList<>();
	private final ChapterTableModel chaptersTableModel = new ChapterTableModel(
			chapters);
	private final List<Download> downloads = new LinkedList<>();
	private final DownloadTableModel downloadsTableModel = new DownloadTableModel(
			downloads);

	// Download
	private int id = 0;
	private volatile boolean interrupted = false;

	// Site
	private Site selectedSite;
	// Selected manga
	private Manga selectedManga;
	private Manga lastSelectedManga = null;

	private final MangaDownloader mangaDownloader = this;

	/**
	 * Creates new form Gui
	 */
	public MangaDownloader() {
		initComponents();

		setLocation(new Double((Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth() / 2)
				- (getWidth() / 2)).intValue(), new Double((Toolkit
				.getDefaultToolkit().getScreenSize().getHeight() / 2)
				- (getHeight() / 2)).intValue());

		// Setup Manga List Table
		mangaListSearchTextField.getDocument().addDocumentListener(
				new MangaListSearchDocumentListener(this,
						mangaListSearchTextField, mangaListTable));
		mangaListTable.getSelectionModel().addListSelectionListener(
				new MangaListSelectionListener(this, mangaListTable));
		mangaListTable.setDefaultRenderer(String.class,
				new MangaTableCellRenderer());

		// Setup Chapter List Table
		chapterListSearchTextField.getDocument().addDocumentListener(
				new ChapterListSearchDocumentListener(this,
						chapterListSearchTextField, chapterListTable));
		chapterListTable.getColumnModel().getColumn(1).setMaxWidth(34);
		chapterListTable.getColumnModel().getColumn(1).setMinWidth(34);
		chapterListTable.setDefaultRenderer(String.class,
				new ChapterTableCellRenderer());

		// Setup Download List Table
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		downloadTable.getColumnModel().getColumn(0)
				.setCellRenderer(leftRenderer);

		downloadTable.getColumnModel().getColumn(0).setMaxWidth(30);
		downloadTable.getColumnModel().getColumn(0).setMinWidth(30);

		// Setup DeSelectAll CheckBox
		chapterDeSelectAllCheckBox
				.addItemListener(new ChapterCheckBoxItemListener(this,
						chapterListTable));

		// Save options before closing
		addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				if (startDownloadButton.isEnabled()) {
					dispose();
				} else {
					JOptionPane.showMessageDialog(mangaDownloader,
							"Can't close while still downloading!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// Load mangas save directory
		mangasDirTextField.setText(Options.INSTANCE.getMangaDir());

		// Load Manga List
		loadManga(SiteHelper.getInstance(Options.INSTANCE.getSelectedSource()));

		// Create options dir when not there
		Options.INSTANCE.createOptionsDir();

		// Deactivate certificate check
		JsoupHelper.deactivateCertificateCheck();
	}

	public final void loadManga(Site site) {
		if (site == null) {
			return;
		}

		if (site == selectedSite) {
			return;
		}

		selectedSite = site;

		resetMangaPanel();

		selectedSiteLabel.setText(selectedSite.getName());
		Options.INSTANCE.setSelectedSource(site.getClass().getSimpleName());
		Options.INSTANCE.saveOptions();

		final Loading loading = new Loading(this, true);
		loading.startRunnable(new Runnable() {

			@Override
			public void run() {
				mangas.addAll(MangaList.load(selectedSite));

				mangasTableModel.fireTableDataChanged();

				loading.dispose();
			}
		});
	}

	public void mangaSelected(final Manga selectedManga) {
		if (selectedManga != lastSelectedManga) {
			this.selectedManga = selectedManga;
			resetChapterPanel();

			final Loading loading = new Loading(this, true);
			loading.startRunnable(new Runnable() {

				@Override
				public void run() {
					try {
						chapters.addAll(selectedSite
								.getChapterList(selectedManga));

						for (Chapter chapter : chapters) {
							Path file = FilenameHelper.buildChapterPath(
									selectedManga, chapter);

							if (Files.exists(file)) {
								chapter.setAlreadyDownloaded(true);
							}
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(mangaDownloader,
								"Cant't connect to "
										+ selectedSite.getClass()
												.getSimpleName() + "!",
								"Error", JOptionPane.ERROR_MESSAGE);
						mangaListTable.clearSelection();
						lastSelectedManga = null;

						loading.dispose();
						return;
					}

					chaptersTableModel.fireTableDataChanged();

					downloadButton.setEnabled(true);

					lastSelectedManga = selectedManga;

					loading.dispose();
				}
			});
		}
	}

	public void mangaSearchChanged() {
		mangaListTable.clearSelection();
		downloadButton.setEnabled(false);
		lastSelectedManga = null;
		resetChapterPanel();
	}

	public void chapterSearchChanged() {
		chapterDeSelectAllCheckBox.setSelected(false);
		deactivateDownloads();
	}

	public void deactivateDownloads() {
		for (Chapter chapter : chapters) {
			chapter.setDownload(false);
		}

		chaptersTableModel.fireTableDataChanged();
	}

	private void resetMangaPanel() {
		mangas.clear();
		mangasTableModel.fireTableDataChanged();
		mangaListSearchTextField.setText("");
		mangaListTable.clearSelection();
		downloadButton.setEnabled(false);
		selectedManga = null;

		resetChapterPanel();
	}

	private void resetChapterPanel() {
		chapters.clear();
		chaptersTableModel.fireTableDataChanged();
		chapterListSearchTextField.setText("");
		chapterDeSelectAllCheckBox.setSelected(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		mangasDirFileChooser = new javax.swing.JFileChooser();
		mangasDirPanel = new javax.swing.JPanel();
		mangasDirTextField = new javax.swing.JTextField();
		mangasDirButton = new javax.swing.JButton();
		sitePanel = new javax.swing.JPanel();
		selectedSiteLabel = new javax.swing.JTextField();
		selectSiteButton = new javax.swing.JButton();
		splitPane = new javax.swing.JSplitPane();
		mangaChapterPanel = new javax.swing.JPanel();
		mangaListPanel = new javax.swing.JPanel();
		mangaListSearchLabel = new javax.swing.JLabel();
		mangaListSearchTextField = new javax.swing.JTextField();
		mangaListScrollPane = new javax.swing.JScrollPane();
		mangaListTable = new javax.swing.JTable();
		chapterListPanel = new javax.swing.JPanel();
		chapterListSearchLabel = new javax.swing.JLabel();
		chapterListSearchTextField = new javax.swing.JTextField();
		chapterDeSelectAllCheckBox = new javax.swing.JCheckBox();
		chapterListScrollPane = new javax.swing.JScrollPane();
		chapterListTable = new javax.swing.JTable();
		downloadButton = new javax.swing.JButton();
		downloadPanel = new javax.swing.JPanel();
		downloadScrollPane = new javax.swing.JScrollPane();
		downloadTable = new javax.swing.JTable();
		startDownloadButton = new javax.swing.JButton();
		stopDownloadButton = new javax.swing.JButton();
		removeDownloadButton = new javax.swing.JButton();

		mangasDirFileChooser.setDialogTitle("Select a manga directory ...");
		mangasDirFileChooser
				.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Manga Downloader");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("images/icon.png")));

		mangasDirPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Mangas Directory"));

		mangasDirTextField.setEditable(false);

		mangasDirButton.setText("Select");
		mangasDirButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mangasDirButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout mangasDirPanelLayout = new javax.swing.GroupLayout(
				mangasDirPanel);
		mangasDirPanel.setLayout(mangasDirPanelLayout);
		mangasDirPanelLayout
				.setHorizontalGroup(mangasDirPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mangasDirPanelLayout
										.createSequentialGroup()
										.addComponent(mangasDirTextField)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												mangasDirButton,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												90,
												javax.swing.GroupLayout.PREFERRED_SIZE)));
		mangasDirPanelLayout
				.setVerticalGroup(mangasDirPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mangasDirPanelLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(
												mangasDirTextField,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(mangasDirButton)));

		sitePanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Site"));

		selectedSiteLabel.setEditable(false);
		selectedSiteLabel.setText("No site selected");

		selectSiteButton.setText("Select site");
		selectSiteButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectSiteButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout sitePanelLayout = new javax.swing.GroupLayout(
				sitePanel);
		sitePanel.setLayout(sitePanelLayout);
		sitePanelLayout
				.setHorizontalGroup(sitePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								sitePanelLayout
										.createSequentialGroup()
										.addComponent(selectedSiteLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												selectSiteButton,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												90,
												javax.swing.GroupLayout.PREFERRED_SIZE)));
		sitePanelLayout.setVerticalGroup(sitePanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				sitePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(selectSiteButton)
						.addComponent(selectedSiteLabel,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)));

		splitPane.setBorder(null);
		splitPane.setDividerLocation(324);
		splitPane.setDividerSize(4);
		splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

		mangaChapterPanel.setLayout(new javax.swing.BoxLayout(
				mangaChapterPanel, javax.swing.BoxLayout.X_AXIS));

		mangaListPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Manga List"));

		mangaListSearchLabel.setText("Search:");

		mangaListTable.setModel(mangasTableModel);
		mangaListTable.setRowSorter(new TableRowSorter<>(mangasTableModel));
		mangaListTable
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		mangaListScrollPane.setViewportView(mangaListTable);

		javax.swing.GroupLayout mangaListPanelLayout = new javax.swing.GroupLayout(
				mangaListPanel);
		mangaListPanel.setLayout(mangaListPanelLayout);
		mangaListPanelLayout
				.setHorizontalGroup(mangaListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(mangaListScrollPane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 373,
								Short.MAX_VALUE)
						.addGroup(
								mangaListPanelLayout
										.createSequentialGroup()
										.addComponent(mangaListSearchLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(mangaListSearchTextField)));
		mangaListPanelLayout
				.setVerticalGroup(mangaListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								mangaListPanelLayout
										.createSequentialGroup()
										.addGroup(
												mangaListPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																mangaListSearchTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																mangaListSearchLabel))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												mangaListScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												275, Short.MAX_VALUE)));

		mangaChapterPanel.add(mangaListPanel);

		chapterListPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Chapter List"));

		chapterListSearchLabel.setText("Search:");

		chapterDeSelectAllCheckBox.setToolTipText("");

		chapterListTable.setModel(chaptersTableModel);
		chapterListTable.setAutoscrolls(false);
		chapterListTable.setRowSelectionAllowed(false);
		chapterListTable.setRowSorter(new TableRowSorter<>(chaptersTableModel));
		chapterListScrollPane.setViewportView(chapterListTable);

		downloadButton.setText("Add download");
		downloadButton.setEnabled(false);
		downloadButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				downloadButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout chapterListPanelLayout = new javax.swing.GroupLayout(
				chapterListPanel);
		chapterListPanel.setLayout(chapterListPanelLayout);
		chapterListPanelLayout
				.setHorizontalGroup(chapterListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(chapterListScrollPane,
								javax.swing.GroupLayout.DEFAULT_SIZE, 382,
								Short.MAX_VALUE)
						.addGroup(
								chapterListPanelLayout
										.createSequentialGroup()
										.addComponent(chapterListSearchLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												chapterListSearchTextField)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												chapterDeSelectAllCheckBox))
						.addComponent(downloadButton,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE));
		chapterListPanelLayout
				.setVerticalGroup(chapterListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								chapterListPanelLayout
										.createSequentialGroup()
										.addGroup(
												chapterListPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																chapterListPanelLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				chapterListSearchTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				chapterListSearchLabel))
														.addComponent(
																chapterDeSelectAllCheckBox))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												chapterListScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												246, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(downloadButton)));

		mangaChapterPanel.add(chapterListPanel);

		splitPane.setLeftComponent(mangaChapterPanel);

		downloadPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Download tasks"));

		downloadTable.setAutoCreateRowSorter(true);
		downloadTable.setModel(downloadsTableModel);
		downloadScrollPane.setViewportView(downloadTable);

		startDownloadButton.setText("Start");
		startDownloadButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						startDownloadButtonActionPerformed(evt);
					}
				});

		stopDownloadButton.setText("Stop");
		stopDownloadButton.setEnabled(false);
		stopDownloadButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						stopDownloadButtonActionPerformed(evt);
					}
				});

		removeDownloadButton.setText("Remove");
		removeDownloadButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						removeDownloadButtonActionPerformed(evt);
					}
				});

		javax.swing.GroupLayout downloadPanelLayout = new javax.swing.GroupLayout(
				downloadPanel);
		downloadPanel.setLayout(downloadPanelLayout);
		downloadPanelLayout
				.setHorizontalGroup(downloadPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								downloadPanelLayout
										.createSequentialGroup()
										.addComponent(
												downloadScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												691, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												downloadPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(
																removeDownloadButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																stopDownloadButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																startDownloadButton,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))));
		downloadPanelLayout
				.setVerticalGroup(downloadPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								downloadPanelLayout
										.createSequentialGroup()
										.addComponent(startDownloadButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(stopDownloadButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												48, Short.MAX_VALUE)
										.addComponent(removeDownloadButton))
						.addComponent(downloadScrollPane,
								javax.swing.GroupLayout.PREFERRED_SIZE, 0,
								Short.MAX_VALUE));

		splitPane.setRightComponent(downloadPanel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														sitePanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														mangasDirPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(splitPane))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(mangasDirPanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(sitePanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(splitPane).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void selectSiteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_selectSiteButtonActionPerformed
		SelectSite siteManager = new SelectSite(this, true);
		siteManager.setVisible(true);
	}// GEN-LAST:event_selectSiteButtonActionPerformed

	private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_downloadButtonActionPerformed
		boolean oneSelected = false;

		for (Chapter chapter : chapters) {
			if (chapter.isDownload()) {
				oneSelected = true;
				break;
			}
		}

		if (oneSelected) {
			for (Chapter chapter : chapters) {
				if (chapter.isDownload()) {
					Download download = new Download(id, selectedSite,
							selectedManga, chapter);

					if (!downloads.contains(download)) {
						downloads.add(download);
						id++;
					}
				}
			}

			downloadsTableModel.fireTableDataChanged();
		} else {
			JOptionPane.showMessageDialog(this,
					"Please select one or more chapters!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_downloadButtonActionPerformed

	private void mangasDirButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mangasDirButtonActionPerformed
		if (mangasDirFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String mangaDir = mangasDirFileChooser.getSelectedFile().toString();
			mangasDirTextField.setText(mangaDir);
			Options.INSTANCE.setMangaDir(mangaDir);
			Options.INSTANCE.saveOptions();
		}
	}// GEN-LAST:event_mangasDirButtonActionPerformed

	private void startDownloadButtonActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_startDownloadButtonActionPerformed
		if (downloads.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please add a download!",
					"Error", JOptionPane.ERROR_MESSAGE);

			return;
		}

		startDownloadButton.setEnabled(false);
		stopDownloadButton.setEnabled(true);
		removeDownloadButton.setEnabled(false);
		interrupted = false;

		new Thread(new Runnable() {

			@Override
			public void run() {
				int c = 0;

				while (c < downloads.size()) {
					Download download = downloads.get(c);

					if (download.getState() != Download.State.DONE) {
						if (interrupted) {
							resetGui();
							return;
						}

						download.setState(Download.State.RUNNING);
						setMessage(download, "Getting image links ...");

						List<Image> imageLinks;

						try {
							imageLinks = download
									.getSite()
									.getChapterImageLinks(download.getChapter());
						} catch (Exception ex) {
							download.setState(Download.State.ERROR);
							setMessage(download,
									"Error while getting image links!");
							c++;
							continue;
						}

						if (interrupted) {
							download.setState(Download.State.CANCELLED);
							setMessage(download, "Cancelled!");
							resetGui();
							return;
						}

						int numberOfImages = imageLinks.size();
						int numberOfImagesDigits = String.valueOf(
								numberOfImages).length();

						Path mangaFile = FilenameHelper.buildChapterPath(
								download.getManga(), download.getChapter());

						try {
							if (!Files.exists(mangaFile.getParent())) {
								Files.createDirectories(mangaFile.getParent());
							}

							if (Files.exists(mangaFile)) {
								Files.delete(mangaFile);
							}

							try (ZipOutputStream zos = new ZipOutputStream(
									new FileOutputStream(mangaFile.toFile()))) {
								for (int i = 0; i < imageLinks.size(); i++) {
									if (interrupted) {
										zos.close();
										removeFile(mangaFile);

										download.setState(Download.State.CANCELLED);
										setMessage(download, "Cancelled!");
										resetGui();
										return;
									}

									setMessage(
											download,
											"Downloading image " + (i + 1)
													+ " of "
													+ imageLinks.size()
													+ " ...");

									ZipEntry ze = new ZipEntry(String.format(
											"%0" + numberOfImagesDigits + "d",
											(i + 1))
											+ "."
											+ imageLinks.get(i).getExtension());
									zos.putNextEntry(ze);

									byte[] image;

									if (imageLinks.get(i).getLinkFragment() == null) {
										image = JsoupHelper.getImage(imageLinks
												.get(i).getLink(), imageLinks
												.get(i).getReferrer());
									} else {
										image = JsoupHelper
												.getImageWithFragment(imageLinks
														.get(i));
									}

									zos.write(image, 0, image.length);
									zos.closeEntry();
								}
							}
						} catch (Exception ex) {
							removeFile(mangaFile);

							download.setState(Download.State.ERROR);
							setMessage(download,
									"Error while downloading images!");
							c++;
							continue;
						}

						download.setState(Download.State.DONE);
						setMessage(download, "Done!");
					}

					c++;
				}

				resetGui();
			}

			private void removeFile(Path mangaFile) {
				if (Files.exists(mangaFile)) {
					try {
						Files.delete(mangaFile);
					} catch (IOException ex) {
					}
				}
			}

			private void setMessage(Download download, String message) {
				download.setMessage(message);
				downloadsTableModel.fireTableDataChanged();
			}

			private void resetGui() {
				startDownloadButton.setEnabled(true);
				stopDownloadButton.setEnabled(false);
				removeDownloadButton.setEnabled(true);
			}

		}).start();
	}// GEN-LAST:event_startDownloadButtonActionPerformed

	private void stopDownloadButtonActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_stopDownloadButtonActionPerformed
		interrupted = true;
	}// GEN-LAST:event_stopDownloadButtonActionPerformed

	private void removeDownloadButtonActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeDownloadButtonActionPerformed
		int[] selectedRows = downloadTable.getSelectedRows();
		List<Download> remove = new LinkedList<>();

		for (int i = 0; i < selectedRows.length; i++) {
			remove.add(downloads.get(downloadTable
					.convertRowIndexToModel(selectedRows[i])));
		}

		downloads.removeAll(remove);

		downloadsTableModel.fireTableDataChanged();
	}// GEN-LAST:event_removeDownloadButtonActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Windows look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Windows is not available, stay with the default look and feel. For
		 * details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel
		 * /plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;

				}
			}
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException
				| javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MangaDownloader.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MangaDownloader().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JCheckBox chapterDeSelectAllCheckBox;
	private javax.swing.JPanel chapterListPanel;
	private javax.swing.JScrollPane chapterListScrollPane;
	private javax.swing.JLabel chapterListSearchLabel;
	private javax.swing.JTextField chapterListSearchTextField;
	private javax.swing.JTable chapterListTable;
	private javax.swing.JButton downloadButton;
	private javax.swing.JPanel downloadPanel;
	private javax.swing.JScrollPane downloadScrollPane;
	private javax.swing.JTable downloadTable;
	private javax.swing.JPanel mangaChapterPanel;
	private javax.swing.JPanel mangaListPanel;
	private javax.swing.JScrollPane mangaListScrollPane;
	private javax.swing.JLabel mangaListSearchLabel;
	private javax.swing.JTextField mangaListSearchTextField;
	private javax.swing.JTable mangaListTable;
	private javax.swing.JButton mangasDirButton;
	private javax.swing.JFileChooser mangasDirFileChooser;
	private javax.swing.JPanel mangasDirPanel;
	private javax.swing.JTextField mangasDirTextField;
	private javax.swing.JButton removeDownloadButton;
	private javax.swing.JButton selectSiteButton;
	private javax.swing.JTextField selectedSiteLabel;
	private javax.swing.JPanel sitePanel;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JButton startDownloadButton;
	private javax.swing.JButton stopDownloadButton;
	// End of variables declaration//GEN-END:variables
}
