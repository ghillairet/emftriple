package org.eclipselabs.emftriple.jena.map;

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
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
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipselabs.emftriple.jena.map.Extensions;
import org.eclipselabs.emftriple.vocabularies.RDF;

@SuppressWarnings("all")
public class Deserializer {
  @Extension
  private Extensions _extensions = new Function0<Extensions>() {
    public Extensions apply() {
      Extensions _extensions = new Extensions();
      return _extensions;
    }
  }.apply();
  
  public void from(final Model graph, final Resource resource) {
    final ResourceSet resourceSet = resource.getResourceSet();
    final EList<EObject> contents = resource.getContents();
    final Map<com.hp.hpl.jena.rdf.model.Resource,EObject> mapOfObjects = CollectionLiterals.<com.hp.hpl.jena.rdf.model.Resource, EObject>newHashMap();
    final List<EObject> listOfObjects = CollectionLiterals.<EObject>newArrayList();
    ResIterator _listSubjects = graph.listSubjects();
    final Function2<List<EObject>,com.hp.hpl.jena.rdf.model.Resource,List<EObject>> _function = new Function2<List<EObject>,com.hp.hpl.jena.rdf.model.Resource,List<EObject>>() {
        public List<EObject> apply(final List<EObject> list, final com.hp.hpl.jena.rdf.model.Resource it) {
          EObject _from = Deserializer.this.from(it, mapOfObjects, resourceSet);
          List<EObject> _appendTo = Deserializer.this.appendTo(_from, list);
          return _appendTo;
        }
      };
    IteratorExtensions.<com.hp.hpl.jena.rdf.model.Resource, List<EObject>>fold(_listSubjects, listOfObjects, _function);
    Set<com.hp.hpl.jena.rdf.model.Resource> _keySet = mapOfObjects.keySet();
    final Procedure1<com.hp.hpl.jena.rdf.model.Resource> _function_1 = new Procedure1<com.hp.hpl.jena.rdf.model.Resource>() {
        public void apply(final com.hp.hpl.jena.rdf.model.Resource it) {
          final com.hp.hpl.jena.rdf.model.Resource res = it;
          final EObject eObject = mapOfObjects.get(it);
          final EClass eClass = eObject.eClass();
          final EList<EReference> references = eClass.getEAllReferences();
          final Procedure1<EReference> _function = new Procedure1<EReference>() {
              public void apply(final EReference it) {
                Deserializer.this.deSerialize(it, res, eObject, mapOfObjects, resourceSet);
              }
            };
          IterableExtensions.<EReference>forEach(references, _function);
        }
      };
    IterableExtensions.<com.hp.hpl.jena.rdf.model.Resource>forEach(_keySet, _function_1);
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
  
  public EObject from(final com.hp.hpl.jena.rdf.model.Resource res, final Map<com.hp.hpl.jena.rdf.model.Resource,EObject> mapOfObjects, final ResourceSet resourceSet) {
    EObject _xblockexpression = null;
    {
      Model _model = res.getModel();
      Property _property = _model.getProperty(RDF.type);
      final Statement statement = res.getProperty(_property);
      EObject _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (statement instanceof Statement) {
          final Statement _statement = (Statement)statement;
          RDFNode _object = _statement.getObject();
          boolean _isURIResource = _object.isURIResource();
          if (_isURIResource) {
            _matched=true;
            EObject _createEObject = this.createEObject(_statement, res, mapOfObjects, resourceSet);
            _switchResult = _createEObject;
          }
        }
      }
      if (!_matched) {
        _switchResult = null;
      }
      _xblockexpression = (_switchResult);
    }
    return _xblockexpression;
  }
  
  protected List<EObject> appendTo(final EObject object, final List<EObject> objects) {
    List<EObject> _xblockexpression = null;
    {
      boolean _notEquals = (!Objects.equal(object, null));
      if (_notEquals) {
        objects.add(object);
      }
      _xblockexpression = (objects);
    }
    return _xblockexpression;
  }
  
