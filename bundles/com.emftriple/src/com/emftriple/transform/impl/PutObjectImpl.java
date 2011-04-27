/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.transform.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.URI;
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
import com.emftriple.ETriple;
import com.emftriple.resource.ETripleResource;
import com.emftriple.transform.IMapping;
import com.emftriple.transform.IPutObject;
import com.emftriple.util.EntityUtil;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class PutObjectImpl implements IPutObject {

	private static final RDFFactory factory = RDFFactory.eINSTANCE;

	private final IMapping mapping;

	private ETripleResource context;

	public PutObjectImpl(ETripleResource context) {
		this.mapping = ETriple.Registry.INSTANCE.getMapping();
		this.context = context;
	}

	@Override
	public Collection<Triple> put(EObject object) {
		final List<Triple> triples = new ArrayList<Triple>();
		final Resource sbj = factory.createResource();
		
		if (context.getPrimaryCache().hasObject(object)) {
			sbj.setURI(context.getPrimaryCache().getObjectId(object));	
		} else {
			String id = EObjectID.getId(object);
			sbj.setURI(id);
			context.getPrimaryCache().cache(id, object);
		}
		
		final Property rdfType = factory.createProperty();
		rdfType.setURI(RDF.type);

		for (URI type: EntityUtil.getRdfTypes(object.eClass())) {
			Resource eType = factory.createResource();
			eType.setURI(type.toString());
			
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

//	@SuppressWarnings("unused")
//	private void createListTriple(EObject aObject, EStructuralFeature aFeature, Object value) {
//		if (Collection.class.isInstance(value)) {
//			final Collection<?> all = (Collection<?>) value;
//			if (all.isEmpty())
//				return;
//
//			final Resource subject = objectCache.get(objectIdCache.get(aObject));
//			final Property property = getProperty((EReference)aFeature, graph);
//
//			final RDFSeq aList = RDFFactory.eINSTANCE.createRDFSeq();
//			graph.getBlankNodes().add(aList);
//
//			for (final Object obj: all) 
//			{
//				Resource object = getResource((EObject) obj, graph);
//				aList.getElements().add(object);
//
//				if (((EReference)aFeature).isContainment()) {
//					containedObjects.add((EObject) obj);
//				}
//			}
//		}
//	}

	private Triple createTriple(Resource sbj, Property property, EReference aFeature, Object value) {
		final Resource object = factory.createResource();
		if (context.getPrimaryCache().hasObject((EObject) value)) {
			object.setURI(context.getPrimaryCache().getObjectId((EObject) value));
		} else {
			String id = EObjectID.getId((EObject) value);
			context.getPrimaryCache().cache(id, (EObject) value);
			object.setURI(id);
		}
		
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

//	private void doAddNamespaces(RDFGraph aGraph, EClass eClass) {
//		final String namespace = doGetEPackageNamespace(eClass);
//		final String prefix = doGetEPackagePrefix(eClass);
//
//		final Namespace aNamespace = factory.createNamespace();
//		aNamespace.setPrefix(prefix);
//		aNamespace.setURI(namespace);
//		aNamespace.setGraph((DocumentGraph) aGraph);
//	}

//	private void createTypeTriple(EObject aObject, RDFGraph aGraph) {
//		checkIsMappedObject(aObject, mapping);
//
//		for (URI aURI: mapping.getRdfTypes(aObject.eClass())) {
//			final Resource subject = getResource(aObject, aGraph);
//			final Property property = aGraph.getProperty(RDF.type);
//			final Resource object = aGraph.getResource(aURI.toString());
//
//			aGraph.addTriple(subject, property, object);
//		}
//	}

//	private Resource getResource(EObject aObject, RDFGraph aGraph) {
//		String id = null;
//
//		synchronized (this) {
//			if (objectIdCache.containsKey(aObject)) {
//				id = objectIdCache.get(aObject);
//			} else {
//				id = EObjectID.getId(aObject).toString();
//				objectIdCache.put(aObject, id);
//			}
//
//			if (id == null) {
//				throw new IllegalArgumentException();
//			}	
//		}
//
//		return aGraph.getResource(id.toString());
//	}

	private Property getProperty(EStructuralFeature aFeature) {
		final Property property = factory.createProperty();
		property.setURI(mapping.getRdfType(aFeature).toString());		
		
		return property;
	}

//	private Property getProperty(EReference aFeature, RDFGraph aGraph) {
//		return aGraph.getProperty(mapping.getRdfType(aFeature).toString());
//	}
//
//	private String doGetEPackageNamespace(EClass aClass) {
//		String namespace = EcoreUtil.getAnnotation(aClass.getEPackage(), "Ontology", "uri");
//		if (namespace == null) {
//			namespace = aClass.getEPackage().getNsURI();
//		}
//		return namespace;
//	}
//
//	private String doGetEPackagePrefix(EClass aClass) {
//		String prefix = EcoreUtil.getAnnotation(aClass.getEPackage(), "Ontology", "prefix");
//		if (prefix == null) {
//			prefix = aClass.getEPackage().getNsPrefix();
//		}
//		return prefix;
//	}
//}

//private static void checkIsMappedObject(EObject aObject, IMapping mapping) {
//	checkNotNull(mapping);
//
//	if (mapping == null) {
//		throw new IllegalStateException("Cannot execute runtime transformation with no mapping set.");
//	}
//
//	if (!mapping.getEPackages().contains( aObject.eClass().getEPackage()) ) {
//		throw new IllegalArgumentException("EObject is not mapped by current mapping.");
//	}
//}
}
