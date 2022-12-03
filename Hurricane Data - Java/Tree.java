/*
  Author: Alexis Nagle
  Email: anagle2020@my.fit.edu
  Course: CSE 2010
  Section: 03
  Description of this file: 
*/
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
public class Tree {
   /**
    * ------------------NESTED NODE CLASS------------------
   * Node of a tree, which stores a reference to its
   * data,to its parent, and to a lsit of children nodes 
   * (which is empty if the current node is a leaf)
   */
   private static class TreeNode {

      private String data; //reference to the data stored in this node
      private TreeNode parent; //reference to the parent node
      private List<TreeNode> children; //reference to a list of children nodes

      /**
       * Creates a node with the given data, an empty children list, 
       * and a null parent pointer to be assigned later
       *
       * @param d  the data to be stored
       */
      public TreeNode(String d) {
         data = d;
         children = new ArrayList<TreeNode>();
         parent = null;
      }
   } 
   //------------------END OF NESTED CLASS------------------

   private TreeNode root = null; // root of the tree (null if empty)
   private int size = 0; // number of nodes in the tree

   public Tree() { } // constructs an initially empty tree

   public int size() { return size; } //returns size of the tree

   public List<TreeNode> allNodes = new ArrayList<TreeNode>(); // array list holding all tree nodes 

   public TreeNode rootNode() { return root; } // returns the root node of the tree

   public boolean isEmpty() { return size == 0; } //returns true if the tree has no nodes

   /**
    * Create the root node
    * 
    * @param e is the data to be stored in the root node
    */
   public void addRoot(String e) { // adds element e as the root
      root = new TreeNode(e); // create and link a new node
      allNodes.add(root);
      size++; // increase counter for the size of the tree
   }

   /**
    * Get a list of children of a specific node
    *
    * @param node node which we desire a list of its children 
    * @return a list of children nodes 
    */
   public List<TreeNode> getChildren(TreeNode node) {
      return node.children;
   }

   /**
    * Get the parent node of a specific node
    *
    * @param node node which we desire its parent node 
    * @return parent node 
    */
   public TreeNode getParent(TreeNode node) {
      return node.parent;
   }

   /**
    * Get the data  of a specific node
    *
    * @param node node which we desire its data 
    * @return data of the node 
    */
   public String getData(TreeNode node) {
      return node.data;
   }

   /**
    * Adds a node as a child to another node (parent node)
    *
    * @param childNode node to be assigned to a parent's list of children 
    * @param parentNode node to which the child is being assigned to its list of children
    */
   public void addChild(TreeNode parentNode, TreeNode childNode) {
      childNode.parent = parentNode;
      parentNode.children.add(childNode);
      allNodes.add(childNode);
      size++; // increase counter for the size of the tree
   }

   /**
    * Overloads then calls addChildMethod
    *
    * @param childData data to be assigned to a node to be assigned to a parent's list of children 
    * @param parentNode node to which the child is being assigned to its list of children
    */
   public void addChild(TreeNode parentNode, String childData) {
      addChild(parentNode, new TreeNode(childData));
   }

   /**
    * Locate a specific node based on its data content
    *
    * @param nodeData data to find its corresponding node
    * @param current is the node currrently being expanded upon  
    * @return node corresponding to the data given 
    */
   public TreeNode findNode(String nodeData) {
      for (int i = 0; i < allNodes.size(); i++) {
         //iterate through all the nodes
         if (allNodes.get(i).data.equals(nodeData))
            return allNodes.get(i); //return the proper node
      }
      return null; // return null if the node does not exist
   }

   /**
    * Locate a all nodes corrsponding to data given
    *
    * @param nodeData data to find its corresponding node 
    * @param current is the node currrently being expanded upon
    * @return list of nodes corresponding to the data given 
    */
   public ArrayList<TreeNode> findAllNodes(String nodeData) {
      ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
      for (int i = 0; i < allNodes.size(); i++) {
         //iterate through all nodes
         if (allNodes.get(i).data.contains(nodeData))
            nodes.add(allNodes.get(i)); // add matching nodes to the list
      }
      return nodes; // return the list of mathcing nodes
   }

