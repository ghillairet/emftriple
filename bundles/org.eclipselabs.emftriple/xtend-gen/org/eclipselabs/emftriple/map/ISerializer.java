package org.eclipselabs.emftriple.map;

import org.eclipse.emf.ecore.resource.Resource;

@SuppressWarnings("all")
public interface ISerializer<T extends Object> {
  public abstract T to(final Resource resource, final T graph);
}
