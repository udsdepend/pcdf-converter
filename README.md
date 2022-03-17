# pcdf-converter
Small tool for converting persistent to intermediate PCDF files.

## Getting started

You can either download the compiled Jar file from the [Releases page](https://github.com/udsdepend/pcdf-converter/releases) or
clone the repository to compile the tool yourself. 

### Building with Gradle 

With *Gradle*, the tool can be used by running the following command in the main directory:

```
./gradlew run --args="<args>"
```


We included a JVM compile script, hence after running:
```
./gradlew jar 
```

the directory **build/libs** contains a file named **PCDFFileConverter-&lt;VERSION&gt;.jar**. This can then be used with:

```
java -jar PCDFFileConverter-version.jar <args>
```

## Parameters
The tool supports persistent to intermediate conversion, more functionality can be added. There are two required arguments:

- **-i, --inputPath**:  *relative* path to input file to be converted
- **-o, --outputPath**: *relative* path to output file to be written to (does not have to exist, existing files will be overwritten)
- **-c, --conversion (optional)**: specifies conversion (currently only persistent to intermediate supported and set as default)
- **-p, --print**: the resulting intermediate events will get printed to the command line. 

One of **-o** or **-p** has to be given (both is also possible).

## Example
*Jar*
```
java -jar PCDFFileConverter-<VERSION>.jar -i ./input.ppcdf -o ./output.ipcdf
```

*Gradle*
```
./gradlew run --args="-i input.ppcdf -o output.ipcdf"
```
with optional conversion-paramter:
```
./gradlew run --args="-i ./input.ppcdf -o ./output.ipcdf -c p2i"
```

