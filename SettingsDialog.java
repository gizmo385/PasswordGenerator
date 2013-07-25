import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.util.Scanner;

/**
 * A dialog to manage PassGen settings from
 * @author gizmo385
 */
public class SettingsDialog extends JDialog implements ActionListener, ChangeListener {
	
	//Dialog components
	private JTextField wordlistDirectory, passwordPattern, maxCharLimit;
	private JCheckBox swapRandomCharacterPatterns, enableMaxCharLimit, enableRandomCapitalization;
	private JButton changeWordlistDirectory, defaultSettings, saveSettings, defaultWordlist, defaultPattern;
	private JSlider charSwapFrequency, randomCapitalizationFrequency;
	
	//JPanels
	private JPanel changeWordlistLocation, manageCapitalization, manageCharacterSwapping, managePattern, characterLimit, buttonPanel;
	
	//Settings
	private String wordlistDirectoryPath, currentPasswordPattern;
	private int currentMaxCharLimit, swapFrequency, capitalizationFrequency;
	private boolean randomlySwappingChars, maxCharLimitFlag, randomCapitalization;

	//Final variables
	private final int SWAP_FREQUENCY_MINIMUM = 0;
	private final int SWAP_FREQUENCY_MAXIMUM = 100;
	
	/**
	 * Creates the settings dialog
	 * @param parent The window that this dialog will be displayed relative to
	 */
	public SettingsDialog( JFrame parent ) {
		super( parent, "Settings", true );
		
		if( new File( ".settings.ini" ).exists() ) {
			loadSettingsFromFile();
			setUpComponents();
		}
		else {
			restoreDefaultSettings();
			setUpComponents();
		}
		
		super.add( this.changeWordlistLocation );
		super.add( this.managePattern );
		super.add( this.characterLimit );
		super.add( this.manageCapitalization );
		super.add( this.manageCharacterSwapping );
		super.add( this.buttonPanel );
		
		super.setSize( 350, 520 );
		super.setResizable( false );
		super.setLayout( new GridLayout( 0, 1 ) );
		super.setLocationRelativeTo( parent );
		super.setVisible( true );
		super.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
	}
	
	/**
	 * Restores the default settings and saves them
	 */
	private void restoreDefaultSettings() {
		//default settings
		this.wordlistDirectoryPath = "default";
		this.currentPasswordPattern = "sn*sns";
		this.currentMaxCharLimit = 0;
		this.swapFrequency = 5;
		this.capitalizationFrequency = 10;
		this.randomlySwappingChars = true;
		this.maxCharLimitFlag = false;
		this.randomCapitalization = true;
		
		//GUI values
		if( this.wordlistDirectory != null ) 
			this.wordlistDirectory.setText( this.wordlistDirectoryPath );
		if( this.passwordPattern != null ) 
			this.passwordPattern.setText( this.currentPasswordPattern );
		if( this.maxCharLimit != null ) 
			this.maxCharLimit.setText( this.currentMaxCharLimit + "" );
		if( this.swapRandomCharacterPatterns != null ) 
			this.swapRandomCharacterPatterns.setSelected( this.randomlySwappingChars );
		if( this.enableMaxCharLimit != null ) 
			this.enableMaxCharLimit.setSelected( this.maxCharLimitFlag );
		if( this.enableRandomCapitalization != null ) 
			this.enableRandomCapitalization.setSelected( this.randomCapitalization );
		if( this.charSwapFrequency != null )
			this.charSwapFrequency.setValue( this.swapFrequency );
		if( this.randomCapitalizationFrequency != null )
			this.randomCapitalizationFrequency.setValue( this.capitalizationFrequency );
		
		//save
		saveSettingsToFile();
	}
	
	/**
	 * Loads the user's settings from the save file
	 */
	private void loadSettingsFromFile() {
		File saveFile = new File( ".settings.ini" );
		
		try {
			//if no save file can be found, load with default settings and save
			if( saveFile.exists() == false ) {
				restoreDefaultSettings();
			}
			else {
				Scanner fileScan = new Scanner( saveFile );
				this.wordlistDirectoryPath = fileScan.nextLine();
				this.currentPasswordPattern = fileScan.nextLine();;
				this.currentMaxCharLimit = Integer.parseInt( fileScan.nextLine() );
				this.swapFrequency = Integer.parseInt( fileScan.nextLine() );
				this.capitalizationFrequency = Integer.parseInt( fileScan.nextLine() );
				this.randomlySwappingChars = Boolean.parseBoolean( fileScan.nextLine() );
				this.maxCharLimitFlag = Boolean.parseBoolean( fileScan.nextLine() );
				this.randomCapitalization = Boolean.parseBoolean( fileScan.nextLine() );
				fileScan.close();
			}
		}
		catch( Exception e ) {
			//An error in loading (IOException, NumberFormatException, etc) indicates a corrupt save
			restoreDefaultSettings();
		}
	}
	
