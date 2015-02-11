package fr.lyrgard.hexScape.control;

import java.util.Collection;

import com.jme3.app.state.AbstractAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.model.SecondarySelectMarker;
import fr.lyrgard.hexScape.model.SelectMarker;
import fr.lyrgard.hexScape.service.SelectMarkerService;

public class SelectMarkerAppState extends AbstractAppState {
	
	private float selectMarkerYOffset = 0.3f;
	private float selectMarkerYVariation = 0.2f;
	private float time = 0;
	
	private float selectMarkerRotation;
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		time = (time + 2 * tpf) % (Float.MAX_VALUE);
		Collection<SelectMarker> selectMarkers = SelectMarkerService.getInstance().getSelectMarkers();
		int i = 0;
		int number = selectMarkers.size();
		for (SelectMarker selectMarker : selectMarkers) {
			float thisMarkerTime = (time + FastMath.TWO_PI * i/number) % FastMath.TWO_PI;
			selectMarkerRotation = (thisMarkerTime + tpf/128) % FastMath.TWO_PI;
			Vector3f localTranslation = selectMarker.getSpatial().getLocalTranslation();
			localTranslation.y = selectMarkerYOffset + selectMarkerYVariation * FastMath.sin(thisMarkerTime);
			selectMarker.getSpatial().setLocalTranslation(localTranslation);
			selectMarker.getSpatial().getLocalRotation().fromAngleAxis(selectMarkerRotation, Vector3f.UNIT_Y);
			for (SecondarySelectMarker secondarySelectMarker : selectMarker.getSecondarySelectMarkers()) {
				localTranslation = secondarySelectMarker.getSpatial().getLocalTranslation();
				localTranslation.y = selectMarkerYOffset + selectMarkerYVariation * FastMath.sin(thisMarkerTime);
				secondarySelectMarker.getSpatial().setLocalTranslation(localTranslation);
				secondarySelectMarker.getSpatial().getLocalRotation().fromAngleAxis(selectMarkerRotation, Vector3f.UNIT_Y);
			}
			i++;
		}
	}

}
