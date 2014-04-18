package fr.lyrgard.hexScape.model.model3d.mesh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.model.Direction;
import fr.lyrgard.hexScape.model.Map;
import fr.lyrgard.hexScape.model.Tile;

public class MapMesh extends Mesh {

	private Map map;
	
	private Collection<Node> decorNodes = new ArrayList<>();

	public MapMesh(Map map) {
		this.map = map;
		update();
	}

	public void update() {
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texCoord = new ArrayList<Vector2f>();
		List<Integer> indexes = new ArrayList<Integer>();
		Set<Tile> alreadyAddedTiles = new HashSet<Tile>();

		Tile tile = map.getFirstTile();

		addTileAndNeighbours(tile, vertices, texCoord, indexes, alreadyAddedTiles, 0, 0, 0);

		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[vertices.size()])));
		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord.toArray(new Vector2f[texCoord.size()])));
		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(toIntArray(indexes)));
		updateBound();
	}

	private void addTileAndNeighbours(Tile tile, List<Vector3f> vertices, List<Vector2f> texCoord, List<Integer> indexes, Set<Tile> alreadyAddedTiles, float currentX, float currentY, float currentZ) {

		for (Direction dir : Direction.values()) {
			Tile neighboor = tile.getNeighbours().get(dir);
			if (neighboor == null || neighboor.getType().isHalfTile()) {
				int firstIndex = vertices.size();
				vertices.addAll(TileMesh.getVertices(dir, tile.getType(), currentX, currentY, currentZ));
				texCoord.addAll(TileMesh.getTexCoord(dir, tile.getType()));
				indexes.addAll(TileMesh.getIndex(dir, firstIndex));
			}
		}
		alreadyAddedTiles.add(tile);

		for (Entry<Direction, Tile> entry : tile.getNeighbours().entrySet()) {
			Tile neighbour = entry.getValue();
			if (!alreadyAddedTiles.contains(neighbour)) {
//				switch (entry.getKey()) {
//				case BOTTOM:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX - - TileMesh.HEX_SIZE_Z, currentY, currentZ);
//					break;
//				case TOP:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX + TileMesh.HEX_SIZE_Z, currentY, currentZ);
//					break;
//				case NORTH_EAST:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX, currentY + TileMesh.TRANSLATION_Y, currentZ + TileMesh.TRANSLATION_Z);
//					break;
//				case EAST:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX, currentY + 2 * TileMesh.TRANSLATION_Y, currentZ);
//					break;
//				case SOUTH_EAST:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX, currentY + TileMesh.TRANSLATION_Y, currentZ - TileMesh.TRANSLATION_Z);
//					break;
//				case SOUTH_WEST:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX, currentY - TileMesh.TRANSLATION_Y, currentZ - TileMesh.TRANSLATION_Z);
//					break;
//				case WEST:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX, currentY - 2* TileMesh.TRANSLATION_Y, currentZ);
//					break;
//				case NORTH_WEST:
//					addTileAndNeighbours(neighbour, vertices, texCoord, indexes, alreadyAddedTiles, currentX, currentY - TileMesh.TRANSLATION_Y, currentZ + TileMesh.TRANSLATION_Z);
//					break;
//				}
			}
		}
	}

	private int[] toIntArray(List<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
			ret[i] = list.get(i);
		return ret;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}


}
