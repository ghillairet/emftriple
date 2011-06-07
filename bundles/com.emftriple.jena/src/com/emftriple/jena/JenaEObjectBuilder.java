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
package com.emftriple.jena;

import static com.emftriple.transform.SparqlQueries.selectObjectByURI;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.transform.DatatypeConverter;
import com.emftriple.transform.EObjectTransform;
import com.emftriple.transform.Metamodel;
import com.emftriple.transform.SparqlQueries;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class JenaEObjectBuilder extends EObjectTransform<RDFNode, Resource, Literal> {

	@SuppressWarnings("rawtypes")
	public JenaEObjectBuilder(JenaResourceImpl resource, IDataSource dataSource) {
		super(resource, dataSource);
	}

	@Override
	protected void setEReferenceValue(EObject object, EReference reference, RDFNode node) {
		if (node.isResource()) {
			Resource res = node.asResource();

			EObject value = null;

			String uri;
			if (res.isAnon() && reference.isContainment()) {
				uri = "_:" + res.getId().getLabelString();
			} else {
				uri = res.getURI();
			}
			
			if (resource.getPrimaryCache().hasKey(uri)) {
				value = resource.getPrimaryCache().getObjectByKey(uri);
			} else {
				@SuppressWarnings("unchecked")
				List<String> uris =
					SparqlQueries.selectAllTypes(dataSource, uri, resource.getGraph());
					
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


	@Override
	protected IResultSet<RDFNode, Resource, Literal> getResultSet(
			EClass eClass, String key, String graphURI, IDataSource dataSource) {
		if (key.startsWith("_:")) {
			return dataSource.selectQuery(selectObjectByURI(eClass, key, graphURI), null);
		} else {
			return super.getResultSet(eClass, key, graphURI, dataSource);
		}

	}

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
			if (attribute.isMany()){
				((Collection<Object>)object.eGet(attribute)).add(value);
			} else {
				object.eSet(attribute, value);
			}
		}

	}

}
