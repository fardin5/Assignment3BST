package referenceBasedTreeImplementation;

import java.io.Serializable;

public class BSTreeNode<T extends Comparable<? super T>> implements Serializable {
    private T data;
    private BSTreeNode<T> leftChild;
    private BSTreeNode<T> rightChild;

    public BSTreeNode(T data) {
        this.data = data;
        this.leftChild = null;
        this.rightChild = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BSTreeNode<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(BSTreeNode<T> leftChild) {
        this.leftChild = leftChild;
    }

    public BSTreeNode<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(BSTreeNode<T> rightChild) {
        this.rightChild = rightChild;
    }
}