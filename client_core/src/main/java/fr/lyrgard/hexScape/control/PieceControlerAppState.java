package fr.lyrgard.hexScape.control;


import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.message.PieceMovedMessage;
import fr.lyrgard.hexScape.message.PiecePlacedMessage;
import fr.lyrgard.hexScape.message.PieceRemovedMessage;
import fr.lyrgard.hexScape.message.PieceSelectedMessage;
import fr.lyrgard.hexScape.message.PieceUnselectedMessage;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.service.DirectionService;
import fr.lyrgard.hexScape.service.PieceManager;

public class PieceControlerAppState extends AbstractAppState implements ActionListener {

	private static final String CLICK_MAPPING = "ControlerAppState_click";
	private static final String CANCEL_MAPPING = "ControlerAppState_cancel";
	private static final String DELETE_MAPPING = "ControlerAppState_delete";
	private static final String MOUSE_WHEEL_UP_MAPPING = "ControlerAppState_mouseWheelUp";
	private static final String MOUSE_WHEEL_DOWN_MAPPING = "ControlerAppState_mouseWheelDown";
	
	private PlacePieceByMouseAppState placePieceByMouseAppState = new PlacePieceByMouseAppState();
	private SelectPieceByMouseAppState selectPieceByMouseAppState = new SelectPieceByMouseAppState();
	
	private State currentState = State.WAITING;
	
	private InputManager inputManager;
	
	private enum State {
		WAITING, ADDING_PIECE, MOVING_PIECE, SELECTING_PIECE
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		placePieceByMouseAppState.initialize(stateManager, app);
		selectPieceByMouseAppState.initialize(stateManager, app);
		
		changeStateTo(State.WAITING);
		
