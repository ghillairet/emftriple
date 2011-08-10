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
package com.emftriple.sail;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IResultSet;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.transform.DatatypeConverter;
import com.emftriple.transform.EObjectBuilder;
import com.emftriple.transform.Metamodel;
import com.emftriple.transform.SparqlQueries;

public class SailEObjectBuilder 
extends EObjectBuilder<Value, URI, Literal>{

	@SuppressWarnings("rawtypes")
	public SailEObjectBuilder(ETripleResourceImpl resource, IDataSource dataSource) {
		super(resource, dataSource);
	}

	@Override
	protected void setEReferenceValue(EObject object, EReference reference, Value node) {
		EObject value = null;
		String uri = node.stringValue();

		if (resource.getPrimaryCache().hasKey(uri)) {
			value = resource.getPrimaryCache().getObjectByKey(uri);
		} else {
			@SuppressWarnings("unchecked")
			EClass eClass = 
			Metamodel.INSTANCE.getEClassByRdfType(
					SparqlQueries.selectAllTypes(dataSource, uri, null));
			if (eClass != null) {
				value = createProxy(eClass, uri);
			}
		}
		
		if (value != null) {
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
	protected void setEAttributeValue(EObject object, EAttribute attribute, Value node) {
		final Literal literal = (Literal)node;

		Object value = DatatypeConverter.convert(attribute.getEAttributeType(), literal.getLabel());
		if (value != null) {
			object.eSet(attribute, value);
		}
	}

	@Override
	protected String getURI(Value node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Map<String, Set<Value>>> createMapOfValuesForBNode(IResultSet<Value, URI, Literal> resultSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
