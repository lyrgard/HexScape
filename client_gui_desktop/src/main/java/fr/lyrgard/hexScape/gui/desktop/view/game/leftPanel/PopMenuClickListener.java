package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class PopMenuClickListener extends MouseAdapter {
	
	private JPopupMenu menu;

	public  PopMenuClickListener(JPopupMenu menu) {
		super();
		this.menu = menu;
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
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
