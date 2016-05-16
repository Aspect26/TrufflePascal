VAR
    a,b:long;

BEGIN
    a:=10;
    b:=6;
    writeln(a+b);
    writeln(a-b);
    writeln(a*b);
    writeln(a div b);
    writeln(a + a * b);
    writeln(a + a * b - a div b);

    //TODO: multiple arguments for writeln
    writeln('A', 'B', 'C');
END.
