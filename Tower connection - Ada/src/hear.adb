-- Author: Alexis Nagle, anagle2020@my.fit.edu
-- Course: CSE 4250, Fall 2022
-- Project: Proj3, Can you hear me now?
with Text_IO, Ada.Integer_Text_IO, Ada.Text_IO.Unbounded_IO, Ada.Strings, Ada.Strings.Maps, Ada.Strings.Unbounded, Ada.Containers.Doubly_Linked_Lists;
use Ada,Text_IO, Ada.Integer_Text_IO, Ada.Text_IO.Unbounded_IO, Ada.Strings, Ada.Strings.Maps, Ada.Strings.Unbounded;
with graph; use graph;

procedure Hear is
   Line   : Unbounded_String := Null_Unbounded_String;   -- Single Line of Input
   from : Unbounded_String := Null_Unbounded_String;     -- name of the "from" tower
   to : Unbounded_String := Null_Unbounded_String;       -- name of the "to" tower
   key : Unbounded_String := Null_Unbounded_String;      -- key to indicate a connection or query
   first : Positive;
   last : Natural;
   Space : constant Character_Set := To_Set(' ');
   Punctuation : constant Character_Set := To_Set('.') or To_Set('?');
   connected: Boolean;
   fromNode : Node;
   toNode : Node;
begin
   while not (End_Of_File) loop                          -- continue until there are no lines left in the file
      Line := To_Unbounded_String(Get_Line);             -- Read in line
      Line := Trim(Line, Both);                          -- Trim both sides of the line
      Find_Token (Line, Space, 1, Outside, first, last); -- Find position of the first space
      --Split the line at the location of the first space into the from tower and the remaining string
      from := Trim(Unbounded_Slice(Line, first, last), Both);
      Line := Trim(Delete(Line, first, last), Both);

      Find_Token (Line, Punctuation, 1, Outside, first, last); --Find the position of the . or ? in the remaining string
      -- Split the remining string at the location of the token into the to tower and the key(. or ?)
      to := Trim(Unbounded_Slice(Line, first, last), Both);
      key := Unbounded_Slice(Trim(Delete(Line, first, last), Both), 1, 1);

      -- Determine the task based on the key, connection or query
      if key = "." then
         -- see if a tower with the "from" name exists, if not create on and add it to the list
         if towerExists(from) then
            fromNode := findTower(from);
         else
            fromNode := addTower(from);
         end if;

          -- see if a tower with the "to" name exists, if not create on and add it to the list
         if towerExists(to) then
            toNode := findTower(to);
         else
            toNode := addTower(to);
         end if;

         addConnection(fromNode, toNode); -- connect from -> to
         subConnections(fromNode, toNode); -- n -> from -> to, connect n -> to and from -> to -> n, connect from -> n

      else
         -- see if a tower with the "from" name and a tower with the "to" name exists
         if not towerExists(from) or not towerExists(to) then
            -- if not, the connection cannot exist
            connected := FALSE;
         else
            -- if so, determine if "from" connects to "to"
            fromNode := findTower(from);
            toNode := findTower(to);
              connected := isConnected(fromNode, toNode);
         end if;

         -- display if the towers are connected
         if connected then
            Put_Line("+ " & from & " => " & to);
         else
            Put_Line("- " & from & " => " & to);
         end if;
      end if;
   end loop;
end Hear;