	/** 
	 * Saves the user settings to the save file
	 */
	private void saveSettingsToFile() {
		File saveFile = new File( ".settings.ini" );
		
		try {
			//delete the file if it already exists
			if( saveFile.exists() ) {
				saveFile.delete();
			}
			//create the file
			saveFile.createNewFile();
			
			this.wordlistDirectoryPath = this.wordlistDirectory == null ? "default" : this.wordlistDirectory.getText();
			this.currentPasswordPattern = this.passwordPattern == null ? "sn*sns" : this.passwordPattern.getText();
			this.currentMaxCharLimit = Integer.parseInt( this.maxCharLimit == null ? "0" : this.maxCharLimit.getText() ) ;
			this.swapFrequency = this.charSwapFrequency == null ? 33 : this.charSwapFrequency.getValue();;
			this.capitalizationFrequency = this.randomCapitalizationFrequency == null ? 33 : this.randomCapitalizationFrequency.getValue();
			this.randomlySwappingChars = this.swapRandomCharacterPatterns == null ? true : this.swapRandomCharacterPatterns.isSelected();
			this.maxCharLimitFlag = this.enableMaxCharLimit == null ? false : this.enableMaxCharLimit.isSelected();
			this.randomCapitalization = this.enableRandomCapitalization == null ? true : this.enableRandomCapitalization.isSelected();
			
			//write to the file
			BufferedWriter bw = new BufferedWriter( new FileWriter( saveFile ) );
			bw.write( this.wordlistDirectoryPath + System.lineSeparator() );
			bw.write( this.currentPasswordPattern + System.lineSeparator() );
			bw.write( this.currentMaxCharLimit + System.lineSeparator() );
			bw.write( this.swapFrequency + System.lineSeparator() );
			bw.write( this.capitalizationFrequency + System.lineSeparator() );
			bw.write( this.randomlySwappingChars + System.lineSeparator() );
			bw.write( this.maxCharLimitFlag + System.lineSeparator() );
			bw.write( this.randomCapitalization + System.lineSeparator() );
			bw.flush();
			
			//close the writer
			bw.close();
			JOptionPane.showMessageDialog( this, "Settings have been saved.", "Settings saved.", JOptionPane.INFORMATION_MESSAGE );
			
		}
		catch( IOException ioe ) {
			System.err.println( ioe );
		}
	}
	
	/**
	 * Sets up window components and layout
	 */
	private final void setUpComponents() {	
		/* Set up components */
		//JTextFields
		this.wordlistDirectory = new JTextField( 23 );
		this.wordlistDirectory.addActionListener( this );
		this.wordlistDirectory.setText( this.wordlistDirectoryPath );
		
		this.passwordPattern = new JTextField( 23 );
		this.passwordPattern.setText( this.currentPasswordPattern );
		
		this.maxCharLimit = new JTextField( 23 );
		this.maxCharLimit.setText( this.currentMaxCharLimit + "" );
		
		//JCheckBoxes
		this.swapRandomCharacterPatterns = new JCheckBox( "Enable character switching for generated passwords: ", this.randomlySwappingChars );
		this.swapRandomCharacterPatterns.setToolTipText( "Will randomly switch characters in your generated passwords." );
		
		this.enableMaxCharLimit = new JCheckBox( "Enable character limit for generated passwords: ", this.maxCharLimitFlag );
		this.enableMaxCharLimit.setToolTipText( "Enabling this will allow the user to specify a length that all passwords may not exceed" );
		
		this.enableRandomCapitalization = new JCheckBox( "Enable random capitalization for generated passwords: ", this.randomCapitalization );
		this.enableRandomCapitalization.setToolTipText( "Enabling this will randomly flip the capitalization on characters in generated passwords" );
		
		//JSliders
		this.charSwapFrequency = new JSlider( this.SWAP_FREQUENCY_MINIMUM, this.SWAP_FREQUENCY_MAXIMUM, this.swapFrequency );
		this.charSwapFrequency.setToolTipText( "The rate at which characters in your password are swapped for random characters (if enabled) - currently: " + this.swapFrequency );
		this.charSwapFrequency.addChangeListener( this );
		
		this.randomCapitalizationFrequency = new JSlider( this.SWAP_FREQUENCY_MINIMUM, this.SWAP_FREQUENCY_MAXIMUM, this.capitalizationFrequency );
		this.randomCapitalizationFrequency.setToolTipText( "The rate at which characters randomly have their capitalization flipped (if enabled) - currently: " + this.swapFrequency );
		this.randomCapitalizationFrequency.addChangeListener( this );
		
		//JButtons
		this.changeWordlistDirectory = new JButton( "Browse" );
		this.changeWordlistDirectory.addActionListener( this );
		
		this.defaultSettings = new JButton( "Return to default settings" );
		this.defaultSettings.addActionListener( this );
		
		this.saveSettings = new JButton( "Save settings" );
		this.saveSettings.addActionListener( this );
		
		this.defaultWordlist = new JButton( "Restore default wordlist" );
		this.defaultWordlist.addActionListener( this );
		
		this.defaultPattern = new JButton( "Restore default pattern" );
		this.defaultPattern.addActionListener( this );
		
		/* Set up JPanels */
		this.changeWordlistLocation = new JPanel();
		this.changeWordlistLocation.setLayout( new FlowLayout() );
		this.changeWordlistLocation.setBorder( BorderFactory.createTitledBorder( "Wordlist management" ) );
		
		this.manageCapitalization = new JPanel();
		this.manageCapitalization.setLayout( new FlowLayout() );
		this.manageCapitalization.setBorder( BorderFactory.createTitledBorder( "Password capitalization settings" ) );
		
		this.manageCharacterSwapping = new JPanel();
		this.manageCharacterSwapping.setLayout( new FlowLayout() );
		this.manageCharacterSwapping.setBorder( BorderFactory.createTitledBorder( "Character swap settings" ) );
		
		this.managePattern = new JPanel();
		this.managePattern.setLayout( new FlowLayout() );
		this.managePattern.setBorder( BorderFactory.createTitledBorder( "Password pattern settings" ) );
		
		this.characterLimit = new JPanel();
		this.characterLimit.setLayout( new FlowLayout() );
		this.characterLimit.setBorder( BorderFactory.createTitledBorder( "Password character limit" ) );
		
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout( new FlowLayout() );

		/* Add items to JPanels */
		this.changeWordlistLocation.add( this.wordlistDirectory );
		this.changeWordlistLocation.add( this.changeWordlistDirectory );
		this.changeWordlistLocation.add( this.defaultWordlist );
		
		this.manageCapitalization.add( this.enableRandomCapitalization );
		this.manageCapitalization.add( new JLabel("0") );
		this.manageCapitalization.add( this.randomCapitalizationFrequency );
		this.manageCapitalization.add( new JLabel("100") );
		
		this.manageCharacterSwapping.add( this.swapRandomCharacterPatterns );
		this.manageCharacterSwapping.add( new JLabel("0") );
		this.manageCharacterSwapping.add( this.charSwapFrequency );
		this.manageCharacterSwapping.add( new JLabel("100") );
		
		this.managePattern.add( this.passwordPattern );
		this.managePattern.add( this.defaultPattern );
		
		this.characterLimit.add( this.enableMaxCharLimit );
		this.characterLimit.add( this.maxCharLimit );
		
		this.buttonPanel.add( this.saveSettings );
		this.buttonPanel.add( this.defaultSettings );
	}
	
