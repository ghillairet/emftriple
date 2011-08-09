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
package org.eclipselabs.emftriple.internal.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * The class {@link ETripleEcoreUtil} is a utility class.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class ETripleEcoreUtil {

	private static final Map<EClass, EAttribute> cacheid = new HashMap<EClass, EAttribute>();

	public static Map<String, String> decodeQueryString(String qryStr) {
		final TreeMap<String, String> result = new TreeMap<String, String>();

		if (qryStr == null) {
			return result;
		}

		final String[] qryParts = qryStr.split("&");
		for (final String qryPart : qryParts) {
			final String fieldName = qryPart.substring(0, qryPart.indexOf('=')).trim();
			final String fieldValue = qryPart.substring(qryPart.indexOf('=') + 1).trim();
			result.put(fieldName, fieldValue);
		}

		return result;
	}
	
	public static URI getID(EObject object) {
		String key = null;
		
		if (object.eIsProxy()) {
			URI uri = ((InternalEObject)object).eProxyURI();
			if (uri.hasFragment()) {
				key = getProxyKey(uri.fragment());
			}
		}

		if (key == null) {
			key = EObjectID.Registry.INSTANCE.createAndStoreObjectID(object);
		}

		return URI.createURI(key);
	}
	
	private static String getProxyKey(String uriFragment) {
		if (uriFragment.startsWith("uri=")) {
			return uriFragment.split("=")[1].replaceAll("%23", "#");
		} else  return null;
	}
	
	public static EAnnotation getETripleAnnotation(EModelElement element, String name) {
		EAnnotation ann = element.getEAnnotation(name);
		if (ann != null)
		{
			return ann;
		}
		return element.getEAnnotation("etriple." + name);
	}

	public static EAttribute getID(EClass eClass) {
		if (cacheid.containsKey(eClass)) {
			return cacheid.get(eClass);
		}

		EAttribute theId = eClass.getEIDAttribute();
		if (theId != null) {
			cacheid.put(eClass, theId);
			return theId;
		}

		theId = getID(eClass, eClass.getEAttributes());
		if (theId == null) {
			theId = getID(eClass, eClass.getEAllAttributes());
		}

		return theId;
	}

	private static EAttribute getID(EClass eClass, EList<EAttribute> attributes) {
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

	public static boolean isBlankNode(EObject object) {
		if (object.eClass().getEAnnotation("BlankNode") != null) {
			return true;
		} else {
			if (object.eContainer() != null) {
				return object.eContainer().eClass().getEAnnotation("BlankNode") != null;
			}
		}
		return false;
	}
	
	// TODO check if the super type has blank node annotation.
	public static boolean isBlankNode(EStructuralFeature feature) {
		if (feature.getEAnnotation("BlankNode") != null) {
			return true;
		}
		return feature.getEType().getEAnnotation("BlankNode") != null;
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
