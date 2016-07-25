UNIT customUnit;

INTERFACE

function RA(w,h:integer):integer;
procedure Hello;

IMPLEMENTATION
var c:integer;
function RA(w,h:integer):integer;
var i:integer;
begin
 RA:=w*w;
end;

procedure Hello;
begin
 writeln('Hello World!');
 writeln(c);
end;

end.
