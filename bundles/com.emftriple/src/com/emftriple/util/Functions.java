/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.util;

import java.util.Collection;

import com.google.common.base.Function;

/**
 * Utility class.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class Functions {

	public static <E, T> T transform(E from, Function<E, T> function) {
		return function.apply(from);
	}
	
	public static <T> boolean exists(T from, Collection<? extends T> collection) {
		for (T object: collection)
		{
			if (object.equals(from))
			{
				return true;
			}
		}
		return false;
	}
	
	public static <T> T get(T from, Collection<? extends T> collection) {
		for (T object: collection)
		{
			if (object.equals(from))
			{
				return object;
			}
		}
		return null;
	}
	
}
