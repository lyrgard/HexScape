package fr.lyrgard.hexScape.io.virtualScape.bean;

public class VirtualScapeTile {
	private int type;
	private double version;
	private int rotation;
	private int posX;
	private int posY;
	private int posZ;
	private char glyphLetter;
	private String glyphName;
	private String startName;
	private int color;
	
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getPosZ() {
		return posZ;
	}

	public void setPosZ(int posZ) {
		this.posZ = posZ;
	}

	public char getGlyphLetter() {
		return glyphLetter;
	}

	public void setGlyphLetter(char glyphLetter) {
		this.glyphLetter = glyphLetter;
	}

	public String getGlyphName() {
		return glyphName;
	}

	public void setGlyphName(String glyphName) {
		this.glyphName = glyphName;
	}

	public String getStartName() {
		return startName;
	}

	public void setStartName(String startName) {
		this.startName = startName;
	}

	public int getColor() {
		return color;
	}

	public void setColorf(int color) {
		this.color = color;
	}
}
