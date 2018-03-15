package algorithmsTests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import elasticSearch.SearchResult;
import factfinders.AverageLog;
import factfinders.InitializeBeliefs;
import factfinders.Investment;
import factfinders.Sums;
import factfinders.Truthfinder;
import graphPlotter.CreateGraph;
import trainingGraph.TrainingResponse;

/**
 * It tests for all the algorithms for a very small graph.
 * Algorithms were solved manually to verify the assertions
 * @author Hussain
 *
 */
public class AllAlgosTests {

	public String claim = new String();
	public Set<String> sources = new LinkedHashSet<String>();
	public HashMap<String, Set<String>> training = new HashMap<String, Set<String>>();
	public TrainingResponse response = new TrainingResponse();
	public InitializeBeliefs beliefs = new InitializeBeliefs();
	public CreateGraph newEdge = new CreateGraph();
	public SearchResult result = new SearchResult();
	public Sums sums = new Sums();
	public AverageLog avg = new AverageLog();
	public Truthfinder tf = new Truthfinder();
	public Investment inv = new Investment();
	
	@Before
	public void plotDummyGraph(){
		/**
		 * TrainingWithSearch
		 */
		claim = "c1";
		sources.add("s1");
		sources.add("s3");
		training.put(claim, sources);
		
		CreateGraph create = new CreateGraph(training);
		response.graph = create.getGraph();
		response.claims = create.getClaims();
		response.sources = create.getSources();
		
		response.graph = beliefs.initialize(response.graph, response.claims);
		
		/**
		 * BulkClaimTest
		 */
		result.claim = "c2";
		result.sources.add("s1");
		result.sources.add("s2");
		response = newEdge.addEdge(response, result);
		response.graph = beliefs.initialize(response.graph, result.claim);
	}
	
	@Test
	public void sumsTest() {
		for(int i = 0; i < 2; i++) {
	 	   	sums.trustScore(response.graph, response.sources);
	        sums.beliefScore(response.graph, response.claims);
			}
		
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 0.93, 0.01);
	}
	
	@Test
	public void AvgLogTest() {
		for(int i = 0; i < 2; i++) {
	 	   	avg.trustScore(response.graph, response.sources);
	        avg.beliefScore(response.graph, response.claims);
			}
		
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 1, 0.01);
	}
	
	@Test
	public void investmnetTest() {
		inv.trustScore(response.graph, response.sources);
		for(int i = 0; i < 2; i++) {
	 	   	inv.trustScore(response.graph, response.sources);
	        inv.beliefScore(response.graph, response.claims);
			}
		
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 0.22, 0.01);
	}
	
	@Test
	public void tfTest() {
		for(int i = 0; i < 2; i++) {
	 	   	tf.trustScore(response.graph, response.sources);
	        tf.beliefScore(response.graph, response.claims);
			}
		assertEquals(response.graph.getVertex("c1").getScore(), 1, 0.01);
		assertEquals(response.graph.getVertex("c2").getScore(), 0.99, 0.01);
	}
	
}
