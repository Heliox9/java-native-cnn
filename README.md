# CNN_Lab (2.0-rc1)

This is a gradle project which builds and manages two types of neural network implementations.

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


