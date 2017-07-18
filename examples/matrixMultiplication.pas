program matrixMultiplication;
uses sysutils;

 const size = BENCH_SIZE;
const iterations = BENCH_ITERS;

type matrixType = array[1..size, 1..size] of integer;

var blackhole : integer; (* Used to prevent FPC from removing unused parts of code *)

procedure dump(var matrix: matrixType);
var i,j : integer;
begin
    for i := 1 to size do begin
        for j := 1 to size do
            write(matrix[i][j], ' ');
        writeln;
    end;
    writeln;
end;

function gen(seed : integer): matrixType;
var i,j: integer;
    r: matrixType;
begin
    for i:= 1 to size do 
        for j:= 1 to size do begin
            seed := seed * 7919 mod 16127 + 7;
            r[i][j] := seed div 8;
        end;
    gen := r;
end;

function multiply(var a, b: matrixType): matrixType;
var i,j,k,tmp: integer;
    res: matrixType;
begin
    for i:= 1 to size do begin
        for j:= 1 to size do begin
            tmp := 0;
            for k := 1 to size do begin
                tmp := tmp + a[i][k] * b[k][j];
            end;
            res[i][j] := tmp;
        end;
    end;
    multiply := res;
end;

function doBenchmark : int64;
var a,b,result:matrixType;
    start, fini:int64;
begin
    a := gen(1234);
    b := gen(5678);
    (* dump(a);
    dump(b); *)
    
    start := getTickCount;
    result := multiply(a,b);
    fini := getTickCount;
    
    blackhole := blackhole + result[1][1];
    
    (* dump(result); *)
    
    doBenchmark := fini - start;
end;


var i: integer;
    results: array[1..iterations] of int64;

begin
    blackhole := 15;
    
    for i := 1 to iterations do
        results[i] := doBenchmark;

    if blackhole = 12 then
        writeln('bench,size,iteration,score')
    else begin
        write('bench,size,');
        writeln('iteration,score');
    end;
    for i := 1 to iterations do
        writeln('matrix,', size, ',', i, ',', results[i]);
end.

