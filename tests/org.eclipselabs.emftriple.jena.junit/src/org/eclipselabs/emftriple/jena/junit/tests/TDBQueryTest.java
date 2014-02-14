package org.eclipselabs.emftriple.jena.junit.tests;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.jena.resource.RDFResourceFactory;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.tdb.handlers.TDBHandler;
import org.junit.Test;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TDBQueryTest {

	@Test
	public void testSelectQuery() throws IOException {
		Dataset dataset = TDBFactory.createDataset();
		String namedGraphURI = "http://m.rdf";

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new TDBHandler(dataset));

		Resource r = resourceSet.createResource(URI.createURI(namedGraphURI));
		
		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFi");
		b.getTags().add("Fantasy");
		
		r.getContents().add(b);
		r.save(null);
		r.unload();

//		Sparql q = new Sparql()
//				.select("q")
//				.where(Sparql.triple(Sparql.var("q"), 
//						Sparql.iri(RDF.type), 
//						Sparql.iri("http://www.eclipselabs.org/emf/junit#//Book")));
//
//		URI qq= q.toURI(URI.createURI(namedGraphURI));
//		System.out.println(qq);
//		Resource r2 = resourceSet.createResource(qq);
//		assertTrue(r2.getContents().isEmpty());
//		
//		r2.load(null);
	}

}
