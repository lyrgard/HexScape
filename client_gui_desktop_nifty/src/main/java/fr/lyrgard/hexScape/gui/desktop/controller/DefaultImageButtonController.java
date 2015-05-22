package fr.lyrgard.hexScape.gui.desktop.controller;

import org.apache.commons.lang.StringUtils;

public class DefaultImageButtonController extends AbstractImageButtonController {

	public static final String GOTO_ATTRIBUTE = "goTo";
	
	@Override
	public void onClick() {
		String gotoScreen =  attributes.get(GOTO_ATTRIBUTE);
		if (StringUtils.isNotEmpty(gotoScreen)) {
			nifty.gotoScreen(gotoScreen);
		}
	}

}
