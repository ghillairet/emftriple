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
package org.eclipselabs.emftriple;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class StoreOptionsRegistry extends HashMap<String, Map<String, Object>> {
	
	private static final long serialVersionUID = 4347150098286440135L;
	
	public static final StoreOptionsRegistry INSTANCE = new StoreOptionsRegistry();
	
	StoreOptionsRegistry() {}
	
}
