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

import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

/**
 * Specific {@link ResourceFactoryImpl} for {@link ETripleResource}. This class needs to be extended 
 * by specific implementation dependent to accessible RDF stores.
 * 
 * @author guillaume hillairet
 * @since 0.8.0
 */
public abstract class ETripleResourceFactoryImpl extends ResourceFactoryImpl {
	
}
