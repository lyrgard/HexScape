package fr.lyrgard.hexScape.utils;

import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.io.virtualScape.bean.Vector3i;
import fr.lyrgard.hexScape.model.model3d.TileMesh;

public class CoordinateUtils {

	public static Vector3i toMapCoordinate(float x, float y, float z) {
		
		int xMap = Math.round(x / (2 * TileMesh.TRANSLATION_X) - z / (2 * TileMesh.TRANSLATION_Z));
		int yMap = Math.round(z / TileMesh.TRANSLATION_Z);
		int zMap = Math.round(y / TileMesh.HEX_SIZE_Y);
		
		Vector3i mapPos = new Vector3i(xMap, yMap, zMap);
		
		return mapPos;
	}
	
	public static Vector3f toSpaceCoordinate(int x, int y, int z) {
		
		float x3d = (2 * x + y) * TileMesh.TRANSLATION_X;
		float y3d = z * TileMesh.HEX_SIZE_Y;
		float z3d = y * TileMesh.TRANSLATION_Z;
		
		Vector3f spacePos = new Vector3f(x3d, y3d, z3d);
		
		return spacePos;
	}
	
	public static void toSpaceCoordinate(int x, int y, int z, Vector3f store) {
		store.x = (2 * x + y) * TileMesh.TRANSLATION_X;
		store.y = z * TileMesh.HEX_SIZE_Y;
		store.z = y * TileMesh.TRANSLATION_Z;
	}
	
	public static void centerPosOnHex(Vector3f pos) {
		Vector3i mapPos = toMapCoordinate(pos.x, pos.y, pos.z);
		toSpaceCoordinate(mapPos.x, mapPos.y, mapPos.z, pos);
	}
}
