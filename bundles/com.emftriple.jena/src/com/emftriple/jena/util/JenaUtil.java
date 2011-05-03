package com.emftriple.jena.util;

import java.util.Iterator;

import com.hp.hpl.jena.query.Dataset;

public class JenaUtil {
	public static Iterable<String> getNamedGraphs(final Dataset dataSet) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return dataSet.listNames();
			}
		};
	}
}
