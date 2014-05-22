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

import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class Scene implements Displayable {

	private Map map;
	
	private java.util.Map<Integer, java.util.Map<Integer, java.util.Map<Integer, MoveablePiece>>> pieces = new TreeMap<Integer, java.util.Map<Integer,java.util.Map<Integer,MoveablePiece>>>();
	
	private Set<MoveablePiece> piecesSet = new HashSet<>();
	
	private Node selectablePieceNode;
	
	private Node sceneNode;
	
	public Scene() {
		sceneNode = new Node("sceneNode");
		selectablePieceNode = new Node("selectablePiece");
		
		sceneNode.attachChild(selectablePieceNode);
	}
	
	public void addPiece(MoveablePiece piece, int x, int y, int z) {
		
		Vector3f spacePos = CoordinateUtils.toSpaceCoordinate(x, y, z);
		
		piece.setX(x);
		piece.setY(y);
		piece.setZ(z);
		
		Tile tile = map.getTile(x, y, z);
		
		if (tile.getType().isHalfTile()) {
			spacePos.y += TileMesh.HEX_SIZE_Y / 2;
		} else {
			spacePos.y += TileMesh.HEX_SIZE_Y;
		}
		
		selectablePieceNode.attachChild(piece.getSpatial());
		piece.getSpatial().setLocalTranslation(spacePos);
		piece.getSpatial().setLocalRotation(new Quaternion().fromAngleAxis(piece.getDirection().getAngle(), Vector3f.UNIT_Y));
		
		java.util.Map<Integer, java.util.Map<Integer, MoveablePiece>> byZ = pieces.get(z);
		if (byZ == null) {
			byZ = new HashMap<Integer, java.util.Map<Integer,MoveablePiece>>();
			pieces.put(z, byZ);
		}
		java.util.Map<Integer, MoveablePiece> byY = byZ.get(y);
		if (byY == null) {
			byY = new HashMap<Integer, MoveablePiece>();
			byZ.put(y, byY);
		}
		byY.put(x, piece);
		piecesSet.add(piece);
	}
	
	public boolean contains(MoveablePiece piece) {
		return piecesSet.contains(piece);
	}
	
	public void removePiece(MoveablePiece piece) {
		java.util.Map<Integer, java.util.Map<Integer, MoveablePiece>> byZ = pieces.get(piece.getZ());
		if (byZ != null) {
			java.util.Map<Integer, MoveablePiece> byY = byZ.get(piece.getY());
			if (byY != null) {
				byY.remove(piece.getX());
			}
		}
		piecesSet.remove(piece);
		selectablePieceNode.detachChild(piece.getSpatial());
	}
	
	public MoveablePiece getNearestPiece(int x, int y, int z) {
		MoveablePiece nearestPiece = null;
		
		List<MoveablePiece> pieces = getPieces(x, y);
		int minDistanceZ = Integer.MAX_VALUE;
		for (MoveablePiece piece : pieces) {
			int distanceZ = Math.abs(piece.getZ() - z);
			if (distanceZ < minDistanceZ) {
				minDistanceZ = distanceZ;
				nearestPiece = piece;
			}
		}
		return nearestPiece;
	}
	
	public List<MoveablePiece> getPieces(int x, int y) {
		List<MoveablePiece> results = new ArrayList<>();
		
		for (java.util.Map<Integer, java.util.Map<Integer, MoveablePiece>> byZ : pieces.values()) {
			java.util.Map<Integer, MoveablePiece> byY = byZ.get(y);
			if (byY != null) {
				MoveablePiece piece = byY.get(x);
				if (piece != null) {
					// if the tile doesn't have a tile on top of it, we add it
					results.add(piece);
				}
			}
		}
		return results;
	}
	
	public MoveablePiece getPiece(int x, int y, int z) {
		MoveablePiece piece = null;
		
		java.util.Map<Integer, java.util.Map<Integer, MoveablePiece>> byZ = pieces.get(z);
		if (byZ != null) {
			java.util.Map<Integer, MoveablePiece> byY = byZ.get(y);
			if (byY != null) {
				piece = byY.get(x);
			}
		}
		return piece;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		if (this.map != null) {
			sceneNode.detachChild(map.getSpatial());
		}
		this.map = map;
		
		if (map != null) {
			sceneNode.attachChild(map.getSpatial());
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
