package com.xht.androidnote.module.dsa;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
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


    /*
        后序遍历
        输出顺序是，左子树、右子树、根节点
               3
           2		8
        9	  10		4

        9 10 2 4 8 3
    */
    public static void postOrderTraversal(TreeNode node) {

        if (node == null) {
            return;
        }

        postOrderTraversal(node.leftChild);
        postOrderTraversal(node.rightChild);
        System.out.print(node.data + " ");
    }


    /*
        后序遍历
        输出顺序是，左子树、右子树、根节点
               3
           2		8
        9	  10		4

        9 10 2 4 8 3
    */
    public static void postOrderTraversalWithStack(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();

        Stack<TreeNode> output = new Stack<>();

        TreeNode treeNode = root;

        //3  3、8     3、8、4       3、8      3          2            2、10               2
        // 9
        //3  3、8     3、8、4       3、8、4   3、8、4    3、8、4、2    3、8、4、2、10      3、8、4、2、10
        // 3、8、4、2、10、9

        //null
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


    /*
        中序遍历
        输出顺序是：左子树、根节点、右子树
               3
           2		8
        9	  10		4

        9 2 10 3 8 4
    */
    public static void inOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }

        inOrderTraversal(node.leftChild);

        System.out.print(node.data + " ");

        inOrderTraversal(node.rightChild);
    }

    /*
        中序遍历
        输出顺序是：左子树、根节点、右子树
               3
           2		8
        9	  10		4

        9 2 10 3 8 4
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

    /*
        前序遍历
        输出顺序是：根节点、左子树、右子树
               3
           2		8
        9	  10		4

        3、2、9、10、8、4
    */
    public static void preOrderTraversal(TreeNode node) {
        if (node == null) {
            return;
        }

        System.out.print(node.data + " ");

        preOrderTraversal(node.leftChild);
        preOrderTraversal(node.rightChild);
    }


    /*
        前序遍历
        输出顺序是：根节点、左子树、右子树
               3
           2		8
        9	  10		4

        3、2、9、10、8、4
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

    /*
        层序遍历
               3
           2		8
        9	  10		4

        3、2、8、9、10、4
    */
    public static void levelOrderTraversal(TreeNode root) {
        //3     2、8     8、9、10      9、10、4

        Queue<TreeNode> queue = new LinkedList<>();

        queue.offer(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println(node.data + " ");

            if (node.leftChild != null) {
                queue.offer(node.leftChild);
            }

            if (node.rightChild != null) {
                queue.offer(node.rightChild);
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
