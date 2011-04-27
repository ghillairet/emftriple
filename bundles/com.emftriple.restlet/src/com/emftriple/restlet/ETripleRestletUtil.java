package com.emftriple.restlet;

import javax.persistence.EntityManager;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.restlet.data.MediaType;
import org.restlet.ext.rdf.RdfRepresentation;

import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.RDFGraph;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.resource.RDFResourceImpl;
import com.emftriple.impl.EObjectEntityManager;
import com.emftriple.transform.IPutObject;
import com.emftriple.transform.impl.PutObjectImpl;

/**
 * 
 * @author ghillairet
 * @since 0.1.0
 */
public class ETripleRestletUtil {
	
	public static RdfRepresentation getRepresentation(Resource resource, EntityManager em, MediaType mediaType) {
		final IPutObject put = new PutObjectImpl(((EObjectEntityManager)em).getMapping(), 
				((EObjectEntityManager)em).getDelegate());
		
		final RDFGraph aGraph = RDFFactory.eINSTANCE.createDocumentGraph();
		final Resource res = new RDFResourceImpl() {			
			@Override
			public Object getDelegate() { return null; }
			
			@Override
			public void addDelegate(Triple obj) {}
		};
		res.getContents().add(aGraph);
		
		for (EObject obj: resource.getContents())
			put.put(obj, aGraph);
		
		return new RdfRepresentation(RDFGraph2RestletGraph.toGraph(aGraph), mediaType);
	}
	
	public static RdfRepresentation getRepresentation(Iterable<? extends EObject> objects, EntityManager em, MediaType mediaType) {
		final IPutObject put = new PutObjectImpl(((EObjectEntityManager)em).getMapping(), 
				((EObjectEntityManager)em).getDelegate());
		
		final RDFGraph aGraph = RDFFactory.eINSTANCE.createDocumentGraph();
		final Resource res = new RDFResourceImpl() {			
			@Override
			public Object getDelegate() { return null; }
			
			@Override
			public void addDelegate(Triple obj) {}
		};
		res.getContents().add(aGraph);
		
		for (EObject o: objects)
			put.put(o, aGraph);
		
		return new RdfRepresentation(RDFGraph2RestletGraph.toGraph(aGraph), mediaType);
	}
	
	public static RdfRepresentation getRepresentation(EObject object, EntityManager em, MediaType mediaType) {
		final IPutObject put = new PutObjectImpl(((EObjectEntityManager)em).getMapping(), 
				((EObjectEntityManager)em).getDelegate());
		
		final RDFGraph aGraph = RDFFactory.eINSTANCE.createDocumentGraph();
		final Resource res = new RDFResourceImpl() {			
			@Override
			public Object getDelegate() { return null; }
			
			@Override
			public void addDelegate(Triple obj) {}
		};
		res.getContents().add(aGraph);
		
		put.put(object, aGraph);
		
		return new RdfRepresentation(RDFGraph2RestletGraph.toGraph(aGraph), mediaType);
	}
}
