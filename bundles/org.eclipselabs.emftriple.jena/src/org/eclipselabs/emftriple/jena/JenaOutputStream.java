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
package org.eclipselabs.emftriple.jena;

import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.getID;
import static org.eclipselabs.emftriple.internal.util.ETripleEcoreUtil.isBlankNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.internal.ETripleOutputStream;
import org.eclipselabs.emftriple.internal.Metamodel;
import org.eclipselabs.emftriple.internal.util.DatatypeConverter;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class JenaOutputStream 
	extends ETripleOutputStream {
	
	protected final JenaRDFBuilder builder;
	
	public JenaOutputStream(URI uri, Map<?, ?> options, IDataSource<?,?> dataSource) {
		super(uri, options, dataSource);
		this.builder = new JenaRDFBuilder();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void saveContent(TreeIterator<EObject> iterator, Map<String, String> queries) {
		final Collection<Statement> triples = new ArrayList<Statement>();
		final Model model = ModelFactory.createDefaultModel();
		
		for (;iterator.hasNext();) {
			EObject obj = iterator.next();
			
			if (!obj.eIsProxy()) {
				triples.addAll( getTriples(obj, model) );
//				((InternalEObject)obj).eSetProxyURI(URI.createURI(uri+"#uri="+getID(obj)));
			}
		}
		
		save(triples, (IDataSource<Model, Statement>) dataSource, queries.get("graph"));	
	}
	
	protected Collection<Statement> getTriples(EObject object, Model model) {
		final URI key = getID(object);
		final Collection<Statement> triples = builder.createTriples(object, key.toString(),  model);
		
		return triples;
	}
	
	protected void save(
			final Collection<Statement> triples, 
			final IDataSource<Model, Statement> dataSource, 
			final String graphURI) {
		
		dataSource.connect();
		dataSource.add(triples, graphURI);
		dataSource.disconnect();
	}
	
	protected static class JenaRDFBuilder 
		extends RDFBuilder<RDFNode, Resource, Literal, Statement, Model> {
		
		public JenaRDFBuilder() {
			super();
		}
		
		@Override
		public Statement createTripleURI(RDFNode subject, Resource predicate, Resource object, Model graph) {
			final Property p = graph.createProperty(predicate.getURI());
			return graph.createStatement(subject.asResource(), p, object);
		}

		@Override
		protected Statement createTripleBlankNode(RDFNode subject, Resource predicate, RDFNode object, Model graph) {
			final Property p = graph.createProperty(predicate.getURI());
			return graph.createStatement(subject.asResource(), p, object); 
		}

		@Override
		protected RDFNode createBlankNode(EObject object, Model graph) {
			return graph.createResource(AnonId.create());
		}

		@Override
		public Statement createTripleLiteral(RDFNode subject, Resource predicate, Literal object, Model graph) {
			return graph.createStatement(subject.asResource(), (Property) predicate, object);
		}

		@Override
		public Literal createLiteral(EObject object, EAttribute attribute, Object value, Model graph) {
			final String literalValue = DatatypeConverter.toString(attribute.getEType().getName(), value);
			final String dataTypeURI = DatatypeConverter.get((EDataType) attribute.getEType());

			return  graph.createTypedLiteral(literalValue, dataTypeURI);
		}

		@Override
		public Resource createURI(EObject object, Model graph) {
			URI uri = getID(object);
			if (uri == null) {
				throw new IllegalArgumentException("Cannot create ID for object "+object);
			}
			return graph.createResource(uri.toString());
		}

		@Override
		public Resource createURI(EStructuralFeature feature, Model graph) {
			final String uri = Metamodel.INSTANCE.getRdfType(feature);
			if (uri == null) {
				throw new IllegalArgumentException("Cannot create URI for feature "+feature);
			}
			return graph.createProperty(uri);
		}

		@Override
		public Resource createURI(String uri, Model graph) {
			return graph.createResource(uri);
		}

		@Override
		public Collection<Statement> createTriples(EObject object, String key, Model graph) {
			final List<Statement> triples = new ArrayList<Statement>();
			
			final RDFNode subject = isBlankNode(object) ? 
					createBlankNode(object, graph) : 
						createURI(key, graph);

			triples.addAll(createRdfTypes(object, subject, graph));

			for (EAttribute attr: object.eClass().getEAllAttributes()) {
				triples.addAll(createTriples(object, attr, subject, graph));
			}

			for (EReference ref: object.eClass().getEAllReferences()) {
				triples.addAll(createTriples(object, ref, subject, graph));
			}

			return triples;
		}
	}
	
}
