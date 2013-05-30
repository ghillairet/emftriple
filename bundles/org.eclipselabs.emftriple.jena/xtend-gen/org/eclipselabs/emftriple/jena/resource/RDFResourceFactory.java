package org.eclipselabs.emftriple.jena.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipselabs.emftriple.jena.resource.RDFResource;

@SuppressWarnings("all")
public class RDFResourceFactory extends ResourceFactoryImpl {
  public Resource createResource(final URI uri) {
    RDFResource _rDFResource = new RDFResource(uri);
    return _rDFResource;
  }
}
