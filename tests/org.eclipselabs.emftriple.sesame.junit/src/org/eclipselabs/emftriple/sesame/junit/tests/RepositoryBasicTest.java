package org.eclipselabs.emftriple.sesame.junit.tests;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.junit.model.Book;
import org.eclipselabs.emftriple.junit.model.ModelFactory;
import org.eclipselabs.emftriple.sesame.handlers.RepositoryHandler;
import org.eclipselabs.emftriple.sesame.resource.SesameResourceFactory;
import org.junit.Test;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

public class RepositoryBasicTest {

	@Test
	public void testSaveOneInMemStore() throws RepositoryException, IOException {
		Repository repo = new SailRepository(new MemoryStore());
		repo.initialize();

		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new SesameResourceFactory());
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new RepositoryHandler(repo));

		Resource r = resourceSet.createResource(URI.createURI("http://m.rdf"));
		
		Book b = ModelFactory.eINSTANCE.createBook();
		b.setTitle("The Book");
		b.getTags().add("SciFI");
		b.getTags().add("Fantasy");
		
		r.getContents().add(b);
		
		r.save(null);
		repo.shutDown();
	}

}
