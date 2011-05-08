package com.emftriple.examples.basic;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.examples.basic.model.Child;
import com.emftriple.examples.basic.model.ModelFactory;
import com.emftriple.examples.basic.model.ModelPackage;
import com.emftriple.examples.basic.model.Parent;
import com.emftriple.jena.tdb.TDBResourceFactory;
import com.emftriple.util.ETripleOptions;

public class Main {
	public static void main(String[] args) throws IOException {
//		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SailResourceFactory());
//		Sail sail = new GraphSail(new Neo4jGraph("/Users/guillaume/tmp/neo/model"));
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new TDBResourceFactory());
		
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < PARENT_COUNT; i++)
		{
			if (i % 100 == 0)
				System.out.println();

			System.out.print(".");
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "data");
			Resource resource = resourceSet.createResource(URI.createURI("emftriple://neo_test?graph=http://test"));
			
			Parent parent = ModelFactory.eINSTANCE.createParent();
			parent.setId("parent_"+i);
			parent.setName("Parent " + i);

			for (int j = 0; j < CHILD_COUNT; j++)
			{
//				if (j % 100 == 0)
//					System.out.println();
//				System.out.print("°");
				Child child = ModelFactory.eINSTANCE.createChild();
				child.setId("child_"+i+"_"+j);
				child.setName("Child " + i + " " + j);
				parent.getChildren().add(child);
				resource.getContents().add(child);
			}
		
			resource.getContents().add(parent);
			resource.save(null);
		}

		System.out.println();
		long endTime = System.currentTimeMillis();
		System.out.println("Time to create " + (PARENT_COUNT * CHILD_COUNT) + " objects: " + ((endTime - startTime) / 1000.0) + " sec");

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "data");
		
		startTime = System.currentTimeMillis();
		Resource resource = resourceSet.getResource(URI.createURI("emftriple://neo_test?graph=http://test"), true);
		endTime = System.currentTimeMillis();
		System.out.println("Time to get first parent: " + (endTime - startTime) + " ms");

		Parent parent = (Parent) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getParent());

		startTime = System.currentTimeMillis();

		for (Child child : parent.getChildren())
			child.getName();

		endTime = System.currentTimeMillis();
		System.out.println("Time to walk " + CHILD_COUNT + " children of first parent: " + (endTime - startTime) + " ms");
	}

	private static final int CHILD_COUNT = 1000;
	private static final int PARENT_COUNT = 100;
}
