package fr.lyrgard.hexScape.io.virtualScape.bean;

import java.util.ArrayList;
import java.util.List;

public class VirtualScapeMap {

	private double version;
	private String name;
	private String author; 
	private String playerNumber;
	private int scenarioLenght;
	private String scenario;
	private int levelPerPage;
	private int printingTransparency;
	private boolean printingGrid;
	private boolean printTileNumber;
	private boolean printStartAreaAsLevel;
	private int tileNumer;
	
	private List<VirtualScapeTile> tiles;

	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(String playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getScenarioLenght() {
		return scenarioLenght;
	}

	public void setScenarioLenght(int scenarioLenght) {
		this.scenarioLenght = scenarioLenght;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public int getLevelPerPage() {
		return levelPerPage;
	}

	public void setLevelPerPage(int levelPerPage) {
		this.levelPerPage = levelPerPage;
	}

	public int getPrintingTransparency() {
		return printingTransparency;
	}

	public void setPrintingTransparency(int printingTransparency) {
		this.printingTransparency = printingTransparency;
	}

	public boolean isPrintingGrid() {
		return printingGrid;
	}

	public void setPrintingGrid(boolean printingGrid) {
		this.printingGrid = printingGrid;
	}

	public boolean isPrintTileNumber() {
		return printTileNumber;
	}

	public void setPrintTileNumber(boolean printTileNumber) {
		this.printTileNumber = printTileNumber;
	}

	public boolean isPrintStartAreaAsLevel() {
		return printStartAreaAsLevel;
	}

	public void setPrintStartAreaAsLevel(boolean printStartAreaAsLevel) {
		this.printStartAreaAsLevel = printStartAreaAsLevel;
	}

	public int getTileNumer() {
		return tileNumer;
	}

	public void setTileNumer(int tileNumer) {
		this.tileNumer = tileNumer;
	}

	public List<VirtualScapeTile> getTiles() {
		if (tiles == null) {
			tiles = new ArrayList<VirtualScapeTile>();
		}
		return tiles;
	}
}
