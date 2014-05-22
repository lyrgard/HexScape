package fr.lyrgard.hexScape.model.marker;

import java.io.File;

public class MarkerDefinition {
	
	private String id;
	
	private MarkerType type;

	private String name;
	
	private File image;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MarkerType getType() {
		return type;
	}

	public void setType(MarkerType type) {
		this.type = type;
	}
	
}
