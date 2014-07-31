package fr.lyrgard.hexScape.model.dice;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiceType {
	
	private String id;
	
	private String name;
	
	private File iconFile;
	
	private int maxNumberThrown = -1;
	
	private List<DiceFace> faces;
	
	private Color backgroundColor;
	
	private Color foregroundColor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DiceFace> getFaces() {
		if (faces == null) {
			faces = new ArrayList<>();
		}
		return faces;
	}

	public File getIconFile() {
		return iconFile;
	}

	public void setIconFile(File iconFile) {
		this.iconFile = iconFile;
	}

	public int getMaxNumberThrown() {
		return maxNumberThrown;
	}

	public void setMaxNumberThrown(int maxNumberThrown) {
		this.maxNumberThrown = maxNumberThrown;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}	
}
