package org.eclipselabs.emftriple.example;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipselabs.emftriple.example.model.Child;
import org.eclipselabs.emftriple.example.model.ModelFactory;
import org.eclipselabs.emftriple.example.model.ModelPackage;
import org.eclipselabs.emftriple.example.model.Parent;
import org.eclipselabs.emftriple.sesame.handlers.RepositoryHandler;
import org.eclipselabs.emftriple.sesame.resource.RDFResourceFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

public class Main {

	public static void main(String[] args) throws IOException, RepositoryException {
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());

		System.out.println("Start ...");
		long startTime = System.currentTimeMillis();

		Repository repo = new SailRepository(new MemoryStore());
		repo.initialize();

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new RepositoryHandler(repo));
		
		Resource resource = resourceSet.createResource(URI.createURI("http://www.example.org/myResource"));

		for (int i = 0; i < PARENT_COUNT; i++)
		{
			if (i % 100 == 0)
				System.out.println();
			System.out.print(".");

			Parent parent = ModelFactory.eINSTANCE.createParent();
			parent.setId("parent_"+i);
			parent.setName("Parent " + i);
			resource.getContents().add(parent);

			for (int j = 0; j < CHILD_COUNT; j++)
			{
				Child child = ModelFactory.eINSTANCE.createChild();
				child.setId("child_"+i+"_"+j);
				child.setName("Child " + i + " " + j);
				parent.getChildren().add(child);
			}

			resource.save(null);
			resource.getContents().clear();
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Time to create and store " + (PARENT_COUNT * CHILD_COUNT) + " objects: " + ((endTime - startTime) / 1000.0) + " sec");

		startTime = System.currentTimeMillis();

		resource = resourceSet.createResource(
				URI.createURI("http://www.example.org/myResource"));
		resource.load(null);

		endTime = System.currentTimeMillis();

		System.out.println("Time to load: " + (PARENT_COUNT * CHILD_COUNT) + " objects " + (endTime - startTime) + " ms");

		Parent parent = (Parent) resource.getContents().get(0);

		startTime = System.currentTimeMillis();

		System.out.println("first parent "+parent.getName()+" has "+parent.getChildren().size()+" children");
		for (Child child : parent.getChildren()) {
			child.getName();
		}

		endTime = System.currentTimeMillis();
		System.out.println("Time to walk " + CHILD_COUNT + " children of first parent: " + (endTime - startTime) + " ms");
	}
	
	private static final int CHILD_COUNT = 1000;
	private static final int PARENT_COUNT = 5;
}
