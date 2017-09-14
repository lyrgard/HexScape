package fr.lyrgard.hexScape.control;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.DirectionalLightShadowRenderer;

import fr.lyrgard.hexScape.service.ConfigurationService;

public class LightControl {

	public static final int LIGHT_INTENSITY_MIN = 1;
	public static final int LIGHT_INTENSITY_MAX = 10;
	public static final int SUN_HEIGHT_MIN = 1;
	public static final int SUN_HEIGHT_MAX = 90;
	public static final int SUN_POSITION_MIN = 1;
	public static final int SUN_POSITION_MAX = 360;
	
	
	private DirectionalLight sun;
	private AmbientLight ambientLight;
	private DirectionalLightShadowRenderer shadow;
	private int sunLightIntensity;
	private int ambiantLightIntensity;
	private int sunHeight;
	private int sunAngle;
	
	public LightControl(DirectionalLight sun, AmbientLight ambientLight, DirectionalLightShadowRenderer shadow) {
		this.sun = sun;
		this.ambientLight = ambientLight;
		this.shadow = shadow;
		ambiantLightIntensity = ConfigurationService.getInstance().getAmbiantLightIntensity();
		sunLightIntensity = ConfigurationService.getInstance().getSunLightIntensity();
		sunHeight = ConfigurationService.getInstance().getSunHeight();
		sunAngle = ConfigurationService.getInstance().getSunPosition();
		updateSun();
		updateAmbientLight();
	}
	
	public int getSunLightIntensity() {
		return sunLightIntensity;
	}
	public void setSunLightIntensity(int sunLightIntensity) {
		this.sunLightIntensity = sunLightIntensity;
		updateSun();
	}
	public int getAmbiantLightIntensity() {
		return ambiantLightIntensity;
	}
	public void setAmbiantLightIntensity(int ambiantLightIntensity) {
		this.ambiantLightIntensity = ambiantLightIntensity;
		updateAmbientLight();
	}
	public int getSunHeight() {
		return sunHeight;
	}
	public void setSunHeight(int sunHeight) {
		this.sunHeight = sunHeight;
		updateSun();
	}
	public int getSunAngle() {
		return sunAngle;
	}
	public void setSunAngle(int sunAngle) {
		this.sunAngle = sunAngle;
		updateSun();
	}
	
	public void save() {
		ConfigurationService.getInstance().setAmbiantLightIntensity(ambiantLightIntensity);
		ConfigurationService.getInstance().setSunLightIntensity(sunLightIntensity);
		ConfigurationService.getInstance().setSunPosition(sunAngle);
		ConfigurationService.getInstance().setSunHeight(sunHeight);
		ConfigurationService.getInstance().save();
	}
	
	private void updateSun() {
		double anglePositionRadian = sunAngle*Math.PI/180d; 
		double x = Math.cos(anglePositionRadian);
		double z = Math.sin(anglePositionRadian);
		
		double angleHeigthRadian = sunHeight*Math.PI/180d;
		double y = Math.sin(angleHeigthRadian);
		x *= Math.cos(angleHeigthRadian);
		z *= Math.cos(angleHeigthRadian);
		
		Vector3f sunDirection = new Vector3f(-(float)x, -(float)y, -(float)z).normalizeLocal();
		sun.setDirection(sunDirection);
		
		sun.setColor(ColorRGBA.White.mult(sunLightIntensity/10f));
		
		if (shadow != null) {
			shadow.setShadowIntensity(sunLightIntensity/20f);
		}
	}
	
	private void updateAmbientLight() {
		ambientLight.setColor(ColorRGBA.White.mult(ambiantLightIntensity/10f));
	}
	
	public float getShadowIntensity() {
		return sunLightIntensity/20f;
	}
	
	public void setShadow(DirectionalLightShadowRenderer shadow) {
		this.shadow = shadow;
	}
}