	/**
	 * Manage button actions
	 * @param ae The ActionEvent triggered by the user
	 */
	public void actionPerformed( ActionEvent ae ) {
		if( ae.getSource() == saveSettings ) {
			saveSettingsToFile();
		}
		
		else if( ae.getSource() == defaultSettings ) {
			restoreDefaultSettings();
		}
		
		else if( ae.getSource() == defaultWordlist ) {
			this.wordlistDirectory.setText( "default" );
			this.wordlistDirectoryPath = "default";
		}
		
		else if( ae.getSource() == defaultPattern ) {
			this.passwordPattern.setText( "sn*sns" );
			this.currentPasswordPattern = "sn*sns";
		}
		
		else if( ae.getSource() == changeWordlistDirectory ) {
			JFileChooser jfc = new JFileChooser( new File ( this.wordlistDirectoryPath ) );
			jfc.setFileFilter( new FileNameExtensionFilter( "Text files", "txt" ) );

			int returnVal = jfc.showOpenDialog( this );
			if( returnVal == JFileChooser.APPROVE_OPTION ) {
				this.wordlistDirectoryPath = jfc.getSelectedFile().getPath();
				this.wordlistDirectory.setText( this.wordlistDirectoryPath );
			}
		}
	}
	
	/**
	 * Manages state changes (JSliders)
	 *
	 * Changing the value of the JSlider will trigger 2 events. 1) The respective variable will be updated and 2) The tool tip text will be changed to reflect the new value
	 * @param ce The ChangeEvent triggered by the user
	 */
	public void stateChanged( ChangeEvent ce ) {
		if( ce.getSource() == this.randomCapitalizationFrequency ) {
			this.capitalizationFrequency = this.randomCapitalizationFrequency.getValue();
			this.randomCapitalizationFrequency.setToolTipText( "The rate at which characters randomly have their capitalization flipped (if enabled) - currently: " + this.capitalizationFrequency );
		}
		
		else if( ce.getSource() == this.charSwapFrequency ) {
			this.swapFrequency = this.charSwapFrequency.getValue();
			this.charSwapFrequency.setToolTipText( "The rate at which characters in your password are swapped for random characters (if enabled) - currently: " + this.swapFrequency );
		}
	}
	
	/**
	 * Used solely to test the dialog outside of it's final environment.
	 */
	public static void main( String[] args ) {
		SettingsDialog sd = new SettingsDialog( null );
	}
}