package experiment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import demo.CreateGraph;
import elasticSearch.Result;
import elasticSearch.SearchWiki;
import factFinders.InitializeBeliefs;

/**
 * train the graph with true claims with belief score as 1.0
 * @author Hussain
 *
 */
public class Training {
	public TrainingResponse train() throws IOException{
		Result result = new Result();
		HashMap<String, ArrayList<String>> graphBuilder = new HashMap<String, ArrayList<String>>();
		TrainingResponse response = new TrainingResponse();
		InitializeBeliefs beliefs = new InitializeBeliefs();
   
		SearchWiki search = new SearchWiki();
		
		String inputFile = "./src/data/true_claims.tsv";
		BufferedReader TSVFile;
		TSVFile = new BufferedReader(new FileReader(inputFile));
		String dataRow = TSVFile.readLine();
   
		try {
			while (dataRow != null){
				result = search.query(dataRow);
				if(result != null) {
					graphBuilder.put(result.claim, result.sources);
					System.out.println(result.claim);
				}
				dataRow = TSVFile.readLine(); 
			}
	        	TSVFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		CreateGraph create = new CreateGraph(graphBuilder);
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();
		
		response.graph = beliefs.initialize(response.graph, response.claims, "training");
		
		return response;
	}

}