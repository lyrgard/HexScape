package fr.lyrgard.hexScape.model.marker;

public class MarkerInfo {

	private String id;
	
	private String markerTypeId;
	
	private String hiddenMarkerTypeId;
	
	private int number;
	
	private String cardId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMarkerTypeId() {
		return markerTypeId;
	}

	public void setMarkerTypeId(String markerTypeId) {
		this.markerTypeId = markerTypeId;
	}

	public String getHiddenMarkerTypeId() {
		return hiddenMarkerTypeId;
	}

	public void setHiddenMarkerTypeId(String hiddenMarkerTypeId) {
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
}
