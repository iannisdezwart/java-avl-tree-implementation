class AVLTreeNode<T>
{
	public T value;
	public int height;
	public AVLTreeNode<T> left;
	public AVLTreeNode<T> right;

	/**
	 * Constructs an AVL tree leaf node with a given value.
	 */
	public
	AVLTreeNode(T value)
	{
		this.value = value;
		height = 1;
		left = null;
		right = null;
	}

	public void
	print(int depth)
	{
		if (left != null)
		{
			left.print(depth + 1);
		}

		System.out.printf("%s%d (%d)\n",
			"\t".repeat(depth), value, height);

		if (right != null)
		{
			right.print(depth + 1);
		}
	}
}

public class AVLTree<T extends Comparable<T>>
{
	public AVLTreeNode<T> root;
	int size;

	/**
	 * Constructs an empty AVL tree.
	 */
	public
	AVLTree()
	{
		root = null;
		size = 0;
	}

	/**
	 * Rebalances the subtree rooted at node `z` for the left-left case.
	 */
	private AVLTreeNode<T>
	avlRebalanceLeftLeft(AVLTreeNode<T> z)
	{
		AVLTreeNode<T> y = z.left;
		AVLTreeNode<T> x = y.left;
		AVLTreeNode<T> T1 = x.left;
		AVLTreeNode<T> T2 = x.right;
		AVLTreeNode<T> T3 = y.right;
		AVLTreeNode<T> T4 = z.right;

		// Reorganise the nodes.

		z.left = T3;
		y.right = z;

		// Update the heights.

		x.height = 1 + Math.max(avlHeight(T1), avlHeight(T2));
		y.height = 1 + Math.max(avlHeight(T3), avlHeight(T4));
		z.height = 1 + Math.max(x.height, y.height);

		// Return the new root.

		return y;
	}

	/**
	 * Rebalances the subtree rooted at node `z` for the left-right case.
	 */
	private AVLTreeNode<T>
	avlRebalanceLeftRight(AVLTreeNode<T> z)
	{
		AVLTreeNode<T> y = z.left;
		AVLTreeNode<T> x = y.right;
		AVLTreeNode<T> T1 = y.left;
		AVLTreeNode<T> T2 = x.left;
		AVLTreeNode<T> T3 = x.right;
		AVLTreeNode<T> T4 = z.right;

		// Reorganise the nodes.

		y.right = T2;
		z.left = T3;
		x.left = y;
		x.right = z;

		// Update the heights.

		y.height = 1 + Math.max(avlHeight(T1), avlHeight(T2));
		z.height = 1 + Math.max(avlHeight(T3), avlHeight(T4));
		x.height = 1 + Math.max(y.height, z.height);

		// Return the new root.

		return x;
	}

	/**
	 * Rebalances the subtree rooted at node `z` for the right-right case.
	 */
	private AVLTreeNode<T>
	avlRebalanceRightRight(AVLTreeNode<T> z)
	{
		AVLTreeNode<T> y = z.right;
		AVLTreeNode<T> x = y.right;
		AVLTreeNode<T> T1 = z.left;
		AVLTreeNode<T> T2 = y.left;
		AVLTreeNode<T> T3 = x.left;
		AVLTreeNode<T> T4 = x.right;

		// Reorganise the nodes.

		z.right = T2;
		y.left = z;

		// Update the heights.

		z.height = 1 + Math.max(avlHeight(T1), avlHeight(T2));
		x.height = 1 + Math.max(avlHeight(T3), avlHeight(T4));
		y.height = 1 + Math.max(x.height, z.height);

		// Return the new root.

		return y;
	}

	/**
	 * Rebalances the subtree rooted at node `z` for the right-left case.
	 */
	private AVLTreeNode<T>
	avlRebalanceRightLeft(AVLTreeNode<T> z)
	{
		AVLTreeNode<T> y = z.right;
		AVLTreeNode<T> x = y.left;
		AVLTreeNode<T> T1 = z.left;
		AVLTreeNode<T> T2 = x.left;
		AVLTreeNode<T> T3 = x.right;
		AVLTreeNode<T> T4 = y.right;

		// Reorganise the nodes.

		z.right = T2;
		y.left = T3;
		x.left = z;
		x.right = y;

		// Update the heights.

		z.height = 1 + Math.max(avlHeight(T1), avlHeight(T2));
		y.height = 1 + Math.max(avlHeight(T3), avlHeight(T4));
		x.height = 1 + Math.max(y.height, z.height);

		// Return the new root.

		return x;
	}

