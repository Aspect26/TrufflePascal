UNIT customUnit;

INTERFACE

function RA(w,h:integer):integer;
procedure Hello;
var c:integer;

IMPLEMENTATION
function RA(w,h:integer):integer;
var i:integer;
begin
 RA:=w*w;
end;

procedure Hello;
begin
 c:=5;
 writeln('Hello World!');
 writeln(c);
end;

end.
