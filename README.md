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

Update Site: <http://ghillairet.github.com/p2> (Composite update site with Jena and Sesame OSGI bundles. See the [content](http://ghillairet.github.io/p2/compositeContent.xml) from your browser.)

## Getting Started

Register the desired `Resource.Factory`, e.g. `RDFResourceFactory` for generating RDF/XML files. Registering a `Resource.Factory` is the only thing you have to do to work with RDF. The `RDFResourceFactory` is implemented for both Jena and Sesame (`examples/org.eclipselabs.emftriple.examples.maven.jena` and `examples/org.eclipselabs.emftriple.examples.maven.sesame`, respectively).

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
```

Create your resources and fill it with some objects. The `Book` class is defined in the `org.eclipselabs.emftriple.junit` project.

```java
ResourceSet resourceSet = new ResourceSetImpl();
Resource r = resourceSet.createResource(URI.createURI("http://my.rdf"));

Book b = ModelFactory.eINSTANCE.createBook();
b.setTitle("The Book");
b.getTags().add("SciFI");
b.getTags().add("Fantasy");

r.getContents().add(b);
```

Save it to a stream.

```java
r.save(System.out, null);
```

The Jena version produces the following RDF/XML document:

```xml
<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:j.0="http://www.eclipselabs.org/emf/junit#//Book/"
    xmlns:j.1="http://www.eclipselabs.org/emf/junit#//" > 
  <rdf:Description rdf:about="http://my.rdf#/">
    <j.0:tags>Fantasy</j.0:tags>
    <j.0:tags>SciFI</j.0:tags>
    <j.0:title>The Book</j.0:title>
    <rdf:type rdf:resource="http://www.eclipselabs.org/emf/junit#//Book"/>
  </rdf:Description>
</rdf:RDF>
```

## Resource Factories

Jena:

* `org.eclipselabs.emftriple.jena.resource.NTResourceFactory`      -> N-Triples
* `org.eclipselabs.emftriple.jena.resource.RDFResourceFactory`     -> RDF/XML
* `org.eclipselabs.emftriple.jena.resource.RDFJSONResourceFactory` -> RDF/JSON
* `org.eclipselabs.emftriple.jena.resource.TTLResourceFactory`     -> Turtle

Sesame:

* `org.eclipselabs.emftriple.sesame.resource.NTResourceFactory`     -> N-Triples
* `org.eclipselabs.emftriple.sesame.resource.RDFResourceFactory`    -> RDF/XML
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

Register the RDF `Resource.Factory` and create a new `ResourceSet`.

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
ResourceSet resourceSet = new ResourceSetImpl();
```
Add to the `resourceSet` the `URIHandler` specific to TDB with the required dataset.

```java
resourceSet.getURIConverter().getURIHandlers().add(0, new TDBHandler(dataset));
```

Create a resource as usual, this one will be saved or loaded from the named graph `http://my.rdf` in
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

Register the RDF `Resource.Factory` and create a new `ResourceSet`.

```java
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new RDFResourceFactory());
ResourceSet resourceSet = new ResourceSetImpl();
```

Add to the `resourceSet` the `URIHandler` specific to `SailRepository` with the required repository.

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

3. Compile and generate update site.

    ```
    mvn clean install
    ```

4. Add the required Maven dependencies. Note that [Tycho dependencies from Maven are not transitive](http://wiki.eclipse.org/Tycho/How_Tos/Dependency_on_pom-first_artifacts#Pom-first_dependencies_of_manifest-first_projects_are_not_fully_transitive), so you have to explicitly add the the dependencies of EMFTriple as well.

    For Sesame:

    ```xml
    <dependency>
        <groupId>org.eclipselabs</groupId>
        <artifactId>org.eclipselabs.emftriple.sesame</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.13</version>
    </dependency>

    <!-- for using a Sail Repository -->

    <dependency>
        <groupId>org.openrdf.sesame</groupId>
        <artifactId>sesame-repository-sail</artifactId>
        <version>2.8.8</version>
    </dependency>
    ```

    For Jena:
    
    ```xml
    <dependency>
        <groupId>org.eclipselabs</groupId>
        <artifactId>org.eclipselabs.emftriple.jena</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.13</version>
    </dependency>
    ```
    
    The example metamodel is available in the `org.eclipselabs.emftriple.junit` project:
    
    ```xml
    <dependency>
        <groupId>org.eclipselabs</groupId>
        <artifactId>org.eclipselabs.emftriple.junit</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
    
## License
This software is distributed under the terms of the Eclipse Public License 1.0 - http://www.eclipse.org/legal/epl-v10.html.