   /**
    * Retrieve leafs that are desendants of a specified node
    * 
    * @param nodes are the specified node of which we want its leafs
    * @return leafs is a list of all the leaf nodes of a certain node
    */
   public ArrayList<String> getLeafDecendants(List<TreeNode> nodes) {
      ArrayList<String> leafs = new ArrayList<String>();
      //iterate through given nodes
      for (int i = 0; i < nodes.size(); i++) {
         // if the node has no children, then it is a leaf, add to the list
         if (getChildren(nodes.get(i)).size() == 0) {
            leafs.add(nodes.get(i).data);
         }
         //else find its decendants
         leafs.addAll(getLeafDecendants(getChildren(nodes.get(i))));
      }
      return leafs;
   }

   /**
    * Retrieve leafs that are desendants of a specified node
    * 
    * @param nodes are the specified node of which we want its leafs
    * @return leafs is a list of all the leaf nodes of a certain node
    */
   public ArrayList<TreeNode> getNodeLeafDecendants(List<TreeNode> nodes) {
      ArrayList<TreeNode> leafs = new ArrayList<TreeNode>();
      //iterate through given nodes
      for (int i = 0; i < nodes.size(); i++) {
         // if the node has no children, then it is a leaf, add to the list
         if (getChildren(nodes.get(i)).size() == 0) {
            leafs.add(nodes.get(i));
         }
         //else find its decendants
         leafs.addAll(getNodeLeafDecendants(getChildren(nodes.get(i))));
      }
      return leafs;
   }

   /**
    * Retrieve leafs that are desendants of two sets of nodes 
    * 
    * @param nodes1 is the first list of node of which we want its leafs
    * @param nodes2 is the second list of nodes which we want its leafs
    * @return leafs is a list of all the leaf nodes of a certain node
    */
   public ArrayList<String> getLeafDecendants(List<TreeNode> nodes1, List<TreeNode> nodes2) {
      ArrayList<String> leafs = new ArrayList<String>();
      // find leafs of node1 list and node2 list
      ArrayList<TreeNode> leaf1 = getNodeLeafDecendants(nodes1);
      ArrayList<TreeNode> leaf2 = getNodeLeafDecendants(nodes2);
      //iterate through both lists of leafs
      for (int i = 0; i < leaf1.size(); i++) {
         for (int j = 0; j < leaf2.size(); j++) {
            //find common leafs and add them to the leafs list
            if (leaf1.get(i).equals(leaf2.get(j))) {
               leafs.add(leaf1.get(i).data);
            }
         }
      }
      return leafs;
   }

   /**
    * Retrieve ancestors of a specified node that contain a word
    * 
    * @param nodes are the specified node of which we want its leafs
    * @return ancestors is a list of the nodes ancestors
    */
   public ArrayList<String> getAncestors(List<TreeNode> nodes, String str) {
      ArrayList<String> ancestors = new ArrayList<String>();
      for (int i = 0; i < nodes.size(); i++) {
         // for each node in the list, iterate though its ancestors
         TreeNode current = nodes.get(i);
         while(getParent(current) != null) {
            if (getParent(current).data.contains(str))
               ancestors.add(getParent(current).data); //add any ancestors that contain the string str
            current = getParent(current);
         }
      }
      return ancestors; //return the list of ancestors 
   }

   /**
    * Creates a List of the duplicate names
    * 
    * @return nodes that have duplicate data
    */
   public ArrayList<String> getDuplicates() {
      ArrayList<String> duplicates = new ArrayList<String>();
      //iterate through the list twice
      for (int i = 0; i < allNodes.size(); i++) {
         for (int j = i+1; j < allNodes.size(); j++) {
            //find common nodes, and add them to the list
            if (allNodes.get(i).data.equals(allNodes.get(j).data))
               duplicates.add(allNodes.get(i).data);
         }
      }
      return duplicates;
   }

}
