This project was built using Maven. To build, run Maven's compile target. This will install project dependencies, such as
Antlr, and generate java code based on the grammar file. To run the project, add the folder src/target/classes to your classpath,
as well as the Maven dependencies installed in the previous step. Then, run the Main class, passing in the name of the tiger file
you want to parse as an argument.

The grammar file can be found in /src/main/antlr3/tiger/ and is labeled Tiger.g
The generated parser and lexer files (TigerLexer.java and TigerParser.java) are located in /src/main/antlr3/tiger/output/
Our Custom Parser file (CustomTigerParser.java) and Main program (Main.java) are both located in /src/main/java/

The test Tiger programs are located in /src/main/resources/ and are labeled large-program.tiger and sample-program.tiger
The outcome of both of these programs is a successful parse.
The ASTs generated for each are located in the root folder and are labeled sample-program-ast.pdf and large-tiger-program-ast.pdf.