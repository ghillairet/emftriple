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
package com.emftriple.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * {@link ETripleEcoreUtil}
 * 
 * Utility Class for EMFTriple.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class ETripleEcoreUtil {

	private static final Map<EClass, EAttribute> cacheid = new HashMap<EClass, EAttribute>();

	/**
	 * Get all sub hierarchy of a given {@link EClass} from a given set. 
	 * 
	 * @param eClass to get the sub hierarchy.
	 * @param classes set of {@link EClass}.
	 * @return a {@link EList} of {@link EClass} being the sub hierarchy of eClass parameter.
	 */
	public static EList<EClass> getESubClasses(EClass eClass, Collection<EClass> classes) {
		EList<EClass> allSubClasses = new BasicEList<EClass>();
		for (EClass other: classes) {
			if (other.getEAllSuperTypes().contains(eClass)) {
				allSubClasses.add(other);
			}
		}
		return allSubClasses;
	}

	public static EAnnotation getETripleAnnotation(EModelElement element, String name) {
		EAnnotation ann = element.getEAnnotation(name);
		if (ann != null)
		{
			return ann;
		}
		return element.getEAnnotation("etriple." + name);
	}

	public static EAttribute getId(EClass eClass) {
		if (cacheid.containsKey(eClass)) {
			return cacheid.get(eClass);
		}

		EAttribute theId = eClass.getEIDAttribute();
		if (theId != null) {
			cacheid.put(eClass, theId);
			return theId;
		}

		theId = getId(eClass, eClass.getEAttributes());
		if (theId == null) {
			theId = getId(eClass, eClass.getEAllAttributes());
		}

		return theId;
	}

	private static EAttribute getId(EClass eClass, EList<EAttribute> attributes) {
		for (EAttribute eAttribute: attributes) {
			for (EAnnotation ann: eAttribute.getEAnnotations()) {
				if (ann.getSource().contains("Id")) { // || ann.getSource().equals("GeneratedId") || ann.getSource().equals("CompositeId")) {
					cacheid.put(eClass, eAttribute);
					return eAttribute;
				}
			}
		}
		return null;
	}
	
	/**
	 * Return the namespace of an EObject corresponding to its EPackage nsURI value.
	 */
	public static String namespace(EObject object) {
		String nsURI = null;
		if (object instanceof EPackage) {
			nsURI = ((EPackage) object).getNsURI();
		}
		if (object instanceof EClassifier) {
			nsURI = ((EClassifier) object).getEPackage().getNsURI();
		}
		else if (object instanceof EStructuralFeature) {
			nsURI = ((EStructuralFeature) object).getEContainingClass().getEPackage().getNsURI();
		}
		else {
			nsURI = object.eClass().getEPackage().getNsURI();
		}
		return nsURI.endsWith("/") || nsURI.endsWith("#") ? nsURI : nsURI + "/";
	}


	public static String getPackageNamespace(EPackage ePackage) {
		EAnnotation ann = getETripleAnnotation(ePackage, "Ontology");
		String namespace = null;
		if (ann != null)
		{
			namespace = ann.getDetails().get("uri");
		}
		if (namespace == null) 
		{
			namespace = ePackage.getNsURI();
		}
		return namespace;
	}

	public static String validNamespace(String namespace) {
		return new URIValidator().apply(namespace);
	}

	public static String wellFormedURI(String namespace) {
		return 
		!((namespace.endsWith("#") || namespace.endsWith("/"))) ? 
				namespace + "#" : 
					namespace;
	}

	public static class URIValidator {
		private static final String HTTP = "http://";

		public String apply(String from) {
			return ((from.indexOf(":") > -1) ? 
					(from.charAt(from.indexOf(":") + 1 ) == '/') ? from : 
						from.replaceFirst(":", "://") : HTTP + from).replaceAll("\\s", "_");
		}	
	}

}
