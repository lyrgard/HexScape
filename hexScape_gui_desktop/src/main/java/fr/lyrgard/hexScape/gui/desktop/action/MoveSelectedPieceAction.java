package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;

public class MoveSelectedPieceAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;
	private static final ImageIcon icon = new ImageIcon(MoveSelectedPieceAction.class.getResource("/gui/icons/move_figure.png"));

	public MoveSelectedPieceAction() {
		super("move", icon);
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeCore.getInstance().getMapService().moveSelectedPiece();
				return null;
			}
		});		

		
	}
}
