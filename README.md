RDF Binding for EMF
---

## Installation


## Getting Started

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());

ResourceSet resourceSet = new ResourceSetImpl();
Resource r = resourceSet.createResource(URI.createURI("my.rdf"));

Book b = ModelFactory.eINSTANCE.createBook();
b.setTitle("The Book");
b.getTags().add("SciFI");
b.getTags().add("Fantasy");

r.getContents().add(b);

r.save(null);
```

## Development


