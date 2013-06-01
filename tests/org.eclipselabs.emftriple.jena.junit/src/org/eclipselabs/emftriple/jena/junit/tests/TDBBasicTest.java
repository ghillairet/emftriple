package org.eclipselabs.emftriple.jena.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipselabs.emftriple.tdb.handlers.TDBHandler;
import org.eclipselabs.emftriple.jena.resource.RDFResourceFactory;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.vocabularies.RDF;
import org.junit.Test;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TDBBasicTest {

	@Test
	public void testSaveOne() throws IOException {
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
		
		assertTrue(dataset.containsNamedModel(namedGraphURI));
		
		Model model = dataset.getNamedModel(namedGraphURI);
		assertNotNull(model);
		
		assertEquals(1, model.listSubjects().toList().size());
		
		com.hp.hpl.jena.rdf.model.Resource res = model.listSubjects().next();
		assertEquals("http://m.rdf#/", res.getURI());
		assertEquals(4, res.listProperties().toList().size());

		com.hp.hpl.jena.rdf.model.Resource type = res.getProperty(model.getProperty(RDF.type)).getObject().asResource();
		assertEquals(EcoreUtil.getURI(ModelPackage.Literals.BOOK).toString(), type.getURI());
		
		Literal title = res.getProperty(model.getProperty(
				EcoreUtil.getURI(ModelPackage.Literals.BOOK__TITLE).toString())).getLiteral();
		assertEquals("The Book", title.getLexicalForm());
		
		List<Statement> tags = res.listProperties(model.getProperty(
				EcoreUtil.getURI(ModelPackage.Literals.BOOK__TAGS).toString())).toList();
		
		assertEquals(2, tags.size());
		
		Literal tag1 = tags.get(0).getLiteral();
		assertEquals("SciFi", tag1.getLexicalForm());
		Literal tag2 = tags.get(1).getLiteral();
		assertEquals("Fantasy", tag2.getLexicalForm());
	}

	protected Model createModel() {
		Model model = com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();
		model.add(model.getResource("http://m.rdf#/"), 
				com.hp.hpl.jena.vocabulary.RDF.type, 
				model.getResource("http://www.eclipselabs.org/emf/junit#//Book"));
		model.add(model.getResource("http://m.rdf#/"), 
				model.getProperty("http://www.eclipselabs.org/emf/junit#//Book/title"), 
				model.createLiteral("The Book"));
		return model;
	}

	@Test
	public void testLoadOne() throws Exception {
		Dataset dataset = TDBFactory.createDataset();
		String namedGraphURI = "http://m.rdf";
		dataset.addNamedModel(namedGraphURI, createModel());
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new TDBHandler(dataset));

		Resource r = resourceSet.createResource(URI.createURI(namedGraphURI));
		r.load(null);
		
		assertEquals(1, r.getContents().size());
		assertEquals(ModelPackage.Literals.BOOK, r.getContents().get(0).eClass());
		
		Book root = (Book) r.getContents().get(0);
		assertEquals("The Book", root.getTitle());
	}
	
}
