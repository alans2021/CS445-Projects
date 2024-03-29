// CS 0445 Spring 2018
// Modified BinarySearchTree class.  This class now extends
// ComparableBinaryTree rather than BinaryTree.
// Add your methods to the class so that it works as specified
// in the assignment.  Note: This class will work without any modifications,
// since it is not defining any new methods -- rather it is overriding already
// existing methods to be more efficient.  However, you will lose credit if
// you do not override the methods to take advantage of the fact that this is
// a BST, not a general ComparableBinaryTree.

package MyTreePackage;
import java.util.Iterator;
/**
   A class that implements the ADT binary search tree by extending BinaryTree.
   Recursive version.
   
   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 4.0
*/
public class BinarySearchTree<T extends Comparable<? super T>>
             extends ComparableBinaryTree<T> 
             implements SearchTreeInterface<T>
{
   public BinarySearchTree()
   {
      super();
   } // end default constructor

   public BinarySearchTree(T rootEntry)
   {
      super();
      setRootNode(new BinaryNode<>(rootEntry));
   } // end constructor

   public void setTree(T rootData) // Disable setTree (see Segment 25.6)
   {
      throw new UnsupportedOperationException();
   } // end setTree

   public void setTree(T rootData, BinaryTreeInterface<T> leftTree, 
                                   BinaryTreeInterface<T> rightTree)
   {
      throw new UnsupportedOperationException();
   } // end setTree
  
	public T getEntry(T entry)
	{
	   return findEntry(getRootNode(), entry);
	} // end getEntry

   // Recursively finds the given entry in the binary tree rooted at the given node.
	private T findEntry(BinaryNode<T> rootNode, T entry)
	{	
      T result = null;

      if (rootNode != null)
      {
         T rootEntry = rootNode.getData();

         if (entry.equals(rootEntry))
            result = rootEntry;
         else if (entry.compareTo(rootEntry) < 0)
            result = findEntry(rootNode.getLeftChild(), entry);
         else
            result = findEntry(rootNode.getRightChild(), entry);
      } // end if

      return result;
	} // end findEntry
	
	public boolean contains(T entry)
	{
		return getEntry(entry) != null;
	} // end contains
	
   public T add(T newEntry)
   {
      T result = null;

      if (isEmpty())
         setRootNode(new BinaryNode<>(newEntry));
      else
         result = addEntry(getRootNode(), newEntry);
       
      return result;
   } // end add

   // Adds newEntry to the nonempty subtree rooted at rootNode.
   private T addEntry(BinaryNode<T> rootNode, T newEntry)
   {
      assert rootNode != null;
      T result = null;
      int comparison = newEntry.compareTo(rootNode.getData());

      if (comparison == 0)
      {
         result = rootNode.getData();
         rootNode.setData(newEntry);
      }
      else if (comparison < 0)
      {
         if (rootNode.hasLeftChild())
            result = addEntry(rootNode.getLeftChild(), newEntry);
         else
            rootNode.setLeftChild(new BinaryNode<>(newEntry));
      }
      else
      {
         assert comparison > 0;

         if (rootNode.hasRightChild())
            result = addEntry(rootNode.getRightChild(), newEntry);
         else
            rootNode.setRightChild(new BinaryNode<>(newEntry));
      } // end if

      return result;
   } // end addEntry

	public T remove(T entry)
   {
      ReturnObject oldEntry = new ReturnObject(null);
      BinaryNode<T> newRoot = removeEntry(getRootNode(), entry, oldEntry);
      setRootNode(newRoot);

      return oldEntry.get();
   } // end remove

	// Removes an entry from the tree rooted at a given node.
   // Parameters:
   //    rootNode  A reference to the root of a tree.
   //    entry  The object to be removed.
   //    oldEntry  An object whose data field is null.
   // Returns:  The root node of the resulting tree; if entry matches
   //           an entry in the tree, oldEntry's data field is the entry
   //           that was removed from the tree; otherwise it is null.
   private BinaryNode<T> removeEntry(BinaryNode<T> rootNode, T entry,
                                     ReturnObject oldEntry)
   {
      if (rootNode != null)
      {
         T rootData = rootNode.getData();
         int comparison = entry.compareTo(rootData);

         if (comparison == 0)       // entry == root entry
         {
            oldEntry.set(rootData);
            rootNode = removeFromRoot(rootNode);
         }
         else if (comparison < 0)   // entry < root entry
         {
            BinaryNode<T> leftChild = rootNode.getLeftChild();
            BinaryNode<T> subtreeRoot = removeEntry(leftChild, entry, oldEntry);
            rootNode.setLeftChild(subtreeRoot);
         }
         else                       // entry > root entry
         {
            BinaryNode<T> rightChild = rootNode.getRightChild();
            rootNode.setRightChild(removeEntry(rightChild, entry, oldEntry));
         } // end if
      } // end if

      return rootNode;
   } // end removeEntry

	// Removes the entry in a given root node of a subtree.
   // Parameter:
   //    rootNode  A reference to the root of the subtree.
   // Returns:  The root node of the revised subtree.
   private BinaryNode<T> removeFromRoot(BinaryNode<T> rootNode)
   {
      // Case 1: rootNode has two children 
      if (rootNode.hasLeftChild() && rootNode.hasRightChild())
      {
         // Find node with largest entry in left subtree
         BinaryNode<T> leftSubtreeRoot = rootNode.getLeftChild();
         BinaryNode<T> largestNode = findLargest(leftSubtreeRoot);

         // Replace entry in root
         rootNode.setData(largestNode.getData());

         // Remove node with largest entry in left subtree
         rootNode.setLeftChild(removeLargest(leftSubtreeRoot));
      } // end if 

      // Case 2: rootNode has at most one child
      else if (rootNode.hasRightChild())
         rootNode = rootNode.getRightChild();
      else
         rootNode = rootNode.getLeftChild();
       
      // Assertion: If rootNode was a leaf, it is now null

      return rootNode; 
   } // end removeEntry

   // Finds the node containing the largest entry in a given tree.
   // Parameter:
   //    rootNode  A reference to the root node of the tree.
   // Returns:  The node containing the largest entry in the tree.
   private BinaryNode<T> findLargest(BinaryNode<T> rootNode)
   {
      if (rootNode.hasRightChild())
         rootNode = findLargest(rootNode.getRightChild());
       
      return rootNode;
   } // end findLargest

	// Removes the node containing the largest entry in a given tree.
   // Parameter:
   //    rootNode  A reference to the root node of the tree.
   // Returns:  The root node of the revised tree.
   private BinaryNode<T> removeLargest(BinaryNode<T> rootNode)
   {
      if (rootNode.hasRightChild())
      {
         BinaryNode<T> rightChild = rootNode.getRightChild();
         rightChild = removeLargest(rightChild);
         rootNode.setRightChild(rightChild);
      }
      else 
         rootNode = rootNode.getLeftChild();
       
      return rootNode;
   } // end removeLargest

	private class ReturnObject
	{
		private T item;
			
		private ReturnObject(T entry)
		{
			item = entry;
		} // end constructor
		
		public T get()
		{
			return item;
		} // end get

		public void set(T entry)
		{
			item = entry;
		} // end set
	} // end ReturnObject
	
	// **************************************************
	// Override the methods specified in Assignment 4 below
	// **************************************************
	
	public T getMax()
	{
		BinaryNode<T> node = this.getRootNode();
		T max = recMax(node);
		return max;
	}
	
	private T recMax(BinaryNode<T> node)
	{
		if(node.hasRightChild()) //Keep going to right-most child
			return recMax(node.getRightChild());
		else
			return node.getData();
	}
	
	public T getMin()
	{
		BinaryNode<T> node = this.getRootNode();
		T min = recMin(node);
		return min;
	}
	
	private T recMin(BinaryNode<T> node)
	{
		if(node.hasLeftChild()) //Keep going to left-most child
			return recMin(node.getLeftChild());
		else
			return node.getData();
	}
	
	public boolean isBST()
	{
		return true;
	}
	
	public T get(int i)
	{
		if(i < 0 || i >= this.getNumberOfNodes())
			throw new IndexOutOfBoundsException();
		BinaryNode<T> node = this.getRootNode();
		Object[] data = new Object[this.getNumberOfNodes()]; //Make array of objects
		recTraverse(node, data, 0); //Go through tree and put in array
		return (T) data[i]; //Return data at right index
	}
	
	private int recTraverse(BinaryNode<T> node, Object[] data, int index)
	{
		if(node != null)
		{
			index = recTraverse(node.getLeftChild(), data, index); //Keep going to left children
			data[index] = node.getData(); //Put the node into array
			index = index + 1; //Update index
			index = recTraverse(node.getRightChild(), data, index); //Go through right children
		}
		return index;
	}
	
	public int rank(T data)
	{
		BinaryNode<T> node = this.getRootNode();
		int count = recRank(node, data, 0);
		return count;
	}
	
	private int recRank(BinaryNode<T> node, T data, int count)
	{
		if(node != null)
		{
			count = recRank(node.getLeftChild(), data, count); //Go through left subtree
			if(node.getData().compareTo(data) < 0) //If current node is less than one trying to find
			{
				count = count + 1; //Increment count
				count = recRank(node.getRightChild(), data, count); //Go through right subtree
			}
			return count;
			
		}
		else
			return count;
	}
} // end BinarySearchTree