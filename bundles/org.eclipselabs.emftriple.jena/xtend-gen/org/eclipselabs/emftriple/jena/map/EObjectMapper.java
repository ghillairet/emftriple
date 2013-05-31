package org.eclipselabs.emftriple.jena.map;

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.util.Map;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.jena.map.Deserializer;
import org.eclipselabs.emftriple.jena.map.Serializer;

@SuppressWarnings("all")
public class EObjectMapper {
  public Model to(final Resource resource, final Map<? extends Object,? extends Object> options) {
    Model _createDefaultModel = ModelFactory.createDefaultModel();
    Model _to = this.to(_createDefaultModel, resource, options);
    return _to;
  }
  
  public Model to(final Model model, final Resource resource, final Map<? extends Object,? extends Object> options) {
    Model _xblockexpression = null;
    {
      Serializer _serializer = new Serializer();
      final Serializer serializer = _serializer;
      Model _xifexpression = null;
      boolean _equals = Objects.equal(model, null);
      if (_equals) {
        Model _createDefaultModel = ModelFactory.createDefaultModel();
        _xifexpression = _createDefaultModel;
      } else {
        _xifexpression = model;
      }
      Model _to = serializer.to(resource, _xifexpression);
      _xblockexpression = (_to);
    }
    return _xblockexpression;
  }
  
  public void from(final Model graph, final Resource resource, final Map<? extends Object,? extends Object> options) {
    Deserializer _deserializer = new Deserializer();
    final Deserializer deserializer = _deserializer;
    deserializer.from(graph, resource);
  }
}
