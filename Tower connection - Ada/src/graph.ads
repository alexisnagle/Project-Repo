-- Author: Alexis Nagle, anagle2020@my.fit.edu
-- Course: CSE 4250, Fall 2022
-- Project: Proj3, Can you hear me now?
with Ada.Containers.Doubly_Linked_Lists, Ada.Strings.Unbounded;
use Ada.Strings.Unbounded;
package graph is
   type Node_Struc;
   type Node is access Node_Struc;
   package Tower_List is new Ada.Containers.Doubly_Linked_Lists(Element_Type => Node);
   use Tower_List;
   Towers: List;
   C: Cursor;
   D: Cursor;
   F: Cursor;

   type Node_Struc is record
      name: Unbounded_String;
      connections: List;
   end record;

   function towerExists(s: Unbounded_String) return Boolean;
   function findTower(s: Unbounded_String) return Node;
   function addTower(s: Unbounded_String) return Node;
   procedure addConnection(from: Node; to: Node);
   procedure subConnections(from: Node; to: Node);
   function isConnected(from: Node; to: Node) return Boolean;
end graph;
