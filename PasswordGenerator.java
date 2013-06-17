import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.swing.JOptionPane;

public class PasswordGenerator {
	
	private ArrayList<String> wordList = new ArrayList<String>();
	private File wordListFile;
	
	public PasswordGenerator() {
	
		try {
			BufferedReader reader = new BufferedReader( new InputStreamReader( getClass().getClassLoader().getResourceAsStream("res/wordlist.txt") ) );
			
			while( reader.ready() )
				this.wordList.add( reader.readLine() );
				
			reader.close();
		}
		catch( IOException ioe ) {
			JOptionPane.showMessageDialog( null, "Error loading resources!", "Error!", JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public PasswordGenerator( String filename ) throws FileNotFoundException {
		this.wordListFile = new File( filename );
		Scanner fileScan = new Scanner( this.wordListFile );
			
		while( fileScan.hasNext() )
			this.wordList.add( fileScan.next() );
				
		fileScan.close();
	}
	
	public String generate( String seedString, String pattern ) {
		
		Random key;
		try {
			key = new Random( Math.abs( Long.parseLong( seedString ) ) );
		}
		catch( NumberFormatException nfe ) {
			key = new Random( Math.abs( seedString.hashCode() ) );
		}
		
		StringBuilder temp = new StringBuilder();
		
		for( char c : pattern.toLowerCase().toCharArray() ) {
			if( c == 'w' || c == 's' )
				temp.append( this.wordList.get( key.nextInt( this.wordList.size() ) ) );
			else if( c == 'n' || c == '#' )
				temp.append( key.nextInt( 100 ) );
			else if( c == '_' )
				temp.append( "_" );
			else if( c == '*' )
				temp.append( (char)( key.nextInt( 96 ) + 32 ) ); //random char with ASCII value between 32 and 127 (printable characters)
		}
		
		return temp.toString();
	}
}