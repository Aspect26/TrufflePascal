var
    i:integer;

BEGIN
    i:=1;

    while i<10 do
    begin
        writeln('i:',i);
        i:=i+1;
        if i>6 then begin 
            break;
        end;
    end;

    case i of
        6: writeln('case is 6');
        7: begin writeln('case is 7'); writeln('case is 7'); end;
        8: writeln('case is 8')
    end;    
    
END.
