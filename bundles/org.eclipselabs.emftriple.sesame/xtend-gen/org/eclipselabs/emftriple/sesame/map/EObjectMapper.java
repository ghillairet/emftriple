package org.eclipselabs.emftriple.sesame.map;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Map;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipselabs.emftriple.sesame.map.Deserializer;
import org.eclipselabs.emftriple.sesame.map.Serializer;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;

@SuppressWarnings("all")
public class EObjectMapper {
  public Model to(final Resource resource, final Map<? extends Object,? extends Object> options) {
    ArrayList<Statement> _newArrayList = CollectionLiterals.<Statement>newArrayList();
    LinkedHashModel _linkedHashModel = new LinkedHashModel(_newArrayList);
    Model _to = this.to(_linkedHashModel, resource, options);
    return _to;
  }
  
  public Model to(final Model graph, final Resource resource, final Map<? extends Object,? extends Object> options) {
    Model _xblockexpression = null;
    {
      Serializer _serializer = new Serializer();
      final Serializer serializer = _serializer;
      Model _xifexpression = null;
      boolean _equals = Objects.equal(graph, null);
      if (_equals) {
        ArrayList<Statement> _newArrayList = CollectionLiterals.<Statement>newArrayList();
        LinkedHashModel _linkedHashModel = new LinkedHashModel(_newArrayList);
        _xifexpression = _linkedHashModel;
      } else {
        _xifexpression = graph;
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
