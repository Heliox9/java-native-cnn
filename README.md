# CNN_Lab (2.0-rc1)

This is a gradle project which builds and manages two types of neural network implementations.
It was created as part of a university project with the aim of building a more understandable CNN implementation for future students to use.
The custom implementation is written purely in java and only uses the apache commons library to implement activation functions.

Execution starts a CLI which can configure, train and use both CNN implementations.

## Core Tasks
* run: executes the project
* build: builds for production
* jar: builds executable jar
* test: executes unit tests

## Components
### Custom
This is a native java implementation of a CNN.
Its purpose is to help understand a CNN through a fully debuggable implementation.
Can be used on datasets as complex as MNIST.

In order to use this implementation a specific form of dataset configuration is needed. (logic gate sample provided)

### DL4J
This is a Java implementation using the DL4J-library.
It is used to classify more complex datasets while maintaining a similar customization.
This implementation is designed and tested with the SVHN-dataset.


