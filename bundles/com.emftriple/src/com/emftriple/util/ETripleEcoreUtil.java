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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * {@link ETripleEcoreUtil}
 * 
 * Utility Class for EMFTriple.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public class ETripleEcoreUtil {

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
	
	public static Set<Resource> createSetByType(Collection<?> objects, EClass type, Resource.Factory factory) {
		final Set<Resource> resources = new HashSet<Resource>();

		for (Object eObject : EcoreUtil.getObjectsByType(objects, type)) {	
			Resource aResource = factory.createResource(URI.createURI("dummy:/query.sparql"));
			EObject copyObject = EcoreUtil.copy((EObject) eObject);
			aResource.getContents().add( copyObject );
			
			resources.add(aResource);
		}
		return resources;
	}

	public static Collection<EObject> filterRootObjects(Collection<EObject> collection) {
		final Collection<EObject> roots = new ArrayList<EObject>();
		for (EObject obj: collection) {
			if (obj.eContainer() == null) {
				roots.add(obj);
			}
		}
		return roots;
	}

	public static <T> List<T> newListOf(EList<T> first, EList<T> second) {
		final List<T> all = new ArrayList<T>();
		all.addAll(first);
		all.addAll(second);
		return all;
	}

	@SuppressWarnings("unchecked")
	public static  <T> EList<T> filter(EList<? extends EObject> eObjects, Class<T> aClass) {
		final EList<T> list = new BasicEList<T>();
		for (EObject eObject: eObjects) 
		{
			if (aClass.isInstance(eObject)) 
			{
				list.add((T) eObject);
			}
		}
		return list;
	}

//	public static EList<EObject> collect(String method, Collection<Object> values) {
//		EList<EObject> ret = new BasicEList<EObject>();
//		for (Object obj: values)
//		{
//			try {
//				Method m = obj.getClass().getMethod(method, new Class<?>[]{});
//				Object val = m.invoke(obj, new Object[]{});
//				if (val instanceof EObject)
//				{
//					ret.add((EObject) val);
//				}
//			} catch (SecurityException e) {
//				e.printStackTrace();
//			} catch (NoSuchMethodException e) {
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
//		}
//		return ret;
//	}

}
