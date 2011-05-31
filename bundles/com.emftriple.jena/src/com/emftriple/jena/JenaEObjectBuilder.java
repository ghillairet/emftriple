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

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.emftriple.datasources.IDataSource;
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

			if (resource.getPrimaryCache().hasKey(res.getURI())) {
				value = resource.getPrimaryCache().getObjectByKey(res.getURI());
			} else {
				@SuppressWarnings("unchecked")
				List<String> uris =
					SparqlQueries.selectAllTypes(dataSource, res.getURI(), resource.getGraph());
					
				EClass eClass = Metamodel.INSTANCE.getEClassByRdfType(uris);
					
				value = createProxy(eClass, res.getURI());
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
	protected void setEAttributeValue(EObject object, EAttribute attribute, RDFNode node) {
		if (node.isLiteral()) {
			Literal literal = node.asLiteral();
			Object value = DatatypeConverter.convert(attribute.getEAttributeType(), literal.getLexicalForm());
			if (value != null) {
				object.eSet(attribute, value);
			}
		}
	}

}
