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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * 
 * @author ghilla01
 *
 */
public class URIBuilder {
	
	public static URI build(Resource resource, URI key) {
		final String fragment = "uri=" + key.toString().replaceAll("#", "%23");
		if (!fragment.startsWith("uri=http://")) {
			throw new IllegalArgumentException();
		}
		final URI uriFragment = resource.getURI().appendFragment(fragment);
		
		return uriFragment;
	}
}