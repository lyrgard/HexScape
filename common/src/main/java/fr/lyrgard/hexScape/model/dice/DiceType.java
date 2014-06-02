package fr.lyrgard.hexScape.model.dice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DiceType {
	
	private String name;
	
	private File iconFile;
	
	private int maxNumberThrown = -1;
	
	private List<DiceFace> faces;

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
}
