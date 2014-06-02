package fr.lyrgard.hexScape.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.event.ErrorEvent;
import fr.lyrgard.hexScape.model.ImageExtensionEnum;
import fr.lyrgard.hexScape.model.dice.DiceFace;
import fr.lyrgard.hexScape.model.dice.DiceType;

public class DiceService {

	private static final File baseFolder = new File("asset/dice");
	private static final String diePropertiesFilename = "dice.properties";
	private static final String dieIconFilename = "icon.png";
	
	private static final String NAME = "name";
	private static final String VALUES = "values";
	private static final String MAX_NUMBER_THROWN = "maxNumberThrown";
	
	
	private Map<String, DiceType> diceTypes;

	public List<DiceFace> roll(int number, DiceType type) {
		
		List<DiceFace> results = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			results.add(type.getFaces().get(roll(type)));
		}
		return results;
	}

	private int roll(DiceType type) {
		return (int)(Math.random() * (type.getFaces().size())); 
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
							
							type.setName(diceProperties.getProperty(NAME));
							
							String maxNumberThrownString = diceProperties.getProperty(MAX_NUMBER_THROWN);
							if (StringUtils.isNotEmpty(maxNumberThrownString)) {
								try {
									type.setMaxNumberThrown(Integer.parseInt(maxNumberThrownString));
								} catch (IllegalArgumentException e) {
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
										HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("No image was found for face \"" + face + "\" for dice \"" + folder.getAbsolutePath() + "\". Dice definition skiped"));
										break diceDefinition;
									}
								}
								DiceFace diceFace = new DiceFace();
								diceFace.setImage(faceFile);
								diceFace.setName(face);
								type.getFaces().add(diceFace);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						File diceIconFile = new File(folder, dieIconFilename);
						if (diceIconFile.exists() && diceIconFile.isFile() && diceIconFile.canRead()) {
							type.setIconFile(diceIconFile);
						}
						
						diceTypes.put(folder.getName(), type);
					}
				}
			}
		} else {
			HexScapeCore.getInstance().getEventBus().post(new ErrorEvent("The dice definition folder \"" + baseFolder + "\" was not found"));
		}
		
	}

	public Collection<DiceType> getDiceTypes() {
		if (diceTypes == null) {
			loadDiceTypes();
		}
		return diceTypes.values();
	}
}
