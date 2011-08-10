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
package org.eclipselabs.emftriple.sail;

import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.getID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.internal.ETripleOutputStream;
import org.eclipselabs.emftriple.internal.Metamodel;
import org.eclipselabs.emftriple.internal.util.DatatypeConverter;
import org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.ValueFactoryImpl;

public class SailOutputStream 
	extends ETripleOutputStream {

	private SailRDFBuilder builder;

	public SailOutputStream(org.eclipse.emf.common.util.URI uri, Map<?, ?> options, IDataSource<?,?> dataSource) {
		super(uri, options, dataSource);
		this.builder = new SailRDFBuilder();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream#saveContent(org.eclipse.emf.common.util.TreeIterator, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void saveContent(TreeIterator<EObject> iterator, Map<String, String> queries) {
		 
		final Map<String, EObject> done = new WeakHashMap<String, EObject>();
		
		for (;iterator.hasNext();) {
			EObject obj = iterator.next();
			final org.eclipse.emf.common.util.URI key = getID(obj);
			
			Collection<Statement> triples = getTriples(obj, key);
			
			if (!done.containsKey(key.toString())) {
				
				if (dataSource.contains(key.toString())) {
					dataSource.delete(key.toString(), queries.get("graph"));
				}

				save(triples, (IDataSource<Graph, Statement>) dataSource, queries.get("graph"));			
				
				done.put(key.toString(), obj);
			}
		}
	}
	
	protected Collection<Statement> getTriples(EObject object, org.eclipse.emf.common.util.URI key) {
		final Graph graph = new GraphImpl();
		final Collection<Statement> triples = builder.createTriples(object, key.toString(), graph);

		return triples;
	}
	
	protected void save(
			final Collection<Statement> triples, 
			final IDataSource<Graph, Statement> dataSource, 
			final String graphURI) {
		
//		dataSource.connect();
		dataSource.add(triples, graphURI);
//		dataSource.disconnect();
	}
	
	protected static class SailRDFBuilder
		extends RDFBuilder<Value, URI, Literal, Statement, Graph> {
		
		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createTripleURI(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
		 */
		@Override
		protected Statement createTripleURI(Value subject, URI predicate, URI object, Graph graph) {
			if (subject == null || predicate == null || object == null)
				return null;
			
			ValueFactory factory = new ValueFactoryImpl();
			Statement stmt = factory.createStatement((Resource) subject, predicate, object);
			
			return stmt;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createTripleLiteral(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
		 */
		@Override
		protected Statement createTripleLiteral(Value subject, URI predicate, Literal object, Graph graph) {
			if (subject == null || predicate == null || object == null)
				return null;
			
			ValueFactory factory = new ValueFactoryImpl();
			Statement stmt = factory.createStatement((Resource) subject, predicate, object);
			
			return stmt;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createURI(org.eclipse.emf.ecore.EObject, java.lang.Object)
		 */
		@Override
		protected URI createURI(EObject object, Graph graph) {
			ValueFactory factory = new ValueFactoryImpl();
			
			org.eclipse.emf.common.util.URI uri = ETripleEcoreUtil.getID(object);
			if (uri == null) {
				throw new IllegalArgumentException("Cannot create ID for object "+object);
			}
			return factory.createURI(uri.toString());
		}
		
		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createURI(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object)
		 */
		@Override
		protected URI createURI(EStructuralFeature feature, Graph graph) {
			ValueFactory factory = new ValueFactoryImpl();
			
			String uri = Metamodel.INSTANCE.getRdfType(feature);
			if (uri == null) {
				throw new IllegalArgumentException("Cannot create URI for feature "+feature);
			}
			return factory.createURI(uri);
		}

		@Override
		public Literal createLiteral(EObject object, EAttribute attribute, Object value, Graph graph) {
			if (value == null)
				return null;
			
			ValueFactory factory = new ValueFactoryImpl();
			
			final String literalValue = DatatypeConverter.toString(attribute.getEType().getName(), value);
			final String dataTypeURI = DatatypeConverter.get((EDataType) attribute.getEType());
			
			return factory.createLiteral(literalValue, dataTypeURI);
		}

		@Override
		public Collection<Statement> createTriples(EObject object, String key, Graph graph) {
			
			final List<Statement> triples = new ArrayList<Statement>();
			final URI subject = createURI(key, graph);
			
			triples.addAll(createRdfTypes(object, subject, graph));
			
			for (EAttribute attr: object.eClass().getEAllAttributes()) {
				triples.addAll(createTriples(object, attr, subject, graph));
			}
			
			for (EReference ref: object.eClass().getEAllReferences()) {
				triples.addAll(createTriples(object, ref, subject, graph));
			}
		
			return triples;
		}

		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createURI(java.lang.String, java.lang.Object)
		 */
		@Override
		public URI createURI(String uri, Graph graph) {
			ValueFactory factory = new ValueFactoryImpl();
			
			return factory.createURI(uri);
		}

		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createTripleBlankNode(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object)
		 */
		@Override
		protected Statement createTripleBlankNode(Value subject, URI predicate, Value object, Graph graph) {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see org.eclipselabs.emftriple.internal.ETripleOutputStream.RDFBuilder#createBlankNode(org.eclipse.emf.ecore.EObject, java.lang.Object)
		 */
		@Override
		protected Value createBlankNode(EObject object, Graph graph) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
