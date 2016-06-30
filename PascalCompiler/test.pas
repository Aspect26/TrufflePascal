var
    i:integer;

BEGIN
    i:=1;
{
    while true do
        writeln('my first infinite loop!');
}

    while i<10 do
    begin
        writeln('i:',i);
        i:=i+1;
    end;
END.