		inputManager = app.getInputManager();
		
		
		inputManager.addMapping(CLICK_MAPPING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(CANCEL_MAPPING, new KeyTrigger(KeyInput.KEY_ESCAPE));
		inputManager.addMapping(DELETE_MAPPING, new KeyTrigger(KeyInput.KEY_DELETE));
		inputManager.addMapping(MOUSE_WHEEL_UP_MAPPING, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping(MOUSE_WHEEL_DOWN_MAPPING, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
	
		inputManager.addListener(this, CLICK_MAPPING, CANCEL_MAPPING, DELETE_MAPPING, MOUSE_WHEEL_UP_MAPPING, MOUSE_WHEEL_DOWN_MAPPING);
	}
	
	public void beginAddingPiece(PieceManager piece) {
		placePieceByMouseAppState.setPieceToPlace(piece);
		changeStateTo(State.ADDING_PIECE);
	}
	
	public void beginMovingSelectedPiece() {
		if (currentState == State.SELECTING_PIECE) {
			changeStateTo(State.MOVING_PIECE);
		}
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (currentState == State.ADDING_PIECE || currentState == State.MOVING_PIECE) {
			placePieceByMouseAppState.update(tpf);
		}
		if (selectPieceByMouseAppState.isEnabled()) {
			selectPieceByMouseAppState.update(tpf);
		}
	}

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
		PieceManager piece;
	
		if (!isEnabled()) {
			return;
		}
		
		if (name.equals(CLICK_MAPPING)) {
			switch (currentState) {
			case WAITING:
				if (keyPressed && selectPieceByMouseAppState.getPieceUnderMouse() != null) {
					changeStateTo(State.SELECTING_PIECE);
					if (selectPieceByMouseAppState.getSelectedPiece() != null) {
						changeStateTo(State.MOVING_PIECE);
					}
				}
				break;
			case ADDING_PIECE:
				if (keyPressed) {
					changeStateTo(State.SELECTING_PIECE);
				}
				break;
			case MOVING_PIECE:
				changeStateTo(State.SELECTING_PIECE);
				break;
			case SELECTING_PIECE:
				changeStateTo(State.SELECTING_PIECE);
				if (keyPressed && selectPieceByMouseAppState.getSelectedPiece() == selectPieceByMouseAppState.getPieceUnderMouse()) {
					changeStateTo(State.MOVING_PIECE);
				}
				break;
			}

		} else if (name.equals(CANCEL_MAPPING) && keyPressed) {
			switch (currentState) {
			case WAITING:
				// Nothing to do here
				break;
			case ADDING_PIECE:
				changeStateTo(State.WAITING);
				break;
			case MOVING_PIECE:
				changeStateTo(State.SELECTING_PIECE);
				break;
			case SELECTING_PIECE:
				changeStateTo(State.WAITING);
				break;
			}
		} else if (name.equals(MOUSE_WHEEL_UP_MAPPING)) {
			switch (currentState) {
			case WAITING:
				// Nothing to do here
				break;
			case ADDING_PIECE:
				rotatePiece(placePieceByMouseAppState.getPieceToPlace(), true);
				break;
			case MOVING_PIECE:
				rotatePiece(placePieceByMouseAppState.getPieceToPlace(), true);
				break;
			case SELECTING_PIECE:
				rotatePiece(selectPieceByMouseAppState.getSelectedPiece(), true);
				break;
			}
		} else if (name.equals(MOUSE_WHEEL_DOWN_MAPPING)) {
			switch (currentState) {
			case WAITING:
				// Nothing to do here
				break;
			case ADDING_PIECE:
				rotatePiece(placePieceByMouseAppState.getPieceToPlace(), false);
				break;
			case MOVING_PIECE:
				rotatePiece(placePieceByMouseAppState.getPieceToPlace(), false);
				break;
			case SELECTING_PIECE:
				rotatePiece(selectPieceByMouseAppState.getSelectedPiece(), false);
				break;
			}
		} else if (name.equals(DELETE_MAPPING) && keyPressed) {
			switch (currentState) {
			case WAITING:
				// Nothing to do here
				break;
			case ADDING_PIECE:
				changeStateTo(State.WAITING);
				break;
			case MOVING_PIECE:
				piece = placePieceByMouseAppState.getPieceToPlace();
				if (piece != null) {
					HexScapeCore.getInstance().getMapManager().removePiece(piece);
					CoreMessageBus.post(new PieceRemovedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				}
				changeStateTo(State.WAITING);
				break;
			case SELECTING_PIECE:
				piece = selectPieceByMouseAppState.getSelectedPiece();
				if (piece != null) {
					HexScapeCore.getInstance().getMapManager().removePiece(piece);
					CoreMessageBus.post(new PieceRemovedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				}
				changeStateTo(State.WAITING);
				break;
			}
		}
	}
	
	public void rotatePiece(PieceManager pieceManager, boolean clockwise) {
		Direction newDir = DirectionService.getInstance().rotate(pieceManager.getPiece().getDirection(), clockwise);
		pieceManager.rotate(newDir);
		CoreMessageBus.post(new PieceMovedMessage(HexScapeCore.getInstance().getPlayerId(), pieceManager.getPiece().getId(), pieceManager.getPiece().getX(), pieceManager.getPiece().getY(),pieceManager.getPiece().getZ(), pieceManager.getPiece().getDirection()));
	}
	
	private void changeStateTo(State newState) {
		PieceManager piece;
		
		switch (newState) {
		case WAITING:
			switch (currentState) {
			case SELECTING_PIECE:
				piece = selectPieceByMouseAppState.getSelectedPiece();
				CoreMessageBus.post(new PieceUnselectedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				selectPieceByMouseAppState.cancelSelection();
				break;
			case MOVING_PIECE:
				piece = selectPieceByMouseAppState.getSelectedPiece();
				CoreMessageBus.post(new PieceUnselectedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				selectPieceByMouseAppState.cancelSelection();
				break;
			default:
				break;
			}
			placePieceByMouseAppState.setEnabled(false);
			selectPieceByMouseAppState.setEnabled(true);
			break;
		case ADDING_PIECE:
			switch (currentState) {
			case WAITING:
				// Nothing to do here
				break;
			case SELECTING_PIECE:
				piece = selectPieceByMouseAppState.getSelectedPiece();
				CoreMessageBus.post(new PieceUnselectedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				selectPieceByMouseAppState.cancelSelection();
				break;
			case ADDING_PIECE:
				// Nothing to do here
				break;
			case MOVING_PIECE:
				// Nothing to do here
				break;
			default:
				break;
			}
			selectPieceByMouseAppState.setEnabled(false);
			placePieceByMouseAppState.setEnabled(true);
			break;
		case MOVING_PIECE:
			switch (currentState) {
			case SELECTING_PIECE:
				placePieceByMouseAppState.setPieceToPlace(selectPieceByMouseAppState.getSelectedPiece());
				break;
			default:
				break;
			}
			selectPieceByMouseAppState.setEnabled(true);
			placePieceByMouseAppState.setEnabled(true);
			break;
		case SELECTING_PIECE:
			switch (currentState) {
			case WAITING:
				if (selectPieceByMouseAppState.selectPiece()) {
					piece = selectPieceByMouseAppState.getSelectedPiece();
					CoreMessageBus.post(new PieceSelectedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				}
				break;
			case SELECTING_PIECE:
				if (selectPieceByMouseAppState.selectPiece()) {
					piece = selectPieceByMouseAppState.getSelectedPiece();
					CoreMessageBus.post(new PieceSelectedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				} else {
					return;
				}
				break;
			case ADDING_PIECE:
				piece = placePieceByMouseAppState.getPieceToPlace();
				if (placePieceByMouseAppState.placePiece()) {
					CoreMessageBus.post(new PiecePlacedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getCard().getId(), piece.getPiece().getId(), piece.getPiece().getModelId(), piece.getPiece().getX(), piece.getPiece().getY(),piece.getPiece().getZ(), piece.getPiece().getDirection()));
					selectPieceByMouseAppState.selectPiece(piece);
					CoreMessageBus.post(new PieceSelectedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId()));
				} else {
					return;
				}
				break;
			case MOVING_PIECE:
				piece = placePieceByMouseAppState.getPieceToPlace();
				if (placePieceByMouseAppState.placePiece()) {
					CoreMessageBus.post(new PieceMovedMessage(HexScapeCore.getInstance().getPlayerId(), piece.getPiece().getId(), piece.getPiece().getX(), piece.getPiece().getY(),piece.getPiece().getZ(), piece.getPiece().getDirection()));
					selectPieceByMouseAppState.selectPiece(piece);
				} else {
					return;
				}
				break;
			default:
				break;
			}
			placePieceByMouseAppState.setEnabled(false);
			selectPieceByMouseAppState.setEnabled(true);
			break;
		}
		
		currentState = newState;
	}
}
