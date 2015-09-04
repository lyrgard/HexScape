package fr.lyrgard.hexScape.model.model3d;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;

public class ObjExternalModel implements ExternalModel {

	private Spatial spatial;
	
	private String name;
	
	@Override
	public Spatial getNewInstance() {
		//return spatial.clone();

		Spatial clone = spatial.clone();
		//makeToonish(clone);
		return clone;
	}
	
	public void makeToonish(Spatial spatial){
        if (spatial instanceof Node){
            Node n = (Node) spatial;
            for (Spatial child : n.getChildren())
                makeToonish(child);
        }else if (spatial instanceof Geometry){
            Material mat = ((Geometry) spatial).getMaterial();
            mat.setBoolean("Toon", true);
            mat.setColor("EdgesColor", ColorRGBA.Red);
            mat.setFloat("EdgeSize", 0.01f);
//            mat.setColor("FogColor", new ColorRGBA(0.8f, 0.8f, 0.8f, 17.0f));
//            mat.setBoolean("Fog_Edges", true);
            
            //spatial.setMaterial(mat);
            //TangentBinormalGenerator.generate(spatial);
        }
    }

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
