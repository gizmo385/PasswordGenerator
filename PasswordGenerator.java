import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import javax.swing.JOptionPane;

/**
 * Generates passwords based on a pattern, a seed, and a set of features
 * @author gizmo385
 */
public class PasswordGenerator {
	
	private ArrayList<String> wordList = new ArrayList<String>();
	private File wordListFile;
	
	/**
	 * Loads the default word list (located inside the .jar archive)
	 */
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
	
	/**
	 * Used when the wordlist is specified by the user
	 * @param filename The path to the wordlist specified by the user
	 */
	public PasswordGenerator( String filename ) throws FileNotFoundException {
		this.wordListFile = new File( filename );
		Scanner fileScan = new Scanner( this.wordListFile );
			
		while( fileScan.hasNext() )
			this.wordList.add( fileScan.next() );
				
		fileScan.close();
	}
	
	/**
	 * Generates a password based on several criteria
	 * @param seedString The seed used to generate the password
	 * @param pattern The template that the password is generated based on
	 * @param characterSwapFrequency The percentage (out of 100) that a character in the generated password will be swapped for another, random character (if enabled)
	 * @param capitalizationFrequency The percentage (out of 100) that a character in the generated password will have its capitalization flipped (if enabled)
	 * @param lengthLimit The length limit on the password (if enabled)
	 * @param swappingEnabled Determines whether or not the generator will randomly swap characters
	 * @param capitalizationEnabled Determines whether or not the generator will randomly flip capitalization on characters
	 * @param lengthLimitEnabled Determines whether the length limit on the password is enabled
	 */
	public String generate( String seedString, String pattern, int characterSwapFrequency, int capitalizationFrequency, int lengthLimit, 
							boolean swappingEnabled, boolean capitalizationEnabled, boolean lengthLimitEnabled ) {
		Random key;
		
		//parse seed
		try {
			key = new Random( Math.abs( Long.parseLong( seedString ) ) );
		}
		catch( NumberFormatException nfe ) {
			key = new Random( Math.abs( seedString.hashCode() ) );
		}
		
		StringBuilder temp = new StringBuilder();
		
		//Iterate through pattern and add appropriate password elements
		for( char c : pattern.toLowerCase().toCharArray() ) {
			if( c == 'w' || c == 's' )
				temp.append( this.wordList.get( key.nextInt( this.wordList.size() ) ) );
			else if( c == 'n' || c == '#' )
				temp.append( key.nextInt( 100 ) );
			else if( c == '_' )
				temp.append( "_" );
			else if( c == '*' )
				temp.append( (char)( key.nextInt( 95 ) + 33 ) ); //random char with ASCII value between 33 and 127 (printable characters)
		}
		
		//Check optional password settings
		if( swappingEnabled ) {
			temp = new StringBuilder( swapRandomCharacters( temp.toString(), characterSwapFrequency ) );
		}
		
		if( capitalizationEnabled ) {
			temp = new StringBuilder( randomCapitalization( temp.toString(), capitalizationFrequency ) );
		}
		
		if( lengthLimitEnabled && temp.length() > lengthLimit && lengthLimit != 0 ) {
			return temp.substring( 0, lengthLimit );
		}
		
		return temp.toString();
	}
	
	/**
	 * Flip the capitalization of random characters based on a frequency
	 * @param pass The password thus far
	 * @param frequency The percentage of characters that will be swapped on average
	 */
	private String randomCapitalization( String pass, int frequency ) {
		char[] characters = pass.toCharArray();
		Random key = new Random( pass.hashCode() );
		
		for( int i = 0; i < characters.length; i++ ) {
			if( key.nextInt( 100 ) < frequency && frequency != 0 ) {
				
				if( Character.isUpperCase( characters[i] ) ) {
					characters[i] = Character.toLowerCase( characters[i] );
				}
				else if( Character.isLowerCase( characters[i] ) ) {
					characters[i] = Character.toUpperCase( characters[i] );
				}
			}
		}
		return new String( characters );
	}
	
	/**
	 * Replaces and standard character with a random character
	 * @param pass The password thus far
	 * @param frequency The percentage of characters that will be swapped on average
	 */
	private String swapRandomCharacters( String pass, int frequency ) {
		char[] characters = pass.toCharArray();
		Random key = new Random( pass.hashCode() );
		
		for( int i = 0; i < characters.length; i++ ) {
			if( key.nextInt( 100 ) < frequency && frequency != 0 ) {
				characters[i] = (char)( key.nextInt( 95 ) + 33 );
			}
		}
		return new String( characters );
	}
}