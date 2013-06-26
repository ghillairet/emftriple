package org.eclipselabs.emftriple.sesame.map;

import com.google.common.base.Objects;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipselabs.emftriple.sesame.map.Extensions;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;

@SuppressWarnings("all")
public class Deserializer {
  @Extension
  private Extensions extensions = new Function0<Extensions>() {
    public Extensions apply() {
      Extensions _extensions = new Extensions();
      return _extensions;
    }
  }.apply();
  
  public void from(final Model graph, final Resource resource) {
    final ResourceSet resourceSet = resource.getResourceSet();
    final EList<EObject> contents = resource.getContents();
    final Map<org.openrdf.model.Resource,EObject> mapOfObjects = CollectionLiterals.<org.openrdf.model.Resource, EObject>newHashMap();
    final List<EObject> listOfObjects = CollectionLiterals.<EObject>newArrayList();
    final Function2<List<EObject>,Statement,List<EObject>> _function = new Function2<List<EObject>,Statement,List<EObject>>() {
        public List<EObject> apply(final List<EObject> list, final Statement it) {
          EObject _deSerialize = Deserializer.this.deSerialize(it, graph, mapOfObjects, resourceSet);
          List<EObject> _appendTo = Deserializer.this.extensions.appendTo(_deSerialize, list);
          return _appendTo;
        }
      };
    IterableExtensions.<Statement, List<EObject>>fold(graph, listOfObjects, _function);
    Set<org.openrdf.model.Resource> _keySet = mapOfObjects.keySet();
    final Procedure1<org.openrdf.model.Resource> _function_1 = new Procedure1<org.openrdf.model.Resource>() {
        public void apply(final org.openrdf.model.Resource it) {
          final org.openrdf.model.Resource res = it;
          final EObject eObject = mapOfObjects.get(it);
          final EClass eClass = eObject.eClass();
          final EList<EReference> references = eClass.getEAllReferences();
          final Procedure1<EReference> _function = new Procedure1<EReference>() {
              public void apply(final EReference it) {
                Deserializer.this.deSerialize(it, res, eObject, graph, mapOfObjects, resourceSet);
              }
            };
          IterableExtensions.<EReference>forEach(references, _function);
        }
      };
    IterableExtensions.<org.openrdf.model.Resource>forEach(_keySet, _function_1);
    Collection<EObject> _values = mapOfObjects.values();
    final Procedure1<EObject> _function_2 = new Procedure1<EObject>() {
        public void apply(final EObject it) {
          EObject _eContainer = it.eContainer();
          boolean _equals = Objects.equal(_eContainer, null);
          if (_equals) {
            contents.add(it);
          }
        }
      };
    IterableExtensions.<EObject>forEach(_values, _function_2);
  }
  
