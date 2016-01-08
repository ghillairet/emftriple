RDF Binding for EMF
---

EMFTriple is an RDF binding for EMF that will help you persist your EMF models in RDF instead of XMI. EMFTriple can work on
regular RDF files and RDF data stores such as the various Sesame implementations (SailRepository) and Jena TDB.

EMFTriple comes with two independent implementation, the first makes use of Jena, the second of Sesame. Both
implementations provide basic support for reading and writing RDF files in different formats (RDF/XML, Turtle, N-Triples).
The Sesame implementation can also work over any SailRepository implementations, while the Jena one
can also work over the TDB store.

## Installation

Current version: 1.0.0

Releases:

 - Update Site: http://ghillairet.github.com/p2 (Composite site with Jena and Sesame OSGI Bunbles)

Snapshots:

 - Update Site: http://ghillairet.github.com/p2/emftriple/snapshots

## Getting Started

Register the desired `Resource.Factory`, e.g. `RDFResourceFactory` for generating RDF/XML files. Registering a `Resource.Factory` is the only thing you have to do to work with RDF.

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
```

Create your resources and fill it with some objects.

```java
ResourceSet resourceSet = new ResourceSetImpl();
Resource r = resourceSet.createResource(URI.createURI("my.rdf"));

Book b = ModelFactory.eINSTANCE.createBook();
b.setTitle("The Book");
b.getTags().add("SciFI");
b.getTags().add("Fantasy");

r.getContents().add(b);
```

Save it.

```java
r.save(null);
```

Resulting RDF/XML document:

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:j.0="http://www.eclipselabs.org/emf/junit#//"
    xmlns:j.1="http://www.eclipselabs.org/emf/junit#//Book/" >
  <rdf:Description rdf:about="http://my.rdf#/">
    <j.1:tags>Fantasy</j.1:tags>
    <j.1:tags>SciFI</j.1:tags>
    <j.1:title>The Book</j.1:title>
    <rdf:type rdf:resource="http://www.eclipselabs.org/emf/junit#//Book"/>
  </rdf:Description>
</rdf:RDF>
```

## Resource Factories

Jena:

* `org.eclipselabs.emftriple.jena.resource.RDFResourceFactory`     -> RDF/XML
* `org.eclipselabs.emftriple.jena.resource.TTLResourceFactory`     -> Turtle
* `org.eclipselabs.emftriple.jena.resource.RDFJSONResourceFactory` -> RDF/JSON

Sesame:

* `org.eclipselabs.emftriple.sesame.resource.RDFResourceFactory`     -> RDF/XML
* `org.eclipselabs.emftriple.sesame.resource.TTLResourceFactory`    -> Turtle

## RDF Stores

When using emftriple on top of an RDF store, you need to use a specific URIHandler that will
tell the EMF Resource framework how the Resource can be loaded and saved.
Also in that case, a Resource will be map to an RDF named graph, with the Resource URI being the named
graph URI or identifier. This will allow a clear storage of EMF models into your RDF store and make it easy
to retrieve them.

### Jena TDB

Initialize the TDB dataset.

```java
Dataset dataset = TDBFactory.createDataset();
```

Register the RDF Resource.Factory and create a new ResourceSet.

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
ResourceSet resourceSet = new ResourceSetImpl();
```
Add to the resourceSet the URIHandler specific to TDB with the required dataset.

```java
resourceSet.getURIConverter().getURIHandlers().add(0, new TDBHandler(dataset));
```

Create a resource as usual, this one will be saved or loaded from the named graph http://my.rdf in
the dataset.

```java
Resource r = resourceSet.createResource(URI.createURI("http://my.rdf"));
```

### Sesame SailRepository

Initialize a SailRepository.

```java
Repository repo = new SailRepository(new MemoryStore());
repo.initialize();
```

Register the RDF Resource.Factory and create a new ResourceSet.

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
ResourceSet resourceSet = new ResourceSetImpl();
```

Add to the resourceSet the URIHandler specific to SailRepository with the required repository.

```java
resourceSet.getURIConverter().getURIHandlers().add(0, new RepositoryHandler(repo));
```

Create a resource as usual, this one will be saved or loaded from the named graph http://my.rdf in
the repository.

```java
Resource r = resourceSet.createResource(URI.createURI("http://my.rdf"));
```

## Using from Maven

1. Get the source code:

    ```
    git clone https://github.com/ghillairet/emftriple.git
    ```

2. Import the root project in Eclipse using Import Maven Projects of the [m2e plugin](http://www.eclipse.org/m2e/).

3. Compile and generate update site:

    ```
    mvn clean install
    ```

4. Add the required Maven dependency:

    ```xml
    <!-- EMFTriple Jena -->
    <dependency>
        <groupId>org.eclipselabs</groupId>
        <artifactId>org.eclipselabs.emftriple.jena</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>

    <!-- EMFTriple Sesame -->
    <dependency>
        <groupId>org.eclipselabs</groupId>
        <artifactId>org.eclipselabs.emftriple.sesame</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```

## License
This software is distributed under the terms of the Eclipse Public License 1.0 - http://www.eclipse.org/legal/epl-v10.html.
