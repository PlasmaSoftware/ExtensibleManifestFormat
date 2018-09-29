# Magic v1.0
These are elements built into the format specification so they have no defined basis in a xmf
hierarchy.

## Magic Verbs
These are verbs built into the format specification and form the basis of the format's utility.

* `IMPORT`: The clause is expected to be either a direct link to a xmf file or a file path to a 
directory containing xmf files or a single xmf filepath.
* `DEFINE`: The clause is expected to be two words. The first being the name of a new verb
being defined, followed by a non whitespace-containing string representing the address to bind
verb logic to. This should generally follow a package.ClassName format. Note that this requires
the logic to be present in the parsers classpath. Note that pre-existing verbs CANNOT be rebound.

## Magic Macros
These are macros which are built into the format specification as utilities.
* `$CWD`: The current working directory

## Implicit Imports
All xmf files implicitly import from the base.xmf file which is distributed with the parser. The
current version of that file is found in the `xmf` directory in the root of this repository.
