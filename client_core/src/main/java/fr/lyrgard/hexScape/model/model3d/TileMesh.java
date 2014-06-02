package fr.lyrgard.hexScape.model.model3d;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.map.TileType;
import fr.lyrgard.hexScape.service.DirectionService;
import fr.lyrgard.hexScape.service.TileService;

public class TileMesh {
	
//	public static final float HEX_SIZE_X = 1.73205f; 
//	public static final float HEX_SIZE_Y = 0.4f;
//	public static final float HEX_SIZE_Z = 2;
//	
//	public static final float TRANSLATION_X = HEX_SIZE_X / 2f;
//	public static final float TRANSLATION_Z = 3 * HEX_SIZE_Z / 4f;
//	
//	static final float v1x = 0;
//	static final float v1z = HEX_SIZE_Z/2;
//
//	static final float v2x = HEX_SIZE_Y/2;
//	static final float v2z = v1z/2;
//
//	static final float v3x = v2x;
//	static final float v3z = -v2z;
//
//	static final float v4x = v1x;
//	static final float v4z = -v1z;
//
//	static final float v5x = -v2x;
//	static final float v5z = -v2z;
//
//	static final float v6x = -v2x;
//	static final float v6z = v2z;
//
//	static final float bottom = 0;
//	static final float top = HEX_SIZE_Y;

	public static final float HEX_SIZE_X = 1.73205f; 
	public static final float HEX_SIZE_Y = 0.4f;
	public static final float HEX_SIZE_Z = 2;
	
	public static final float TRANSLATION_X = HEX_SIZE_X / 2f;
	public static final float TRANSLATION_Z = -3 * HEX_SIZE_Z / 4f;
	
	static final float v1x = 0;
	static final float v1z = HEX_SIZE_Z/2;

	static final float v2x = HEX_SIZE_X/2;
	static final float v2z = v1z/2;

	static final float v3x = v2x;
	static final float v3z = -v2z;

	static final float v4x = v1x;
	static final float v4z = -v1z;

	static final float v5x = -v2x;
	static final float v5z = -v2z;

	static final float v6x = -v2x;
	static final float v6z = v2z;

	static final float bottom = 0;
	static final float top = HEX_SIZE_Y;

	public static List<Vector3f> getVertices(Direction direction, TileType tileType, float currentX, float currentY, float currentZ) {
		
		float tileTop = top;
		if (TileService.getInstance().isHalfTile(tileType)) {
			tileTop /= 2;
		}
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		switch (direction) {
		case TOP:
			vertices.add(new Vector3f(v1x + currentX, tileTop + currentY, v1z + currentZ));
			vertices.add(new Vector3f(v2x + currentX, tileTop + currentY, v2z + currentZ));
			vertices.add(new Vector3f(v3x + currentX, tileTop + currentY, v3z + currentZ));
			vertices.add(new Vector3f(v4x + currentX, tileTop + currentY, v4z + currentZ));
			vertices.add(new Vector3f(v5x + currentX, tileTop + currentY, v5z + currentZ));
			vertices.add(new Vector3f(v6x + currentX, tileTop + currentY, v6z + currentZ));
			break;
		case BOTTOM:
			// nothing to do here
			break;
		case NORTH_EAST:
			vertices.add(new Vector3f(v3x + currentX,tileTop + currentY, v3z + currentZ));
			vertices.add(new Vector3f(v4x + currentX,tileTop + currentY, v4z + currentZ));
			vertices.add(new Vector3f(v3x + currentX,bottom + currentY, v3z + currentZ));
			vertices.add(new Vector3f(v4x + currentX,bottom + currentY, v4z + currentZ));
			break;
		case EAST:
			vertices.add(new Vector3f(v2x + currentX,tileTop + currentY, v2z + currentZ));
			vertices.add(new Vector3f(v3x + currentX,tileTop + currentY, v3z + currentZ));
			vertices.add(new Vector3f(v2x + currentX,bottom + currentY, v2z + currentZ));
			vertices.add(new Vector3f(v3x + currentX,bottom + currentY, v3z + currentZ));
			break;
		case SOUTH_EAST:
			vertices.add(new Vector3f(v1x + currentX,tileTop + currentY, v1z + currentZ));
			vertices.add(new Vector3f(v2x + currentX,tileTop + currentY, v2z + currentZ));
			vertices.add(new Vector3f(v1x + currentX,bottom + currentY, v1z + currentZ));
			vertices.add(new Vector3f(v2x + currentX,bottom + currentY, v2z + currentZ));
			break;
		case SOUTH_WEST:
			vertices.add(new Vector3f(v6x + currentX,tileTop + currentY, v6z + currentZ));
			vertices.add(new Vector3f(v1x + currentX,tileTop + currentY, v1z + currentZ));
			vertices.add(new Vector3f(v6x + currentX,bottom + currentY, v6z + currentZ));
			vertices.add(new Vector3f(v1x + currentX,bottom + currentY, v1z + currentZ));
			break;
		case WEST:
			vertices.add(new Vector3f(v5x + currentX,tileTop + currentY, v5z + currentZ));
			vertices.add(new Vector3f(v6x + currentX,tileTop + currentY, v6z + currentZ));
			vertices.add(new Vector3f(v5x + currentX,bottom + currentY, v5z + currentZ));
			vertices.add(new Vector3f(v6x + currentX,bottom + currentY, v6z + currentZ));
			break;
		case NORTH_WEST:
			vertices.add(new Vector3f(v4x + currentX,tileTop + currentY, v4z + currentZ));
			vertices.add(new Vector3f(v5x + currentX,tileTop + currentY, v5z + currentZ));
			vertices.add(new Vector3f(v4x + currentX,bottom + currentY, v4z + currentZ));
			vertices.add(new Vector3f(v5x + currentX,bottom + currentY, v5z + currentZ));
			break;
		}
		return vertices;
	}
	
