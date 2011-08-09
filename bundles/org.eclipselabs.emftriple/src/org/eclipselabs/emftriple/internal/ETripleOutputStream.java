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
package org.eclipselabs.emftriple.internal;

import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.decodeQueryString;
import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.isBlankNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.vocabularies.RDF;


/**
 * The {@link ETripleOutputStream} class provides methods for converting EObject to RDF triples. 
 * This class needs to be extended to fulfill specific RDF APIs.
 *
 * @param <U> abstract type for RDF URI (Label)
 * @param <L> abstract type for RDF Literal
 * @param <T> abstract type for RDF Triple (Statement) 
 * @param <G> abstract type for RDF Graph
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public abstract class ETripleOutputStream
	extends ByteArrayOutputStream
	implements URIConverter.Saveable {

	protected final IDataSource<?,?> dataSource;
	protected final Map<?, ?> options;	
	protected final URI uri;
	
	public ETripleOutputStream(URI uri, Map<?, ?> options, IDataSource<?,?> dataSource) {
		this.uri = uri;
		this.options = options;
		this.dataSource = dataSource;
	}
	
	@Override
	public void saveResource(Resource resource) throws IOException {
		if (!(dataSource.isMutable())) {
			throw new IllegalStateException("Cannot save in a non mutable RDF Store");
		}
		
		final Map<String, String> queries = decodeQueryString(uri.query());
		
		boolean inGraph = queries.containsKey("graph");
		if (inGraph && !(dataSource.supportsNamedGraph())) {
			throw new IllegalStateException("RDF Store does not support named graphs");
		}
		
		saveContent(resource.getAllContents(), queries);
	}
	
	protected abstract void saveContent(TreeIterator<EObject> iterator, Map<String, String> queries);
	
	protected abstract static class RDFBuilder<N, U extends N, L extends N, T, G> {
		
		public abstract Collection<T> createTriples(EObject object, String key, G graph);

		public abstract U createURI(String uri, G graph);
		
		public Collection<T> createRdfTypes(EObject object, N subject, G graph) {
			final List<T> triples = new ArrayList<T>();

			for (String type: Metamodel.INSTANCE.getRdfTypes(object.eClass())) {
				T stmt = createTripleURI(subject, createURI(RDF.type, graph), createURI(type, graph), graph);
				if (stmt != null) {
					triples.add(stmt);
				}
			}

			return triples;
		}
		
		public Collection<T> createTriples(EObject object, EReference reference, N subject, G graph) {
			final List<T> triples = new ArrayList<T>();
			final U predicate = createURI(reference, graph);

			if (!reference.isTransient() && !reference.isVolatile() && object.eIsSet(reference)) {
				
				Object value = object.eGet(reference);

				if (reference.isMany()) {
					@SuppressWarnings("unchecked")
					List<EObject> targetObjects = ((InternalEList<EObject>) value).basicList();

					for (EObject targetObject : targetObjects) {
						T triple = getTripleFromReferenceValue(subject, predicate, targetObject, graph, object.eResource());
						if (triple != null) {
							triples.add(triple);
						}
					}
				} else {
					T triple = getTripleFromReferenceValue(subject, predicate, (EObject) value, graph, object.eResource());
					if (triple != null) {
						triples.add(triple);
					}
				}
			}
			return triples;
		}

		@SuppressWarnings("unchecked")
		protected T getTripleFromReferenceValue(N subject, U predicate, EObject value, G graph, Resource container) {
			N obj;
			T stmt = null;
			if (isBlankNode(value)) {
				obj = createBlankNode(value, graph);
				if (obj != null) {
					stmt = createTripleBlankNode(subject, predicate, obj, graph);
				}
			} else {			
				obj = createURI(value, graph);
				if (obj != null) {
					stmt = createTripleURI(subject, predicate, (U) obj, graph);
				}
			}

			return stmt;
		}
		
		public Collection<T> createTriples(EObject object, EAttribute attribute, N subject, G graph) {
			final List<T> triples = new ArrayList<T>();
			final U predicate = createURI(attribute, graph);

			if (!attribute.isTransient() && !attribute.isVolatile() && 
					!attribute.isDerived() && object.eIsSet(attribute)) {

				Object value = object.eGet(attribute);

				if (FeatureMapUtil.isFeatureMap(attribute)) {
					FeatureMap.Internal featureMap = (FeatureMap.Internal)value;
					Iterator<FeatureMap.Entry> iterator = featureMap.basicIterator();

					while (iterator.hasNext()) {
						FeatureMap.Entry entry = iterator.next();
						EStructuralFeature feature = entry.getEStructuralFeature();

						T stmt = null;
						if (feature instanceof EAttribute) {
							stmt = createTripleLiteral(subject, 
									predicate, 
									createLiteral(object, attribute, entry.getValue(), graph), 
									graph);
						} else {
							stmt = createTripleURI(
									subject, predicate, 
									createURI((EObject)entry.getValue(), graph),
									graph);
						}

						if (stmt != null) {
							triples.add(stmt);
						}
					}
				} else if (attribute.isMany()) {
					EList<?> rawValues = (EList<?>) value;
					for (Object rawValue : rawValues) {
						L literal = createLiteral(object, attribute, rawValue, graph);
						if (literal != null) {
							T stmt = createTripleLiteral(subject, predicate, literal, graph);
							if (stmt != null) {
								triples.add(stmt);
							}
						}
					}
				} else {
					L literal = createLiteral(object, attribute, value, graph);
					if (literal != null) {
						T stmt = createTripleLiteral(subject, predicate, literal, graph);
						if (stmt != null) {
							triples.add(stmt);
						}
					}
				}
			}
			return triples;
		}

		protected abstract T createTripleURI(N subject, U predicate, U object, G graph);

		protected abstract T createTripleBlankNode(N subject, U predicate, N object, G graph);

		protected abstract T createTripleLiteral(N subject, U predicate, L object, G graph);

		protected abstract U createURI(EObject object, G graph);

		protected abstract N createBlankNode(EObject object, G graph);

		protected abstract U createURI(EStructuralFeature feature, G graph);

		protected abstract L createLiteral(EObject object, EAttribute attribute, Object value, G graph);

	}
}
