public class Util {
    public static String indentBlock(String block, int indentation) {
        String indented = "";
        String whiteSpace = "";
        String[] lines = block.split("\n");
        int i;

        for (i = 0; i < indentation; i ++) {
            whiteSpace += "\t";
        }

        for (i = 0; i < lines.length; i ++) {
            indented += whiteSpace + lines[i] + "\n";
        }

        return indented;
    }
}
