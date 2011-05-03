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
package com.emftriple.resource;

import static com.emftriple.util.ETripleEcoreUtil.getETripleAnnotation;

import java.util.UUID;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.emftriple.util.ETripleEcoreUtil;

final class EObjectID {

	private static final String ID = "Id";

	public static final String BASE = "base";

	/**
	 * Return the value of object id.
	 */
	public static String getId(EObject object) {
		final EAttribute attrId = ETripleEcoreUtil.getId(object.eClass());
		if (isURI(attrId)) {
			return valueOf(object, attrId);
		} else {
			return processId(object, attrId);
		} 
	}

	private static boolean isURI(final EAttribute id) {
		return (id != null && id.getName() != null) ? id.getName().equals("URI") : false;
	}

	/**
	 * Return the value of object id.
	 */
	private static String processId(EObject object, EAttribute id) throws IllegalArgumentException {
		if (id == null) {
			return generateID(object);
		}

		String value = null;
		final EAnnotation eAnnotation = getETripleAnnotation(id, ID);
		final Boolean annotatedId = eAnnotation != null;
		final Boolean hasBase = annotatedId ? eAnnotation.getDetails().containsKey(BASE) : false;
		final Boolean conExpr = hasBase ? containsExpr(eAnnotation) : false;

		if (!annotatedId) {
			value = ETripleEcoreUtil.namespace(object) + valueOf(object, id);
		} else if (hasBase) {
			final String base;
			if (conExpr) {
				base = new IdParser(object, object.eClass()).parse(eAnnotation.getDetails().get(BASE));
			} 
			else {
				base = eAnnotation.getDetails().get(BASE);
			}
			Object val = valueOf(object, id);
			value = val == null ? null : 
				new ETripleEcoreUtil.URIValidator().apply(base + val.toString());
		}

		return value;
	}

	private static String generateID(EObject object) {
		String ns = object.eClass().getEPackage().getNsURI();
		if (ns.endsWith("#"))
			ns = ns.substring(ns.lastIndexOf("#"));
		if (!ns.endsWith("/"))
			ns = ns+"/";
		ns+=object.eClass().getName().toLowerCase()+"/";
		return ns + UUID.randomUUID().toString();
	}

	private static String valueOf(EObject object, EAttribute attr) { 
		return object.eGet(attr).toString().replaceAll(" ", "_"); 
	}

	private static boolean containsExpr(EAnnotation eAnnotation) {
		return eAnnotation.getDetails().containsKey(BASE) ? 
				eAnnotation.getDetails().get(BASE).indexOf("[") > -1 : false;
	}

	private static class IdParser {
		final EClass eClass;
		private EObject eObject;
		final String[] properties;

		IdParser(EObject eObject, EClass eClass) {
			this.eClass = eClass;
			this.eObject = eObject;
			this.properties = properties(eClass);
		}

		String[] properties(EClass eClass) {
			String[] props = new String[20];
			int i = 0;
			for (EStructuralFeature feature: eClass.getEAllStructuralFeatures())
			{
				if (i < props.length) {
					props[i++] = feature.getName();
				}
			}
			return props;
		}

		public String parse(String from) {
			Pattern pattern = Pattern.compile("\\[");

			if (pattern.matcher(from).find()) 
			{
				for (String str: properties)
				{
					if (str != null) 
					{
						pattern = Pattern.compile("\\[" + str + "\\]");
						if (pattern.matcher(from).find())
						{
							from = from.replaceAll("\\[" + str + "\\]", 
									eObject.eGet(eClass.getEStructuralFeature(str)).toString().toLowerCase());		
						}
						else
						{
							pattern = Pattern.compile("\\[" + str.toLowerCase() + "\\]");
							if (pattern.matcher(from).find())
							{
								from = from.replaceAll("\\[" + str.toLowerCase() + "\\]", 
										eObject.eGet(eClass.getEStructuralFeature(str)).toString().toLowerCase());
							}
						}
					}
				}
				from = from.replaceAll("\\[Class\\]", eClass.getName().toLowerCase());
			}

			return from;
		}
	}

}