package com.emftriple.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.emftriple.resource.ETripleResource;
import com.emftriple.util.RDF;

public abstract class RDFTransform<U, L, T, G> {
	
	public RDFTransform() {
	
	}
	
	public abstract Collection<T> createTriples(EObject object, String key, G graph);

	public abstract U createURI(String uri, G graph);
	
	public Collection<T> createRdfTypes(EObject object, U subject, G graph) {
		final List<T> triples = new ArrayList<T>();
		
		for (String type: Metamodel.INSTANCE.getRdfTypes(object.eClass())) {
			T stmt = 
				createTripleURI(subject, createURI(RDF.type, graph), createURI(type, graph), graph);
			if (stmt != null) {
				triples.add(stmt);
			}
		}
		
		return triples;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection<T> createTriples(EObject object, EReference reference, U subject, G graph) {
		final List<T> triples = new ArrayList<T>();
		final U predicate = createURI(reference, (ETripleResource) object.eResource(), graph);
		
		if (!reference.isTransient() && !reference.isVolatile() &&
				object.eIsSet(reference)) {
			
			Object value = object.eGet(reference);
			
			if (reference.isMany()) {
				@SuppressWarnings("unchecked")
				List<EObject> targetObjects = ((InternalEList<EObject>) value).basicList();
				
				for (EObject targetObject : targetObjects) {
					U obj = createURI(targetObject, (ETripleResource) object.eResource(), graph);
					if (obj != null) {
						T stmt = createTripleURI(subject, predicate, obj, graph);
						if (stmt != null) {
							triples.add(stmt);
						}
					}
				}
			} else {
				U obj = createURI((EObject) value, (ETripleResource) object.eResource(), graph);
				if (obj != null) {
					T stmt = createTripleURI(subject, predicate, obj, graph);
					if (stmt != null) {
						triples.add(stmt);
					}
				}
			}
		}
		return triples;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection<T> createTriples(EObject object, EAttribute attribute, U subject, G graph) {
		final List<T> triples = new ArrayList<T>();
		final U predicate = createURI(attribute, (ETripleResource) object.eResource(), graph);
		
		if (!attribute.isTransient() && !attribute.isVolatile() && 
				!attribute.isDerived() && object.eIsSet(attribute)) {
			
			Object value = object.eGet(attribute);
		
			if (FeatureMapUtil.isFeatureMap(attribute)) {
				FeatureMap.Internal featureMap = (FeatureMap.Internal)value;
				Iterator<FeatureMap.Entry> iterator = featureMap.basicIterator();
				while (iterator.hasNext()) {
					FeatureMap.Entry entry = iterator.next();
					EStructuralFeature feature = entry.getEStructuralFeature();
					
					T stmt = null;
					if (feature instanceof EAttribute) {
						stmt = createTripleLiteral(subject, 
								predicate, 
								createLiteral(object, attribute, entry.getValue(), graph), 
								graph);
					} else {
						stmt = createTripleURI(
								subject, predicate, 
								createURI((EObject)entry.getValue(), (ETripleResource)object.eResource(), graph),
								graph);
					}
					
					if (stmt != null) {
						triples.add(stmt);
					}
				}
			} else if (attribute.isMany()) {
				EList<?> rawValues = (EList<?>) value;
				for (Object rawValue : rawValues) {
					L literal = createLiteral(object, attribute, rawValue, graph);
					if (literal != null) {
						T stmt = createTripleLiteral(subject, predicate, literal, graph);
						if (stmt != null) {
							triples.add(stmt);
						}
					}
				}
			} else {
				L literal = createLiteral(object, attribute, value, graph);
				if (literal != null) {
					T stmt = createTripleLiteral(subject, predicate, literal, graph);
					if (stmt != null) {
						triples.add(stmt);
					}
				}
			}
		}
		return triples;
	}
	
	public abstract T createTripleURI(U subject, U predicate, U object, G graph);
	
	public abstract T createTripleLiteral(U subject, U predicate, L object, G graph);
	
	public abstract U createURI(EObject object, @SuppressWarnings("rawtypes") ETripleResource resource, G graph);
	
	public abstract U createURI(EStructuralFeature feature, @SuppressWarnings("rawtypes") ETripleResource resource, G graph);
	
	public abstract L createLiteral(EObject object, EAttribute attribute, Object value, G graph);

}
