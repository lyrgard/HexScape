package fr.lyrgard.hexScape.model;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;

public class Link extends Geometry {

	public Link(Vector3f start, Vector3f end) {

		super("LineCylinder");

		Cylinder cyl = new Cylinder(4, 8, .08f, start.distance(end));
		this.mesh=cyl;
		
		setLocalTranslation(FastMath.interpolateLinear(.5f, start, end));
		lookAt(end, Vector3f.UNIT_Y);
	}
}