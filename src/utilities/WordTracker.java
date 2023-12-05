package utilities;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

import exceptions.TreeException;
import referenceBasedTreeImplementation.BSTreeNode;

public class WordTracker {
    private BSTree<WordNode> wordTree;

    public WordTracker() {
        wordTree = new BSTree<>();
    }
    
    private void restoreTree() {
        // Check if the repository file exists and restore the tree if it does
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("repository.ser"))) {
            wordTree = (BSTree<WordNode>) ois.readObject();
            System.out.println("Tree restored from repository.ser");
        } catch (IOException | ClassNotFoundException e) {
            // If the file does not exist or there is an issue with deserialization, ignore and create a new tree
        }
    }
    
    
    
    public void processFile(String fileName) throws TreeException {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int lineNumber = 1;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().toLowerCase(); // Convert to lowercase for case-insensitive comparison
                String[] words = line.split("\\s+"); // Split by whitespace

                for (String word : words) {
                    // Remove punctuation from the word (you might need more sophisticated handling)
                    word = word.replaceAll("[^a-zA-Z0-9]", "");

                    if (!word.isEmpty()) {
                        BSTreeNode<WordNode> existingNode = wordTree.search(new WordNode(word));

                        if (existingNode != null) {
                            // Word already exists in the tree, update line numbers and file names
                            existingNode.addLineInfo(fileName, lineNumber);
                        } else {
                            // Word does not exist, add it to the tree
                            WordNode newNode = new WordNode(word);
                            newNode.addLineInfo(fileName, lineNumber);
                            wordTree.add(newNode);
                        }
                    }
                }

                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }
        wordTree.serializeTree("repository.ser");
    }

    
    public void printReport(String option, String outputFileName) {
        switch (option) {
        	// Print in alphabetic order all words with corresponding list of files
        	case "-pf":
                printAlphabeticOrderFiles();
                break;
            // Print in alphabetic order all words with corresponding list of files and line numbers
            case "-pl":
                printAlphabeticOrderFilesAndLines();
                break;
            // Print in alphabetic order all words with corresponding list of files, line numbers, and frequency
            case "-po":
                printAlphabeticOrderFull();
                break;
            default:
                System.err.println("Invalid option: " + option);
                return;
        }

        // Optionally, redirect the report to the specified output file
        if (outputFileName != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
                // Redirect the report to the output file
            } catch (IOException e) {
                System.err.println("Error writing to the output file: " + e.getMessage());
            }
        }
    }
    

    private void printAlphabeticOrderFiles() {
        Iterator<WordNode> iterator = wordTree.inorderIterator();
        while (iterator.hasNext()) {
            WordNode wordNode = iterator.next();
            System.out.println(wordNode.getData() + ":");
            wordNode.getLineInfoList().forEach(lineInfo -> {
                System.out.println("  - " + lineInfo.getFileName());
            });
        }
    }

    private void printAlphabeticOrderFilesAndLines() {
        Iterator<WordNode> iterator = wordTree.inorderIterator();
        while (iterator.hasNext()) {
            WordNode wordNode = iterator.next();
            System.out.println(wordNode.getData() + ":");
            wordNode.getLineInfoList().forEach(lineInfo -> {
                System.out.println("  - " + lineInfo.getFileName() + ", lines: " + lineInfo.getLineNumbers());
            });
        }
    }

    private void printAlphabeticOrderFull() {
        Iterator<WordNode> iterator = wordTree.inorderIterator();
        while (iterator.hasNext()) {
            WordNode wordNode = iterator.next();
            System.out.println(wordNode.getData() + ":");
            wordNode.getLineInfoList().forEach(lineInfo -> {
                System.out.println("  - " + lineInfo.getFileName() + ", lines: " + lineInfo.getLineNumbers() +
                        ", frequency: " + lineInfo.getLineNumbers().size());
            });
        }
    }
    
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Please use in this format: java -jar WordTracker.jar <input.txt> -pf/-pl/-po -f [<output.txt>]");
            System.exit(1);
        }

        String inputFileName = args[0];
        String option = args[1];
        String outputFileName = (args.length > 3) ? args[3] : null;

        WordTracker wordTracker = new WordTracker();
        try {
			wordTracker.processFile(inputFileName);
		} catch (TreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        wordTracker.printReport(option, outputFileName);
    }

}
