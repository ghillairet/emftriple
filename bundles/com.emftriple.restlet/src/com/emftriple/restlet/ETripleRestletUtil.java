/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package com.emftriple.restlet;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.restlet.data.MediaType;
import org.restlet.ext.rdf.RdfRepresentation;

import com.emf4sw.rdf.Triple;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.transform.IPutObject;
import com.emftriple.transform.impl.PutObjectImpl;

/**
 * 
 * @author ghillairet
 * @since 0.1.0
 */
public class ETripleRestletUtil {
	
	public static RdfRepresentation getRepresentation(Resource resource, MediaType mediaType) {
		final IPutObject put = new PutObjectImpl((ETripleResourceImpl) resource);
		
		final Collection<Triple> triples = new ArrayList<Triple>();
		for (EObject obj: resource.getContents())
			triples.addAll(put.put(obj));
		
		return new RdfRepresentation(RDFGraph2RestletGraph.toGraph(triples), mediaType);
	}
	
	public static RdfRepresentation getRepresentation(Resource resource, Iterable<? extends EObject> objects, MediaType mediaType) {
		final IPutObject put = new PutObjectImpl((ETripleResourceImpl) resource);
		
		final Collection<Triple> triples = new ArrayList<Triple>();
		for (EObject o: objects) {
			triples.addAll(put.put(o));
		}
		
		return new RdfRepresentation(RDFGraph2RestletGraph.toGraph(triples), mediaType);
	}
	
	public static RdfRepresentation getRepresentation(Resource resource, EObject object, MediaType mediaType) {
		final IPutObject put = new PutObjectImpl((ETripleResourceImpl) resource);
		
		final Collection<Triple> triples = new ArrayList<Triple>();
		triples.addAll(put.put(object));
		
		return new RdfRepresentation(RDFGraph2RestletGraph.toGraph(triples), mediaType);
	}
}
