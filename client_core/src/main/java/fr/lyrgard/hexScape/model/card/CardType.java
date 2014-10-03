package fr.lyrgard.hexScape.model.card;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardType {

	private String id;
	
	private String name;
	
	private List<String> figureNames = new ArrayList<>();
	
	private File folder;
	
	private String iconPath;
	
	private String imagePath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFigureNames() {
		return figureNames;
	}

	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
