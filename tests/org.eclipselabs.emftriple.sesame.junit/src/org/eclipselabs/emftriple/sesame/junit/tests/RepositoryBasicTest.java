package org.eclipselabs.emftriple.sesame.junit.tests;

import static org.junit.Assert.assertEquals;
import info.aduna.iteration.Iterations;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.junit.model.ModelPackage;
import org.eclipselabs.emftriple.sesame.handlers.RepositoryHandler;
import org.eclipselabs.emftriple.sesame.resource.RDFResourceFactory;
import org.junit.Test;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

public class RepositoryBasicTest {

	@Test
	public void testMemStore() throws RepositoryException {
		Repository repo = new SailRepository(new MemoryStore());
		repo.initialize();
		RepositoryConnection con = repo.getConnection();
		Model model = new LinkedHashModel();
		model.add(
				new URIImpl("http://m.rdf/one"), 
				new URIImpl("http://m.rdf/name"),
				new URIImpl("http://m.rdf/john"));
		try {
			con.add(model, new URIImpl("http://m.rdf"));
		} finally {
			con.close();
		}

		RepositoryConnection con2 = repo.getConnection();
		try {
			RepositoryResult<Statement> stmts = con2.getStatements(null, null, null, true, new URIImpl("http://m.rdf"));
			Model m = Iterations.addAll(stmts, new LinkedHashModel());
			assertEquals(1, m.subjects().size());
		} finally {
			con2.close();
		}
		con.close();
	}

	@Test
	public void testSaveOneInMemStore() throws RepositoryException, IOException {
		Repository repo = new SailRepository(new MemoryStore());
		repo.initialize();

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new RepositoryHandler(repo));

		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));

		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFi");
		b.getTags().add("Fantasy");

		r.getContents().add(b);

		r.save(null);

		RepositoryConnection con = repo.getConnection();
		try {
			RepositoryResult<Statement> stmts = con.getStatements(null, null, null, true, new URIImpl("http://m.rdf"));
			Model m = Iterations.addAll(stmts, new LinkedHashModel());
			assertEquals(1, m.subjects().size());
			org.openrdf.model.Resource sbj = m.subjects().iterator().next();
			assertEquals("http://m.rdf#/", sbj.stringValue());

			Model typeModel = m.filter(sbj, RDF.TYPE, null);
			assertEquals(1, typeModel.size());
			Statement typeStmt = typeModel.iterator().next();
			assertEquals(EcoreUtil.getURI(ModelPackage.Literals.BOOK).toString(), typeStmt.getObject().stringValue());

			URIImpl titleURI = new URIImpl(EcoreUtil.getURI(ModelPackage.Literals.BOOK__TITLE).toString());
			Model titleModel = m.filter(sbj, titleURI, null);
			assertEquals(1, titleModel.size());
			Statement titleStmt1 = titleModel.iterator().next();
			assertEquals("The Book", titleStmt1.getObject().stringValue());

			URIImpl tagsURI = new URIImpl(EcoreUtil.getURI(ModelPackage.Literals.BOOK__TAGS).toString());
			Model tagsModel = m.filter(sbj, tagsURI, null);
			assertEquals(2, tagsModel.size());
			Iterator<Statement> tagsIt = tagsModel.iterator();
			Statement tagsStmt1 = tagsIt.next();
			assertEquals("SciFi", tagsStmt1.getObject().stringValue());
			Statement tagsStmt2 = tagsIt.next();
			assertEquals("Fantasy", tagsStmt2.getObject().stringValue());

		} finally {
			con.close();
		}

		repo.shutDown();
	}

}