  protected EObject createEObject(final Statement stmt, final com.hp.hpl.jena.rdf.model.Resource res, final Map<com.hp.hpl.jena.rdf.model.Resource,EObject> mapOfObjects, final ResourceSet resourceSet) {
    EObject _switchResult = null;
    RDFNode _object = stmt.getObject();
    com.hp.hpl.jena.rdf.model.Resource _asResource = _object.asResource();
    EObject _eObject = this._extensions.getEObject(resourceSet, _asResource);
    final EObject eClass = _eObject;
    boolean _matched = false;
    if (!_matched) {
      if (eClass instanceof EClass) {
        final EClass _eClass = (EClass)eClass;
        _matched=true;
        EObject _xblockexpression = null;
        {
          final EObject eObject = this._extensions.create(_eClass);
          EList<EAttribute> _eAllAttributes = _eClass.getEAllAttributes();
          final Procedure1<EAttribute> _function = new Procedure1<EAttribute>() {
              public void apply(final EAttribute it) {
                Deserializer.this.deSerialize(it, res, eObject);
              }
            };
          IterableExtensions.<EAttribute>forEach(_eAllAttributes, _function);
          mapOfObjects.put(res, eObject);
          _xblockexpression = (eObject);
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  public Object deSerialize(final EAttribute attribute, final com.hp.hpl.jena.rdf.model.Resource resource, final EObject eObject) {
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
    Model _model = resource.getModel();
    Property _property = this._extensions.getProperty(attribute, _model);
    final StmtIterator stmts = resource.listProperties(_property);
    boolean _isMany = attribute.isMany();
    if (_isMany) {
      Object _eGet = eObject.eGet(attribute);
      final Collection<Object> values = ((Collection<Object>) _eGet);
      final Procedure1<Statement> _function = new Procedure1<Statement>() {
          public void apply(final Statement it) {
            EDataType _eAttributeType = attribute.getEAttributeType();
            RDFNode _object = it.getObject();
            Literal _asLiteral = _object.asLiteral();
            String _lexicalForm = _asLiteral.getLexicalForm();
            final Object v = EcoreUtil.createFromString(_eAttributeType, _lexicalForm);
            boolean _notEquals = (!Objects.equal(v, null));
            if (_notEquals) {
              values.add(v);
            }
          }
        };
      IteratorExtensions.<Statement>forEach(stmts, _function);
    } else {
      boolean _hasNext = stmts.hasNext();
      if (_hasNext) {
        EDataType _eAttributeType = attribute.getEAttributeType();
        Statement _head = IteratorExtensions.<Statement>head(stmts);
        RDFNode _object = _head.getObject();
        Literal _asLiteral = _object.asLiteral();
        String _lexicalForm = _asLiteral.getLexicalForm();
        final Object v = EcoreUtil.createFromString(_eAttributeType, _lexicalForm);
        boolean _notEquals = (!Objects.equal(v, null));
        if (_notEquals) {
          eObject.eSet(attribute, v);
        }
      }
    }
    return null;
  }
  
  public Object deSerialize(final EReference reference, final com.hp.hpl.jena.rdf.model.Resource resource, final EObject eObject, final Map<com.hp.hpl.jena.rdf.model.Resource,EObject> mapOfObjects, final ResourceSet resourceSet) {
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
    Model _model = resource.getModel();
    Property _property = this._extensions.getProperty(reference, _model);
    final StmtIterator stmts = resource.listProperties(_property);
    boolean _isMany = reference.isMany();
    if (_isMany) {
      Object _eGet = eObject.eGet(reference);
      final Collection<Object> values = ((Collection<Object>) _eGet);
      final Procedure1<Statement> _function = new Procedure1<Statement>() {
          public void apply(final Statement it) {
            RDFNode _object = it.getObject();
            final com.hp.hpl.jena.rdf.model.Resource v = _object.asResource();
            final EObject o = mapOfObjects.get(v);
            boolean _notEquals = (!Objects.equal(o, null));
            if (_notEquals) {
              values.add(o);
            }
          }
        };
      IteratorExtensions.<Statement>forEach(stmts, _function);
    } else {
      boolean _hasNext = stmts.hasNext();
      if (_hasNext) {
        Statement _head = IteratorExtensions.<Statement>head(stmts);
        RDFNode _object = _head.getObject();
        final com.hp.hpl.jena.rdf.model.Resource v = _object.asResource();
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
