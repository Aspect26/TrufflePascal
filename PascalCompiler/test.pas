var
    i:integer;

BEGIN
    i:=1;
{
    while true do
        writeln('my first infinite loop!');
}

    writeln('WHILE');
    while i<10 do
    begin
        writeln('i:',i);
        i:=i+1;
    end;

    writeln('REPEAT');
    repeat
        writeln('i:',i);
        i:=i-1;
    until i<6;
END.
