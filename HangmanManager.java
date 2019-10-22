// Name: Saasha Mor
// Date: 04/2/2018
// Section: AR
// TA: Alissa Adornato
// Class HangmanManager allows you to manage a game of hangman.

import java.io.*;
import java.util.*;

public class HangmanManager {
	private SortedSet<Character> guessesMade;
	private Set<String> currentDictionary;
	private String currentPattern;
	private int guessesLeft;
	
	// pre:  the length of the word should be atleast one and the maximum guesses wrong allowed
	//		 should be greater than zero otherwise throws IllegalArgumentException
	// post: Adds all the words of specified length from given dictionary to local dictionary
	// parameters needed:
	//			dictionary: a dictionary of words
	//			length: target word length
	//			max: maximum number of wrong guesses a player is allowed
	public HangmanManager(Collection<String> dictionary, int length, int max) {
		if(length < 1 || max < 0) {
			throw new IllegalArgumentException();
		}
		currentDictionary = new TreeSet<String>();
		guessesMade = new TreeSet<Character>();
		guessesLeft = max;
		for(String word: dictionary) {
			if(word.length() == length) {
				currentDictionary.add(word);
			}
		}
		currentPattern = "-";
		for(int i = 1; i < length; i++) {
			currentPattern += " -";
		}
	}
	
	// post: returns the current set of words being considered as the dictionary
	public Set<String> words() {
		return currentDictionary;
	}
	
	// post: returns the number of guesses the player has left
	public int guessesLeft() {
		return guessesLeft;
	}
	
	// post: returns a sorted list of all the guesses the player has made
	public SortedSet<Character> guesses() {
		return guessesMade;
	}
	
	// pre:  the current dictionary should not be empty otherwise throws IllegalStateException
	// post: returns the word that has to be guessed with dashes for letters that
	// 		 haven't been guessed.
	public String pattern() {
		if(currentDictionary.isEmpty()) {
			throw  new IllegalStateException("Empty dictonary");
		}
		return currentPattern;
	}
	
	
	// pre:  Guessed letter shouldn't have been guessed before and the current dictionary
	//		 should not be empty otherwise throws IlegalArgumentException
	//		 guesses left should not be less than one and current dictionary should not be
	//		 empty otherwise throws IllegalStateException
	// post: Decides what set of words to use as the current dictionary and records
	//		 the guess made by the player. It also decreases the amount of guesses
	//		 the player has left accordingly
	// 		 returns the number of occurences of the guessed letter.
	// parameters needed:
	//			guess: the character guessed by the user
	public int record(char guess) {
		if(guessesMade.contains(guess) && !currentDictionary.isEmpty()) {
			throw new IllegalArgumentException();
			
		} else if(guessesLeft() < 1 || currentDictionary.isEmpty()) {
			throw new IllegalStateException();
		}
		guessesMade.add(guess);
		Map<String, Set<String>> wordPatternMap = makeMap(guess);
		String newKey = updateDictionary(wordPatternMap);
		return findOccurences(newKey, guess);
	}
	
	// post: determines which set of words of a particular pattern
	// 		 in the map has the maximum number of elements and returns its key
	// parameters needed:
	//			wordPatternMap: the map with the words that correspond to a pettern
	private String updateDictionary(Map<String, Set<String>> wordPatternMap) {
		int max = 0;
		String keyOfMax = "";
		Set<String> currentSet;
		for(String currentKey: wordPatternMap.keySet()){
			currentSet = wordPatternMap.get(currentKey);
			if(currentSet.size() > max) {
				max = currentSet.size();
				keyOfMax = currentKey;
			}
		}
		currentDictionary = wordPatternMap.get(keyOfMax);
		return keyOfMax;
	}
	
	
	// post: Makes and returns map with key representing the
	// 		 pattern of the words using the guessed  letter and
	//		 the set of words that correspond to that pattern
	// parameters needed:
	//		guess: the letter the player guessed
	private Map<String, Set<String>> makeMap(char guess) {
		Map<String, Set<String>> currentMap = new TreeMap<String, Set<String>>();
		String key;
		Set<String> theWords;
		for(String word: currentDictionary) {
			key = returnKey(word, guess);
			if (!currentMap.containsKey(key)) {
				theWords = new TreeSet<String>();
			} else {
				theWords = currentMap.get(key);
			}
			theWords.add(word);
			currentMap.put(key, theWords);
		}
		return currentMap;
	}
	
	// post: returns the occurences of the guessed letter from the chosen key,
	// 		 adjusts the number of guesses left and makes a new pattern
	//		 based on the previous guesses and the current one
	// Parameters needed:
	//		key: the pattern of the set of words chosen to be the current dictionary
	//		guess: the letter guessed by the player
	private int findOccurences(String key, char guess) {
		String newKey = "";
		int occurences = 0;
		for(int i = 0; i < key.length(); i++) {
			if(key.charAt(i) == guess) {
				newKey += guess;
				occurences++;
			} else {
				newKey += currentPattern.charAt(i);
			}
		}
		currentPattern = newKey;
		if(occurences == 0) {
			guessesLeft--;
		}
		return occurences;
	}
	
	// post: returns the pattern form of the given word with dashes
	// 		 for every letter that isn't the guessed letter
	// parameters needed:
	//		word: the word that needs to be converted to the pattern
	//		guess: the letter guessed by the player
	private String returnKey(String word, char guess) {
		String key = "";
		for(int i = 0; i < word.length(); i++) {
			if(i != 0) {
				key += " ";
			}
			if(word.charAt(i) == guess) {
				key += guess;
			} else {
				key += '-';
			}
		}
		return key;
	}
}
