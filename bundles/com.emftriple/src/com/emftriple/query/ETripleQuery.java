package com.emftriple.query;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

public interface ETripleQuery {

	EObject node(String key);
	
	EObject node(URI key);
	
	Collection<EObject> by(EClass eClass);
	
	<T> Collection<T> by(Class<T> eClass);
	
}