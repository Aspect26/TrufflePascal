UNIT testunit;



INTERFACE
 var a:integer;
 procedure wb;
 procedure wb2;



IMPLEMENTATION
 var b:integer;
 
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
