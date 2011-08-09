/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *    Moritz Hoffmann - bnode handling
 *******************************************************************************/
package org.eclipselabs.emftriple.jena;

import static org.eclipselabs.emftriple.internal.SparqlQueries.selectAllTypes;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.datasources.IResultSet;
import org.eclipselabs.emftriple.datasources.IResultSet.Solution;
import org.eclipselabs.emftriple.internal.ETripleInputStream;
import org.eclipselabs.emftriple.internal.Metamodel;
import org.eclipselabs.emftriple.internal.util.DatatypeConverter;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class JenaInputStream 
	extends ETripleInputStream {
	
	public JenaInputStream(URI uri, Map<?, ?> options, IDataSource<?,?> dataSource) {
		super(uri, options, dataSource);
		this.builder = new JenaEObjectBuilder(dataSource, this);
	}
	
	@Override
	protected Set<String> loadingContentFromResultSet(IResultSet resultSet) {
		final Set<String> uris = new HashSet<String>();

		if (resultSet == null) {
			return uris;
		}

		for (;resultSet.hasNext();) {
			Solution s = resultSet.next();
			for (String var: s.getSolutionNames()) {
				if (s.isResource(var)) {
					Resource res = s.getResource(var);
					if (!res.isAnon() && !uris.contains(res.getURI())) {
						uris.add(res.getURI());
					}
				}
			}
		}

		return uris;
	}

	@Override
	public int read() throws IOException {
		return 0;
	}

	protected static class JenaEObjectBuilder
		extends EObjectBuilder<RDFNode, Model> {

		public JenaEObjectBuilder(IDataSource<?,?> dataSource, ETripleInputStream inStream) {
			super(dataSource, inStream);
		}

		@Override
		protected void setEReferenceValue(EObject object, EReference reference, RDFNode node,  String graph) {
			if (node.isResource()) {
				Resource res = node.asResource();

				EObject value = null;
				String uri = res.getURI();
				
				List<String> uris = selectAllTypes(dataSource, uri, graph);
				
				EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(uris);
				
				if (eClass == null) {
					eClass = reference.getEReferenceType();
				}

				value = inStream.createProxy(eClass, uri);
				loadEObject(value, uri, graph, true);
				
				if (!reference.isContainer()) {
					if (object.eResource() != null) {
						object.eResource().getContents().add(value);
					}
				}
				
				if (reference.isMany()) {
					@SuppressWarnings("unchecked")
					EList<EObject> values = (EList<EObject>) object.eGet(reference);
					values.add(value);
				} else {
					object.eSet(reference, value);
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void setEAttributeValue(EObject object, EAttribute attribute, RDFNode node) {
			Object value = null;

			if (node.isLiteral()) {
				Literal literal = node.asLiteral();
				value = DatatypeConverter.convert(attribute.getEAttributeType(), literal.getLexicalForm());
			} else if (node.isURIResource()) {
				value = node.asResource().getURI();
			}

			if (value != null) {
				if (attribute.isMany()) {
					((Collection<Object>)object.eGet(attribute)).add(value);
				} else {
					object.eSet(attribute, value);
				}
			}

		}

		@Override
		protected Map<String, Map<String, Set<RDFNode>>> createMapOfValuesForBNode(IResultSet resultSet) {

			final Map<String, Map<String, Set<RDFNode>>> mapOfNodes = new HashMap<String, Map<String, Set<RDFNode>>>();

			for (;resultSet.hasNext();) {
				Solution sol = resultSet.next();
				Resource bn = sol.getResource("bn_");
				String bnId = bn.getId().getLabelString();

				Map<String, Set<RDFNode>> map;
				if (!mapOfNodes.containsKey(bnId)) {
					map = new HashMap<String, Set<RDFNode>>();
				} else {
					map = mapOfNodes.get(bnId);
				}

				Resource p = sol.getResource("p");
				Set<RDFNode> nodes;
				if (!map.containsKey(p.getURI())) {
					nodes = new HashSet<RDFNode>();
				} else {
					nodes = map.get(p.getURI());
				}

				RDFNode o = sol.get("o");
				nodes.add(o);
				map.put(p.getURI(), nodes);
				mapOfNodes.put(bnId, map);
			}

			return mapOfNodes;
		}

		@Override
		protected String getURI(RDFNode node) {
			if (node.isResource()) {
				return node.asResource().getURI();
			}
			return null;
		}
		
	}
}
