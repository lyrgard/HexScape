package fr.lyrgard.hexScape.gui.desktop.view.game;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.google.common.eventbus.Subscribe;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.gui.desktop.action.LookAtMapAction;
import fr.lyrgard.hexScape.gui.desktop.action.LookFreelyAction;
import fr.lyrgard.hexScape.gui.desktop.action.LookFromPieceAction;
import fr.lyrgard.hexScape.message.LookingFreelyMessage;
import fr.lyrgard.hexScape.message.LookingFromAboveMessage;
import fr.lyrgard.hexScape.message.LookingFromPieceMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;

public class ActionMenu extends JPanel {


	private static final long serialVersionUID = -3836798307597735579L;
	
	
	//final private JButton placePiece = new JButton();
	final private JButton lookAtPointOfView = new JButton();
	final private JButton lookAtMap = new JButton(new LookAtMapAction());
	final private JButton lookFreely = new JButton(new LookFreelyAction());
	
	private String selectedPieceId;
	
	public ActionMenu() {
		setLayout(new FlowLayout());
		
		lookAtPointOfView.setVisible(false);
		lookAtMap.setVisible(false);
		lookFreely.setVisible(true);
		
		
		add(lookAtPointOfView);
		add(lookAtMap);
		add(lookFreely);
		
		GuiMessageBus.register(this);
	}
	
//	@Subscribe public void onCardSelected(CardSelectedMessage message) {
//		//final CardInstance card = message.getCard();
//
//		EventQueue.invokeLater(new Runnable() {
//
//			public void run() {
//				//lookAtPointOfView.setAction(new AddPieceAction(pieceModelId, card));
//				lookAtPointOfView.setVisible(true);
//			}
//		});
//	}
	
	@Subscribe public void onPieceSelected(PieceSelectedMessage message) {

		final String pieceId = message.getPieceId();
		String playerId = message.getPlayerId();
		selectedPieceId = pieceId; 

		if (playerId != null && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					lookAtPointOfView.setAction(new LookFromPieceAction(pieceId));
					lookAtPointOfView.setVisible(true);
				}
			});
		}
	}

	@Subscribe public void onPieceUnselected(PieceUnselectedMessage message) {
		String playerId = message.getPlayerId();
		selectedPieceId = null;

		if (playerId != null && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					lookAtPointOfView.setVisible(false);
				}
			});
		}
	}

	@Subscribe public void onPieceRemoved(PieceRemovedMessage message) {
		String playerId = message.getPlayerId();
		selectedPieceId = null;

		if (playerId != null && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					lookAtPointOfView.setVisible(false);
				}
			});
		}
	}
	
	@Subscribe public void onLookingFromPiece(LookingFromPieceMessage message) {
		String playerId = message.getPlayerId();
		String pieceId = message.getPieceId();
		selectedPieceId = pieceId;

		if (playerId != null && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					lookAtPointOfView.setVisible(false);
					lookAtMap.setVisible(true);
					lookFreely.setVisible(true);
				}
			});
		}
	}
	
	@Subscribe public void onLookingFromAbove(LookingFromAboveMessage message) {
		String playerId = message.getPlayerId();

		if (playerId != null && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					if (selectedPieceId != null) {
						new LookFromPieceAction(selectedPieceId);
						lookAtPointOfView.setVisible(true);
					}
					lookAtMap.setVisible(false);
					lookFreely.setVisible(true);
				}
			});
		}
	}
	
	@Subscribe public void onLookingFreely(LookingFreelyMessage message) {
		String playerId = message.getPlayerId();

		if (playerId != null && playerId.equals(CurrentUserInfo.getInstance().getPlayerId())) {
			EventQueue.invokeLater(new Runnable() {

				public void run() {
					if (selectedPieceId != null) {
						new LookFromPieceAction(selectedPieceId);
						lookAtPointOfView.setVisible(true);
					}
					lookAtMap.setVisible(true);
					lookFreely.setVisible(false);
				}
			});
		}
	}
}
