import javax.swing.*;
import javax.swing.ImageIcon.*;
import java.awt.FlowLayout;
import java.awt.event.*;

public class HelpDialog extends JDialog implements ActionListener {
	
	private ImageIcon howToUse;
	private JButton okButton;
	
	public HelpDialog( JFrame parent ) {
		super( parent, "Help", true );
		
		this.howToUse = new ImageIcon( getClass().getResource( "res/How_To_Use.jpg" ) );
		this.okButton = new JButton( "Ok" );
		this.okButton.addActionListener( this );
		
		super.add( new JLabel( howToUse ) );
		super.add( okButton );
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
	}
}