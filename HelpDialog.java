import javax.swing.*;
import javax.swing.ImageIcon.*;
import java.awt.FlowLayout;
import java.awt.event.*;

/**
 * Displays basic help information about the program
 */
public class HelpDialog extends JDialog implements ActionListener {
	
	private ImageIcon howToUse;
	private JButton okButton, viablePasswordCharacters;
	
	/**
	 * Constructs the dialog
	 * @param parent The window that this dialog will be displayed relative to
	 */
	public HelpDialog( JFrame parent ) {
		super( parent, "Help", true );
		
		this.howToUse = new ImageIcon( getClass().getResource( "res/How_To_Use.jpg" ) );
		this.okButton = new JButton( "Ok" );
		this.okButton.addActionListener( this );
		
		this.viablePasswordCharacters = new JButton( "Creating your own password pattern" );
		this.viablePasswordCharacters.addActionListener( this );
		
		super.add( new JLabel( howToUse ) );
		super.add( okButton );
		super.add( viablePasswordCharacters );
		super.setLayout( new FlowLayout() );
		super.setLocationRelativeTo( parent );
		super.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		super.setSize( 600, 330 );
		super.setVisible( true );
	}
	
	public void actionPerformed( ActionEvent ae ) {
		if( ae.getSource() == okButton ) {
			dispose();
		}
		
		else if( ae.getSource() == viablePasswordCharacters ) {
			JOptionPane.showMessageDialog( this, "When you create a password pattern, the program will create passwords based on that template." +
														"\nThese templates are strings of characters. To add various elements to your password," +
														"\n-add the respective character: \nTo add a word, use an 's' or a 'w'" +
														"\n-To add a number, use an 'n' or a '#'" +
														"\n-To add a random character, use a '*'" +
														"\n-To add a underscore, use a '_'", "Creating a password pattern", JOptionPane.INFORMATION_MESSAGE );
		}
	}
}