	public static List<Vector2f> getTexCoord(Direction direction, TileType type) {
		
		List<Vector2f> texCoord = new ArrayList<Vector2f>();
		switch (direction) {
		case TOP:			
			texCoord.add(TileService.getInstance().getTopTexture(type).getCoord(0.5f,0));
			texCoord.add(TileService.getInstance().getTopTexture(type).getCoord(1,0.25f));
			texCoord.add(TileService.getInstance().getTopTexture(type).getCoord(1,0.75f));
			texCoord.add(TileService.getInstance().getTopTexture(type).getCoord(0.5f,1));
			texCoord.add(TileService.getInstance().getTopTexture(type).getCoord(0,0.75f));
			texCoord.add(TileService.getInstance().getTopTexture(type).getCoord(0,0.25f));
			break;
		case BOTTOM:
			// nothing to do here
			break;
		case NORTH_EAST:
		case EAST:
		case SOUTH_EAST:
		case SOUTH_WEST:
		case WEST:
		case NORTH_WEST:
			texCoord.add(TileService.getInstance().getSideTexture(type).getCoord(1,0));
			texCoord.add(TileService.getInstance().getSideTexture(type).getCoord(0,0));
			texCoord.add(TileService.getInstance().getSideTexture(type).getCoord(1,1));
			texCoord.add(TileService.getInstance().getSideTexture(type).getCoord(0,1));
			break;
		}
		return texCoord;
	}
	
	public static List<Integer> getIndex(Direction direction, int firstIndex) {
		
		List<Integer> indexes = new ArrayList<Integer>();
		switch (direction) {
		case TOP:
			indexes.add(firstIndex + 1);
			indexes.add(firstIndex + 5);
			indexes.add(firstIndex);
			
			indexes.add(firstIndex + 3);
			indexes.add(firstIndex + 4);
			indexes.add(firstIndex + 5);
			
			indexes.add(firstIndex + 1);
			indexes.add(firstIndex + 2);
			indexes.add(firstIndex + 3);
			
			indexes.add(firstIndex + 3);
			indexes.add(firstIndex + 5);
			indexes.add(firstIndex + 1);
			break;
		case BOTTOM:
			// nothing to do here
			break;
		case NORTH_EAST:
		case EAST:
		case SOUTH_EAST:
		case SOUTH_WEST:
		case WEST:
		case NORTH_WEST:
			indexes.add(firstIndex + 2);
			indexes.add(firstIndex + 1);
			indexes.add(firstIndex);
			
			indexes.add(firstIndex + 2);
			indexes.add(firstIndex + 3);
			indexes.add(firstIndex + 1);
			break;
		}
		return indexes;
	}
	
public static List<Vector3f> getNormals(Direction direction) {
				
		List<Vector3f> normals = new ArrayList<Vector3f>();
		Vector3f normal;
		switch (direction) {
		case TOP:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		case BOTTOM:
			// nothing to do here
			break;
		case NORTH_EAST:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		case EAST:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		case SOUTH_EAST:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		case SOUTH_WEST:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		case WEST:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		case NORTH_WEST:
			normal = DirectionService.getInstance().getNormal(direction);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			normals.add(normal);
			break;
		}
		return normals;
	}
}
