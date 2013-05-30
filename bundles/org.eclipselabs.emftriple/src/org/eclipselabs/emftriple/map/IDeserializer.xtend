package org.eclipselabs.emftriple.map

import org.eclipse.emf.ecore.resource.Resource

interface IDeserializer<T> {
	
	def void from(T graph, Resource resource)

}