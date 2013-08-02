#PassGen
- - -

PassGen is a password generation utility programmed in Java. It comes with many features allowing users to drastically customize the passwords generated. Included with the utility is a wordlist containing over 14,500 words.

- - -

##Feature Set

Features available in this utility include:
* Customizable password generation patterns. This means you can decide how many numbers, words, and symbols are included in your password.
* Seed-based generation. This allows the user to store their seed in a secure place, assured that re-entering it into PassGen (with the same generation settings), will return the same password
* Advanced password generation settings. These are all optional and fully customizable. They include:
	+ Randomised character swapping: Based on a frequency percentage entered by the user, each character in your password has a chance to be swapped out for another random character (ASCII 33-127)
	+ Randomised capitalization: Also based on a frequency percentage, each character in your password has a chance to have its capitalization flipped (lower -> upper || upper -> lower)
	+ A character limit for password generation: The complexity of the words in the word list means that many of the generated passwords can be quite lengthy. This allows the user to trim it down to a maximum.
* Customisable word lists: Should you find the wordlist included with the utility to not be up to your standards, the utility fully supports the ability of the user to use a word list of their choice.

- - -

##Compiling on your own machine:

Run the following commands:

	`javac *.java`
	`javadoc -d /doc -quiet *.java`
	`jar -cfm PassGen.jar manifest.txt res *.class`

- - -
