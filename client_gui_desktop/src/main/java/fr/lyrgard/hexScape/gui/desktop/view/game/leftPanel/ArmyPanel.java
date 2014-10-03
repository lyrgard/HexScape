package fr.lyrgard.hexScape.gui.desktop.view.game.leftPanel;

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
	
	public ArmyPanel(String playerId) {
		this.playerId = playerId;
		armyPanel = new JPanel();
		armyPanel.setLayout(new BoxLayout(armyPanel, BoxLayout.Y_AXIS));
		setViewportView(armyPanel);
		//setPreferredSize(new Dimension(180, 500));
		//setMaximumSize(new Dimension(180, 500));
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		
		armyPanel.add(new JButton(new ChooseArmyAction(getTopLevelAncestor(), playerId)));
	}
	
	public ArmyPanel(String playerId, Army army) {
		this(playerId);
		setArmy(army);
	}
	
	public void setArmy(Army army) {
		armyPanel.removeAll();
		if (army != null) {
			for (CardInstance card : army.getCards()) {
				armyPanel.add(new ArmyCardPanel(card, playerId));

			}
			armyPanel.validate();
			armyPanel.repaint();
			revalidate();
		} else {
			armyPanel.add(new JButton(new ChooseArmyAction(getTopLevelAncestor(), playerId)));
		}
	}
}
