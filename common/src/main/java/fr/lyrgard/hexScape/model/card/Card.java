package fr.lyrgard.hexScape.model.card;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import fr.lyrgard.hexScape.model.marker.MarkerInstance;

public class Card {

	private String id;
	
	private String name;
	
	private List<String> figureNames = new ArrayList<>();
	
	private File folder;
	
	private TreeSet<MarkerInstance> markers;

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

	public TreeSet<MarkerInstance> getMarkers() {
		if (markers == null) {
			markers = new TreeSet<>(new Comparator<MarkerInstance>() {

				@Override
				public int compare(MarkerInstance m1, MarkerInstance m2) {
					return m1.getMarkerDefinition().getId().compareTo(m2.getMarkerDefinition().getId());
				}
			});
		}
		return markers;
	}
}
