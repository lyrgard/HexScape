package fr.lyrgard.hexScape.gui.desktop.components.cardComponent;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.lyrgard.hexScape.gui.desktop.action.ChooseArmyAction;
import fr.lyrgard.hexScape.model.card.Army;
import fr.lyrgard.hexScape.model.card.CardInstance;

public class ArmyPanel extends JScrollPane {

	private static final long serialVersionUID = -3883276797446834875L;
	
	private JPanel armyPanel;
	
	private String playerId;
	
	public ArmyPanel() {
		armyPanel = new JPanel();
		armyPanel.setLayout(new BoxLayout(armyPanel, BoxLayout.Y_AXIS));
		setViewportView(armyPanel);
		setPreferredSize(new Dimension(180, 500));
		setMaximumSize(new Dimension(180, 500));
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		
		armyPanel.add(new JButton(new ChooseArmyAction(getTopLevelAncestor())));
	}
	
	public ArmyPanel(String playerId, Army army) {
		this();
		setArmy(army, playerId);
	}
	
	public void setArmy(Army army, String playerId) {
		this.playerId = playerId;
		armyPanel.removeAll();
		if (army != null) {
			for (CardInstance card : army.getCardsById().values()) {
				armyPanel.add(new ArmyCardPanel(card, playerId));

			}
			armyPanel.validate();
			armyPanel.repaint();
		} else {
			armyPanel.add(new JButton(new ChooseArmyAction(getTopLevelAncestor())));
		}
	}
}
