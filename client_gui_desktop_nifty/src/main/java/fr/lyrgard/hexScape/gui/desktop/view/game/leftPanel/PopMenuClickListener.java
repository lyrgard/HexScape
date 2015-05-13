package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class PopMenuClickListener extends MouseAdapter {
	
	private JPopupMenu menu;
	
	private PopupMenuFatcory menuFactory;

	public  PopMenuClickListener(JPopupMenu menu) {
		super();
		this.menu = menu;
	}
	
	public  PopMenuClickListener(PopupMenuFatcory menuFactory) {
		super();
		this.menuFactory = menuFactory;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

	@Override
    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e){
    	JPopupMenu theMenu;
    	if (menu == null) {
    		theMenu = menuFactory.getMenu();
    	} else {
    		theMenu = menu;
    	}
    	theMenu.show(e.getComponent(), e.getX(), e.getY());
    }
}
