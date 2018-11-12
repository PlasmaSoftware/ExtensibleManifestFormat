# ExtensibleManifestFormat
The reference implementation for the XMF markup language.

## What is this, exactly?
The XMF file format specifies a declarative and fairly simple language designed for "manifests". It is designed to be easily 
modified in order to be applied many different cases. The best way to think of these files are to compare them to 
[Docker's](https://www.docker.com/) `Dockerfile`s as they serve very similar purposes. The main differences are that XMFs are
entirely customizable, and have the ability to interact with and manipulate variables.

## Example .xmf File
```
IMPORT path/to/another/file.xmf  # Extend xmfs or simply import their defined functions
IMPORT classpath:/xmf/utils.xmf  # We bundle some optional utilities for conveinence
DEFINE MyVerb some.identifier.MyVerb  # This hooks your implementation of MyVerb to the current .xmf file

MyVerb blah blah  # Interact with verbs

$MY_VARIABLE<-yes, this is my variable  # Declare a variable

PRINT $MY_VARIABLE  # Prints the contents of $MY_VARIABLE to stdout
```

## Want more info?
Check out the XMF file specifications [here](spec/) or view some example (and builtin) xmf files [here](src/main/resources/xmf)
