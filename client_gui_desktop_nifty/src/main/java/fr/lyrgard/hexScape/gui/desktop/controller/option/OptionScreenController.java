package fr.lyrgard.hexScape.gui.desktop.controller.option;

import java.awt.EventQueue;
import java.io.File;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.lyrgard.hexScape.gui.desktop.HexScapeFrame;
import fr.lyrgard.hexScape.service.AssetService;
import fr.lyrgard.hexScape.service.ConfigurationService;

public class OptionScreenController implements ScreenController {

	final private static ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
	
	private Nifty nifty;
	private Screen screen;
	private DropDown<String> gameTypeDropDown;
	private TextField playerNameTextField;
	private TextField serverUrlTextField;
	private Element saveSuccessMessage;
	private Element importAssetSuccessMessage;

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onStartScreen() {
		playerNameTextField = screen.findNiftyControl("playerNameTextField", TextField.class);
		playerNameTextField.setText(ConfigurationService.getInstance().getUserName());
		
		gameTypeDropDown = screen.findNiftyControl("gameTypeDropDown", DropDown.class);
		refreshGameTypes();

		serverUrlTextField = screen.findNiftyControl("serverUrlTextField", TextField.class);
		serverUrlTextField.setText(ConfigurationService.getInstance().getServerHost());
		
		saveSuccessMessage = screen.findElementByName("saveSuccessMessage");
		importAssetSuccessMessage = screen.findElementByName("importAssetSuccessMessage");
	}
	
	private void refreshGameTypes() {
		List<String> gameTypes = ConfigurationService.getInstance().getGameFolders();
		gameTypeDropDown.removeAllItems(gameTypeDropDown.getItems());
		for (String gameType : gameTypes) {
			gameTypeDropDown.addItem(gameType);
		}
		gameTypeDropDown.selectItem(ConfigurationService.getInstance().getGameFolder());
	}
	
	public void saveOption() {
		boolean gameChanged = !ConfigurationService.getInstance().getGameFolder().equals(gameTypeDropDown.getSelection());
		
		ConfigurationService.getInstance().setUserName(playerNameTextField.getRealText());
		ConfigurationService.getInstance().setGameFolder(gameTypeDropDown.getSelection());
		ConfigurationService.getInstance().setServerHost(serverUrlTextField.getRealText());
		ConfigurationService.getInstance().save();
		
		if (gameChanged) {
			AssetService.getInstance().reloadAssets();
		}
		
		saveSuccessMessage.setVisible(true);
		
		exec.schedule(new Runnable() {
			
			@Override
			public void run() {
				saveSuccessMessage.setVisible(false);
			}
		}, 2, TimeUnit.SECONDS);
	}
	
	public void importAsset() {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"HexScape Data files", "hsd");
				chooser.setFileFilter(filter);

				int returnVal = chooser.showOpenDialog(HexScapeFrame.getInstance());
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File assetFile = chooser.getSelectedFile();

					if (assetFile != null) {
						Element importingAssetPopup = nifty.createPopup("importingAssetPopup");
						nifty.showPopup(nifty.getCurrentScreen(), importingAssetPopup.getId(), null);
						
						boolean resultOk = AssetService.getInstance().importAssets(assetFile);
						nifty.closePopup(importingAssetPopup.getId());
						if (resultOk) {
							importAssetSuccessMessage.setVisible(true);
							// There may be new game types avalaible
							refreshGameTypes();
							
							exec.schedule(new Runnable() {
								
								@Override
								public void run() {
									importAssetSuccessMessage.setVisible(false);
								}
							}, 2, TimeUnit.SECONDS);
						}
					}
				}
			}
		});
	}
}
