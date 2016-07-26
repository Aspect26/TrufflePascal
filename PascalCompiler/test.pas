uses customUnit;

function factorial(a:integer):integer;
begin
 if a < 2 then 
  begin factorial := 1; end
 else 
  begin factorial := a*factorial(a-1); end;
end;

function fib(a:integer):integer;
begin
 if a<2 then fib:=1
 else fib:=fib(a-1) + fib(a-2)
end;

var a:integer;

begin
 writeln(factorial(12));
 writeln(fib(30));
 
 //if 'a' then writeln('roflmao!');
 //c:='a';
 //c:='a'+5;
5+5;
 c:=8;
 Hello();
 writeln(c);

 while(true) do
    break;

 case 5040 of
    factorial(7): writeln('b');
    factorial(5): writeln('c')
 end;
end.
