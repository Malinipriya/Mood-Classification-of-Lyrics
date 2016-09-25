import java.io.BufferedReader;
import java.io.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SentimentScoreExtraction
{
	private Map<String, Double> dictionary;
	public SentimentScoreExtraction(String pathToSWN) throws IOException 
	{
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();
		BufferedReader csv = null;
		try 
		{
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;
			String line;
			while ((line = csv.readLine()) != null) 
			{
				lineNumber++;
				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) 
				{
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];
					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc
					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) 
					{
						throw new IllegalArgumentException("Incorrect tabulation format in file, line: "+ lineNumber);
					}
					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2]) - Double.parseDouble(data[3]);
					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");
					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) 
					{
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"+ wordTypeMarker;
						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}
						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) 
						{
							tempDictionary.put(synTerm,new HashMap<Integer, Double>());
						}
						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,synsetScore);
					}
				}
			}
			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet()) 
			{
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();
				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) 
				{
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;
				dictionary.put(word, score);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			if (csv != null) 
			{
				csv.close();
			}
		}
	}

	public void extract(String word, String pos, PrintWriter pw) throws IOException
	{
		double score=0.0;
		if(dictionary.containsKey(word + "#" + pos))
		{
			score=dictionary.get(word + "#" + pos);
			if(score!=0.0)
			{
				pw.write(word + "#" + pos + " " +score);
				pw.write("\n");
			}
		}
	}
	
	public void extractMoodScore(String word, String pos) throws IOException
	{
		double score=0.0;
		if(dictionary.containsKey(word + "#" + pos))
		{
			score=dictionary.get(word + "#" + pos);
			if(score!=0.0)
			{
				System.out.println(word + "#" + pos + " " +score);
			}
		}
	}

	public static void main(String [] args) throws IOException 
	{
		String pathToSWN = "SentiWordNet_3.0.0.txt";
		String fileName = "pos_angry6.txt";
		SentimentScoreExtraction sentiwordnet = new SentimentScoreExtraction(pathToSWN);
		String line = null;

        try 
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            PrintWriter pw = new PrintWriter(new FileWriter("sentiwordnet_results/angry6.txt"));
            while((line = bufferedReader.readLine()) != null) 
            {
                //System.out.println(line);
                String[] content = line.split("\\s+");
                if(content.length>1)
                	sentiwordnet.extract(content[0], content[1], pw);
                else 
                	pw.write("\n");
                //System.out.println(content[0]+" "+sentiwordnet.extract(content[0], content[1]));
            }   
            bufferedReader.close();
            pw.close();
            sentiwordnet.extractMoodScore("angry", "a");
            sentiwordnet.extractMoodScore("love", "v");
            sentiwordnet.extractMoodScore("happy", "a");
            sentiwordnet.extractMoodScore("sad","a");
        }
        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");  
            System.out.println(ex);
        }
        catch(IOException ex) 
        {
            System.out.println("Error reading file '" + fileName + "'");                  
        }
	}
}