package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	Graph<Fermata,DefaultEdge> grafo;
	Map<Fermata,Fermata> predecessore;
	
	public void creaGrafo() {
		grafo= new SimpleGraph<>(DefaultEdge.class);
		
		MetroDAO dao= new MetroDAO();
		List<Fermata> fermate= dao.getAllFermate();
		
		
		/*for(Fermata f: fermate) {
			this.grafo.addVertex(f); //aggiunge al grafo tutte le fermate
		}*/
		
		Graphs.addAllVertices(this.grafo, fermate); //FA LA STESSA COSA DI SOPRA
		
		//Aggiungiamo gli archi
		/*for(Fermata f1: this.grafo.vertexSet()) {
			for(Fermata f2: this.grafo.vertexSet()) {
				if(!f1.equals(f2) && dao.fermateCollegate(f1,f2)) {
					this.grafo.addEdge(f1, f2);
				}
			}
		}*/
		
		List<Connessione> connessioni= dao.getAllConnessioni(fermate);
		for(Connessione c: connessioni) {
			this.grafo.addEdge(c.getStazP(), c.getStazA());
			
		}
		
		System.out.println(this.grafo);
	}
	
	
	public List <Fermata> fermateRaggiungibili(Fermata partenza){
		BreadthFirstIterator<Fermata, DefaultEdge> bfv= new BreadthFirstIterator<>(this.grafo, partenza);
		
		this.predecessore= new HashMap<>();
		this.predecessore.put(partenza,  null);
		bfv.addTraversalListener(new TraversalListener<Fermata,DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				DefaultEdge arco= e.getEdge();
				Fermata a= grafo.getEdgeSource(arco);
				Fermata b= grafo.getEdgeTarget(arco);
				//ho scoperto a arrivando da b
				if(predecessore.containsKey(b)) {
					predecessore.put(a, b); //a viene scoperto da b
					//System.out.println(a+" scoperto da "+b);
				}else {
					//di sicuro conoscevo a e quindi ho scoperto b
					predecessore.put(b,a);
					//System.out.println(b+" scoperto da "+a);
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Fermata> e) {
				//Fermata nuova= e.getVertex();
				//Fermata precedente= vertice adiacete a nuova che sia già aggiunto(cioè già presente nella mappa);
				
				
				//this.predecessore.put(nuova, precedente);
				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Fermata> e) {
				// TODO Auto-generated method stub
				
			}
			
			
		
	});
    List<Fermata> result = new ArrayList<>() ;
		
		while(bfv.hasNext()) {
			Fermata f = bfv.next() ;
			result.add(f) ;
		}
		
		return result ;
	
	}


	public Fermata trovaFermata(String nome) {
		for(Fermata f: this.grafo.vertexSet()) {
			if(f.getNome().equals(nome)) {
				return f ;
			}
		}
		return null ;
	}
	
	public List<Fermata> trovaCammino(Fermata partenza, Fermata arrivo) {
		fermateRaggiungibili(partenza) ;
		
		List<Fermata> result = new LinkedList<>() ;
		result.add(arrivo) ;
		Fermata f = arrivo ;
		while(predecessore.get(f)!=null) {
			f = predecessore.get(f) ;
			result.add(0, f) ;
		}
		
		return result ;
	}
	
	
	// Implementazione di 'trovaCammino' che NON usa il traversal listener ma sfrutta
	// il metodo getParent presente in BreadthFirstIterator
	public List<Fermata> trovaCammino2(Fermata partenza, Fermata arrivo) {
		BreadthFirstIterator<Fermata, DefaultEdge> bfv = 
				new BreadthFirstIterator<Fermata, DefaultEdge>(this.grafo, partenza) ;
		
		// fai lavorare l'iteratore per trovare tutti i vertici
		while(bfv.hasNext())
			bfv.next() ; // non mi serve il valore
		
		List<Fermata> result = new LinkedList<>() ;
		result.add(arrivo);
		Fermata f = arrivo ;
		while(f!=null) {
			result.add(f) ;
			f = bfv.getParent(f) ;
		}
		
		return result ;
		
	}
}
