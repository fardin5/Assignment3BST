package utilities;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Stack;

import exceptions.TreeException;
import referenceBasedTreeImplementation.BSTreeNode;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BSTreeNode<E> root;

    public BSTree() {
        this.root = null;
    }

    @Override
    public BSTreeNode<E> getRoot() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Tree is empty.");
        }
        return root;
    }

    @Override
    public int getHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(BSTreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = calculateHeight(node.getLeftChild());
        int rightHeight = calculateHeight(node.getRightChild());
        return 1 + Math.max(leftHeight, rightHeight);
    }

    @Override
    public int size() {
        return countNodes(root);
    }

    private int countNodes(BSTreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.getLeftChild()) + countNodes(node.getRightChild());
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean contains(E entry) throws TreeException {
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) throws TreeException {
        return searchNode(root, entry);
    }

    private BSTreeNode<E> searchNode(BSTreeNode<E> node, E entry) {
        if (node == null) {
            return null;
        }

        int compareResult = entry.compareTo(node.getData());
        if (compareResult == 0) {
            return node;
        } else if (compareResult < 0) {
            return searchNode(node.getLeftChild(), entry);
        } else {
            return searchNode(node.getRightChild(), entry);
        }
    }

    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if (newEntry == null) {
            throw new NullPointerException("Cannot add null entry to the tree.");
        }

        root = addNode(root, newEntry);
        return true; // Assuming the add operation always succeeds in this implementation.
    }

    private BSTreeNode<E> addNode(BSTreeNode<E> node, E newEntry) {
        if (node == null) {
            return new BSTreeNode<>(newEntry);
        }

        int compareResult = newEntry.compareTo(node.getData());
        if (compareResult < 0) {
            node.setLeftChild(addNode(node.getLeftChild(), newEntry));
        } else if (compareResult > 0) {
            node.setRightChild(addNode(node.getRightChild(), newEntry));
        }

        return node;
    }

    @Override
    public Iterator<E> inorderIterator() {
        return new InorderIterator();
    }

    @Override
    public Iterator<E> preorderIterator() {
        return new PreorderIterator();
    }

    @Override
    public Iterator<E> postorderIterator() {
        return new PostorderIterator();
    }

    private class InorderIterator implements Iterator<E> {
        private Stack<BSTreeNode<E>> stack;

        public InorderIterator() {
            stack = new Stack<>();
            pushInorder(root);
        }

        private void pushInorder(BSTreeNode<E> node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeftChild();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iteration.");
            }

            BSTreeNode<E> current = stack.pop();
            pushInorder(current.getRightChild());

            return current.getData();
        }
    }

    private class PreorderIterator implements Iterator<E> {
        private Stack<BSTreeNode<E>> stack;

        public PreorderIterator() {
            stack = new Stack<>();
            if (root != null) {
                stack.push(root);
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iteration.");
            }

            BSTreeNode<E> current = stack.pop();

            if (current.getRightChild() != null) {
                stack.push(current.getRightChild());
            }
            if (current.getLeftChild() != null) {
                stack.push(current.getLeftChild());
            }

            return current.getData();
        }
    }

    private class PostorderIterator implements Iterator<E> {
        private Stack<BSTreeNode<E>> stack;

        public PostorderIterator() {
            stack = new Stack<>();
            pushPostorder(root);
        }

        private void pushPostorder(BSTreeNode<E> node) {
            if (node == null) {
                return;
            }

            stack.push(node);

            pushPostorder(node.getRightChild());
            pushPostorder(node.getLeftChild());
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the iteration.");
            }

            BSTreeNode<E> current = stack.pop();
            return current.getData();
        }
    }
    
    public void serializeTree(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("Tree serialized and stored in " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static BSTree<WordNode> deserializeTree(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (BSTree<WordNode>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
