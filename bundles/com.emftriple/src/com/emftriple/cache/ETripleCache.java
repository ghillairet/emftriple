package com.emftriple.cache;

import org.eclipse.emf.ecore.EObject;

public interface ETripleCache {

	boolean hasKey(String key);
	
	boolean hasObject(EObject obj);
	
	EObject getObjectByKey(String key);
	
	String getObjectId(EObject obj);
	
	void cache(String key, EObject obj);
	
}
