package fr.lyrgard.hexScape.gui.desktop.controller.game;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.screen.Screen;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.message.LookingFreelyMessage;
import fr.lyrgard.hexScape.message.LookingFromAboveMessage;
import fr.lyrgard.hexScape.message.LookingFromPieceMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;

public class CameraButtonController {

	private AbstractImageButtonController lookAtTheMapButton;
	private AbstractImageButtonController lookFromPointOfViewButton;
	private AbstractImageButtonController moveCameraFreelyButton;
	
	private boolean lookingFromPointOfView = false;
	private boolean pieceSelected = false;
	
	public CameraButtonController(Screen screen) {
		lookAtTheMapButton = screen.findControl("lookAtTheMapButton", AbstractImageButtonController.class);
		lookFromPointOfViewButton = screen.findControl("lookFromPointOfViewButton", AbstractImageButtonController.class);
		moveCameraFreelyButton = screen.findControl("moveCameraFreelyButton", AbstractImageButtonController.class);
		
		lookFromPointOfViewButton.setVisible(false);
	}
	
	@Subscribe public void onLookingAtTheMap(LookingFromAboveMessage message) {
		lookingFromPointOfView = false;
		lookAtTheMapButton.activate();
		moveCameraFreelyButton.desactivate();
		lookFromPointOfViewButton.desactivate();
		if (!pieceSelected) {
			lookFromPointOfViewButton.setVisible(false);
		}
	}
	
	@Subscribe public void onLookingFromPointOfView(LookingFromPieceMessage message) {
		lookingFromPointOfView = true;
		lookFromPointOfViewButton.activate();
		lookAtTheMapButton.desactivate();
		moveCameraFreelyButton.desactivate();
	}

	@Subscribe public void onLookingFreely(LookingFreelyMessage message) {
		lookingFromPointOfView = false;
		moveCameraFreelyButton.activate();
		lookAtTheMapButton.desactivate();
		lookFromPointOfViewButton.desactivate();
		if (!pieceSelected) {
			lookFromPointOfViewButton.setVisible(false);
		}
	}
	
	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {
		pieceSelected = true;
		lookFromPointOfViewButton.setVisible(true);
	}
	
	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		pieceSelected = false;
		if (!lookingFromPointOfView) {
			lookFromPointOfViewButton.setVisible(false);
		}
	}
}
