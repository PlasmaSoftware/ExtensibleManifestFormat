# Syntactical Elements v1.0

This is a very simple manifest format based on a Dockerfile-like syntax. It has
the goal of being very flexible, so very little elements are actually statically
defined. 

## Basic Concepts
There are four main types of elements in any xmf file. 
1. **Macro**: These are essentially equivalent to `#define` statements in C.
2. **Verb**: These are used to perform an action which may or may not have parameters.
3. **Clause**: These are passed to verbs in order to modify verb invocations.
4. **Comments**: These are stripped and ignored by the parser.

Macros are for simple preprocessing via find and replace before the xmf file is processed so
they may be placed anywhere in a manifest file. But, all other parts of the file follow the 
format of a list of verbs which are each followed by a clause. 

## Syntax Rules
The syntax rules are very simple. 

### Escaped Characters
This format does not support arbitrary escaping of characters. Instead it is limited to a few 
special cases. Currently these are:
* `\n`: Translated to a newline character.
* `\#`: Translated to a `#` character. This is useful for escaping comments.

### Comments
Comments are prefixed by the `#` character. Any remaining portion of the line will be totally
ignored by the parser. However these are ignored if they are within a quoted string block. 

### Blocks
Blocks are a term used to describe a multi-word/multi-line string which are parsed as a single
element in the script. These are used for macro assignment and clause definitions. Blocks are 
defined in one of two ways:
1. **Whitespace prefixed lines**. Blocks are implicitly defined if lines after the first line
are prefixed with any kind of whitespace. The parser will then interpret it as a string literal
with the whitespace prefixes stripped and newline characters are stripped. 
2. **Quoted string**. Blocks can be explicitly defined by being surrounded by either single 
quotes (') or double quotes ("). In this case, ALL contents in between the quotes are taken
as literals so the parser will NOT attempt to parse escaped characters or comments. 

### Macros
Macros are defined and accessed using a syntax similar to bash variables. Pointing to a macro 
is done with the form `$NAME` where `NAME` is a string with no spaces. Assignment of these 
macros are done in the form of `$NAME<-BLOCK` where `NAME` is a string with no spaces and 
`BLOCK` is a valid block as defined above. Note that whitespace is NOT allowed between the 
`$NAME`, `<-` and `BLOCK` portions as the parser will begin reading the block directly after
the `-` character.

### Verbs and Clauses
Actions are performed by listing a variety of verbs and clauses. These are in the form
`VERB BLOCK` where `VERB` is a valid verb and `BLOCK` is a valid block. Note that there 
cannot be any whitespace before the verb and each verb are required to have their own lines. 
It should also be noted that verbs are NOT case sensitive.
