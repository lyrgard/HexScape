package fr.lyrgard.hexScape.model.marker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;


@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
@JsonSubTypes({
		@Type(value = MarkerInstance.class, name = "MarkerInstance"),
		@Type(value = RevealableMarkerInstance.class, name = "RevealableMarkerInstance"),
	    @Type(value = StackableMarkerInstance.class, name = "StackableMarkerInstance"),
	    @Type(value = HiddenMarkerInstance.class, name = "HiddenMarkerInstance"),
	    @Type(value = UnknownTypeMarkerInstance.class, name = "UnknownTypeMarkerInstance"),
	    }) 
public class MarkerInstance implements Comparable<MarkerInstance>{
	
	private String id;
	
	private String markerDefinitionId;

	@JsonCreator
	public MarkerInstance(@JsonProperty("markerDefinitionId") String markerDefinitionId) {
		this.markerDefinitionId = markerDefinitionId;
	}

	public String getMarkerDefinitionId() {
		return markerDefinitionId;
	}

	public void setMarkerDefinitionId(String markerDefinitionId) {
		this.markerDefinitionId = markerDefinitionId;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public int compareTo(MarkerInstance m2) {
		int result = getMarkerDefinitionId().compareTo(m2.getMarkerDefinitionId());
		if (result == 0) {
			if (this instanceof HiddenMarkerInstance) {
				if (((HiddenMarkerInstance)this).getHiddenMarkerDefinitionId() == null) {
					if (((HiddenMarkerInstance)m2).getHiddenMarkerDefinitionId() == null) {
						result = 0;
					} else {
						result = 1;
					}
				} else {
					if (((HiddenMarkerInstance)m2).getHiddenMarkerDefinitionId() == null) {
						result = -1;
					} else {
						result = ((HiddenMarkerInstance)this).getHiddenMarkerDefinitionId().compareTo(((HiddenMarkerInstance)m2).getHiddenMarkerDefinitionId());
					}
				}
			}
			
		}
		if (result == 0) {
			result = getId().compareTo(m2.getId());
		}
		return result;
	}

	public void setId(String id) {
		this.id = id;
	}

	
}
