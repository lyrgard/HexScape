package fr.lyrgard.hexScape.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import fr.lyrgard.hexScape.model.model3d.TileMesh;
import fr.lyrgard.hexScape.model.player.Player;
import fr.lyrgard.hexScape.service.PieceManager;
import fr.lyrgard.hexScape.service.SelectMarkerService;

public class SelectMarker implements Displayable {
	
	private static Mesh mesh;
	
	private Geometry geometry;
	
	private PieceManager piece;
	
	private Player player;
	
	static {
		mesh = new Mesh();
		
		float value = TileMesh.HEX_SIZE_Z / 2;
		
		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(new Vector3f[] {new Vector3f(-value, 0, value), new Vector3f(value, 0, value), new Vector3f(value, 0, -value), new Vector3f(-value, 0, -value)}));
		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(new Vector2f[] {new Vector2f(0, 0), new Vector2f(1, 0), new Vector2f(1, 1), new Vector2f(0, 1)}));
		mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(new int[]{0, 1, 2, 0, 2, 3}));		
		mesh.updateBound();
	}
	
	private List<SecondarySelectMarker> secondarySelectMarkers = new ArrayList<SecondarySelectMarker>();
	
	public SelectMarker(Player player) {
		this.player = player;
		geometry = new Geometry("selectCross", mesh);
		geometry.setQueueBucket(Bucket.Translucent);  
		geometry.setShadowMode(ShadowMode.Off);
		geometry.setMaterial(SelectMarkerMaterialFactory.getMaterial(true, player.getColor()));
	}
	
	public void attachTo(PieceManager piece) {
		piece.getSpatial().attachChild(getSpatial());
		getSpatial().setLocalTranslation(0, 0.3f, 0);
		this.piece = piece;
	}
	
	public void detach() {
		if (piece != null) {
			piece.getSpatial().detachChild(getSpatial());
			while (!getSecondarySelectMarkers().isEmpty()) {
				SecondarySelectMarker secondarySelectMarker = getSecondarySelectMarkers().get(0);
				secondarySelectMarker.getSecondarySelectedPiece().switchSecondarySelect(player.getId(), piece);
			}
		}
	}
	
	public boolean switchSecondarySelect(PieceManager secondarySelectedPiece) {
		boolean secondarySelected = false;
		Iterator<SecondarySelectMarker> it = getSecondarySelectMarkers().iterator();
		boolean secondarySelectMarkerFound = false;
		while (it.hasNext()) {
			SecondarySelectMarker secondarySelectMarker = it.next();
			if (secondarySelectedPiece.equals(secondarySelectMarker.getSecondarySelectedPiece())) {
				secondarySelectMarkerFound = true;
				secondarySelectedPiece.getSpatial().detachChild(secondarySelectMarker.getSpatial());
				it.remove();
				secondarySelected = false;
				break;
			}
		}
		if (!secondarySelectMarkerFound) {
			SecondarySelectMarker secondarySelectMarker = SelectMarkerService.getInstance().getNewSecondarySelectMarker(player.getId(), piece, secondarySelectedPiece);
			getSecondarySelectMarkers().add(secondarySelectMarker);
			secondarySelectedPiece.getSpatial().attachChild(secondarySelectMarker.getSpatial());
			secondarySelectMarker.getSpatial().setLocalTranslation(0, 0.3f, 0);
			secondarySelected = true;
		}
		return secondarySelected;
	}
	
	@Override
	public Spatial getSpatial() {
		return geometry;
	}

	public List<SecondarySelectMarker> getSecondarySelectMarkers() {
		return secondarySelectMarkers;
	}

	public PieceManager getPiece() {
		return piece;
	}

}
