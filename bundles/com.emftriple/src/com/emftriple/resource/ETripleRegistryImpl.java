package com.emftriple.resource;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import com.emftriple.ETriple.Registry;
import com.emftriple.transform.IMapping;
import com.emftriple.transform.impl.ETripleMappingImpl;

public class ETripleRegistryImpl implements Registry {

	private static Map<String, EPackage> packages = new HashMap<String, EPackage>();
	
	private static ETripleMappingImpl mapping = new ETripleMappingImpl();
	
	@Override
	public void register(EPackage ePackage) {
		if (!packages.containsKey(ePackage.getNsURI())) {
			packages.put(ePackage.getNsURI(), ePackage);
		}
		if (!EPackage.Registry.INSTANCE.containsKey(ePackage.getNsURI())) {
			EPackage.Registry.INSTANCE.put(ePackage.getNsURI(), ePackage);
		}
		mapping.addEPackage(ePackage);
	}

	@Override
	public IMapping getMapping() {
		return mapping;
	}

}
