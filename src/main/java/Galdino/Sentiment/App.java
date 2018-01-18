package Galdino.Sentiment;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App {

	public static List<Sentence> readFile(String filename) {

		String text ; 
		List<Sentence> list = new ArrayList<Sentence>() ;
		Charset charset = Charset.forName("US-ASCII");

		if (filename == null) {
			return list; 
		}
		else if ( filename.isEmpty()) {
			return list;
		}
		else if (filename.contains(" ")) {
			return list;
		}

		Path path = Paths.get("src/main/resource/file/" + filename);

		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 4 ) {
					int j=0, negative = 1;
					if (line.charAt(j) == 45 ) {
						j++;
						negative = -1;
					}
					if (Character.isDigit(line.charAt(j))){
						int score = Character.getNumericValue(line.charAt(j) * negative);
						if ( score >= -2 && score <= 2) {
							j++;
							if (Character.toString(line.charAt(j)) != " ") {
								j++;
								if (Character.isLetter(line.charAt(j)) ) {
									text = line.substring(j);
									Sentence value = new Sentence(score, text);
									list.add(value);
								}
								else continue;
							}
							else continue;
						}
						else continue;
					}
					else continue;
				}
				else continue;
			}
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		return list; 

	}

	public static Set<Word> allWords(List<Sentence> sentences) {

		int i,j = 0;

		Set<Word> newSet = new HashSet<Word>() ;
		Word wobject ; 

		if (sentences != null) {
			if ( !sentences.isEmpty()) {
				for(i=0; i < sentences.size() ; i ++) {
					String lowercase = sentences.get(i).text.toString().toLowerCase().replace(".", "");
					int score = sentences.get(i).score;
					String[] word = lowercase.split("\\s");
					for (j=0; j < word.length ; j++ ) {
						if(word[j].matches("[a-z]+")) {
							wobject = new Word(word[j]);
							if (newSet.contains(wobject)) {
								newSet.remove(wobject);  
								newSet.add(wobject);     
								wobject.increaseTotal(score); 
							}
							else {
								newSet.add(wobject);
								wobject.increaseTotal(score);
							}
						}
					}
				}
			}
		}
		return newSet; 

	}

	public static Map<String, Double> calculateScores(Set<Word> words) {

		Map<String,Double> mapobj = new HashMap<String, Double>();

		if ( words != null) {
			if (!words.isEmpty()) {
				Iterator<Word> word = words.iterator();
				while(word.hasNext()) {
					Word aux = word.next();
					String text = aux.getText(); 
					Double score  = aux.calculateScore();
					if(text != null) {
						mapobj.put(text, score);	
					}
					else {
						continue;
					}

				}

			}

		}
		return mapobj; 

	}

	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {

		int count;
		double average = 0;

		if (sentence != null) {
			if ( !sentence.isEmpty()) {
				if (wordScores != null) {
					if (!wordScores.isEmpty()) {
						String lowercase = sentence.toString().toLowerCase().replace(".", "");
						String[] word = lowercase.split("\\s");
						if (word.length > 1) {
							for (count=0; count < word.length ; count++ ) {
								if(word[count].matches("[a-z]+")) {
									Double wvalue = wordScores.get(word[count]);
									if (wvalue == null) {
										wvalue = (double) 0;
									}
									average = average + wvalue;
								}
								else {
									continue;
								}
							}
							average = average / count ;
						}
					}
				}
			}
		}

		return average; 

	}

	public static void learning(String newScore, String Text, String filename) {

		if (newScore != null) {
			if (!newScore.isEmpty()) {
				int score = Integer.parseInt(newScore);
				if (score > 2) {
					score = 2;
				}
				else if (score < -2) {
					score = -2;
				}
				try(FileWriter fw = new FileWriter("src/main/resource/file/" + filename, true);
						BufferedWriter bw = new BufferedWriter(fw);
						PrintWriter out = new PrintWriter(bw)){
					out.println(score + " " + Text);
				}  
				catch( IOException e ){
					System.out.println("Not possible to open the file");
				}
			}

		}
	}


	public static void main(String[] args) {

		String filename = "reviews.txt";

		System.out.print("Please enter a sentence: ");
		Scanner in = new Scanner(System.in);
		String sentence = in.nextLine();

		List<Sentence> sentences = readFile(filename);
		
		Set<Word> words = App.allWords(sentences);
		Map<String, Double> wordScores = App.calculateScores(words);
		double score = App.calculateSentenceScore(wordScores, sentence);
		String Sentiment;
		
		if ( score > 0) {
			if (score > 1) {
				Sentiment = "Very Happy";
			}
			Sentiment = "Happy";
			
		}
		else {
			if ( score < -1 ) {
				Sentiment = "Very Sad";
			}
			Sentiment = "In the Middle";
		}
		
		
		System.out.println("The sentiment score is " + score + " --> " + Sentiment);

		System.out.println("Wrong value? (Y/N): ");
		String correct = in.nextLine();
		if(correct.equalsIgnoreCase("Y")) {
			System.out.println("Please enter the correct one: ");
			String newvalue = in.nextLine();
			if(!newvalue.equalsIgnoreCase("q")) {
				learning(newvalue,sentence,filename );	
			}		
		}

		in.close();



	}
}
