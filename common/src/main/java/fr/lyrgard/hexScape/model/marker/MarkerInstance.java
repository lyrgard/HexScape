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
	    
	    }) 
public class MarkerInstance implements Comparable<MarkerInstance>{
	
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

	@Override
	public int compareTo(MarkerInstance m2) {
		return getMarkerDefinitionId().compareTo(m2.getMarkerDefinitionId());
	}
}
