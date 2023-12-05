package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import referenceBasedTreeImplementation.BSTreeNode;

public class WordNode extends BSTreeNode<String> implements Comparable<WordNode>, Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<LineInfo> lineInfoList;

    public WordNode(String word) {
        super(word);
        lineInfoList = new ArrayList<>();
    }

    @Override
    public void addLineInfo(String fileName, int lineNumber) {
        // Check if the file name already exists in lineInfoList
        for (LineInfo lineInfo : lineInfoList) {
            if (lineInfo.getFileName().equals(fileName)) {
                // File name exists, add the line number to the existing entry
                lineInfo.addLineNumber(lineNumber);
                return;
            }
        }

        // File name doesn't exist, create a new entry
        LineInfo newLineInfo = new LineInfo(fileName);
        newLineInfo.addLineNumber(lineNumber);
        lineInfoList.add(newLineInfo);
    }
    
    public List<LineInfo> getLineInfoList() {
        return lineInfoList;
    }

	@Override
	public int compareTo(WordNode o) {
		return this.getData().compareTo(o.getData());
	}
	
}
