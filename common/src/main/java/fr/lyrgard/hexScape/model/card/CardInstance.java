package fr.lyrgard.hexScape.model.card;

import java.util.Comparator;
import java.util.TreeSet;

import fr.lyrgard.hexScape.model.marker.MarkerInstance;

public class CardInstance {
	
	private String id;
	
	private CardType type;
	
	private int number;
	
	private TreeSet<MarkerInstance> markers;

	public CardInstance(String id, CardType type, int number) {
		super();
		this.id = id;
		this.type = type;
		this.number = number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CardType getType() {
		return type;
	}

	public void setType(CardType type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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