	private AVLTreeNode<T>
	avlInsert(AVLTreeNode<T> node, T value)
	{
		// When we reach the end of the tree
		// insert the new value at this position.

		if (node == null)
		{
			size++;
			return new AVLTreeNode<T>(value);
		}

		// Traverse the tree according to how the new value compares
		// to the left and right children of the current node.

		if (value.compareTo(node.value) < 0)
		{
			node.left = avlInsert(node.left, value);
		}
		else if (value.compareTo(node.value) > 0)
		{
			node.right = avlInsert(node.right, value);
		}
		else
		{
			// The value already exists in the tree.
			// We will do nothing.

			return node;
		}

		// Get the height of the left and right children.

		int leftHeight = avlHeight(node.left);
		int rightHeight = avlHeight(node.right);

		// After the recursive downwards step, we will traverse the
		// tree upwards again and update the height of the current node.

		node.height = 1 + Math.max(leftHeight, rightHeight);

		// We will check if the current node is unbalanced.
		// If it is, we will have to find a rotation to fix it.

		int balance = leftHeight - rightHeight;

		if (balance > 1)
		{
			// The left subtree is too tall.

			if (value.compareTo(node.left.value) < 0)
			{
				// Left-left case.

				return avlRebalanceLeftLeft(node);
			}
			else
			{
				// Left-right case.

				return avlRebalanceLeftRight(node);
			}
		}

		if (balance < -1)
		{
			// The right subtree is too tall.

			if (value.compareTo(node.right.value) > 0)
			{
				// Right-right case.

				return avlRebalanceRightRight(node);
			}
			else
			{
				// Right-left case.

				return avlRebalanceRightLeft(node);
			}
		}

		// This node is already balanced, so we just return it
		// as it currently is.

		return node;
	}

	private int
	avlBalance(AVLTreeNode<T> node)
	{
		if (node == null)
		{
			return 0;
		}

		return avlHeight(node.left) - avlHeight(node.right);
	}

	private AVLTreeNode<T>
	avlRemove(AVLTreeNode<T> node, T value)
	{
		// When we reach the end of the tree,
		// the value we want to remove is not in the tree.
		// We will simply return and do nothing.

		if (node == null)
		{
			return null;
		}

		// Traverse the tree according to how the value to be removed
		// compares to the left and right children of the current node.

		if (value.compareTo(node.value) < 0)
		{
			node.left = avlRemove(node.left, value);
		}
		else if (value.compareTo(node.value) > 0)
		{
			node.right = avlRemove(node.right, value);
		}
		else
		{
			// We found the node to be removed.
			// Now we have to check if this node has one or two children
			// or if it is a leaf.

			size--;
			node = avlDeleteNode(node);
		}

		// Make sure the node exists.

		if (node == null)
		{
			return null;
		}

		// Get the height of the left and right children.

		int leftHeight = avlHeight(node.left);
		int rightHeight = avlHeight(node.right);

		// After the recursive downwards step, we will traverse the
		// tree upwards again and update the height of the current node.

		node.height = 1 + Math.max(leftHeight, rightHeight);

		// We will check if the current node is unbalanced.
		// If it is, we will have to find a rotation to fix it.

		int balance = leftHeight - rightHeight;

		if (balance > 1)
		{
			// The left subtree is too tall.

			if (avlBalance(node.left) >= 0)
			{
				// Left-left case.

				return avlRebalanceLeftLeft(node);
			}
			else
			{
				// Left-right case.

				return avlRebalanceLeftRight(node);
			}
		}

		if (balance < -1)
		{
			// The right subtree is too tall.

			if (avlBalance(node.left) <= 0)
			{
				// Right-right case.

				return avlRebalanceRightRight(node);
			}
			else
			{
				// Right-left case.

				return avlRebalanceRightLeft(node);
			}
		}

		// This node is already balanced, so we just return it
		// as it currently is.

		return node;
	}

	private AVLTreeNode<T>
	avlDeleteNode(AVLTreeNode<T> node)
	{
		// Check how many children this node has.

		if (node.left == null && node.right == null)
		{
			// The node has no children.
			// We can just delete it from the tree.

			return null;
		}

		if (node.left == null)
		{
			// The node has only a right child.
			// We can just replace the node with its right child.

			return node.right;
		}

		if (node.right == null)
		{
			// The node has only a left child.
			// We can just replace the node with its left child.

			return node.left;
		}

		// The node has two children.
		// We will replace the node with its in-order successor.
		// This is the left-most child of the right subtree.

		AVLTreeNode<T> successor = node.right;

		while (successor.left != null)
		{
			successor = successor.left;
		}

		// Copy the value from the successor node.

		node.value = successor.value;

		// Delete the successor node.

		node.right = avlRemove(node.right, successor.value);

		// Calling the above function has the unwanted side-effect
		// of decrementing the size of the AVL tree again.
		// We will counter this by incrementing the size back up.

		size++;

		// Return the "new" root of the subtree.
		// It's not actually new, because we just replace the value
		// with the value of the successor node.

		return node;
	}

	public void
	insert(T value)
	{
		root = avlInsert(root, value);
	}

	public void
	remove(T value)
	{
		root = avlRemove(root, value);
	}

	public boolean
	has(T value)
	{
		return avlHas(root, value);
	}


	/**
	 * Returns the height of a node in an AVL tree.
	 * If the node is null, returns 0.
	 */
	private int
	avlHeight(AVLTreeNode<T> node)
	{
		if(node == null)
		{
			return 0;
		}

		return node.height;
	}

	public boolean
	avlHas(AVLTreeNode<T> node, T value)
	{
		// If we reach the end of the tree, the value is not in the tree.

		if (node == null)
		{
			return false;
		}

		// Traverse the tree according to how the value to be found
		// compares to the left and right children of the current node.

		if (value.compareTo(node.value) < 0)
		{
			return avlHas(node.left, value);
		}

		if (value.compareTo(node.value) > 0)
		{
			return avlHas(node.right, value);
		}

		// We found the value.

		return true;
	}

	public void
	print()
	{
		System.out.printf("AVL Tree (%d)\n", size);

		if (root != null)
		{
			root.print(0);
		}
	}
}