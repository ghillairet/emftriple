package org.eclipselabs.emftriple.example;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.example.model.Child;
import org.eclipselabs.emftriple.example.model.ModelFactory;
import org.eclipselabs.emftriple.example.model.ModelPackage;
import org.eclipselabs.emftriple.example.model.Parent;
import org.eclipselabs.emftriple.sesame.nat.SesameNativeURIHandlerImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		EPackage.Registry.INSTANCE.put(ModelPackage.eNS_URI, ModelPackage.eINSTANCE);

		System.out.println("Start ...");
		long startTime = System.currentTimeMillis();

		Map<String, Object> options = new HashMap<String, Object>();
		options.put(ETripleOptions.OPTION_DATASOURCE_LOCATION, "data");

		StoreOptionsRegistry.INSTANCE.put("example", options);

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new SesameNativeURIHandlerImpl());

		Resource resource = resourceSet.createResource(URI.createURI("native://example"));

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
				URI.createURI("native://example?uri=http://eclipselabs.org/emftriple/model/parent_0"));
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

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	private static final int CHILD_COUNT = 1000;
	private static final int PARENT_COUNT = 10;
}
