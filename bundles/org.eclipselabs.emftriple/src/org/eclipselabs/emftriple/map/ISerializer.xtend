package org.eclipselabs.emftriple.map

import org.eclipse.emf.ecore.resource.Resource

interface ISerializer<T> {

	def T to(Resource resource, T graph)

}