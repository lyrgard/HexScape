package fr.lyrgard.hexScape.model.model3d.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.model.model3d.texture.TileTexture;

public class TileSide1Mesh extends Mesh {

//	public TileSide1Mesh() {
//		Vector3f [] vertices = new Vector3f[24];
//		vertices[0] = new Vector3f(Tile1Mesh.v1x,Tile1Mesh.v1y,Tile1Mesh.top);
//		vertices[1] = new Vector3f(Tile1Mesh.v2x,Tile1Mesh.v2y,Tile1Mesh.top);
//		vertices[2] = new Vector3f(Tile1Mesh.v1x,Tile1Mesh.v1y,Tile1Mesh.bottom);
//		vertices[3] = new Vector3f(Tile1Mesh.v2x,Tile1Mesh.v2y,Tile1Mesh.bottom);
//		
//		vertices[4] = new Vector3f(Tile1Mesh.v2x,Tile1Mesh.v2y,Tile1Mesh.top);
//		vertices[5] = new Vector3f(Tile1Mesh.v3x,Tile1Mesh.v3y,Tile1Mesh.top);
//		vertices[6] = new Vector3f(Tile1Mesh.v2x,Tile1Mesh.v2y,Tile1Mesh.bottom);
//		vertices[7] = new Vector3f(Tile1Mesh.v3x,Tile1Mesh.v3y,Tile1Mesh.bottom);
//		
//		vertices[8] = new Vector3f(Tile1Mesh.v3x,Tile1Mesh.v3y,Tile1Mesh.top);
//		vertices[9] = new Vector3f(Tile1Mesh.v4x,Tile1Mesh.v4y,Tile1Mesh.top);
//		vertices[10] = new Vector3f(Tile1Mesh.v3x,Tile1Mesh.v3y,Tile1Mesh.bottom);
//		vertices[11] = new Vector3f(Tile1Mesh.v4x,Tile1Mesh.v4y,Tile1Mesh.bottom);
//		
//		vertices[12] = new Vector3f(Tile1Mesh.v4x,Tile1Mesh.v4y,Tile1Mesh.top);
//		vertices[13] = new Vector3f(Tile1Mesh.v5x,Tile1Mesh.v5y,Tile1Mesh.top);
//		vertices[14] = new Vector3f(Tile1Mesh.v4x,Tile1Mesh.v4y,Tile1Mesh.bottom);
//		vertices[15] = new Vector3f(Tile1Mesh.v5x,Tile1Mesh.v5y,Tile1Mesh.bottom);
//		
//		vertices[16] = new Vector3f(Tile1Mesh.v5x,Tile1Mesh.v5y,Tile1Mesh.top);
//		vertices[17] = new Vector3f(Tile1Mesh.v6x,Tile1Mesh.v6y,Tile1Mesh.top);
//		vertices[18] = new Vector3f(Tile1Mesh.v5x,Tile1Mesh.v5y,Tile1Mesh.bottom);
//		vertices[19] = new Vector3f(Tile1Mesh.v6x,Tile1Mesh.v6y,Tile1Mesh.bottom);
//		
//		vertices[20] = new Vector3f(Tile1Mesh.v6x,Tile1Mesh.v6y,Tile1Mesh.top);
//		vertices[21] = new Vector3f(Tile1Mesh.v1x,Tile1Mesh.v1y,Tile1Mesh.top);
//		vertices[22] = new Vector3f(Tile1Mesh.v6x,Tile1Mesh.v6y,Tile1Mesh.bottom);
//		vertices[23] = new Vector3f(Tile1Mesh.v1x,Tile1Mesh.v1y,Tile1Mesh.bottom);
//		
//		
//		Vector2f[] texCoord = new Vector2f[24];
//		texCoord[0] = TileTexture.SIDE_GOUND.getCoord(1, 0);
//		texCoord[1] = TileTexture.SIDE_GOUND.getCoord(0, 0);
//		texCoord[2] = TileTexture.SIDE_GOUND.getCoord(1, 1);
//		texCoord[3] = TileTexture.SIDE_GOUND.getCoord(0, 1);
//		
//		texCoord[4] = TileTexture.SIDE_GOUND.getCoord(1, 0);
//		texCoord[5] = TileTexture.SIDE_GOUND.getCoord(0, 0);
//		texCoord[6] = TileTexture.SIDE_GOUND.getCoord(1, 1);
//		texCoord[7] = TileTexture.SIDE_GOUND.getCoord(0, 1);
//		
//		texCoord[8] = TileTexture.SIDE_GOUND.getCoord(1, 0);
//		texCoord[9] = TileTexture.SIDE_GOUND.getCoord(0, 0);
//		texCoord[10] = TileTexture.SIDE_GOUND.getCoord(1, 1);
//		texCoord[11] = TileTexture.SIDE_GOUND.getCoord(0, 1);
//		
//		texCoord[12] = TileTexture.SIDE_GOUND.getCoord(1, 0);
//		texCoord[13] = TileTexture.SIDE_GOUND.getCoord(0, 0);
//		texCoord[14] = TileTexture.SIDE_GOUND.getCoord(1, 1);
//		texCoord[15] = TileTexture.SIDE_GOUND.getCoord(0, 1);
//		
//		texCoord[16] = TileTexture.SIDE_GOUND.getCoord(1, 0);
//		texCoord[17] = TileTexture.SIDE_GOUND.getCoord(0, 0);
//		texCoord[18] = TileTexture.SIDE_GOUND.getCoord(1, 1);
//		texCoord[19] = TileTexture.SIDE_GOUND.getCoord(0, 1);
//		
//		texCoord[20] = TileTexture.SIDE_GOUND.getCoord(1, 0);
//		texCoord[21] = TileTexture.SIDE_GOUND.getCoord(0, 0);
//		texCoord[22] = TileTexture.SIDE_GOUND.getCoord(1, 1);
//		texCoord[23] = TileTexture.SIDE_GOUND.getCoord(0, 1);
//	
//		
//		
//		
//		int [] indexes = { 0,1,2, 1,3,2, 4,5,6, 5,7,6, 8,9,10, 9,11,10, 12,13,14, 13,15,14, 16,17,18, 17,19,18, 20,21,22, 21,23,22};
//		
//		
//		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
//		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
//		setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
//		updateBound();
//	}
}
