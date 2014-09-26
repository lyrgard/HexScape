package fr.lyrgard.hexScape.model.marker;

public class UnknownTypeMarkerInstance extends MarkerInstance {
	
	private String hiddenMarkerTypeId;
	
	private int number;
	
	public UnknownTypeMarkerInstance(String id, String markerDefinitionId, String hiddenMarkerTypeId, int number) {
		super(markerDefinitionId);
		setId(id);
		this.hiddenMarkerTypeId = hiddenMarkerTypeId;
		this.number = number;
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
}
