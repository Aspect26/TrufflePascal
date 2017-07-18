(*
    Sample AI script for our Tic Tac Toe implementation. It simply chooses the first empty cell in the play area. 
    The program arguments are cells in the play area. If the cell's value is 0 then it is empty, if it is 1 then player
    occupies this slot and if it is 2 then it is occupied by the AI. The script returns index of the cell it wants to
    play (row * column, indexed from 0 to 2). 
*)
program tttAI(i11, i12, i13, i21, i22, i23, i31, i32, i33);
var i11, i12, i13, i21, i22, i23, i31, i32, i33,i,j: integer;

begin
  if i11 = 0 then exit(0)
  else if i12 = 0 then exit(1)
  else if i13 = 0 then exit(2)
  else if i21 = 0 then exit(3)
  else if i22 = 0 then exit(4)
  else if i23 = 0 then exit(5)
  else if i31 = 0 then exit(6)
  else if i32 = 0 then exit(7)
  else if i33 = 0 then exit(8);
end.
