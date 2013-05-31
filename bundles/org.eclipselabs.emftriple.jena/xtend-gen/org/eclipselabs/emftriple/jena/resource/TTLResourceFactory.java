package org.eclipselabs.emftriple.jena.resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.jena.resource.RDFResourceFactory;
import org.eclipselabs.emftriple.jena.resource.TTLResource;

@SuppressWarnings("all")
public class TTLResourceFactory extends RDFResourceFactory {
  public Resource createResource(final URI uri) {
    TTLResource _tTLResource = new TTLResource(uri);
    return _tTLResource;
  }
}
