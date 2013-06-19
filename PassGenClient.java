import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class PassGenClient extends JFrame implements ActionListener {
	
	private PasswordGenerator pg;
	private String passwordPattern, filePath;
	private JTextField seedField, outputField;
	private JButton randomSeed, generate;
	private JPanel controls, output;
	private TitledBorder controlsBorder, outputBorder;
	private JMenuBar menuBar;
	private JMenu file, about, changeSettings;
	private JMenuItem exit, howToUse, aboutProgram, changeFilePath, resetFilePath, changePasswordPattern, sourceCode;
	
	public PassGenClient() {
		super( "Password Generator" );
		
		//Methods to set up various components of program
		setUpPanels();
		setUpMenuBar();
		loadSettings();
		
		//JFrame settings
		super.add( this.controls );
		super.add( this.output );
		super.setLayout( new FlowLayout() );
		super.setSize( 300, 180 );
		super.setLocationRelativeTo( null );
		super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		super.setResizable( false );
		super.setJMenuBar( this.menuBar );
		super.setVisible( true );
		
		try {
			super.setIconImage( ImageIO.read( getClass().getResource( "res/lock.png" ) ) );
		}
		catch( IOException ioe ) {
			JOptionPane.showMessageDialog( this, "Could not load application icon!" , "Error!", JOptionPane.ERROR_MESSAGE );
		}
	}
	
	/**
	 * Manage program actions
	 */
	public void actionPerformed( ActionEvent ae ) {
		if( ae.getSource() == generate || ae.getSource() == seedField ) {
			this.outputField.setText( pg.generate( this.seedField.getText(), this.passwordPattern ) );
		}
		
		else if( ae.getSource() == randomSeed ) {
			this.seedField.setText( System.currentTimeMillis() + "" );
		}
		
		else if( ae.getSource() == exit ) {
			System.exit(0);
		}
		
		else if( ae.getSource() == changeFilePath ) {
			changeDefaultFilePath();
		}
		
		else if( ae.getSource() == changePasswordPattern ) {
			String newPasswordPattern = JOptionPane.showInputDialog( this, "Enter a new password pattern - current pattern is: " + this.passwordPattern, "Change password pattern", JOptionPane.INFORMATION_MESSAGE );
			
			if( newPasswordPattern != null && newPasswordPattern.equals("") == false ) {
				int confirm = JOptionPane.showConfirmDialog( this, "Are you sure you want \"" + newPasswordPattern + "\" to be your new pattern?", "Confirmation", JOptionPane.INFORMATION_MESSAGE );
				if( confirm == JOptionPane.YES_OPTION ) {
					this.passwordPattern = newPasswordPattern;
					saveSettings();
					JOptionPane.showMessageDialog( this, "Settings have been saved!", "Settings information", JOptionPane.INFORMATION_MESSAGE );
				}
				else JOptionPane.showMessageDialog( this, "Settings not saved!", "Settings information", JOptionPane.INFORMATION_MESSAGE );
			}
		}
		
		else if( ae.getSource() == aboutProgram ) {
			JOptionPane.showMessageDialog( this, "PassGen was created by Gizmo385 and is freely distributed, along \nwith " + 
													"source code to any and all who wish to view it.", "About", JOptionPane.INFORMATION_MESSAGE );
		}
		
		else if( ae.getSource() == sourceCode ) {
			JOptionPane.showMessageDialog( this, "The source code for PassGen is freely accessible to any and all who wish to view it on Github @gizmo385", "Source code information", JOptionPane.INFORMATION_MESSAGE );
		}
		
		else if( ae.getSource() == howToUse ) {
			HelpDialog hd = new HelpDialog( this );
		}
		
		else if( ae.getSource() == resetFilePath ) {
			this.filePath = "default";
			saveSettings();
		}
	}
	
	/**
	 * Create a file chooser to change the default wordlist file path
	 */
	private void changeDefaultFilePath() {
		JFileChooser jfc = new JFileChooser( new File ( this.filePath ) );
		jfc.setFileFilter( new FileNameExtensionFilter( "Text files", "txt" ) );
		
		int returnVal = jfc.showOpenDialog( this );
    	if( returnVal == JFileChooser.APPROVE_OPTION ) {
            this.filePath = jfc.getSelectedFile().getPath();
				saveSettings();
				loadSettings();
    	}
	}
	
	/**
	 * Saves program settings
	 */
	private final void saveSettings() {
		try {
			File settingsFile = new File( ".settings.ini" );
			
			if( settingsFile.exists() )
				settingsFile.delete();
			
			settingsFile.createNewFile();
			
			BufferedWriter bw = new BufferedWriter( new FileWriter( settingsFile ) );
			bw.write( this.passwordPattern + System.lineSeparator() );
			bw.write( this.filePath );
			bw.flush();
		}
		catch( IOException ioe ) {
			JOptionPane.showMessageDialog( this, "Error saving settings!", "Error!", JOptionPane.ERROR_MESSAGE );
		}
	}
	
	/**
	 * Loads program settings
	 */
	private final void loadSettings() {
		try {
			File settingsFile = new File( ".settings.ini" );
			
			Scanner fileScan = new Scanner( settingsFile );
			this.passwordPattern = fileScan.nextLine();
				
			this.filePath = fileScan.nextLine();
			if( this.filePath.equalsIgnoreCase( "default" ) ) {
				this.pg = new PasswordGenerator();
			}
			else {
				try {
					this.pg = new PasswordGenerator( this.filePath );
				}
				catch( FileNotFoundException fnfe ) {
					JOptionPane.showMessageDialog( this, "Could not locate specified wordlist! Loading default!", "Error loading wordlist!", JOptionPane.ERROR_MESSAGE );
					this.pg = new PasswordGenerator();
				}
			}
			fileScan.close();

			
		}
		catch( Exception e ) {
			this.passwordPattern = "snsns";
			this.pg = new PasswordGenerator();
			this.filePath = "default";
			saveSettings();
		}	
	}
	
	/**
	 * Sets up JPanels used in the GUI
	 */
	private final void setUpPanels() {
		//panel components
		this.seedField = new JTextField( 15 );
		this.seedField.addActionListener( this );
		this.seedField.setToolTipText( "Enter the seed for your password: " );
		
		this.outputField = new JTextField( 23 );
		this.outputField.setEditable( false );
		
		this.randomSeed = new JButton( "#" );
		this.randomSeed.addActionListener( this );
		this.randomSeed.setToolTipText( "Get a random seed, generated by the system time" );
		
		this.generate = new JButton( ">" );
		this.generate.addActionListener( this );
		this.controlsBorder = BorderFactory.createTitledBorder( "Generator Controls" );
		this.outputBorder = BorderFactory.createTitledBorder( "Output" );
		
		//panels
		this.output = new JPanel();
		this.output.add( outputField );
		this.output.setBorder( outputBorder );
		
		this.controls = new JPanel();
		this.controls.add( seedField );
		this.controls.add( randomSeed );
		this.controls.add( generate );
		this.controls.add( output );
		this.controls.setLayout( new FlowLayout() );
		this.controls.setBorder( controlsBorder );
	}
	
	/**
	 * Sets up the menu bar
	 */
	private final void setUpMenuBar() {
		//menu items
		this.exit = new JMenuItem( "Exit" );
		this.exit.addActionListener( this );
		this.exit.setIcon( new ImageIcon( getClass().getResource( "res/close.png" ) ) );
		this.howToUse = new JMenuItem( "Help" );
		this.howToUse.addActionListener( this );
		this.aboutProgram = new JMenuItem( "About" );
		this.aboutProgram.addActionListener( this );
		this.changePasswordPattern = new JMenuItem( "Change password pattern" );
		this.changePasswordPattern.addActionListener( this );
		this.sourceCode = new JMenuItem( "Program source" );
		this.sourceCode.addActionListener( this );
		this.changeFilePath = new JMenuItem( "Use custom wordlist" );
		this.changeFilePath.addActionListener( this );
		this.resetFilePath = new JMenuItem( "Use default wordlist" );
		this.resetFilePath.addActionListener( this );
		
		//menus
		this.changeSettings = new JMenu( "Change Settings" );
		this.changeSettings.setIcon( new ImageIcon( getClass().getResource( "res/gear.png" ) ) );
		this.changeSettings.add( changePasswordPattern );
		this.changeSettings.addSeparator();
		this.changeSettings.add( changeFilePath );
		this.changeSettings.add( resetFilePath );
		
		this.file = new JMenu( "General Settings" );
		this.file.add( changeSettings );
		this.file.add( exit );
		
		this.about = new JMenu( "About & Help" );
		this.about.add( howToUse );
		this.about.add( aboutProgram );
		this.about.add( sourceCode );
		
		//menu bar
		this.menuBar = new JMenuBar();
		this.menuBar.add( file );
		this.menuBar.add( about );
	}
	
	public static void main( String[] args ) {
		PassGenClient pgc = new PassGenClient();
	}
}