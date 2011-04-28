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
package com.emftriple.transform.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emf4sw.rdf.Datatype;
import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Property;
import com.emf4sw.rdf.RDFFactory;
import com.emf4sw.rdf.Resource;
import com.emf4sw.rdf.Triple;
import com.emf4sw.rdf.operations.DatatypeConverter;
import com.emf4sw.rdf.vocabulary.RDF;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.transform.IPutObject;
import com.emftriple.transform.Metamodel;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class PutObjectImpl implements IPutObject {

	private static final RDFFactory factory = RDFFactory.eINSTANCE;

	private ETripleResourceImpl context;

	public PutObjectImpl(ETripleResourceImpl context) {
		this.context = context;
	}

	@Override
	public Collection<Triple> put(EObject object) {
		final List<Triple> triples = new ArrayList<Triple>();
		final Resource sbj = factory.createResource();
		
		sbj.setURI(context.getID(object).toString());
		
		final Property rdfType = factory.createProperty();
		rdfType.setURI(RDF.type);

		for (String type: Metamodel.INSTANCE.getRdfTypes(object.eClass())) {
			Resource eType = factory.createResource();
			eType.setURI(type);
			
			Triple triple = factory.createTriple();
			triple.setSubject(sbj);
			triple.setPredicate(rdfType);
			triple.setObject(eType);
			
			triples.add( triple );
		}

		for (EStructuralFeature aFeature: object.eClass().getEAllStructuralFeatures()) 
		{
			if (!(aFeature.isTransient() || aFeature.isDerived() || aFeature.isVolatile())) 
			{
				if (object.eIsSet(aFeature))
				{
					Object value = object.eGet(aFeature, true);
					if (aFeature instanceof EAttribute) {
						Property property = getProperty(aFeature);
						if (aFeature.isMany()) {
							triples.addAll( createManyLiteralTriples(sbj, property, (EAttribute) aFeature, value) );
						} else {
							triples.add( createLiteralTriple(sbj, property, (EAttribute) aFeature, value) );
						}
					}
					else {
						Property property = getProperty(aFeature);
						if (aFeature.isMany()) {
							triples.addAll( createManyTriples(sbj, property, (EReference) aFeature, value) );
						} else {
							triples.add( createTriple(sbj, property, (EReference) aFeature, value) );
						}
					}
				}
			}
		}

		return triples;
	}

	private Triple createTriple(Resource sbj, Property property, EReference aFeature, Object value) {
		final Resource object = factory.createResource();
		object.setURI(context.getID((EObject) value).toString());
		
		final Triple triple = factory.createTriple();
		triple.setSubject(sbj);
		triple.setPredicate(property);
		triple.setObject(object);
		
		return triple;
	}

	private List<Triple> createManyTriples(Resource sbj, Property property, EReference aFeature, Object value) {
		final List<Triple> triples = new ArrayList<Triple>();
		
		if (Collection.class.isInstance(value)) {
			Collection<?> all = (Collection<?>) value;
			for (Object obj: all) {
				triples.add( createTriple(sbj, property, aFeature, obj) );
			}
		}
		
		return triples;
	}

	private List<Triple> createManyLiteralTriples(Resource sbj, Property property, EAttribute aFeature, Object value) {
		final List<Triple> triples = new ArrayList<Triple>();
		
		if (Collection.class.isInstance(value)) {
			Collection<?> all = (Collection<?>) value;
			for (Object obj: all) { 
				triples.add( createLiteralTriple(sbj, property, aFeature, obj) );
			}
		} else if (value.getClass().isArray()) {
			Object[] all = (Object[]) value;
			for (Object obj: all) {
				triples.add( createLiteralTriple(sbj, property, aFeature, obj) );
			}
		}
		
		return triples;
	}

	private Triple createLiteralTriple(Resource sbj, Property property, EAttribute aFeature, Object obj) {
		final String literalValue = DatatypeConverter.toString( aFeature.getEType().getName(), obj );
		final Literal aLiteral = factory.createLiteral();
		aLiteral.setLexicalForm( literalValue );

		final String dt = DatatypeConverter.get((EDataType) aFeature.getEType());
		
		final Datatype aDatatype = factory.createDatatype();
		aDatatype.setURI(dt);
		aLiteral.setDatatype(aDatatype);
		
		final Triple triple = factory.createTriple();
		triple.setSubject(sbj);
		triple.setPredicate(property);
		triple.setObject(aLiteral);
		
		return triple;
	}

	private Property getProperty(EStructuralFeature aFeature) {
		final Property property = factory.createProperty();
		property.setURI(Metamodel.INSTANCE.getRdfType(aFeature));		
		
		return property;
	}

}
