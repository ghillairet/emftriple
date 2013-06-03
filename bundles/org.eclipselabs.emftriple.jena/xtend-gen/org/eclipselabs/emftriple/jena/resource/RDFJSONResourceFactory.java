package org.eclipselabs.emftriple.jena.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.jena.resource.RDFJSONResource;
import org.eclipselabs.emftriple.jena.resource.RDFResourceFactory;

@SuppressWarnings("all")
public class RDFJSONResourceFactory extends RDFResourceFactory {
  public Resource createResource(final URI uri) {
    RDFJSONResource _rDFJSONResource = new RDFJSONResource(uri);
    return _rDFJSONResource;
  }
}
