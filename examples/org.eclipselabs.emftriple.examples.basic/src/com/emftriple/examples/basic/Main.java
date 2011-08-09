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
import com.emftriple.sesame.nat.SesameNativeResourceFactory;
import com.emftriple.util.ETripleOptions;

public class Main {
	public static void main(String[] args) throws IOException {
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("emftriple", new SesameNativeResourceFactory());
		
		System.out.println("Start ...");
		long startTime = System.currentTimeMillis();

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "data");
		Resource resource = resourceSet.createResource(URI.createURI("emftriple://sesame"));
		
		for (int i = 0; i < PARENT_COUNT; i++)
		{
			if (i % 100 == 0)
				System.out.println();
			System.out.print(".");
			
			Parent parent = ModelFactory.eINSTANCE.createParent();
			parent.setId("parent_"+i);
			parent.setName("Parent " + i);
			
			for (int j = 0; j < CHILD_COUNT; j++)
			{
				Child child = ModelFactory.eINSTANCE.createChild();
				child.setId("child_"+i+"_"+j);
				child.setName("Child " + i + " " + j);
				parent.getChildren().add(child);
				resource.getContents().add(child);
			}
		
			resource.getContents().add(parent);
			resource.save(null);
			resource.getContents().clear();
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Time to create and store " + (PARENT_COUNT * CHILD_COUNT) + " objects: " + ((endTime - startTime) / 1000.0) + " sec");

		startTime = System.currentTimeMillis();
		
		resource = resourceSet.createResource(
				URI.createURI("emftriple://sesame?uri=http://eclipselabs.org/emftriple/model/parent_0"));
		resource.load(null);
		
		endTime = System.currentTimeMillis();
		
		System.out.println("Time to get first parent: " + (endTime - startTime) + " ms");

		Parent parent = (Parent) EcoreUtil.getObjectByType(resource.getContents(), ModelPackage.eINSTANCE.getParent());

		startTime = System.currentTimeMillis();

		System.out.println("first parent "+parent.getName()+" has "+parent.getChildren().size()+" children");
		for (Child child : parent.getChildren()) {
			child.getName();
		}

		endTime = System.currentTimeMillis();
		System.out.println("Time to walk " + CHILD_COUNT + " children of first parent: " + (endTime - startTime) + " ms");
	}

	private static final int CHILD_COUNT = 1000;
	private static final int PARENT_COUNT = 1000;
}
