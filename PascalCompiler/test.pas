uses customUnit;

function factorial(a:integer):integer;
begin
 if a < 2 then 
  begin factorial := 1; end
 else 
  begin factorial := a*factorial(a-1); end;
end;

var a:integer;

begin
 writeln(factorial(7));
 c:=8;
 Hello();
 writeln(c);

 while(true) do
    break;

end.
