UNIT testunit;



INTERFACE
 var a:integer;
 procedure wb;
 procedure wb2;
 type Color = (red,green,blue);
 function factorial(n:integer):integer;


IMPLEMENTATION
 var b:integer;
 var red:integer;

 function factorial(n:integer):integer;
 begin
  if(n<2) then factorial:=1
  else factorial:=n*factorial(n-1);
 end;
 
 procedure notseen;
 begin
  b:=5;
  writeln(b);
 end;
 
 procedure wb2;
 begin
 end;

 procedure wb;
 begin
  a:=8;
  writeln(b);
  notseen();
  writeln(b);
 end;



END.
