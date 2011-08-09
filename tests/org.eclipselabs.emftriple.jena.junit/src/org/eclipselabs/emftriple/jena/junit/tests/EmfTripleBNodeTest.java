package org.eclipselabs.emftriple.jena.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipselabs.emftriple.internal.SparqlQueries;
import org.eclipselabs.emftriple.jena.junit.model.BookBNode;
import org.eclipselabs.emftriple.jena.junit.model.ModelPackage;
import org.eclipselabs.emftriple.jena.junit.model.PersonBNode;
import org.eclipselabs.emftriple.jena.junit.support.TestSupport;
import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public abstract class EmfTripleBNodeTest {

	TestSupport support;
	
	public EmfTripleBNodeTest(TestSupport support) {
		this.support = support;
		this.support.init();
	}
	
//	@Test
	public void testBlankNodeQuery() {
		Model model = ModelFactory.createDefaultModel();
		model.getReader("TTL").read(model, "file:bnode.ttl");
		
		String query = SparqlQueries.selectBlankNodeObject( 
				"http://www.eclipselabs.org/emf/junit/person/f68b5b7c-1c67-455b-860c-6fd000348434", 
				ModelPackage.eINSTANCE.getPerson_Books(), null);
		
		System.out.println(query);
		
//		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();
//		rs.getResourceModel().write(System.out);
		
//		ResultSetFormatter.asRDF(model, rs).getModel().write(System.out);
		
//		for (;rs.hasNext();) {
//			QuerySolution sol = rs.next();
//			for (;sol.varNames().hasNext();) {
//				com.hp.hpl.jena.rdf.model.Resource var = sol.getResource("bnode_type");
//				System.out.println(var);
////				System.out.println(var);
////				System.out.println("    "+sol.get(var));
//			}
//		}
	}
	
//	@Test
	public void testDelete() throws IOException {
		Resource resource = support.createResource(null);
		
		resource.delete(null);
		
		assertTrue(support.dataStoreIsEmpty());
	}
	
	@Test
	public void testLoadResource() throws IOException {
		
		Resource resource = support.createResource(null);
		
		assertNotNull(resource);
		resource.load(null);

		assertFalse(resource.getContents().isEmpty());
		
		for (EObject o: resource.getContents())
			System.out.println(o);
		
		assertTrue(resource.getContents().size() == 1);
		
		Object obj = EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getPersonBNode());
		
		assertTrue(obj instanceof PersonBNode);
		assertFalse( ((PersonBNode)obj).getBooks().isEmpty() );
		assertEquals( ((PersonBNode)obj).getBooks().size(), 2 );
		
		System.out.println(((PersonBNode) obj).getName());
		
		for (BookBNode b: ((PersonBNode) obj).getBooks()) {
			System.out.println(b.getTitle());
		}
		
		for (EObject o: resource.getContents())
			System.out.println(o);
	}
	
//	@Test
	public void testLoadTreeOfBlankNodes() throws IOException {
		Resource resource = support.createResource(null);
		resource.load(null);
		
		System.out.println(resource.getContents());
	}
}
