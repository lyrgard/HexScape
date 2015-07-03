package fr.lyrgard.hexScape.gui.desktop.controller.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import com.google.common.eventbus.Subscribe;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.gui.desktop.controller.AbstractImageButtonController;
import fr.lyrgard.hexScape.gui.desktop.controller.ImageButtonBuilder;
import fr.lyrgard.hexScape.gui.desktop.message.ChangeDieTypeSelectedMessage;
import fr.lyrgard.hexScape.model.dice.DiceType;
import fr.lyrgard.hexScape.service.DiceService;

public class DicePanelController {

	private Map<String, Element> images = new HashMap<>();
	private Element rollDicePanel;
	private Nifty nifty;
	private Screen screen;

	public DicePanelController(final Nifty nifty, final Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
		
		final Element diceTypesPanel = screen.findElementByName("diceTypesPanel");
		rollDicePanel = screen.findElementByName("rollDicePanel");
		
		final Collection<DiceType> diceTypes = DiceService.getInstance().getDiceTypes();
		
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				for (Element element : diceTypesPanel.getElements()) {
					element.markForRemoval();
				}
				
				new PanelBuilder() {{
					width("*");
					height("1px");
				}}.build(nifty, screen, diceTypesPanel);
				
				for (final DiceType dieType : diceTypes) {
					
					Element image = new ImageButtonBuilder("dieSelectorButton_" + dieType.getId()) {{
						width("24px");
						height("24px");
						controller("fr.lyrgard.hexScape.gui.desktop.controller.game.DiceTypeButtonController");
						parameter("buttonImage", HexScapeCore.APP_DATA_FOLDER.toURI().relativize(dieType.getIconFile().toURI()).getPath());
						parameter(DieButtonController.DIE_TYPE_ID, dieType.getId());
						
					}}.build(nifty, screen, diceTypesPanel);
					
					images.put(dieType.getId(), image);
				}	
				selectDieType(diceTypes.iterator().next().getId());
				return null;
			}
		});
		
		
		
	}
	
	private void selectDieType(String dieTypeId) {
		
		final DiceType dieType = DiceService.getInstance().getDiceType(dieTypeId);

		if (dieType != null) {

			for (Element image : images.values()) {
				image.getControl(AbstractImageButtonController.class).desactivate();
			}

			Element image = images.get(dieTypeId);
			image.getControl(AbstractImageButtonController.class).activate();

			for (Element child : rollDicePanel.getElements()) {
				child.markForRemoval();
			}

			for (int i = 1; i <= dieType.getMaxNumberThrown(); i++) {
				final int number = i;
				new ImageButtonBuilder("launchdiceButton_" + dieType.getId() + "_" + number) {{
					width("24px");
					height("24px");
					controller("fr.lyrgard.hexScape.gui.desktop.controller.game.DieButtonController");
					parameter(DieButtonController.DIE_TYPE_ID, dieType.getId());
					parameter(DieButtonController.NUMBER_OF_DICE, String.valueOf(number));
					parameter("text", String.valueOf(number));
					parameter("buttonImage", "gui/images/dieButton.png");
				}}.build(nifty, screen, rollDicePanel);
			}
		}
	}
	
	@Subscribe public void onChangeDieTypeSelected(final ChangeDieTypeSelectedMessage message) {
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				selectDieType(message.getNewDieTypeId());
				return null;
			}
		});
	}
}
