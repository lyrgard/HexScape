package fr.lyrgard.hexScape.gui.desktop.controller.chat;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.ScrollPanel;
import de.lessvoid.nifty.controls.ScrollPanel.AutoScroll;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.model.player.User;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main controller for the chat control.
 * 
 * @author Mark
 * @version 0.1
 */
public class HexScapeChatControl extends AbstractController implements KeyInputHandler {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private static final String CHAT_BOX = "#chatBox";
	private static final String PLAYER_LIST = "#playerList";
	private static final String CHAT_TEXT_INPUT = "#chat-text-input";
	private static final String CONTENT = "#content";
	private static final Logger LOGGER = Logger.getLogger(HexScapeChatControl.class.getName());
	private TextField textControl;
	private UserComparator playerComparator = new UserComparator();
	private Nifty nifty;
	private TreeSet<User> playerList = new TreeSet<User>(playerComparator);
	private List<ChatEntryModel> linesBuffer = new ArrayList<ChatEntryModel>();

	private ScrollPanel chatScrollPanel;
	private Element chatContent;
	private ScrollPanel playerScrollPanel;
	private Element playerContent;
	private List<ChatEntryModel> chatLines = new ArrayList<ChatEntryModel>();

	/**
	 * Default constructor.
	 */
	public HexScapeChatControl() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void bind(final Nifty niftyParam, final Screen screenParam, final Element newElement, final Properties properties, final Attributes controlDefinitionAttributes) {
		super.bind(newElement);
		LOGGER.fine("binding chat control");
		nifty = niftyParam;

		chatScrollPanel = getScrollPanel(CHAT_BOX);
		chatContent = chatScrollPanel.getElement().findElementByName(CONTENT);
		chatContent.setWidth(chatScrollPanel.getWidth() - 35);

		playerScrollPanel = getScrollPanel(PLAYER_LIST);
		playerContent = playerScrollPanel.getElement().findElementByName(CONTENT);
		playerContent.setWidth(playerScrollPanel.getWidth() - 10);


		// this buffer is needed because in some cases the entry is added to either list before the emelent is bound.
		while (!playerList.isEmpty()) {
			refreshUsersDisplay();
		}
		while (!linesBuffer.isEmpty()) {
			ChatEntryModel line = linesBuffer.remove(0);
			receivedChatLine(line);        
		}
	}

