package fr.lyrgard.hexScape.gui.desktop.action;

import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.MoveablePiece;

public class AddPieceAction extends AbstractAction {

	private static final long serialVersionUID = 6124817922902744899L;

	private String pieceName;
	private static final ImageIcon icon = new ImageIcon(ChooseMapAction.class.getResource("/gui/icons/add_piece.png"));
	
	public AddPieceAction(String pieceName) {
		super("", icon);
		this.pieceName = pieceName;  
	}

	public void actionPerformed(ActionEvent paramActionEvent) {
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			public Void call() throws Exception {
				HexScapeCore.getInstance().getMapService().placePiece(new MoveablePiece(pieceName));
				return null;
			}
		});		

		
	}
}
