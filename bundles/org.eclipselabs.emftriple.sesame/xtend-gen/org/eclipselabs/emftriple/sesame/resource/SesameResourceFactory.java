package org.eclipselabs.emftriple.sesame.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipselabs.emftriple.sesame.resource.SesameResource;

@SuppressWarnings("all")
public class SesameResourceFactory extends ResourceFactoryImpl {
  public Resource createResource(final URI uri) {
    SesameResource _sesameResource = new SesameResource(uri);
    return _sesameResource;
  }
}
