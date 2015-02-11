package fr.lyrgard.hexScape.model;

import java.util.HashMap;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.player.ColorEnum;

public class SelectMarkerMaterialFactory {
	
	private static Texture selectTileTexture;
	
	private static Texture secondarySelectTileTexture;
	
	private static Map<ColorEnum, Material> selectMarkerMaterialByColor = new HashMap<ColorEnum, Material>();
	
	private static Map<ColorEnum, Material> secondarySelectMarkerMaterialByColor = new HashMap<ColorEnum, Material>();
	
	static {
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		selectTileTexture = assetManager.loadTexture("model/texture/select_cross_white.png");
		secondarySelectTileTexture = assetManager.loadTexture("model/texture/target_cross_white.png");
	}
	
	static Material getMaterial(boolean primarySelectMarker, ColorEnum colorEnum) {
		
		Map<ColorEnum, Material> mapToSearch;
		if (primarySelectMarker) {
			mapToSearch = selectMarkerMaterialByColor;
		} else {
			mapToSearch = secondarySelectMarkerMaterialByColor;
		}
		Material material = mapToSearch.get(colorEnum);
		if (material != null) {
			return material;
		}
		
		AssetManager assetManager = HexScapeCore.getInstance().getHexScapeJme3Application().getAssetManager();
		
		ColorRGBA color = new ColorRGBA(colorEnum.getColor().getRed()/256f, colorEnum.getColor().getGreen()/256f, colorEnum.getColor().getBlue()/256f, 1);
		
		Texture texture;
		if (primarySelectMarker) {
			texture = selectTileTexture;
		} else {
			texture = secondarySelectTileTexture;
		}
		
		material = new Material(assetManager, 
				"Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors",true);
		material.setTexture("DiffuseMap", texture);
		material.setColor("Ambient", color);
		material.setColor("Diffuse",ColorRGBA.White);  // minimum material color
		material.setColor("Specular",ColorRGBA.White); // for shininess
		material.setFloat("Shininess", 50f);
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		mapToSearch.put(colorEnum, material);
		
		return material;
	}
	
	
}
