package fr.lyrgard.hexScape.model.model3d;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
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
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.io.virtualScape.VirtualScapeMapReader;
import fr.lyrgard.hexScape.model.Decor;
import fr.lyrgard.hexScape.model.Direction;
import fr.lyrgard.hexScape.model.Map;
import fr.lyrgard.hexScape.model.Tile;
import fr.lyrgard.hexScape.model.model3d.mesh.TileMesh;

public class MapLoader {
	
	private AssetManager assetManager;
	private ExternalModelLoader externalModelLoader;

	public MapLoader(AssetManager assetManager) {
		this.assetManager = assetManager;
		this.externalModelLoader = new ExternalModelLoader(assetManager);
	}



	public Node getMap(File file) {
		Node mapNode = new Node("map");
		
		VirtualScapeMapReader mapReader = new VirtualScapeMapReader();
		Map map = mapReader.readMap(file);

		
		
		
		Spatial mapSpatial = getMapSpatial(map);
		Collection<Spatial> decorNodes = getDecorsSpatials(map);
		
		mapNode.attachChild(mapSpatial);
		for (Spatial decorNode : decorNodes) {
			mapNode.attachChild(decorNode);
		}

		
		
		return mapNode;
	}
	
	private Collection<Spatial> getDecorsSpatials(Map map) {
		Collection<Spatial> results = new ArrayList<>();
		for (Decor decor : map.getDecors()) {
			if (decor != null) {
				Spatial decorSpatial = externalModelLoader.getModel(decor.getName());
				
				float x3d = (2 * decor.getX() + decor.getY()) * TileMesh.TRANSLATION_X;
				float y3d = decor.getZ() * TileMesh.HEX_SIZE_Y;
				float z3d = (decor.getY()) * TileMesh.TRANSLATION_Z;
				
				decorSpatial.setLocalRotation(new Quaternion().fromAngleAxis(decor.getDirection().getAngle(), Vector3f.UNIT_Y));
				decorSpatial.setLocalTranslation(x3d, y3d, z3d);
				
				results.add(decorSpatial);
			}
		}
		return results;
	}
	
	private Spatial getMapSpatial(Map map) {
		Mesh mapMesh = new Mesh();
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoord = new ArrayList<Vector2f>();
		List<Integer> indexes = new ArrayList<Integer>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		Set<Tile> alreadyAddedTiles = new HashSet<Tile>();

		Tile tile = map.getFirstTile();
		
		float x3d = (2 * tile.getX() + tile.getY()) * TileMesh.TRANSLATION_X;
		float y3d = tile.getZ() * TileMesh.HEX_SIZE_Y;
		float z3d = (tile.getY()) * TileMesh.TRANSLATION_Z;

		addTileAndNeighbours(tile, vertices, texCoord, indexes, normals, alreadyAddedTiles, x3d, y3d, z3d);

		mapMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
		mapMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord.toArray(new Vector2f[texCoord.size()])));
		mapMesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(toIntArray(indexes)));
		mapMesh.setBuffer(Type.Normal,   3, BufferUtils.createFloatBuffer(normals.toArray(new Vector3f[normals.size()])));
		
		mapMesh.updateBound();
		
		Geometry geo = new Geometry("mapMesh", mapMesh);
		
		Texture tileTexture = assetManager.loadTexture(
		        "model/texture/tile/TileTexture.bmp");
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

	private void addTileAndNeighbours(Tile tile, List<Vector3f> vertices, List<Vector2f> texCoord, List<Integer> indexes, List<Vector3f> normals, Set<Tile> alreadyAddedTiles, float currentX, float currentY, float currentZ) {

		for (Direction dir : Direction.values()) {
			Tile neighboor = tile.getNeighbours().get(dir);
			if (neighboor == null || neighboor.getType().isHalfTile()) {
				int firstIndex = vertices.size();
				vertices.addAll(TileMesh.getVertices(dir, tile.getType(), currentX, currentY, currentZ));
				texCoord.addAll(TileMesh.getTexCoord(dir, tile.getType()));
				indexes.addAll(TileMesh.getIndex(dir, firstIndex));
				normals.addAll(TileMesh.getNormals(dir));
			}
		}
		
		alreadyAddedTiles.add(tile);

		for (Entry<Direction, Tile> entry : tile.getNeighbours().entrySet()) {
			Tile neighbour = entry.getValue();
			if (!alreadyAddedTiles.contains(neighbour)) {
				switch (entry.getKey()) {
				case BOTTOM:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX, currentY - TileMesh.HEX_SIZE_Y, currentZ);
					break;
				case TOP:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX, currentY + TileMesh.HEX_SIZE_Y, currentZ);
					break;
				case NORTH_EAST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX + TileMesh.TRANSLATION_X, currentY, currentZ + TileMesh.TRANSLATION_Z);
					break;
				case EAST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX + 2 * TileMesh.TRANSLATION_X, currentY, currentZ);
					break;
				case SOUTH_EAST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX + TileMesh.TRANSLATION_X, currentY, currentZ - TileMesh.TRANSLATION_Z);
					break;
				case SOUTH_WEST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX - TileMesh.TRANSLATION_X, currentY, currentZ - TileMesh.TRANSLATION_Z);
					break;
				case WEST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX - 2* TileMesh.TRANSLATION_X, currentY, currentZ);
					break;
				case NORTH_WEST:
					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, normals, alreadyAddedTiles, currentX - TileMesh.TRANSLATION_X, currentY, currentZ + TileMesh.TRANSLATION_Z);
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
}
