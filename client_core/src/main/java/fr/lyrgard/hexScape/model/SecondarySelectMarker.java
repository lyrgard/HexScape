package fr.lyrgard.hexScape.model;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.model.player.ColorEnum;
import fr.lyrgard.hexScape.service.PieceManager;

public class SecondarySelectMarker implements Displayable {
	
	private static Mesh targetMesh;
	
	private Node node;
	
	private Geometry targetGeometry;
	
	private PieceManager secondarySelectedPiece;
	
	static {
		targetMesh = new Mesh();
		
		float value = TileMesh.HEX_SIZE_Z / 2;
		
		targetMesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(new Vector3f[] {new Vector3f(-value, 0, value), new Vector3f(value, 0, value), new Vector3f(value, 0, -value), new Vector3f(-value, 0, -value)}));
		targetMesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1)}));
		targetMesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(new int[]{0, 1, 2, 0, 2, 3}));		
		targetMesh.updateBound();
	}
	
	public SecondarySelectMarker(ColorEnum color, PieceManager selectedPiece, PieceManager secondarySelectedPiece) {
		node = new Node();
		
		this.secondarySelectedPiece = secondarySelectedPiece;
		
		targetGeometry = new Geometry("selectCross", targetMesh);
		targetGeometry.setQueueBucket(Bucket.Translucent);  
		targetGeometry.setShadowMode(ShadowMode.Off);
		targetGeometry.setMaterial(SelectMarkerMaterialFactory.getMaterial(false, color));
		node.attachChild(targetGeometry);
		
		if (selectedPiece != null && secondarySelectedPiece != null) {
			Link link = new Link(Vector3f.ZERO, selectedPiece.getSpatial().getWorldTranslation().subtract(secondarySelectedPiece.getSpatial().getWorldTranslation()));  
			link.setShadowMode(ShadowMode.Off);
			link.setMaterial(SelectMarkerMaterialFactory.getLinkMaterial(color));
			node.attachChild(link);
		}
	}
	
	public PieceManager getSecondarySelectedPiece() {
		return secondarySelectedPiece;
	}

	@Override
	public Spatial getSpatial() {
		return node;
	}
	
	public Spatial getRotatingSpatial() {
		return targetGeometry;
	}

}
