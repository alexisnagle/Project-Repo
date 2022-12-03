-- Author: Alexis Nagle, anagle2020@my.fit.edu
-- Course: CSE 4250, Fall 2022
-- Project: Proj3, Can you hear me now?
with Ada.Containers.Doubly_Linked_Lists, Ada.Text_IO;
package body graph is
   -- see if a tower with name s exists
   function towerExists(s: Unbounded_String) return Boolean is
   begin
      -- loop through the towers in the list
      C := Towers.First;
      while C /= No_Element loop
         if Element(C).name = s then
            return TRUE;    -- return true if a tower with name s exists
         end if;
         C := Next(C);
      end loop;
      return FALSE;         -- return false is a tower with name s does not exist
   end towerExists;

   -- find the node with the name s
   function findTower(s: Unbounded_String) return Node is
   begin
      -- loop through the towers in the list
      C := Towers.First;
      while C /= No_Element loop
         if Element(C).name = s then
            return Element(C);   -- return the tower with name s
         end if;
         C := Next(C);
      end loop;
      return First_Element(Towers);  -- never get here becasue we only call this method if towerExists(s) is TRUE
   end findTower;

   --add node with name s to the list of towers
   function addTower(s: Unbounded_String) return Node is
      n: Node;
   begin
      n := new Node_Struc'(s, Empty_List); -- create a new node with name s and an empty connections list
      Towers.Append(n);                    -- add thenew node to the list of towers
      return n;
   end addTower;

   -- add a connection between the from and to node
   procedure addConnection(from: Node; to: Node) is
   begin
      if not from.connections.Contains(to) then
         from.connections.append(to);     -- add "to" to the connection list of "from"
      end if;
   end addConnection;

   -- add extra conections that are made by connecting from -> to
   procedure subConnections(from: Node; to: Node) is
   begin
      -- add connections for from -> to -> n, connect from -> n
      -- loop through the connection list of "to"
      C := to.connections.First;
      while C /= No_Element loop
         addConnection(from, Element(C));-- connect "from" to each node
         C := Next(C);
      end loop;

      -- add connections for  n -> from -> to, connect n -> to
      -- loop throught the list of towers
      C := Towers.First;
      while C /= No_Element loop
         -- loop throught the list of connections for each tower
         D := Element(C).connections.First;
         while D /= No_Element loop
            -- if from is in the list of connections, then connect the current tower to "to"
            if Element(D).name = from.name then
               addConnection(Element(C), to);
               -- add connections for  n -> from -> to -> m, connect n -> m
               F := to.connections.First;
               while F /= No_Element loop
                  addConnection(Element(C), Element(F));
                  F := Next(F);
               end loop;
            end if;
            D := Next(D);
         end loop;
         C := Next(C);
      end loop;
   end subConnections;

   -- check if there is a connection between from and to
   function isConnected(from: Node; to: Node) return Boolean is
   begin
      -- loop through the connection list of "from"
      C := from.connections.First;
      while C /= No_Element loop
         if Element(C).name = to.name then
            return TRUE;      -- return true if "to" is in the list
         end if;
         C := Next(C);
         end loop;
      return FALSE;           -- return false if "to" is not in the list
   end isConnected;
end graph;
