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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.lyrgard.hexScape.bus.GuiMessageBus;
import fr.lyrgard.hexScape.io.virtualScape.bean.TileType;
import fr.lyrgard.hexScape.io.virtualScape.bean.Vector3i;
import fr.lyrgard.hexScape.io.virtualScape.bean.VirtualScapeDecorType;
import fr.lyrgard.hexScape.io.virtualScape.bean.VirtualScapeMap;
import fr.lyrgard.hexScape.io.virtualScape.bean.VirtualScapePiece;
import fr.lyrgard.hexScape.message.ErrorMessage;
import fr.lyrgard.hexScape.model.CurrentUserInfo;
import fr.lyrgard.hexScape.model.map.Decor;
import fr.lyrgard.hexScape.model.map.Direction;
import fr.lyrgard.hexScape.model.map.Map;
import fr.lyrgard.hexScape.model.map.Tile;
import fr.lyrgard.hexScape.service.MapManager;

public class VirtualScapeMapReader {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(VirtualScapeMapReader.class);
	
	public MapManager readMap(byte[] bytes, String name) {
		ByteBuffer bB = null;
		
		bB = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN );
		
		VirtualScapeMap virtualScapeMap = new VirtualScapeMap();
		
		virtualScapeMap.setVersion(bB.getDouble());
		if (virtualScapeMap.getVersion() != 7.0E-4) {
			GuiMessageBus.post(new ErrorMessage(CurrentUserInfo.getInstance().getId(), "Old VirtualScape file version, not supported.\nTry opening the map file with the latest Virtualscape version and save it again."));
			return null;
		}
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
			VirtualScapePiece tile = new VirtualScapePiece();
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
				// Particular tiles
				bB.getInt(); // m_NbTile
				readCString(bB); // m_PersonalTexture
				readCString(bB); // m_PersonalTextureSide
				readCString(bB); // m_Char
				readCString(bB); // m_Name
			}
			virtualScapeMap.getTiles().add(tile);
		}

		return toMap(virtualScapeMap,name);
	}

	public MapManager readMap(File mapFile) {
		try {
			byte[] bytes = Files.readAllBytes(mapFile.toPath());
			return readMap(bytes, mapFile.getName());
		} catch (IOException e) {
			LOGGER.error("Error while reading map " + mapFile.getAbsolutePath(), e);
		} catch (Exception e) {
			LOGGER.error("Error while reading map " + mapFile.getAbsolutePath(), e);
		}
		return null;
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
	
	private MapManager toMap(VirtualScapeMap virtualScapeMap, String defaultName) {
		Map map = new Map();
		MapManager mapManager = new MapManager(map);

		java.util.Map<Integer, List<VirtualScapePiece>> tilesByHeight = new HashMap<Integer, List<VirtualScapePiece>>();
		int maxHeight = 0;
		for (VirtualScapePiece tile : virtualScapeMap.getTiles()) {
			List<VirtualScapePiece> tiles = tilesByHeight.get(tile.getPosZ());
			if (tiles == null) {
				tiles = new ArrayList<VirtualScapePiece>();
				tilesByHeight.put(tile.getPosZ(), tiles);
			}
			tiles.add(tile);
			
			if (tile.getPosZ() > maxHeight) {
				maxHeight = tile.getPosZ();
			}
		}
		
		List<VirtualScapePiece> decors = new ArrayList<>();
		List<VirtualScapePiece> startZones = new ArrayList<>();
		for (int z = 0; z <= maxHeight; z++) {
			List<VirtualScapePiece> pieces = tilesByHeight.get(z);
			if (pieces != null) {
				for (VirtualScapePiece piece : pieces) {
					if (piece.getType() == 15001) {
						piece.setPosZ(z);
						startZones.add(piece);
					} else if (getTileType(piece) != null) {
						addTileToMap(piece, z, mapManager);
					} else {
						piece.setPosZ(z);
						decors.add(piece);
					}
				}
				
			}
			
		}
		
		for (VirtualScapePiece decor : decors) {
			addDecorToMap(decor, mapManager);
		}
		addStartZonesToMap(startZones, mapManager);
		
		mapManager.getMap().setName(virtualScapeMap.getName());
		if (StringUtils.isEmpty(mapManager.getMap().getName())) {
			mapManager.getMap().setName(defaultName);
		}
		
		return mapManager;
	}
	

	private void addDecorToMap(VirtualScapePiece decor, MapManager mapManager) {
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
			mapManager.getDecors().add(decorBean);

			// Some decors has Tiles under them
			if (type == VirtualScapeDecorType.BRUSH_1 || type == VirtualScapeDecorType.PALM_1) {
				addSingleTilesToMap(mapManager, 1, 0, TileType.SWAMP, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.HIVE_6) {
				addSingleTilesToMap(mapManager, 6, decor.getRotation(), TileType.SWAMP_WATER, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_6) {
				addSingleTilesToMap(mapManager, 6, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_4) {
				addSingleTilesToMap(mapManager, 4, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_3) {
				addSingleTilesToMap(mapManager, 3, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.GLACIER_1) {
				addSingleTilesToMap(mapManager, 1, decor.getRotation(), TileType.ICE, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.OUTCROP_3) {
				addSingleTilesToMap(mapManager, 3, decor.getRotation(), TileType.SHADOW, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.OUTCROP_1) {
				addSingleTilesToMap(mapManager, 1, decor.getRotation(), TileType.SHADOW, x, y, decor.getPosZ());
			} else if (type == VirtualScapeDecorType.CASTLE_LADDER) {
				addSingleTilesToMap(mapManager, 1, decor.getRotation(), TileType.INVISIBLE, x, y, decor.getPosZ());
			}
			
		}
	}
	
	private void addStartZonesToMap(List<VirtualScapePiece> startZones, MapManager mapManager) {
		
		java.util.Map<Integer, List<VirtualScapePiece>> map = new HashMap<>();
		
		for (VirtualScapePiece startZone : startZones) {
			List<VirtualScapePiece> list = map.get(startZone.getColor());
			if (list == null) {
				list = new ArrayList<>();
				map.put(startZone.getColor(), list);
			}
			list.add(startZone);
		}
		
		int i = 0;
		for (List<VirtualScapePiece> list : map.values()) {
			for (VirtualScapePiece startZone : list) {
				int x = convertCoordX(startZone);
				int y = convertCoordY(startZone);
				Tile tile = mapManager.getNearestTile(x, y, startZone.getPosZ());
				if (tile != null) {
					tile.setStartZone(true);
					tile.setStartZoneNumber(i);
				}
			}
			i++;
		}
	}


	private void addTileToMap(VirtualScapePiece tile, int z ,MapManager mapManager) {
		
		int tileNumber = tile.getType() % 1000;
		int rotation = tile.getRotation();
		
		TileType tileType = getTileType(tile);
		
		int x = convertCoordX(tile);
		int y = convertCoordY(tile);
		
		addSingleTilesToMap(mapManager, tileNumber, rotation, tileType, x, y, z);
	}


	private void addSingleTilesToMap(MapManager mapManager, int tileNumber, int rotation, TileType tileType, int x, int y, int z) {
		switch (tileNumber) {
		case 1:
			mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
			break;
		case 2:
			if (tileType == TileType.WATER || tileType == TileType.SHADOW) {
				// Somehow Virtualscape has shadow and water tiles "2", that are in fact normal single tiles
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);	
				break;
			}
			switch (rotation) {
			case 0:
			case 3:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 2:
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
		case 3:
			switch (rotation) {
			case 0:
			case 2:
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
			case 3:
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
		case 4:
			switch (rotation) {
			case 0:
			case 3:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 2:
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
		case 5:
			switch (rotation) {
			case 0:
			case 3:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 2:
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
		case 6:
			switch (rotation) {
			case 0:
			case 3:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 2:
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
		case 7:
			if (tileType == TileType.ROAD) {
				switch (rotation) {
				case 0:
					mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+3, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+3, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					break;
				case 1:
					mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					break;
				case 2:
					mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					break;
				case 3:
					mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+3, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					break;
				case 4:
					mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					break;
				case 5:
					mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
					break;
				}
				break;
			} else {
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
			}
			break;
		case 9:
			switch (rotation) {
			case 0:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 2:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 3:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
		case 24:
			switch (rotation) {
			case 0:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+6, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+6, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+7, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 1:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-6, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-6, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-7, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 2:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 3:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+6, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+6, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+6, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+7, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+6, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+7, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 4:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+5, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-6, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-6, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-6, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+4, y-6, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-7, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+3, y-7, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			case 5:
				mapManager.addTile(x, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-3, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-1, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-3, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-2, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x-1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-3, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+1, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-4, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				mapManager.addTile(x+2, y-5, z, tileType.isHalfSize(), tileType.getTopTexture(), tileType.getSideTexture(), tileType.isVisible(), false, 0);
				break;
			}
			break;
			default:
				throw new RuntimeException("Tile number " + tileNumber + " not supported yet");
		}
	}

	

	private int convertCoordX(VirtualScapePiece tile) {
		return tile.getPosX() + (tile.getPosY() + 1)/2;
	}


	private int convertCoordY(VirtualScapePiece tile) {
		return -tile.getPosY();
	}


	private TileType getTileType(VirtualScapePiece tile) {
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
	
	private VirtualScapeDecorType getDecorType(VirtualScapePiece tile) {
		
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
			if (tile.getType() > 14000 && tile.getType() < 14256) {
				decorType = VirtualScapeDecorType.HIDDEN_GLYPH;
			} else {
				decorType = null;
			}
		}
		
		return decorType;
	}
	
	private Direction getDirection(VirtualScapePiece tile) {
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
