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
package com.emftriple.sesame.mem;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import com.emftriple.resource.ETripleResourceFactoryImpl;

public class MemoryResourceFactory extends ETripleResourceFactoryImpl {

	@Override
	public Resource createResource(URI uri) {
		return new MemoryResourceImpl(uri);
	}

}
