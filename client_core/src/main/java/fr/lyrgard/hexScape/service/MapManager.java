package fr.lyrgard.hexScape.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.bus.CoreMessageBus;
import fr.lyrgard.hexScape.control.PieceControlerAppState;
import fr.lyrgard.hexScape.io.virtualScape.VirtualScapeMapReader;
import fr.lyrgard.hexScape.message.WarningMessage;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.Scene;
import fr.lyrgard.hexScape.model.map.Decor;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.map.TileType;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class MapManager {

	private static VirtualScapeMapReader mapReader = new VirtualScapeMapReader();
	
	private Map map;
	
	private Node mapNode;
	
	private Spatial mapWithoutDecorsSpatial;
	
	private java.util.Map<String, PieceManager> pieceManagersByPieceIds = new ConcurrentHashMap<String, PieceManager>();

	public MapManager(Map map) {
		super();
		this.map = map;
	}
	
	public static MapManager fromFile(File file) {		
		return mapReader.readMap(file);
	}

	public void placePiece(PieceManager piece) {
		if (HexScapeCore.getInstance().getHexScapeJme3Application().getScene() == null) {
			CoreMessageBus.post(new WarningMessage(HexScapeCore.getInstance().getPlayerId(), "No map was loaded. Please load a map before trying to place pieces"));
			return;
		}
		PieceControlerAppState pieceController = HexScapeCore.getInstance().getHexScapeJme3Application().getPieceControlerAppState();
		pieceController.addPiece(piece);
	}

	public boolean placePiece(PieceManager piece, int x, int y, int z) {
		Scene scene = HexScapeCore.getInstance().getHexScapeJme3Application().getScene();
		if (scene == null) {
			CoreMessageBus.post(new WarningMessage(HexScapeCore.getInstance().getPlayerId(), "No map was loaded. Please load a map before trying to place pieces"));
			return false;
		}
		
		Tile nearestTile = getNearestTile(x, y, z);
		
		if (nearestTile != null) {
			PieceManager alreadyTherePiece = scene.getPiece(nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());
			if (alreadyTherePiece != null && alreadyTherePiece != piece) {
				// already another piece here !
				return false;
			}
			
			if (scene.contains(piece)) {
				scene.removePiece(piece);
			}
			
			pieceManagersByPieceIds.put(piece.getPiece().getId(), piece);
			HexScapeCore.getInstance().getHexScapeJme3Application().getScene().addPiece(piece, nearestTile.getX(), nearestTile.getY(), nearestTile.getZ());
			return true;
		} else {
			return false;
		}
	}

	public Map getMap() {
		return map;
	}

	public void moveSelectedPiece() {
		if (HexScapeCore.getInstance().getHexScapeJme3Application().getScene() == null) {
			CoreMessageBus.post(new WarningMessage(HexScapeCore.getInstance().getPlayerId(), "No map was loaded. Please load a map before trying to move a piece"));
			return;
		}
		PieceControlerAppState pieceController = HexScapeCore.getInstance().getHexScapeJme3Application().getPieceControlerAppState();
		pieceController.moveSelectedPiece();
	}

	public void removePiece(PieceManager piece) {
		Scene scene = HexScapeCore.getInstance().getHexScapeJme3Application().getScene();
		if (scene.contains(piece)) {
			pieceManagersByPieceIds.remove(piece.getPiece().getId());
			scene.removePiece(piece);
		}
	}
	
	public void addTile(TileType type, int x, int y, int z) {
		map.addTile(type, x, y, z);
	}
	
	public Tile getNearestTile(int x, int y, int z) {
		Tile nearestTile = null;
		
		List<Tile> tiles = getTiles(x, y);
		int minDistanceZ = Integer.MAX_VALUE;
		for (Tile tile : tiles) {
			int distanceZ = Math.abs(tile.getZ() - z);
			if (distanceZ < minDistanceZ) {
				minDistanceZ = distanceZ;
				nearestTile = tile;
			}
		}
		return nearestTile;
	}
	
	public List<Tile> getTiles(int x, int y) {
		List<Tile> results = new ArrayList<>();
		
		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : map.getTiles().values()) {
			java.util.Map<Integer, Tile> byY = byZ.get(y);
			if (byY != null) {
				Tile tile = byY.get(x);
				if (tile != null && tile.getNeighbours().get(Direction.TOP) == null) {
					// if the tile doesn't have a tile on top of it, we add it
					results.add(tile);
				}
			}
		}
		return results;
	}

	public List<Decor> getDecors() {
		return map.getDecors();
	}

	public Spatial getSpatial() {
		
		if (mapNode == null) {
			mapNode = new Node("mapNode");
			mapWithoutDecorsSpatial = getMapSpatial();
			Collection<Spatial> decorNodes = getDecorsSpatials();
			
			mapNode.attachChild(mapWithoutDecorsSpatial);
			for (Spatial decorNode : decorNodes) {
				mapNode.attachChild(decorNode);
			}
		}

		return mapNode;
	}
	
	private Collection<Spatial> getDecorsSpatials() {
		Collection<Spatial> results = new ArrayList<>();
		ExternalModelService externalModelService = ExternalModelService.getInstance(); 
		
		Vector3f spacePos = new Vector3f();
		
		for (Decor decor : getDecors()) {
			if (decor != null) {
				Spatial decorSpatial = externalModelService.getModel(decor.getName());
				
				CoordinateUtils.toSpaceCoordinate(decor.getX(), decor.getY(), decor.getZ(), spacePos);
				
				decorSpatial.setLocalRotation(new Quaternion().fromAngleAxis(DirectionService.getInstance().getAngle(decor.getDirection()), Vector3f.UNIT_Y));
				decorSpatial.setLocalTranslation(spacePos.x, spacePos.y, spacePos.z);
				
				results.add(decorSpatial);
			}
		}
		return results;
	}
	
	private Spatial getMapSpatial() {
		Mesh mapMesh = new Mesh();
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoord = new ArrayList<Vector2f>();
		List<Integer> indexes = new ArrayList<Integer>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		Queue<Tile> notAddedYetTiles = new LinkedList<>();

		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : map.getTiles().values()) {
			for (java.util.Map<Integer, Tile> byY : byZ.values()) {
				for (Tile tile : byY.values()) {
					notAddedYetTiles.add(tile);
				}
			}
		}

		while (notAddedYetTiles.size() != 0) {
			Tile tile = notAddedYetTiles.poll();
			
			float x3d = (2 * tile.getX() + tile.getY()) * TileMesh.TRANSLATION_X;
			float y3d = tile.getZ() * TileMesh.HEX_SIZE_Y;
			float z3d = (tile.getY()) * TileMesh.TRANSLATION_Z;
			
			addTileAndNeighbours(tile, vertices, texCoord, indexes, normals, notAddedYetTiles, x3d, y3d, z3d);
		}

		mapMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
		mapMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord.toArray(new Vector2f[texCoord.size()])));
		mapMesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(toIntArray(indexes)));
		mapMesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[normals.size()])));
		
		mapMesh.updateBound();
		
		Geometry geo = new Geometry("mapMesh", mapMesh);
		
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		Texture tileTexture = assetManager.loadTexture(
		        "asset/tiles/TileTexture.bmp");
		tileTexture.setMinFilter(MinFilter.BilinearNoMipMaps);
			
		Material mat = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseMaterialColors",true);
		mat.setTexture("DiffuseMap", tileTexture);
		mat.setColor("Ambient", ColorRGBA.White);
		mat.setColor("Diffuse",ColorRGBA.White);  // minimum material color
        mat.setColor("Specular",ColorRGBA.White); // for shininess
        mat.setFloat("Shininess", 50f); // [1,128] for shininess
		
		
		geo.setMaterial(mat);
		
		return geo;
	}

	private void addTileAndNeighbours(Tile tile, List<Vector3f> vertices, List<Vector2f> texCoord, List<Integer> indexes, List<Vector3f> normals, Queue<Tile> notAddedYetTiles, float currentX, float currentY, float currentZ) {

		for (Direction dir : Direction.values()) {
			Tile neighboor = tile.getNeighbours().get(dir);
			if (neighboor == null || TileService.getInstance().isHalfTile(neighboor.getType())) {
				int firstIndex = vertices.size();
				vertices.addAll(TileMesh.getVertices(dir, tile.getType(), currentX, currentY, currentZ));
				texCoord.addAll(TileMesh.getTexCoord(dir, tile.getType()));
				indexes.addAll(TileMesh.getIndex(dir, firstIndex));
				normals.addAll(TileMesh.getNormals(dir));
			}
		}

		for (Entry<Direction, Tile> entry : tile.getNeighbours().entrySet()) {
			Tile neighbour = entry.getValue();
			if (notAddedYetTiles.contains(neighbour)) {
				notAddedYetTiles.remove(neighbour);
				switch (entry.getKey()) {
				case BOTTOM:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX, currentY - TileMesh.HEX_SIZE_Y, currentZ);
					break;
				case TOP:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX, currentY + TileMesh.HEX_SIZE_Y, currentZ);
					break;
				case NORTH_EAST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX + TileMesh.TRANSLATION_X, currentY, currentZ + TileMesh.TRANSLATION_Z);
					break;
				case EAST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX + 2 * TileMesh.TRANSLATION_X, currentY, currentZ);
					break;
				case SOUTH_EAST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX + TileMesh.TRANSLATION_X, currentY, currentZ - TileMesh.TRANSLATION_Z);
					break;
				case SOUTH_WEST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX - TileMesh.TRANSLATION_X, currentY, currentZ - TileMesh.TRANSLATION_Z);
					break;
				case WEST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX - 2* TileMesh.TRANSLATION_X, currentY, currentZ);
					break;
				case NORTH_WEST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, notAddedYetTiles, currentX - TileMesh.TRANSLATION_X, currentY, currentZ + TileMesh.TRANSLATION_Z);
					break;
				}
			}
		}
	}

	private int[] toIntArray(List<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}

	public Spatial getMapWithoutDecorsSpatial() {
		return mapWithoutDecorsSpatial;
	}

	public java.util.Map<String, PieceManager> getPieceManagersByPieceIds() {
		return pieceManagersByPieceIds;
	}

}
