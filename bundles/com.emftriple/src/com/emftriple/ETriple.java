/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EPackage;

import com.emftriple.transform.impl.EAnnotationMapping;
import com.emftriple.util.Functions;
import com.google.common.base.Function;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.internal.Lists;

/**
 * 
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class ETriple {

	private static ETriple INSTANCE;
	
	private static List<Module> modules = new ArrayList<Module>();

	public static EAnnotationMapping mapping;

	private ETriple() {}

	public static ETriple getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ETriple();
		}
		return INSTANCE;
	}

	public static synchronized void init(EPackage ePackage) {
		mapping = new EAnnotationMapping(Lists.newArrayList(ePackage));
	}
	
	public static synchronized void init(EPackage ePackage, Module... modules) {
		mapping = new EAnnotationMapping(Lists.newArrayList(ePackage));
		ETriple.modules.addAll(Arrays.asList(modules));
	}

	public static Injector inject(Module module) {
		return Guice.createInjector(module);
	}
	
	public static Module get(Class<? extends Module> aClass) {
		return Functions.transform(aClass, new ETriple.ModuleFinder());
	}
	
	private static class ModuleFinder implements Function<Class<? extends Module>, Module> {

		public ModuleFinder() {}

		@Override
		public Module apply(Class<? extends Module> aClass) {
			for (Module module: modules) {
				if (aClass.isInstance(module)) {
					return module;
				}
			}
			return null;
		}
	}

}