	private void refreshUsersDisplay() {
		for (Element element : playerContent.getElements()) {
			element.markForRemoval();
		}

		for (final User user : playerList) {
			new PanelBuilder() {{
				width(percentage(100));
				childLayout(ChildLayoutType.Horizontal);
				paddingTop("2px");
				image(new ImageBuilder() {{
					width("12px");
					height("11px");
					if (user.isPlayingGame()) {
						filename("/gui/images/chat/player.png");
					} else {
						filename("/gui/images/chat/observer.png");
					}
					valign(VAlign.Top);
					marginLeft("1px");
					marginRight("1px");
					marginTop("2px");
				}});
				text(new TextBuilder() {{
					if (user.isPlayingGame()) {
						text(user.getPlayer().getDisplayName());
						style(ChatUtils.getHeaderStyle(user.getPlayer()));
					} else {
						text(user.getName());
						style(ChatUtils.getHeaderStyle(null));
					}

					textHAlign(Align.Left);
					wrap(true);
					width(percentage(100));
					marginLeft("1px");
					marginRight("1px");
				}});
			}}.build(nifty, nifty.getCurrentScreen(), playerContent);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFocus(final boolean arg0) {
		textControl.setFocus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onStartScreen() {
		LOGGER.fine("starting chat screen");
		textControl = getElement().findNiftyControl(CHAT_TEXT_INPUT, TextField.class);
		textControl.getElement().addInputHandler(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public void receivedChatLine(final ChatEntryModel line) {
		HexScapeCore.getInstance().getHexScapeJme3Application().enqueue(new Callable<Void>() {
			
			public Void call() throws Exception {
				if (linesBuffer.isEmpty()) {
					try {

						switch (line.getType()) {
						case ACTION:
							new PanelBuilder() {{
								width(percentage(100));
								childLayout(ChildLayoutType.Horizontal);
								paddingTop("2px");
								image(new ImageBuilder() {{
									width("12px");
									height("11px");
									filename("gui/images/chat/action.png");
									valign(VAlign.Top);
									marginLeft("1px");
									marginRight("1px");
									marginTop("2px");
								}});
								text(new TextBuilder() {{
									text(sdf.format(new Date()) + " - " + line.getActorName() + " : " + line.getMessage());
									style(ChatUtils.getHeaderStyle(line.getPlayer()));
									textHAlign(Align.Left);
									wrap(true);
									width(percentage(100));
									marginLeft("1px");
									marginRight("1px");
								}});
							}}.build(nifty, nifty.getCurrentScreen(), chatContent);
							break;
						case MESSAGE:
							new PanelBuilder() {{
								width(percentage(100));
								childLayout(ChildLayoutType.Horizontal);
								paddingTop("2px");
								image(new ImageBuilder() {{
									width("12px");
									height("11px");
									filename("gui/images/chat/message.png");
									valign(VAlign.Center);
									marginLeft("1px");
									marginRight("1px");
								}});
								text(new TextBuilder() {{
									text(sdf.format(new Date()) + " - " + line.getActorName() + " :");
									style(ChatUtils.getHeaderStyle(line.getPlayer()));
									textHAlign(Align.Left);
									wrap(true);
									width(percentage(100));
									marginLeft("1px");
									marginRight("1px");
								}});
							}}.build(nifty, nifty.getCurrentScreen(), chatContent);
							new PanelBuilder() {{
								width(percentage(100));
								childLayout(ChildLayoutType.Horizontal);
								paddingTop("2px");
								panel(new PanelBuilder() {{
									width("12px");
									height("2px");
								}});
								text(new TextBuilder() {{
									text(line.getMessage());
									style("userText");
									textHAlign(Align.Left);
									wrap(true);
									width(percentage(100));
								}});
							}}.build(nifty, nifty.getCurrentScreen(), chatContent);
							break;
						case DICE_ROLL:
							new PanelBuilder() {{
								width(percentage(100));
								childLayout(ChildLayoutType.Horizontal);
								paddingTop("2px");
								image(new ImageBuilder() {{
									width("11px");
									height("11px");
									filename("gui/images/chat/dice.png");
									valign(VAlign.Center);
									marginLeft("1px");
									marginRight("1px");
								}});
								text(new TextBuilder() {{
									text(sdf.format(new Date()) + " - " + line.getActorName() + " rolled : ");
									style(ChatUtils.getHeaderStyle(line.getPlayer()));
									textHAlign(Align.Left);
									wrap(true);
									width(percentage(100));
									marginLeft("1px");
									marginRight("1px");
								}});
							}}.build(nifty, nifty.getCurrentScreen(), chatContent);
							Element diceResultContainer = new PanelBuilder() {{
								width(percentage(100));
								childLayout(ChildLayoutType.Horizontal);
								paddingTop("2px");
								panel(new PanelBuilder() {{
									width("12px");
									height("2px");
								}});

							}}.build(nifty, nifty.getCurrentScreen(), chatContent);

							Element diceResultPanel = new PanelBuilder() {{
								width(percentage(100));
								set("childLayout", "flow");
								paddingTop("2px");
								for (final Integer result : line.getDiceResults()) {
									image(new ImageBuilder() {{
										filename(HexScapeCore.APP_DATA_FOLDER.toURI().relativize(line.getDiceType().getFaces().get(result).getImage().toURI()).getPath());
										width("24px");
										height("24px");
										marginLeft("1px");
										marginRight("1px");
									}});
								}
							}}.build(nifty, nifty.getCurrentScreen(), diceResultContainer);

							//diceResultPanel.setLayoutManager(FLOW_LAYOUT);
							//diceResultContainer.resetLayout();
							break;
						}

						// Autoscroll to bottom
						chatScrollPanel.getElement().layoutElements();
						chatScrollPanel.setUp(0, 5, 0, 50, AutoScroll.OFF);
						chatScrollPanel.setVerticalPos(chatContent.getHeight());

						// Add line to lines list
						chatLines.add(line);
					} catch (NullPointerException npe) {
						linesBuffer.add(line);
					}
				} else {
					linesBuffer.add(line);
				}
				return null;
			}
		});
	}

	/**
	 * {@inheritDoc 
	 */
	public void addUser(User user) {
		playerList.add(user);
		refreshUsersDisplay();
	}

	/**
	 * {@inheritDoc 
	 */
	public final void removeUser(final User user) {
		playerList.remove(user);
		refreshUsersDisplay();
	}

	/**
	 * {@inheritDoc}
	 */
	public TreeSet<User> getUsers() {
		return playerList;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ChatEntryModel> getLines() {
		return chatLines;
	}

	public void clear() {
		for (Element element : chatContent.getElements()) {
			element.markForRemoval();
		}
		chatLines.clear();
		playerList.clear();
		refreshUsersDisplay();
	}

	/**
	 * {@inheritDoc}
	 */
	public void update() {
		refreshUsersDisplay();
	}

	/**
	 * This method is called when the player either presses the send button or
	 * the Return key.
	 */
	public final void sendText() {
		final String text = textControl.getText();
		LOGGER.log(Level.FINE, "sending text {0}", text);
		nifty.publishEvent(getId(), new HexScapeChatTextSendEvent(this, text));
		textControl.setText("");
	}

	private ScrollPanel getScrollPanel(final String name) {
		return (ScrollPanel) getElement().findNiftyControl(name, ScrollPanel.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean keyEvent(final NiftyInputEvent inputEvent) {
		LOGGER.log(Level.FINE, "event received: {0}", inputEvent);
		if (inputEvent == NiftyInputEvent.SubmitText) {
			sendText();
			return true;
		} else if (inputEvent == NiftyInputEvent.MoveCursorRight) {
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean inputEvent(final NiftyInputEvent inputEvent) {
		return keyEvent(inputEvent);
	}

	/**
	 * Class used to sort the list of players by name.
	 * 
	 * @author Mark
	 * @version 0.2
	 */
	private class UserComparator implements Comparator<User> {

		/**
		 * Default constructor.
		 */
		public UserComparator() {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(final User user1, final User user2) {
			if (user1.isPlayingGame()) {
				if (user2.isPlayingGame()) {
					return user1.getName().compareTo(user2.getName());
				} else {
					return 1;
				}
			} else {
				if (user2.isPlayingGame()) {
					return -1;
				} else {
					return user1.getName().compareTo(user2.getName());
				}
			}
		}
	}


}
