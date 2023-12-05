package utilities;

import java.util.ArrayList;
import java.util.List;

public class LineInfo {
    private String fileName;
    private List<Integer> lineNumbers;

    public LineInfo(String fileName) {
        this.fileName = fileName;
        this.lineNumbers = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public List<Integer> getLineNumbers() {
        return lineNumbers;
    }

    public void addLineNumber(int lineNumber) {
        lineNumbers.add(lineNumber);
    }
}
