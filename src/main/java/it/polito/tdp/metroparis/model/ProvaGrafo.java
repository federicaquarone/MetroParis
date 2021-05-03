package it.polito.tdp.metroparis.model;
import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
public class ProvaGrafo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Graph<String, DefaultEdge> grafo=  new SimpleDirectedGraph<>(DefaultEdge.class);
		
		grafo.addVertex("UNO");
		grafo.addVertex("DUE");
		grafo.addVertex("TRE");
		
		grafo.addEdge("UNO", "TRE");
		grafo.addEdge("TRE", "DUE");
		
		
		System.out.println(grafo);
	}

}