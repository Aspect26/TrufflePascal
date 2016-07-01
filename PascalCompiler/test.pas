var
    i:integer;

BEGIN
    i:=1;

    while i<10 do
    begin
        writeln('i:',i);
        i:=i+1;
        if i>6 then break;
    end;
END.