  public EObject deSerialize(final Statement statement, final Model graph, final Map<org.openrdf.model.Resource,EObject> mapOfObjects, final ResourceSet resourceSet) {
    EObject _xblockexpression = null;
    {
      final org.openrdf.model.Resource sbj = statement.getSubject();
      boolean _containsKey = mapOfObjects.containsKey(sbj);
      boolean _not = (!_containsKey);
      if (_not) {
        final Model subModel = graph.filter(sbj, null, null);
        final Model types = subModel.filter(sbj, RDF.TYPE, null);
        EObject _switchResult = null;
        Statement _head = IterableExtensions.<Statement>head(types);
        Value _object = _head.getObject();
        EObject _eObject = this.extensions.getEObject(resourceSet, _object);
        final EObject eClass = _eObject;
        boolean _matched = false;
        if (!_matched) {
          if (eClass instanceof EClass) {
            final EClass _eClass = (EClass)eClass;
            _matched=true;
            EObject _createEObject = this.createEObject(_eClass, sbj, subModel, mapOfObjects);
            _switchResult = _createEObject;
          }
        }
        if (!_matched) {
          _switchResult = null;
        }
        return _switchResult;
      }
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
  
  public EObject createEObject(final EClass eClass, final org.openrdf.model.Resource sbj, final Model model, final Map<org.openrdf.model.Resource,EObject> mapOfObjects) {
    EObject _xblockexpression = null;
    {
      final EObject eObject = EcoreUtil.create(eClass);
      mapOfObjects.put(sbj, eObject);
      EList<EAttribute> _eAllAttributes = eClass.getEAllAttributes();
      final Procedure1<EAttribute> _function = new Procedure1<EAttribute>() {
          public void apply(final EAttribute it) {
            Deserializer.this.deSerialize(it, sbj, eObject, model);
          }
        };
      IterableExtensions.<EAttribute>forEach(_eAllAttributes, _function);
      _xblockexpression = (eObject);
    }
    return _xblockexpression;
  }
  
  public Object deSerialize(final EAttribute attribute, final org.openrdf.model.Resource sbj, final EObject eObject, final Model model) {
    boolean _or = false;
    boolean _isDerived = attribute.isDerived();
    if (_isDerived) {
      _or = true;
    } else {
      boolean _isTransient = attribute.isTransient();
      _or = (_isDerived || _isTransient);
    }
    if (_or) {
      return null;
    }
    URIImpl _uRI = this.extensions.toURI(attribute);
    final Model subModel = model.filter(sbj, _uRI, null);
    boolean _isMany = attribute.isMany();
    if (_isMany) {
      Object _eGet = eObject.eGet(attribute);
      final List<Object> value = ((List<Object>) _eGet);
      final Procedure1<Statement> _function = new Procedure1<Statement>() {
          public void apply(final Statement it) {
            EDataType _eAttributeType = attribute.getEAttributeType();
            Value _object = it.getObject();
            String _stringValue = _object.stringValue();
            Object _createFromString = EcoreUtil.createFromString(_eAttributeType, _stringValue);
            value.add(0, _createFromString);
          }
        };
      IterableExtensions.<Statement>forEach(subModel, _function);
    } else {
      int _size = subModel.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        EDataType _eAttributeType = attribute.getEAttributeType();
        Statement _head = IterableExtensions.<Statement>head(subModel);
        Value _object = _head.getObject();
        String _stringValue = _object.stringValue();
        final Object value_1 = EcoreUtil.createFromString(_eAttributeType, _stringValue);
        boolean _notEquals = (!Objects.equal(value_1, null));
        if (_notEquals) {
          eObject.eSet(attribute, value_1);
        }
      }
    }
    return null;
  }
  
  public Object deSerialize(final EReference reference, final org.openrdf.model.Resource sbj, final EObject eObject, final Model model, final Map<org.openrdf.model.Resource,EObject> mapOfObjects, final ResourceSet resourceSet) {
    boolean _or = false;
    boolean _isDerived = reference.isDerived();
    if (_isDerived) {
      _or = true;
    } else {
      boolean _isTransient = reference.isTransient();
      _or = (_isDerived || _isTransient);
    }
    if (_or) {
      return null;
    }
    URIImpl _uRI = this.extensions.toURI(reference);
    final Model subModel = model.filter(sbj, _uRI, null);
    boolean _isMany = reference.isMany();
    if (_isMany) {
      Object _eGet = eObject.eGet(reference);
      final List<Object> values = ((List<Object>) _eGet);
      final Procedure1<Statement> _function = new Procedure1<Statement>() {
          public void apply(final Statement it) {
            final Value v = it.getObject();
            final EObject o = mapOfObjects.get(v);
            boolean _notEquals = (!Objects.equal(o, null));
            if (_notEquals) {
              values.add(0, o);
            }
          }
        };
      IterableExtensions.<Statement>forEach(subModel, _function);
    } else {
      int _size = subModel.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        Statement _head = IterableExtensions.<Statement>head(subModel);
        final Value v = _head.getObject();
        final EObject o = mapOfObjects.get(v);
        boolean _notEquals = (!Objects.equal(o, null));
        if (_notEquals) {
          eObject.eSet(reference, o);
        }
      }
    }
    return null;
  }
}
