package fr.lyrgard.hexScape.model.model3d.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.model.model3d.texture.TileTexture;

public class Tile1Mesh extends Mesh {
	
//	static final float v1x = 0;
//	static final float v1y = HEX_SIZE_Y/2;
//	
//	static final float v2x = HEX_SIZE_X/2;
//	static final float v2y = v1y/2;
//	
//	static final float v3x = v2x;
//	static final float v3y = -v2y;
//	
//	static final float v4x = v1x;
//	static final float v4y = -v1y;
//	
//	static final float v5x = -v2x;
//	static final float v5y = -v2y;
//	
//	static final float v6x = -v2x;
//	static final float v6y = v2y;
//	
//	static final float bottom = 0;
//	static final float top = HEX_SIZE_Z;
//	
//	public Tile1Mesh() {
//		Vector3f [] vertices = new Vector3f[6];
//		vertices[0] = new Vector3f(v1x,v1y,top);
//		vertices[1] = new Vector3f(v2x,v2y,top);
//		vertices[2] = new Vector3f(v3x,v3y,top);
//		vertices[3] = new Vector3f(v4x,v4y,top);
//		vertices[4] = new Vector3f(v5x,v5y,top);
//		vertices[5] = new Vector3f(v6x,v6y,top);
//		
//		Vector2f[] texCoord = new Vector2f[6];
//		texCoord[0] = TileTexture.GRASS.getCoord(0.5f,0);
//		texCoord[1] = TileTexture.GRASS.getCoord(1,0.25f);
//		texCoord[2] = TileTexture.GRASS.getCoord(1,0.75f);
//		texCoord[3] = TileTexture.GRASS.getCoord(0.5f,1);
//		texCoord[4] = TileTexture.GRASS.getCoord(0,0.75f);
//		texCoord[5] = TileTexture.GRASS.getCoord(0,0.25f);
//	
//		
//		
//		int [] indexes = { 0,5,1, 5,4,3, 3,2,1, 1,5,3 };
//		
//		
//		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
//		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
//		updateBound();
//	}
//	
	
}
