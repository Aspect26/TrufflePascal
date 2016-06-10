var
    c:char;
    b:boolean;
    i:integer;

BEGIN
    b:=true;

    if b then 
    begin
        writeln('true');
    end
    else
    begin
        writeln('false');
    end;

    c:='a';

    writeln(b);
    writeln(c);
END.
