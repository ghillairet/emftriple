package org.eclipselabs.emftriple.map;

import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("all")
public interface IDeserializer<T extends Object> {
  public abstract void from(final T graph, final Resource resource);
}
