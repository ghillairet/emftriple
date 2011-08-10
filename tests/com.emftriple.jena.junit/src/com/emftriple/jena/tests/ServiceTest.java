package com.emftriple.jena.tests;

import static com.emftriple.query.Sparql.filter;
import static com.emftriple.query.Sparql.iri;
import static com.emftriple.query.Sparql.triple;
import static com.emftriple.query.Sparql.var;
import static com.emftriple.vocabularies.RDF.type;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import com.emftriple.jena.tdb.TDBResourceFactory;
import com.emftriple.query.Sparql;
import com.emftriple.query.result.ListResult;
import com.emftriple.util.ETripleOptions;
import com.emftriple.util.ETripleUtil;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.junit.model.Actor;
import com.junit.model.ModelPackage;
import com.junit.model.Movie;

public class ServiceTest {

	private static final EClass MOVIE = ModelPackage.eINSTANCE.getMovie();
	
	static {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
	}
	
	public static void main(String[] args) throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(
				ETripleOptions.OPTION_DATASOURCE_LOCATION, "/linkedmdb");
		
		Sparql sparql = new Sparql().select("m")
				.select("filmid")
				.where(triple(var("m"), iri(type), MOVIE))
				.orderBy("filmid")
				.offset(0)
				.limit(10);
		
		Resource resource = resourceSet.createResource(
				sparql.toURI(URI.createURI("emftriple://data.linkedmdb.org/resource/movie/")));
		resource.load(null);
		
		System.out.println(resource.getContents().size());
		
		ListResult result = (ListResult) resource.getContents().get(0);
		System.out.println(result.getResult().size());

		for (Movie movie: ETripleUtil.findAll(Movie.class, result.getResult())) {
			System.out.print(movie.getTitle());
			System.out.println("  "+movie.getId());
			
//			for (Actor actor: movie.getActors()) {
//				System.out.println("    "+actor.getName()+"  "+actor.getId()+" acts in "+actor.getActorOf());
//			}
		}
		
		Sparql s = new Sparql()
		.where(	triple(var("m"), iri(type), MOVIE), 
				Sparql.regex("?m , \"http://data.linkedmdb.org/resource/movie/11\""))
				;
		
		System.out.println(s.get());
//		
		Dataset ds = TDBFactory.createDataset("/linkedmdb");
		Model model = QueryExecutionFactory.create(
				"construct { ?s ?p ?o } where { " +
				"?s ?p ?o . " +
//				"?s a <http://data.linkedmdb.org/resource/movie/film> " +
//				"<http://data.linkedmdb.org/resource/movie/60> ?p ?o "+ //; <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://data.linkedmdb.org/resource/movie/film> . " +
				"filter (?s = <http://data.linkedmdb.org/resource/film/1100>) " +
				"} ", ds).execConstruct();		
		model.write(System.out);
		
		
	}
	
}
