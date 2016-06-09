var
    a,b,d:integer;
    c:longint;
    e:boolean;

BEGIN
    // *** BLOCK 1,2 (write + arithmetics)
    {
    a:=10;
    B:=6;
    c:=8000000000000000000;
    d:=a+b;
    writeln(-2+5+5);
    writeln(c);
    writeln(5------------------------------5);
    WriTeLn(5);
    writeln(5,5);
    WRITELN('---------------------');
    writeln( (a+b) MoD 3); 
    WRItE('A', 'B', 'C');
    //write('''');
    }

    // *** BLOCK 3 (boolean + controlflow)
    a:=42;
    if a<>42 then begin
        b:=53;
        c:=54;
    end else
        b:=64;
    
    c:=65;
    
    writeln(b,' ',c);
END.
