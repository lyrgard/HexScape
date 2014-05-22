package fr.lyrgard.hexScape.service;

import java.util.Collection;
import java.util.List;

import fr.lyrgard.hexScape.model.DiceFace;
import fr.lyrgard.hexScape.model.DiceType;

public interface DiceService {

	Collection<DiceType> getDiceTypes();
	
	List<DiceFace> roll(int number, DiceType type);
}
