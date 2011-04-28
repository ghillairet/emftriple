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

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.emftriple.datasources.IDataSource;
import com.emftriple.resource.ETripleResourceImpl;
import com.emftriple.transform.IGetObject;
import com.emftriple.util.ETripleEcoreUtil;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public abstract class AbstractGetObject implements IGetObject {

	protected final ETripleResourceImpl resource;

//	protected final ETripleCache cache;

	protected final IDataSource dataSource;
	
	protected AbstractGetObject(ETripleResourceImpl resource) {
		this.resource = resource;
//		this.cache = this.resource.getPrimaryCache();
		this.dataSource = this.resource.getDataSource();
	}

	protected void setIdValue(EObject returnedObject, String key, EAttribute id) {
		if (id == null)
			return;

		EAnnotation ann = ETripleEcoreUtil.getETripleAnnotation(id, "Id");

		if (ann == null) {
			returnedObject.eSet(id, key);
			return;
		}

		if (ann.getDetails().containsKey("base")) {
			String base = ann.getDetails().get("base");
			if (key.startsWith(base)) {
				String localName = key.substring(base.length(), key.length());
				if (localName != null && localName.length() > 0)
					returnedObject.eSet(id, EcoreUtil.createFromString((EDataType) id.getEType(), localName));
			}
		}
	}

}
