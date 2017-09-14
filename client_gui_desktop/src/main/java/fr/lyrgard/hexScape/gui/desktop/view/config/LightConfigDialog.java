package fr.lyrgard.hexScape.gui.desktop.view.config;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import fr.lyrgard.hexScape.HexScapeCore;
import fr.lyrgard.hexScape.control.LightControl;
import net.miginfocom.swing.MigLayout;

public class LightConfigDialog extends JDialog {

	private static final long serialVersionUID = 1341073321559283227L;

	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Reset");
	JSlider ambientLightLevel;
	JSlider sunLightLevel;
	JSlider sunPosition;
	JSlider sunHeight;
	
	
	private int initialAmbiantLightLevel = 10;
	private int initialSunLightLevel = 7;
	
	public LightConfigDialog() {
		setLayout(new MigLayout(
				 "wrap", // Layout Constraints
				 "[][]", // Column constraints
				 "[][][][]20[]" // Row constraints
				));
		
		ambientLightLevel = new JSlider(LightControl.LIGHT_INTENSITY_MIN, LightControl.LIGHT_INTENSITY_MAX, HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().getAmbiantLightIntensity());
		sunLightLevel = new JSlider(LightControl.LIGHT_INTENSITY_MIN, LightControl.LIGHT_INTENSITY_MAX, HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().getSunLightIntensity());
		sunPosition = new JSlider(LightControl.SUN_POSITION_MIN, LightControl.SUN_POSITION_MAX, HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().getSunAngle());
		sunHeight = new JSlider(LightControl.SUN_HEIGHT_MIN, LightControl.SUN_HEIGHT_MAX, HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().getSunHeight());
		
		add(new JLabel("Sun light intensity"),"align right");
		add(sunLightLevel,"align left, wrap");
		add(new JLabel("Ambiant light intensity"),"align right");
		add(ambientLightLevel,"align left, wrap");
		add(new JLabel("Sun position"),"align right");
		add(sunPosition,"align left, wrap");
		add(new JLabel("Sun height"),"align right");
		add(sunHeight,"align left, wrap");
		add(okButton,"align right");
		add(cancelButton,"align left");
		
		sunLightLevel.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setSunIntensity(sunLightLevel.getValue());
			}
		});
		
		ambientLightLevel.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setAmbiantLightIntensity(ambientLightLevel.getValue());
			}
		});
		
		sunPosition.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setSunPosition(sunPosition.getValue());
			}
		});

		sunHeight.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				setSunHeight(sunHeight.getValue());
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().save();
				LightConfigDialog.this.dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setSunIntensity(initialSunLightLevel);
				setAmbiantLightIntensity(initialAmbiantLightLevel);
			}
		});
		pack();
	}
	
	private void setSunIntensity(int intensity) {
		HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().setSunLightIntensity(intensity);
	}
	
	private void setAmbiantLightIntensity(int intensity) {
		HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().setAmbiantLightIntensity(intensity);
	}
	
	private void setSunPosition(int position) {
		HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().setSunAngle(position);
	}
	
	private void setSunHeight(int height) {
		HexScapeCore.getInstance().getHexScapeJme3Application().getLightControl().setSunHeight(height);
	}
}
