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

import static org.eclipselabs.emftriple.internal.SparqlQueries.selectAllTypes;

import java.util.Collection;
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
import org.eclipselabs.emftriple.internal.ETripleInputStream;
import org.eclipselabs.emftriple.internal.Metamodel;
import org.eclipselabs.emftriple.internal.util.DatatypeConverter;
import org.eclipselabs.emftriple.sail.util.SailResultSet.SailSolution;
import org.openrdf.model.Graph;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;


public class SailInputStream
	extends ETripleInputStream {

	
	public SailInputStream(URI uri, Map<?, ?> options, IDataSource<?, ?> dataSource) {
		super(uri, options, dataSource);
		this.builder = new SailEObjectBuilder(dataSource, this);
	}
	
	@Override
	protected Set<String> loadingContentFromResultSet(IResultSet resultSet) {
		final Set<String> uris = new HashSet<String>();

		for (;resultSet.hasNext();) {
			SailSolution s = (SailSolution) resultSet.next();
			for (String var: s.getSolutionNames()) {
				if (s.isResource(var)) {
					org.openrdf.model.URI res = s.getResource(var);
					if (!uris.contains(res.stringValue())) {
						uris.add(res.stringValue());
					}
				}
			}
		}

		return uris;
	}
	
	protected static class SailEObjectBuilder
		extends EObjectBuilder<Value, Graph> {

		/**
		 * @param dataSource
		 * @param sailInputStream
		 */
		public SailEObjectBuilder(IDataSource<?, ?> dataSource, SailInputStream sailInputStream) {
			super(dataSource, sailInputStream);
		}
		
		
		@Override
		protected void setEReferenceValue(EObject object, EReference reference, Value node, String graph) {
			EObject value = null;
			String uri = node.stringValue();
			
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

		@SuppressWarnings("unchecked")
		@Override
		protected void setEAttributeValue(EObject object, EAttribute attribute, Value node) {
			final Literal literal = (Literal)node;

			Object value = DatatypeConverter.convert(attribute.getEAttributeType(), literal.getLabel());
			
			if (value != null) {
				if (attribute.isMany()) {
					((Collection<Object>)object.eGet(attribute)).add(value);
				} else {
					object.eSet(attribute, value);
				}
			}
		}

		@Override
		protected String getURI(Value node) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected Map<String, Map<String, Set<Value>>> createMapOfValuesForBNode(IResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
