package fr.lyrgard.hexScape.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.service.DirectionService;
import fr.lyrgard.hexScape.service.MapManager;
import fr.lyrgard.hexScape.service.PieceManager;
import fr.lyrgard.hexScape.service.TileService;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class Scene implements Displayable {

	private MapManager mapManager;
	
	private java.util.Map<Integer, java.util.Map<Integer, java.util.Map<Integer, PieceManager>>> pieces = new TreeMap<Integer, java.util.Map<Integer,java.util.Map<Integer,PieceManager>>>();
	
	private Set<PieceManager> piecesSet = new HashSet<>();
	
	private Node selectablePieceNode;
	
	private Node sceneNode;
	
	public Scene() {
		sceneNode = new Node("sceneNode");
		selectablePieceNode = new Node("selectablePiece");
		
		sceneNode.attachChild(selectablePieceNode);
	}
	
	public void addPiece(PieceManager pieceManager, int x, int y, int z) {
		
		Vector3f spacePos = CoordinateUtils.toSpaceCoordinate(x, y, z);
		
		pieceManager.getPiece().setX(x);
		pieceManager.getPiece().setY(y);
		pieceManager.getPiece().setZ(z);
		
		Tile tile = mapManager.getMap().getTile(x, y, z);
		
		if (TileService.getInstance().isHalfTile(tile.getType())) {
			spacePos.y += TileMesh.HEX_SIZE_Y / 2;
		} else {
			spacePos.y += TileMesh.HEX_SIZE_Y;
		}
		
		selectablePieceNode.attachChild(pieceManager.getSpatial());
		pieceManager.getSpatial().setLocalTranslation(spacePos);
		float angle = DirectionService.getInstance().getAngle(pieceManager.getPiece().getDirection());
		pieceManager.getSpatial().setLocalRotation(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
		
		java.util.Map<Integer, java.util.Map<Integer, PieceManager>> byZ = pieces.get(z);
		if (byZ == null) {
			byZ = new HashMap<Integer, java.util.Map<Integer,PieceManager>>();
			pieces.put(z, byZ);
		}
		java.util.Map<Integer, PieceManager> byY = byZ.get(y);
		if (byY == null) {
			byY = new HashMap<Integer, PieceManager>();
			byZ.put(y, byY);
		}
		byY.put(x, pieceManager);
		piecesSet.add(pieceManager);
	}
	
	public boolean contains(PieceManager piece) {
		return piecesSet.contains(piece);
	}
	
	public void removePiece(PieceManager pieceManager) {
		java.util.Map<Integer, java.util.Map<Integer, PieceManager>> byZ = pieces.get(pieceManager.getPiece().getZ());
		if (byZ != null) {
			java.util.Map<Integer, PieceManager> byY = byZ.get(pieceManager.getPiece().getY());
			if (byY != null) {
				byY.remove(pieceManager.getPiece().getX());
			}
		}
		piecesSet.remove(pieceManager);
		selectablePieceNode.detachChild(pieceManager.getSpatial());
	}
	
	public PieceManager getNearestPiece(int x, int y, int z) {
		PieceManager nearestPiece = null;
		
		List<PieceManager> pieces = getPieces(x, y);
		int minDistanceZ = Integer.MAX_VALUE;
		for (PieceManager pieceManager : pieces) {
			int distanceZ = Math.abs(pieceManager.getPiece().getZ() - z);
			if (distanceZ < minDistanceZ) {
				minDistanceZ = distanceZ;
				nearestPiece = pieceManager;
			}
		}
		return nearestPiece;
	}
	
	public List<PieceManager> getPieces(int x, int y) {
		List<PieceManager> results = new ArrayList<>();
		
		for (java.util.Map<Integer, java.util.Map<Integer, PieceManager>> byZ : pieces.values()) {
			java.util.Map<Integer, PieceManager> byY = byZ.get(y);
			if (byY != null) {
				PieceManager piece = byY.get(x);
				if (piece != null) {
					// if the tile doesn't have a tile on top of it, we add it
					results.add(piece);
				}
			}
		}
		return results;
	}
	
	public PieceManager getPiece(int x, int y, int z) {
		PieceManager piece = null;
		
		java.util.Map<Integer, java.util.Map<Integer, PieceManager>> byZ = pieces.get(z);
		if (byZ != null) {
			java.util.Map<Integer, PieceManager> byY = byZ.get(y);
			if (byY != null) {
				piece = byY.get(x);
			}
		}
		return piece;
	}

	public MapManager getMapManager() {
		return mapManager;
	}

	public void setMapManager(MapManager mapManager) {
		if (this.mapManager != null) {
			sceneNode.detachChild(mapManager.getSpatial());
		}
		this.mapManager = mapManager;
		
		if (mapManager != null) {
			sceneNode.attachChild(mapManager.getSpatial());
		}
	}


	@Override
	public Spatial getSpatial() {
		return sceneNode;
	}

	public Node getSelectablePieceNode() {
		return selectablePieceNode;
	}
}
