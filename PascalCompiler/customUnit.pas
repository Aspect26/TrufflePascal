UNIT customUnit;

INTERFACE

function RA(w,h:integer):integer;
procedure Hello;

IMPLEMENTATION

function RA(w,h:integer):integer;
var i:integer;
begin
 RA:=w*w;
end;

procedure Hello;
begin
 writeln('Hello World!');
end;

endunit.
