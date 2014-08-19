package fr.lyrgard.hexScape.gui.desktop.components.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.message.InfoMessage;
import fr.lyrgard.hexScape.service.AssetService;

public class AssetLoaderPanel extends JPanel {

	private static final long serialVersionUID = -2593663852886307208L;
	
	private JTextField zipFileLabel;
	
	//private JProgressBar progressBar;
	
	private File assetFile;
	
	public AssetLoaderPanel() {
		this.setLayout(new MigLayout(
				"wrap", // Layout Constraints
				"[right][left]", // Column constraints
				"[]20[]" // Row constraints
				));
		
		final JButton importButton = new JButton("Import assets");
		importButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (assetFile != null) {
					//progressBar.setVisible(true);
					AssetService.getInstance().importAssets(assetFile);
					//progressBar.setVisible(false);
					GuiMessageBus.post(new InfoMessage(HexScapeCore.getInstance().getPlayerId(), "Assets imported"));
				}
			}
		});
		importButton.setEnabled(false);
		
		
		JButton chooseFile = new JButton();
		chooseFile.setText("Choose file");
		chooseFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"HexScape Data files", "hsd");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(AssetLoaderPanel.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					assetFile = chooser.getSelectedFile();

					zipFileLabel.setText(assetFile.getAbsolutePath());
					
					importButton.setEnabled(true);
				}
				
			}
		});
		zipFileLabel = new JTextField(50);
		zipFileLabel.setEditable(false);
		
//		progressBar = new JProgressBar();
//		progressBar.setVisible(false);
//		progressBar.setIndeterminate(true);
		
		
		add(chooseFile);
		add(zipFileLabel);
		//add(progressBar, "span 2, align center");
		add(importButton, "span 2, align center");
	}
	
	
	
	
}
