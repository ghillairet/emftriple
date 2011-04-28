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
package com.emftriple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.emftriple.util.Functions;
import com.google.common.base.Function;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * ETriple.
 * 
 * @since 0.7.0
 */
public class ETriple {

	/** The INSTANCE. */
	private static ETriple INSTANCE;
	
	/** The modules. */
	private static List<Module> modules = new ArrayList<Module>();

	/**
	 * Instantiates a new e triple.
	 */
	private ETriple() {}

	/**
	 * Gets the single instance of ETriple.
	 *
	 * @return single instance of ETriple
	 */
	public static ETriple getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ETriple();
		}
		return INSTANCE;
	}
	
	/**
	 * Inits the.
	 *
	 * @param modules the modules
	 */
	public static synchronized void init(Module... modules) {
		ETriple.modules.addAll(Arrays.asList(modules));
	}

	/**
	 * Inject.
	 *
	 * @param module the module
	 * @return the injector
	 */
	public static Injector inject(Module module) {
		return Guice.createInjector(module);
	}
	
	/**
	 * Gets the.
	 *
	 * @param aClass the a class
	 * @return the module
	 */
	public static Module get(Class<? extends Module> aClass) {
		return Functions.transform(aClass, new ETriple.ModuleFinder());
	}
	
	/**
	 * The Class ModuleFinder.
	 */
	private static class ModuleFinder implements Function<Class<? extends Module>, Module> {

		/**
		 * Instantiates a new module finder.
		 */
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
