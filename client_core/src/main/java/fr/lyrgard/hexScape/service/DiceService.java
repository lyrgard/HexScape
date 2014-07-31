package fr.lyrgard.hexScape.service;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.ImageExtensionEnum;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;

public class DiceService {
	
	

	public static DiceService getInstance() {
		return INSTANCE;
	}
	
	
	private static final File baseFolder = new File("asset/dice");
	private static final String diePropertiesFilename = "dice.properties";
	private static final String dieIconFilename = "icon.png";
	
	private static final String NAME = "name";
	private static final String VALUES = "values";
	private static final String MAX_NUMBER_THROWN = "maxNumberThrown";
	private static final String BACKGROUND_COLOR = "backgroundColor";
	private static final String FOREGROUND_COLOR = "foregroundColor";
	
	private static final DiceService INSTANCE = new DiceService();
	
	private Map<String, DiceType> diceTypes;

	private DiceService() {
		loadDiceTypes();
	}
	
	private void loadDiceTypes() {
		diceTypes = new HashMap<>();
		if (baseFolder.exists()) {
			diceDefinition: for (File folder : baseFolder.listFiles()) {
				if (folder.exists() && folder.isDirectory()) {
					File dicePropertiesFile = new File(folder, diePropertiesFilename);
					if (dicePropertiesFile.exists() && dicePropertiesFile.isFile() && dicePropertiesFile.canRead()) {
						DiceType type = new DiceType();
						Properties diceProperties = new Properties();
						InputStream input;
						try {
							input = new FileInputStream(dicePropertiesFile);
							diceProperties.load(input);
							
							type.setId(folder.getName());
							type.setName(diceProperties.getProperty(NAME));
							
							String maxNumberThrownString = diceProperties.getProperty(MAX_NUMBER_THROWN);
							if (StringUtils.isNotEmpty(maxNumberThrownString)) {
								try {
									type.setMaxNumberThrown(Integer.parseInt(maxNumberThrownString));
								} catch (IllegalArgumentException e) {
								}
							}
							
							String backgroundColor = diceProperties.getProperty(BACKGROUND_COLOR);
							if (StringUtils.isNotEmpty(backgroundColor)) {
								try {
									type.setBackgroundColor(Color.decode(backgroundColor));
								} catch (NumberFormatException e) {
								}
							}
							
							String foregroundcolor = diceProperties.getProperty(FOREGROUND_COLOR);
							if (StringUtils.isNotEmpty(foregroundcolor)) {
								try {
									type.setForegroundColor(Color.decode(foregroundcolor));
								} catch (NumberFormatException e) {
								}
							}
							
							String[] faces = diceProperties.getProperty(VALUES).split(",");
							for (String face : faces) {
								face = face.trim();
								File faceFile = new File(folder, face);
								if (!faceFile.exists()) {
									for (ImageExtensionEnum extension : ImageExtensionEnum.values()) {
										faceFile = new File(folder, face + "." + extension.name().toLowerCase());
										if (faceFile.exists()) {
											break;
										}
										faceFile = null;
									}
									if (faceFile == null) {
										CoreMessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayerId(), "No image was found for face \"" + face + "\" for dice \"" + folder.getAbsolutePath() + "\". Dice definition skiped"));
										break diceDefinition;
									}
								}
								DiceFace diceFace = new DiceFace();
								diceFace.setImage(faceFile);
								diceFace.setName(face);
								diceFace.setId(face);
								type.getFaces().add(diceFace);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						File diceIconFile = new File(folder, dieIconFilename);
						if (diceIconFile.exists() && diceIconFile.isFile() && diceIconFile.canRead()) {
							type.setIconFile(diceIconFile);
						}
						
						diceTypes.put(type.getId(), type);
					}
				}
			}
		} else {
			CoreMessageBus.post(new ErrorMessage(HexScapeCore.getInstance().getPlayerId(), "The dice definition folder \"" + baseFolder + "\" was not found"));
		}
		
	}

	public Collection<DiceType> getDiceTypes() {
		if (diceTypes == null) {
			loadDiceTypes();
		}
		return diceTypes.values();
	}
	
	public DiceType getDiceType(String diceId) {
		if (diceTypes == null) {
			loadDiceTypes();
		}
		return diceTypes.get(diceId);
	}
}
