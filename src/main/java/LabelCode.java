/**
 * Created by andrewmehlberg on 4/3/15.
 */
public class LabelCode extends IntermediateCode {

    private String label;

    public LabelCode(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }

    public String getLabel() {
        return label;
    }
}
