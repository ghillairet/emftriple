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
package com.emftriple.jena;

import static com.emftriple.transform.SparqlQueries.selectAllTypes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.datasources.IResultSet.Solution;
import com.emftriple.transform.DatatypeConverter;
import com.emftriple.transform.EObjectBuilder;
import com.emftriple.transform.Metamodel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class JenaEObjectBuilder 
	extends EObjectBuilder<RDFNode, Resource, Literal> {
	
	@SuppressWarnings("rawtypes")
	public JenaEObjectBuilder(JenaResourceImpl resource, IDataSource dataSource) {
		super(resource, dataSource);
	}

	@Override
	protected void setEReferenceValue(EObject object, EReference reference, RDFNode node) {
		if (node.isResource()) {
			Resource res = node.asResource();

			EObject value = null;
			String uri = res.getURI();
			if (resource.getPrimaryCache().hasKey(uri)) {
				value = resource.getPrimaryCache().getObjectByKey(uri);
			} else {
				@SuppressWarnings("unchecked")
				List<String> uris = selectAllTypes(dataSource, uri, resource.getGraph());

				EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(uris);

				if (eClass == null) {
					eClass = reference.getEReferenceType();
				}

				value = createProxy(eClass, uri);
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

//
//	@Override
//	protected IResultSet<RDFNode, Resource, Literal> getResultSet(EObject object, String key, String graphURI, IDataSource dataSource) {
//		if (null == key) { 
//			return dataSource.selectQuery(selectObjectByURI(object.eClass(), object.eContainer(), (EReference)object.eContainingFeature(), graphURI), null);
//		} else {
//			return super.getResultSet(object, key, graphURI, dataSource);
//		}
//
//	}

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
	protected Map<String, Map<String, Set<RDFNode>>> createMapOfValuesForBNode(IResultSet<RDFNode, Resource, Literal> resultSet) {
		final Map<String, Map<String, Set<RDFNode>>> mapOfNodes = new HashMap<String, Map<String, Set<RDFNode>>>();
		for (;resultSet.hasNext();) {
			Solution<RDFNode, Resource, Literal> sol = resultSet.next();
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
