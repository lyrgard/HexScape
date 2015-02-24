package fr.lyrgard.hexScape.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.util.BufferUtils;
import com.jme3.util.SkyFactory;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.control.PieceControlerAppState;
import fr.lyrgard.hexScape.io.virtualScape.VirtualScapeMapReader;
import fr.lyrgard.hexScape.model.map.Decor;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.utils.CoordinateUtils;

public class MapManager {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MapManager.class);

	private static VirtualScapeMapReader mapReader = new VirtualScapeMapReader();

	private Map map;

	private Node sceneNode;

	public Node getSelectablePieceNode() {
		return selectablePieceNode;
	}

	private Node selectablePieceNode;

	private Node mapNode;

	private Node mapWithoutDecorsNode;

	private java.util.Map<String, PieceManager> pieceManagersByPieceIds = new ConcurrentHashMap<String, PieceManager>();

	public MapManager(Map map) {
		super();
		this.map = map;
		sceneNode = new Node("sceneNode");
		selectablePieceNode = new Node("selectablePiece");
		
		

		sceneNode.attachChild(selectablePieceNode);
	}

	public static MapManager fromFile(File file) {		
		return mapReader.readMap(file);
	}

	public static MapManager fromInputStream(InputStream stream) {
		byte[] bytes;
		try {
			bytes = ByteStreams.toByteArray(stream);
			return mapReader.readMap(bytes, "");
		} catch (IOException e) {
			LOGGER.error("Error while reading map", e);
		}
		return null;
	}

	public void beginPlacingPiece(PieceManager piece) {

		PieceControlerAppState pieceController = HexScapeCore.getInstance().getHexScapeJme3Application().getPieceControlerAppState();
		pieceController.beginAddingPiece(piece);
	}


	public boolean placePiece(PieceManager piece, int x, int y, int z, Direction direction) {

		for (PieceManager otherPiece : pieceManagersByPieceIds.values()) {

			if (otherPiece != piece && 
					otherPiece.getPiece().getX() == x  &&
					otherPiece.getPiece().getY() == y && 
					otherPiece.getPiece().getZ() == z ) {
				//already another piece here !
				return false;
			}
		}

		if (!pieceManagersByPieceIds.containsKey(piece.getPiece().getId())) {
			pieceManagersByPieceIds.put(piece.getPiece().getId(), piece);
		}

		if (!selectablePieceNode.hasChild(piece.getSpatial())) {
			selectablePieceNode.attachChild(piece.getSpatial());
		}

		piece.moveTo(x, y, z, direction);
		return true;
	}

	public Map getMap() {
		return map;
	}

	public void moveSelectedPiece() {
		PieceControlerAppState pieceController = HexScapeCore.getInstance().getHexScapeJme3Application().getPieceControlerAppState();
		pieceController.beginMovingSelectedPiece();
	}

	public void removePiece(PieceManager piece) {
		if (pieceManagersByPieceIds.containsKey(piece.getPiece().getId())) {
			pieceManagersByPieceIds.remove(piece.getPiece().getId());
			selectablePieceNode.detachChild(piece.getSpatial());
		}
	}

	public void addTile(int x, int y, int z, boolean halfSize, int topTexture, int sideTexture, boolean visible, boolean startZone, int startZoneNumber) {
		map.addTile(x, y, z, halfSize, topTexture, sideTexture, visible, startZone, startZoneNumber);
	}

	public Tile getNearestTile(int x, int y, int z) {
		Tile nearestTile = null;

		List<Tile> tiles = getTiles(x, y);
		int minDistanceZ = Integer.MAX_VALUE;
		for (Tile tile : tiles) {
			int distanceZ = Math.abs(tile.getZ() - z + 1);
			if (distanceZ < minDistanceZ) {
				minDistanceZ = distanceZ;
				nearestTile = tile;
			}
		}
		return nearestTile;
	}

	public List<Tile> getTiles(int x, int y) {
		List<Tile> results = new ArrayList<>();

		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : map.getTilesMap().values()) {
			java.util.Map<Integer, Tile> byY = byZ.get(y);
			if (byY != null) {
				Tile tile = byY.get(x);
				if (tile != null) {
					Tile topNeighbours = tile.getNeighbours().get(Direction.TOP);
					if (topNeighbours == null || !topNeighbours.isVisible()) {
						// if the tile doesn't have a tile on top of it, we add it
						results.add(tile);
					}
				}
			}
		}
		return results;
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

		for (PieceManager piece : pieceManagersByPieceIds.values()) {
			if (piece.getPiece().getX() == x && piece.getPiece().getY() == y) {
				results.add(piece);
			}
		}
		return results;
	}

	public boolean contains(PieceManager piece) {
		return pieceManagersByPieceIds.containsKey(piece.getPiece().getId());
	}

	public List<Decor> getDecors() {
		return map.getDecors();
	}

	public Spatial getSpatial() {

		if (mapNode == null) {
			mapNode = new Node("mapNode");
			populateMapNode();
			sceneNode.attachChild(mapNode);
		}

		return sceneNode;
	}
	
	public void redraw() {
		populateMapNode();
	}
	
	private void populateMapNode() {
		mapNode.detachAllChildren();
		mapWithoutDecorsNode = new Node("mapWithoutDecorsNode");
		Spatial visibleMap = getMapSpatial();
		Spatial invisibleMap = getInvisibleTilesSpatial();
		Collection<Spatial> decorNodes = getDecorsSpatials();
		Node startZones = getStartZones();

		
		mapWithoutDecorsNode.attachChild(visibleMap);
		if (invisibleMap != null) {
			mapWithoutDecorsNode.attachChild(invisibleMap);
		}

		mapNode.attachChild(mapWithoutDecorsNode);
		
		for (Spatial decorNode : decorNodes) {
			mapNode.attachChild(decorNode);
		}
		if (startZones != null) {
			mapNode.attachChild(startZones);
		}
		
//		Box b = new Box(50, 10, 1); // create cube shape
//        Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
//        Material mat = new Material(HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager(),
//          "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
//        mat.setColor("Color", ColorRGBA.Red);   // set color of material to blue
//        geom.setMaterial(mat);                   // set the cube's material
//        mapNode.attachChild(geom);       
	}

	private Node getStartZones() {
		Node node = new Node();
		node.setShadowMode(ShadowMode.Off);
		
		
		
		java.util.Map<Integer, List<Tile>> startZones = new HashMap<Integer, List<Tile>>();
		
		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : map.getTilesMap().values()) {
			for (java.util.Map<Integer, Tile> byY : byZ.values()) {
				for (Tile tile : byY.values()) {
					if (tile.isStartZone()) {
						List<Tile> list = startZones.get(tile.getStartZoneNumber());
						if (list == null) {
							list = new ArrayList<>();
							startZones.put(tile.getStartZoneNumber(), list);
						}
						list.add(tile);
						
						
					}
				}
			}
		}
		
		if (startZones.isEmpty()) {
			return null;
		}
		
		
		for (Entry<Integer, List<Tile>> startZone : startZones.entrySet()) {
			Mesh lineMesh = new Mesh();
			lineMesh.setMode(Mesh.Mode.Lines);
			
			List<Vector3f> vertices = new ArrayList<Vector3f>();
			List<Integer> indexes = new ArrayList<Integer>();
			
			for (Tile tile : startZone.getValue()) {
				float x3d = (2 * tile.getX() + tile.getY()) * TileMesh.TRANSLATION_X;
				float y3d = tile.getZ() * TileMesh.HEX_SIZE_Y;
				float z3d = (tile.getY()) * TileMesh.TRANSLATION_Z;
				
				for (Direction dir : Direction.values()) {
					if (dir != Direction.BOTTOM && dir != Direction.TOP) {
						Tile neighbours = tile.getNeighbours().get(dir);
						if (neighbours == null || !neighbours.isStartZone()) {
							int firstIndex = vertices.size();
							vertices.addAll(TileMesh.getEdgeVertices(dir, tile.isHalfSize(), x3d, y3d, z3d));
							indexes.addAll(TileMesh.getEdgeIndex(firstIndex));
						}
					}
				}
			}
			
			lineMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
			lineMesh.setBuffer(Type.Index,    2, BufferUtils.createIntBuffer(toIntArray(indexes)));
			lineMesh.setLineWidth(50f);
			lineMesh.updateBound();
			lineMesh.updateCounts();
			
			Geometry lineGeometry = new Geometry("line", lineMesh);
			AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			ColorEnum color = ColorEnum.values()[startZone.getKey() % ColorEnum.values().length];
			;
			mat.setColor("Color", new ColorRGBA(color.getColor().getRed()/255f, color.getColor().getGreen()/255f, color.getColor().getBlue()/255f, 0));
			lineGeometry.setMaterial(mat);
			
			node.attachChild(lineGeometry);
		}
		
		
		
		return node;
	}

	private Collection<Spatial> getDecorsSpatials() {
		Collection<Spatial> results = new ArrayList<>();
		ExternalModelService externalModelService = ExternalModelService.getInstance(); 

		Vector3f spacePos = new Vector3f();

		for (Decor decor : getDecors()) {
			if (decor != null) {
				Spatial decorSpatial = externalModelService.getModel(decor.getName());

				if (decorSpatial != null) {
					CoordinateUtils.toSpaceCoordinate(decor.getX(), decor.getY(), decor.getZ(), spacePos);

					decorSpatial.setLocalRotation(new Quaternion().fromAngleAxis(DirectionService.getInstance().getAngle(decor.getDirection()), Vector3f.UNIT_Y));
					decorSpatial.setLocalTranslation(spacePos.x, spacePos.y, spacePos.z);

					results.add(decorSpatial);
				}
			}
		}
		return results;
	}

	private Spatial getMapSpatial() {
		Mesh mapMesh = new Mesh();
		
		Texture tileTexture = TextureService.getInstance().getTileTexture();

		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoord = new ArrayList<Vector2f>();
		List<Integer> indexes = new ArrayList<Integer>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		Queue<Tile> notAddedYetTiles = new LinkedList<>();

		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : map.getTilesMap().values()) {
			for (java.util.Map<Integer, Tile> byY : byZ.values()) {
				for (Tile tile : byY.values()) {
					if (tile.isVisible()) {
						notAddedYetTiles.add(tile);
					}
				}
			}
		}

		while (notAddedYetTiles.size() != 0) {
			Tile tile = notAddedYetTiles.poll();

			float x3d = (2 * tile.getX() + tile.getY()) * TileMesh.TRANSLATION_X;
			float y3d = tile.getZ() * TileMesh.HEX_SIZE_Y;
			float z3d = (tile.getY()) * TileMesh.TRANSLATION_Z;

			addTileToMesh(tile, vertices, texCoord, indexes, normals, x3d, y3d, z3d);
			
			//addTileAndNeighbours(tile, vertices, texCoord, indexes, normals, notAddedYetTiles, x3d, y3d, z3d);
		}

		mapMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
		mapMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord.toArray(new Vector2f[texCoord.size()])));
		mapMesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(toIntArray(indexes)));
		mapMesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[normals.size()])));

		mapMesh.updateBound();

		Geometry geo = new Geometry("mapMesh", mapMesh);

		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();

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

	private Spatial getInvisibleTilesSpatial() {
		Mesh invisibleTilesMesh = new Mesh();

		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Integer> indexes = new ArrayList<Integer>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		Queue<Tile> notAddedYetTiles = new LinkedList<>();

		for (java.util.Map<Integer, java.util.Map<Integer, Tile>> byZ : map.getTilesMap().values()) {
			for (java.util.Map<Integer, Tile> byY : byZ.values()) {
				for (Tile tile : byY.values()) {
					if (!tile.isVisible()) {
						notAddedYetTiles.add(tile);
					}
				}
			}
		}

		if (notAddedYetTiles.isEmpty()) {
			return null;
		}
		
		while (notAddedYetTiles.size() != 0) {
			Tile tile = notAddedYetTiles.poll();

			float x3d = (2 * tile.getX() + tile.getY()) * TileMesh.TRANSLATION_X;
			float y3d = tile.getZ() * TileMesh.HEX_SIZE_Y;
			float z3d = (tile.getY()) * TileMesh.TRANSLATION_Z;

			addTileDirectionToMesh(tile, vertices, null, indexes, normals, x3d, y3d, z3d, Direction.TOP);
		}

		invisibleTilesMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
		invisibleTilesMesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(toIntArray(indexes)));
		invisibleTilesMesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[normals.size()])));

		invisibleTilesMesh.updateBound();

		Geometry geo = new Geometry("invisibleTilesMesh", invisibleTilesMesh);

		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();


		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", new ColorRGBA(1, 1, 1, 0));
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);



		geo.setMaterial(mat);
		geo.setQueueBucket(Bucket.Translucent);
		geo.setShadowMode(ShadowMode.Off);


		return geo;
	}


	private void addTileToMesh(Tile tile, List<Vector3f> vertices, List<Vector2f> texCoord, List<Integer> indexes, List<Vector3f> normals, float currentX, float currentY, float currentZ) {
		for (Direction dir : Direction.values()) {
			Tile neighboor = tile.getNeighbours().get(dir);
			if (neighboor == null || !neighboor.isVisible() || neighboor.isHalfSize()) {
				addTileDirectionToMesh(tile, vertices, texCoord, indexes, normals, currentX, currentY, currentZ, dir);
			}
		}
	}

	private void addTileDirectionToMesh(Tile tile, List<Vector3f> vertices, List<Vector2f> texCoord, List<Integer> indexes, List<Vector3f> normals, float currentX, float currentY, float currentZ, Direction dir) {
		int firstIndex = vertices.size();
		vertices.addAll(TileMesh.getVertices(dir, tile.isHalfSize(), currentX, currentY, currentZ));
		if (texCoord != null) {
			texCoord.addAll(TileMesh.getTexCoord(dir, tile.getTopTexture(), tile.getSideTexture()));
		}
		indexes.addAll(TileMesh.getIndex(dir, firstIndex));
		normals.addAll(TileMesh.getNormals(dir));
	}


	private int[] toIntArray(List<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}

	public Spatial getMapWithoutDecorsNode() {
		return mapWithoutDecorsNode;
	}

	public java.util.Map<String, PieceManager> getPieceManagersByPieceIds() {
		return pieceManagersByPieceIds;
	}

}
