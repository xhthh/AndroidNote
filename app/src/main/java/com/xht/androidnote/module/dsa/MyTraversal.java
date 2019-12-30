package com.xht.androidnote.module.dsa;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by xht on 2019/12/30.
 */
public class MyTraversal {

    public static void main(String[] args) {
        LinkedList<Integer> inputList = new LinkedList<>(Arrays.asList(new Integer[]{3, 2, 9,
                null, null, 10, null, null, 8, null, 4}));

        TreeNode node = createBinaryTree(inputList);

        //        preOrderTraversalWithStack(node);
        //
        //        System.out.println("\n");
        //
        //        preOrderTraversal(node);
        //        System.out.println("\n");
        //
        //
        //        inOrderTraversal(node);
        //        System.out.println("\n");
        //
        //
        //
        //
        //        postOrderTraversal(node);

        //        inOrderTraversal(node);
        //
        //        System.out.println("\n");
        //
        //        inOrderTraversalWithStack(node);

        postOrderTraversal(node);

        System.out.println("\n");

        postOrderTraversalWithStack(node);

    }


    /**
     * [3, 2, 9, null, null, 10, null, null, 8, null, 4]
     */
    public static TreeNode createBinaryTree(LinkedList<Integer> inputList) {
        TreeNode node = null;

        if (inputList == null || inputList.isEmpty()) {
            return null;
        }

        Integer data = inputList.removeFirst();

        //System.out.println("data = " + data);

        if (data != null) {
            node = new TreeNode(data);

            //System.out.println("node = " + node.data);

            node.leftChild = createBinaryTree(inputList);
            //System.out.println("node.data=" + node.data + "  ←  leftChild=" + (node.leftChild
            // == null ? null : node.leftChild.data));
            node.rightChild = createBinaryTree(inputList);
            //System.out.println("node.data=" + node.data + "  →  rightChild=" + (node.rightChild
            // == null ? null : node.rightChild.data));
        }

        //System.out.println("return node.data = " + (node == null ? null : node.data));

        return node;
    }


    /**
     * 后序遍历
     * <p>
     * 输出顺序是，左子树、右子树、根节点
     * <p>
     * 3
     * 2			8
     * 9	    10			4
     * <p>
     * 9 10 2 4 8 3
     *
     * @param node
     */
    public static void postOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }

        postOrderTraversal(node.leftChild);
        postOrderTraversal(node.rightChild);
        System.out.print(node.data + " ");
    }


    /**
     *
     *                  3
     *           2			8
     *       9	    10			4
     * @param root
     */
    public static void postOrderTraversalWithStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();

        Stack<TreeNode> output = new Stack<>();

        TreeNode treeNode = root;

        //3     3、8     3、8、4       3、8         3          2            2、10                2                   9
        //3     3、8     3、8、4       3、8、4      3、8、4    3、8、4、2     3、8、4、2、10      3、8、4、2、10       3、8、4、2、10、9

        //
        //3、8、4、2、10、9  跳出循环

        while (treeNode != null || !stack.isEmpty()) {
            if (treeNode != null) {
                output.push(treeNode);
                stack.push(treeNode);
                treeNode = treeNode.rightChild;
            } else {
                treeNode = stack.pop();
                treeNode = treeNode.leftChild;
            }
        }

        while (!output.isEmpty()) {
            System.out.print(output.pop().data + " ");
        }


    }


    /**
     * 中序遍历
     * 输出顺序为左子树、根节点、右子树
     * <p>
     * 3
     * 2			8
     * 9	    10			4
     * <p>
     * 9 2 10 3 8 4
     *
     * @param node
     */
    public static void inOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }

        inOrderTraversal(node.leftChild);

        System.out.print(node.data + " ");

        inOrderTraversal(node.rightChild);
    }

    /**
     * 3
     * 2			8
     * 9	    10			4
     *
     * @param root
     */
    public static void inOrderTraversalWithStack(TreeNode root) {

        Stack<TreeNode> stack = new Stack<>();

        TreeNode treeNode = root;

        while (treeNode != null || !stack.isEmpty()) {

            //3
            //3、2
            //3、2、9

            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.leftChild;
            }

            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                System.out.print(treeNode.data + " ");
                treeNode = treeNode.rightChild;
            }
        }


    }


    /**
     * 前序遍历
     * 输出顺序为根节点、左子树、右子树
     * <p>
     * 3
     * 2			8
     * 9	    10			4
     *
     * <p>
     * 3、2、9、10、8、4
     *
     * @param node
     */
    public static void preOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }

        System.out.print(node.data + " ");

        preOrderTraversal(node.leftChild);
        preOrderTraversal(node.rightChild);
    }


    /**
     * 3
     * 2			8
     * 9	    10			4
     * <p>
     * 3、2、9、10、8、4
     *
     * @param root
     */
    public static void preOrderTraversalWithStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();

        TreeNode treeNode = root;

        while (treeNode != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (treeNode != null) {
                System.out.print(treeNode.data + " ");

                stack.push(treeNode);
                treeNode = treeNode.leftChild;
            }

            //如果节点没有左孩子，则弹出栈顶节点，访问右孩子
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.rightChild;
            }
        }
    }


    /**
     * 二叉树节点
     */
    private static class TreeNode {
        int data;
        TreeNode leftChild;
        TreeNode rightChild;

        public TreeNode(int data) {
            this.data = data;
        }
    }

}
