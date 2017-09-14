package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.QualitySetting;

public class ChangeSamplingMessage extends AbstractMessage {

	private QualitySetting quality;

	/**
	 * Change the antialiasing sampling
	 * @param value must be between 0 and 4. 0 is antialiasing off, 4 is best quality
	 */
	public ChangeSamplingMessage(QualitySetting quality) {
		this.quality = quality;
	}
	
	public QualitySetting getQuality() {
		return quality;
	}
}
