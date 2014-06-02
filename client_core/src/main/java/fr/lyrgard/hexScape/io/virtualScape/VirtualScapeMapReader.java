package fr.lyrgard.hexScape.io.virtualScape;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fr.lyrgard.hexScape.io.virtualScape.bean.Vector3i;
import fr.lyrgard.hexScape.io.virtualScape.bean.VirtualScapeDecorType;
import fr.lyrgard.hexScape.io.virtualScape.bean.VirtualScapeMap;
import fr.lyrgard.hexScape.io.virtualScape.bean.VirtualScapeTile;
import fr.lyrgard.hexScape.model.Direction;
import fr.lyrgard.hexScape.model.map.Decor;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.map.TileType;

public class VirtualScapeMapReader {
	
	

	public Map readMap(File mapFile) {
		ByteBuffer bB = null;
		try {
			byte[] bytes = Files.readAllBytes(mapFile.toPath());
			bB = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		VirtualScapeMap virtualScapeMap = new VirtualScapeMap();
		
		virtualScapeMap.setVersion(bB.getDouble());
		virtualScapeMap.setName(readCString(bB));
		virtualScapeMap.setAuthor(readCString(bB)); 
		virtualScapeMap.setPlayerNumber(readCString(bB));
		virtualScapeMap.setScenarioLenght(bB.getInt());
		String scenario = "";
		for (int i = 0; i < virtualScapeMap.getScenarioLenght(); i++) {
			scenario += (char)bB.get();
		}
		virtualScapeMap.setScenario(scenario);
		virtualScapeMap.setLevelPerPage(bB.getInt());
		virtualScapeMap.setPrintingTransparency(bB.getInt());
		virtualScapeMap.setPrintingGrid(bB.getInt() != 0);
		virtualScapeMap.setPrintTileNumber(bB.getInt() != 0);
		virtualScapeMap.setPrintStartAreaAsLevel(bB.getInt() != 0);
		virtualScapeMap.setTileNumer(bB.getInt());
		for (int i = 0; i < virtualScapeMap.getTileNumer(); i++) {
			VirtualScapeTile tile = new VirtualScapeTile();
			tile.setType(bB.getInt());
			tile.setVersion(bB.getDouble());
			tile.setRotation(bB.getInt());
			tile.setPosX(bB.getInt());
			tile.setPosY(bB.getInt());
			tile.setPosZ(bB.getInt());
			tile.setGlyphLetter((char)bB.get());
			tile.setGlyphName(readCString(bB));
			tile.setStartName(readCString(bB));
			tile.setColorf(bB.getInt());
			if (tile.getType() / 1000 == 17) {
				// Tuiles particulières
				bB.getInt(); // m_NbTile
				readCString(bB); // m_PersonalTexture
				readCString(bB); // m_PersonalTextureSide
				readCString(bB); // m_Char
				readCString(bB); // m_Name
			}
			virtualScapeMap.getTiles().add(tile);
		}

		return toMap(virtualScapeMap, mapFile.getName());
	}


	private String readCString(ByteBuffer bB) {
		int lenght = readCStringLenght(bB);
		
		String value = "";
		for (int i = 0; i < lenght; i++) {
			value += bB.getChar();
		}
		return value; 
	}
	
	private int readCStringLenght(ByteBuffer bB) {
		int lenght = 0;
		byte b = bB.get();
		
		if (b != (byte)0xFF) {
			lenght = b;
		} else {
			short s = bB.getShort();
			if (s == (short)0xFFFE) {
				lenght = readCStringLenght(bB);
			} else if (s == (short)0xFFFF) {
				lenght = bB.getInt();				
			} else {
				lenght = s;
			}
		}
		
		return lenght;
	}
	
	private Map toMap(VirtualScapeMap virtualScapeMap, String defaultName) {
		Map map = new Map();

		java.util.Map<Integer, List<VirtualScapeTile>> tilesByHeight = new HashMap<Integer, List<VirtualScapeTile>>();
		int maxHeight = 0;
		for (VirtualScapeTile tile : virtualScapeMap.getTiles()) {
			List<VirtualScapeTile> tiles = tilesByHeight.get(tile.getPosZ());
			if (tiles == null) {
				tiles = new ArrayList<VirtualScapeTile>();
				tilesByHeight.put(tile.getPosZ(), tiles);
			}
			tiles.add(tile);
			
			if (tile.getPosZ() > maxHeight) {
				maxHeight = tile.getPosZ();
			}
		}
		
		List<VirtualScapeTile> decors = new ArrayList<>();
		for (int z = 0; z <= maxHeight; z++) {
			List<VirtualScapeTile> tiles = tilesByHeight.get(z);
			if (tiles != null) {
				for (VirtualScapeTile tile : tiles) {
					if (getTileType(tile) != null) {
						addTileToMap(tile, z, map);
					} else {
						decors.add(tile);
					}
				}
				
			}
			
		}
		
		for (VirtualScapeTile decor : decors) {
			addDecorToMap(decor, map);
		}
		
		map.setName(virtualScapeMap.getName());
		if (StringUtils.isEmpty(map.getName())) {
			map.setName(defaultName);
		}
		
		return map;
	}
	
	
	private void addDecorToMap(VirtualScapeTile decor, Map map) {
		int x = convertCoordX(decor);
		int y = convertCoordY(decor);
		VirtualScapeDecorType type = getDecorType(decor);
		Direction direction = getDirection(decor);
		
		
		if (type != null) {
			Decor decorBean = new Decor();
			decorBean.setName(type.getExternalModelName());
			decorBean.setDirection(direction);
			Vector3i tileAttachementOffset = type.getAttachementTilePosition().get(direction);

			decorBean.setPosition(x + tileAttachementOffset.x, y + tileAttachementOffset.y, decor.getPosZ() + tileAttachementOffset.z);
			map.getDecors().add(decorBean);

			// Some decors has Tiles under them
			if (type == VirtualScapeDecorType.BRUSH_1 || type == VirtualScapeDecorType.PALM_1) {
				addSingleTilesToMap(map, 1, 0, TileType.SWAMP, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.HIVE_6) {
				addSingleTilesToMap(map, 6, decor.getRotation(), TileType.SWAMP_WATER, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_6) {
				addSingleTilesToMap(map, 6, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_4) {
				addSingleTilesToMap(map, 4, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_3) {
				addSingleTilesToMap(map, 3, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_1) {
				addSingleTilesToMap(map, 1, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.OUTCROP_3) {
				addSingleTilesToMap(map, 3, decor.getRotation(), TileType.SHADOW, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.OUTCROP_1) {
				addSingleTilesToMap(map, 1, decor.getRotation(), TileType.SHADOW, x, y, decor.getPosZ());
			}
			
		}
	}


	private void addTileToMap(VirtualScapeTile tile, int z ,Map map) {
		
		int tileNumber = tile.getType() % 1000;
		int rotation = tile.getRotation();
		
		TileType tileType = getTileType(tile);
		
		int x = convertCoordX(tile);
		int y = convertCoordY(tile);
		
		addSingleTilesToMap(map, tileNumber, rotation, tileType, x, y, z);
	}


	private void addSingleTilesToMap(Map map, int tileNumber, int rotation, TileType tileType, int x, int y, int z) {
		switch (tileNumber) {
		case 1:
			map.addTile(tileType, x, y, z);
			break;
		case 2:
			if (tileType == TileType.WATER || tileType == TileType.SHADOW) {
				// Somehow Virtualscape has shadow and water tiles "2", that are in fact normal single tiles
				map.addTile(tileType, x, y, z);	
				break;
			}
			switch (rotation) {
			case 0:
			case 3:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				break;
			case 1:
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y-1, z);
				break;
			case 2:
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				break;
			}
			break;
		case 3:
			switch (rotation) {
			case 0:
			case 2:
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+1, y-1, z);
				break;
			case 1:
			case 3:
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x, y-1, z);
				break;
			}
			break;
		case 4:
			switch (rotation) {
			case 0:
			case 3:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				break;
			case 1:
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+1, y-2, z);
				break;
			case 2:
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				break;
			}
			break;
		case 5:
			switch (rotation) {
			case 0:
			case 3:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y, z);
				map.addTile(tileType, x+3, y, z);
				map.addTile(tileType, x+4, y, z);
				break;
			case 1:
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+3, y-3, z);
				map.addTile(tileType, x+4, y-4, z);
				break;
			case 2:
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x, y-3, z);
				map.addTile(tileType, x, y-4, z);
				break;
			}
			break;
		case 6:
			switch (rotation) {
			case 0:
			case 3:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				break;
			case 1:
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+3, y-2, z);
				break;
			case 2:
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+1, y-3, z);
				break;
			}
			break;
		case 7:
			if (tileType == TileType.ROAD) {
				switch (rotation) {
				case 0:
					map.addTile(tileType, x, y, z);
					map.addTile(tileType, x+1, y, z);
					map.addTile(tileType, x+2, y, z);
					map.addTile(tileType, x+3, y, z);
					map.addTile(tileType, x+1, y-1, z);
					map.addTile(tileType, x+2, y-1, z);
					map.addTile(tileType, x+3, y-1, z);
					break;
				case 1:
					map.addTile(tileType, x, y, z);
					map.addTile(tileType, x, y-1, z);
					map.addTile(tileType, x+1, y-1, z);
					map.addTile(tileType, x+1, y-2, z);
					map.addTile(tileType, x+2, y-2, z);
					map.addTile(tileType, x+2, y-3, z);
					map.addTile(tileType, x+3, y-3, z);
					break;
				case 2:
					map.addTile(tileType, x, y, z);
					map.addTile(tileType, x+1, y, z);
					map.addTile(tileType, x, y-1, z);
					map.addTile(tileType, x+1, y-1, z);
					map.addTile(tileType, x, y-2, z);
					map.addTile(tileType, x+1, y-2, z);
					map.addTile(tileType, x+1, y-3, z);
					break;
				case 3:
					map.addTile(tileType, x, y, z);
					map.addTile(tileType, x+1, y, z);
					map.addTile(tileType, x+2, y, z);
					map.addTile(tileType, x, y-1, z);
					map.addTile(tileType, x+1, y-1, z);
					map.addTile(tileType, x+2, y-1, z);
					map.addTile(tileType, x+3, y-1, z);
					break;
				case 4:
					map.addTile(tileType, x, y, z);
					map.addTile(tileType, x+1, y, z);
					map.addTile(tileType, x+1, y-1, z);
					map.addTile(tileType, x+2, y-1, z);
					map.addTile(tileType, x+2, y-2, z);
					map.addTile(tileType, x+3, y-2, z);
					map.addTile(tileType, x+3, y-3, z);
					break;
				case 5:
					map.addTile(tileType, x, y, z);
					map.addTile(tileType, x, y-1, z);
					map.addTile(tileType, x+1, y-1, z);
					map.addTile(tileType, x, y-2, z);
					map.addTile(tileType, x+1, y-2, z);
					map.addTile(tileType, x, y-3, z);
					map.addTile(tileType, x+1, y-3, z);
					break;
				}
				break;
			} else {
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
			}
			break;
		case 9:
			switch (rotation) {
			case 0:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y, z);
				map.addTile(tileType, x+3, y, z);
				map.addTile(tileType, x+4, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+3, y-1, z);
				map.addTile(tileType, x+4, y-1, z);
				break;
			case 1:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+2, y-3, z);
				map.addTile(tileType, x+3, y-3, z);
				map.addTile(tileType, x+3, y-4, z);
				map.addTile(tileType, x+4, y-4, z);
				break;
			case 2:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x, y-3, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x+1, y-4, z);
				break;
			case 3:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y, z);
				map.addTile(tileType, x+3, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+3, y-1, z);
				map.addTile(tileType, x+4, y-1, z);
				break;
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+3, y-2, z);
				map.addTile(tileType, x+3, y-3, z);
				map.addTile(tileType, x+4, y-3, z);
				map.addTile(tileType, x+4, y-4, z);
				break;
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x, y-3, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x, y-4, z);
				map.addTile(tileType, x+1, y-4, z);
				break;
			}
			break;
		case 24:
			switch (rotation) {
			case 0:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+3, y-2, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x+2, y-3, z);
				map.addTile(tileType, x+3, y-3, z);
				map.addTile(tileType, x+4, y-3, z);
				map.addTile(tileType, x+5, y-3, z);
				map.addTile(tileType, x+2, y-4, z);
				map.addTile(tileType, x+3, y-4, z);
				map.addTile(tileType, x+4, y-4, z);
				map.addTile(tileType, x+5, y-4, z);
				map.addTile(tileType, x+6, y-4, z);
				map.addTile(tileType, x+2, y-5, z);
				map.addTile(tileType, x+3, y-5, z);
				map.addTile(tileType, x+4, y-5, z);
				map.addTile(tileType, x+5, y-5, z);
				map.addTile(tileType, x+6, y-5, z);
				map.addTile(tileType, x+7, y-5, z);
				break;
			case 1:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x-1, y-1, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x-2, y-2, z);
				map.addTile(tileType, x-1, y-2, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x-1, y-3, z);
				map.addTile(tileType, x, y-3, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x+2, y-3, z);
				map.addTile(tileType, x, y-4, z);
				map.addTile(tileType, x+1, y-4, z);
				map.addTile(tileType, x+2, y-4, z);
				map.addTile(tileType, x+1, y-5, z);
				map.addTile(tileType, x+2, y-5, z);
				map.addTile(tileType, x+3, y-5, z);
				map.addTile(tileType, x+2, y-6, z);
				map.addTile(tileType, x+3, y-6, z);
				map.addTile(tileType, x+3, y-7, z);
				break;
			case 2:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+3, y-2, z);
				map.addTile(tileType, x+4, y-2, z);
				map.addTile(tileType, x, y-3, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x+2, y-3, z);
				map.addTile(tileType, x+3, y-3, z);
				map.addTile(tileType, x+4, y-3, z);
				map.addTile(tileType, x+5, y-3, z);
				map.addTile(tileType, x, y-4, z);
				map.addTile(tileType, x+1, y-4, z);
				map.addTile(tileType, x+2, y-4, z);
				map.addTile(tileType, x+3, y-4, z);
				map.addTile(tileType, x+4, y-4, z);
				map.addTile(tileType, x+5, y-4, z);
				map.addTile(tileType, x, y-5, z);
				map.addTile(tileType, x+1, y-5, z);
				map.addTile(tileType, x+2, y-5, z);
				break;
			case 3:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y, z);
				map.addTile(tileType, x+3, y, z);
				map.addTile(tileType, x+4, y, z);
				map.addTile(tileType, x+5, y, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x+3, y-1, z);
				map.addTile(tileType, x+4, y-1, z);
				map.addTile(tileType, x+5, y-1, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+3, y-2, z);
				map.addTile(tileType, x+4, y-2, z);
				map.addTile(tileType, x+5, y-2, z);
				map.addTile(tileType, x+6, y-2, z);
				map.addTile(tileType, x+4, y-3, z);
				map.addTile(tileType, x+5, y-3, z);
				map.addTile(tileType, x+6, y-3, z);
				map.addTile(tileType, x+5, y-4, z);
				map.addTile(tileType, x+6, y-4, z);
				map.addTile(tileType, x+7, y-4, z);
				map.addTile(tileType, x+6, y-5, z);
				map.addTile(tileType, x+7, y-5, z);
				break;
			case 4:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x+2, y-3, z);
				map.addTile(tileType, x+3, y-3, z);
				map.addTile(tileType, x+1, y-4, z);
				map.addTile(tileType, x+2, y-4, z);
				map.addTile(tileType, x+3, y-4, z);
				map.addTile(tileType, x+4, y-4, z);
				map.addTile(tileType, x+1, y-5, z);
				map.addTile(tileType, x+2, y-5, z);
				map.addTile(tileType, x+3, y-5, z);
				map.addTile(tileType, x+4, y-5, z);
				map.addTile(tileType, x+5, y-5, z);
				map.addTile(tileType, x+1, y-6, z);
				map.addTile(tileType, x+2, y-6, z);
				map.addTile(tileType, x+3, y-6, z);
				map.addTile(tileType, x+4, y-6, z);
				map.addTile(tileType, x+2, y-7, z);
				map.addTile(tileType, x+3, y-7, z);
				break;
			case 5:
				map.addTile(tileType, x, y, z);
				map.addTile(tileType, x+1, y, z);
				map.addTile(tileType, x+2, y, z);
				map.addTile(tileType, x-3, y-1, z);
				map.addTile(tileType, x-2, y-1, z);
				map.addTile(tileType, x-1, y-1, z);
				map.addTile(tileType, x, y-1, z);
				map.addTile(tileType, x+1, y-1, z);
				map.addTile(tileType, x+2, y-1, z);
				map.addTile(tileType, x-3, y-2, z);
				map.addTile(tileType, x-2, y-2, z);
				map.addTile(tileType, x-1, y-2, z);
				map.addTile(tileType, x, y-2, z);
				map.addTile(tileType, x+1, y-2, z);
				map.addTile(tileType, x+2, y-2, z);
				map.addTile(tileType, x-2, y-3, z);
				map.addTile(tileType, x-1, y-3, z);
				map.addTile(tileType, x, y-3, z);
				map.addTile(tileType, x+1, y-3, z);
				map.addTile(tileType, x+2, y-3, z);
				map.addTile(tileType, x, y-4, z);
				map.addTile(tileType, x+1, y-4, z);
				map.addTile(tileType, x+2, y-4, z);
				map.addTile(tileType, x+2, y-5, z);
				break;
			}
			break;
			default:
				throw new RuntimeException("Tile number " + tileNumber + " not supported yet");
		}
	}

	

	private int convertCoordX(VirtualScapeTile tile) {
		return tile.getPosX() + (tile.getPosY() + 1)/2;
	}


	private int convertCoordY(VirtualScapeTile tile) {
		return -tile.getPosY();
	}


	private TileType getTileType(VirtualScapeTile tile) {
		int tileFamily = tile.getType() / 1000;
		TileType tileType = null;
		switch (tileFamily) {
		case 1:
			tileType = TileType.GRASS;
			break;
		case 2:
			tileType = TileType.ROCK; 
			break;
		case 3:
			tileType = TileType.SAND; 
			break;
		case 4:
			tileType = TileType.WATER; 
			break;
		case 5:
			tileType = TileType.ICE;
			break;
		case 6:
			tileType = TileType.MOLTEN_LAVA; 
			break;
		case 7:
			tileType = TileType.LAVA_FIELD; 
			break;
		case 8:
			tileType = TileType.ROAD; 
			break;
		case 9:
			tileType = TileType.SNOW;
			break;
		case 16:
			if (tile.getType() < 16010) {
				tileType = TileType.ROAD;
			}
			break;
		case 19:
			tileType = TileType.SWAMP_WATER; 
			break;
		case 20:
			tileType = TileType.SWAMP; 
			break;
		case 21:
			tileType = TileType.CONCRETE; 
			break;
		case 22:
			tileType = TileType.ASPHALT; 
			break;
		case 25:
			tileType = TileType.SHADOW; 
			break;
		case 26:
			tileType = TileType.DUNGEON; 
			break;
		default:
			tileType = null;
		}
		
		return tileType;
	}
	
	private VirtualScapeDecorType getDecorType(VirtualScapeTile tile) {
		
		VirtualScapeDecorType decorType;
		switch (tile.getType()) {
		case 11002:
			decorType = VirtualScapeDecorType.RUIN_2;
			break;
		case 11003:
			decorType = VirtualScapeDecorType.RUIN_3; 
			break;
		case 11006:
			decorType = VirtualScapeDecorType.RUIN_6;
			break;
		case 11007:
			decorType = VirtualScapeDecorType.RUIN_6_BREAKED; 
			break;
		case 10011:
			decorType = VirtualScapeDecorType.TREE_1_10;
			break;
		case 10012:
			decorType = VirtualScapeDecorType.TREE_1_11; 
			break;
		case 10013:
			decorType = VirtualScapeDecorType.TREE_1_12;
			break;
		case 10004:
			decorType = VirtualScapeDecorType.TREE_4;
			break;
		case 12004:
			decorType = VirtualScapeDecorType.STONE_WALL_4;
			break;
		case 23006:
			decorType = VirtualScapeDecorType.HIVE_6;
			break;
		case 16101:
			decorType = VirtualScapeDecorType.CASTLE_BASE_CORNER;
			break;
		case 16102:
			decorType = VirtualScapeDecorType.CASTLE_BASE_STRAIGHT;
			break;
		case 16103:
			decorType = VirtualScapeDecorType.CASTLE_BASE_END;
			break;
		case 16201:
			decorType = VirtualScapeDecorType.CASTLE_WALL_CORNER;
			break;
		case 16202:
			decorType = VirtualScapeDecorType.CASTLE_WALL_STRAIGHT;
			break;
		case 16203:
			decorType = VirtualScapeDecorType.CASTLE_WALL_END;
			break;
		case 16301:
			decorType = VirtualScapeDecorType.CASTLE_BATTLEMENT;
			break;
		case 16401:
			decorType = VirtualScapeDecorType.CASTLE_DOOR;
			break;
		case 16402:
			decorType = VirtualScapeDecorType.CASTLE_LADDER;
			break;
		case 16403:
			decorType = VirtualScapeDecorType.CASTLE_FLAG;
			break;
		case 16404:
			decorType = VirtualScapeDecorType.CASTLE_ARCH;
			break;
		case 13001:
			decorType = VirtualScapeDecorType.GLACIER_1;
			break;
		case 13003:
			decorType = VirtualScapeDecorType.GLACIER_3;
			break;
		case 13004:
			decorType = VirtualScapeDecorType.GLACIER_4;
			break;
		case 13006:
			decorType = VirtualScapeDecorType.GLACIER_6;
			break;
		case 24014:
		case 24015:
		case 24016:
			decorType = VirtualScapeDecorType.PALM_1;
			break;
		case 24002:
			decorType = VirtualScapeDecorType.BRUSH_1;
			break;
		case 27001:
			decorType = VirtualScapeDecorType.OUTCROP_1;
			break;
		case 27003:
			decorType = VirtualScapeDecorType.OUTCROP_3;
			break;
		default:
			decorType = null;
		}
		
		return decorType;
	}
	
	private Direction getDirection(VirtualScapeTile tile) {
		Direction direction;
		switch (tile.getRotation()) {
		case 0:
			direction = Direction.EAST;
			break;
		case 1:
			direction = Direction.SOUTH_EAST;
			break;
		case 2:
			direction = Direction.SOUTH_WEST;
			break;
		case 3:
			direction = Direction.WEST; 
			break;
		case 4:
			direction = Direction.NORTH_WEST;
			break;
		case 5:
			direction = Direction.NORTH_EAST; 
			break;
		default:
			direction = null;
		}
		
		return direction;
	}

}
