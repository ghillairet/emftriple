package com.emftriple.jena.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;

import com.emftriple.jena.file.FileResourceFactory;
import com.emftriple.jena.file.FileResourceImpl;
import com.emftriple.transform.SparqlQueries;
import com.emftriple.util.ETripleOptions;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.junit.model.BookBNode;
import com.junit.model.ModelPackage;
import com.junit.model.PersonBNode;

public class BlankNodeTest {

	Map<String, Object> options = new HashMap<String, Object>();
	
	@Before
	public void tearUp() {
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new FileResourceFactory());
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "bnode.ttl");
		options.put(FileResourceImpl.OPTION_RDF_FORMAT, "TTL");
	}
	
//	@Test
	public void testBlankNodeQuery() {
		Model model = ModelFactory.createDefaultModel();
		model.getReader("TTL").read(model, "file:bnode.ttl");
		
		String query = SparqlQueries.selectBlankNodeObject( 
				"http://www.eclipselabs.org/emf/junit/person/f68b5b7c-1c67-455b-860c-6fd000348434", 
				ModelPackage.eINSTANCE.getPerson_Books(), null);
		
		System.out.println(query);
		
		ResultSet rs = QueryExecutionFactory.create(query, model).execSelect();
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
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().putAll(options);
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://bnode"));
		
		resource.delete(null);
	}
	
	@Test
	public void testLoadResource() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://bnode"));
		
		assertNotNull(resource);
		resource.load(options);
		System.out.println(resource);

		assertFalse(resource.getContents().isEmpty());
		System.out.println(resource.getContents().size());
		
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
	
	@Test
	public void testLoadTreeOfBlankNodes() throws IOException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "bnode_tree.ttl");
		resourceSet.getLoadOptions().put(FileResourceImpl.OPTION_RDF_FORMAT, "TTL");
		
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://bnode_tree"));
		resource.load(null);
		
		System.out.println(resource.getContents());
	}
}
