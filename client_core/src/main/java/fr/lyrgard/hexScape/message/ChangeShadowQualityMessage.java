package fr.lyrgard.hexScape.message;

import fr.lyrgard.hexScape.model.QualitySetting;

public class ChangeShadowQualityMessage extends AbstractMessage {

	private QualitySetting quality;

	/**
	 * Change the shadow quality
	 * @param value must be 0, 1024, 2048, 4096. 0 desactivate shadows
	 */
	public ChangeShadowQualityMessage(QualitySetting quality) {
		this.quality = quality;
	}
	
	public QualitySetting getQuality() {
		return quality;
	}
}
