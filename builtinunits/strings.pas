UNIT strings;
uses stringscorejava;

INTERFACE

{
    Concatenase two strings. The source argument is concatenated to the destinations argument
    which is then returned
}
function strcat(var destination, source: PChar): PChar;

{
    Compares two strings. Returns:
        0: if both strings are equal
        negative integer: if the left operand is shorter than the right operand
        positive integer: if the right operand is shorter than the left operand
}
function strcomp(leftOperand, rightOperand: PChar): integer;

{
    Convert null-terminated string to all-uppercase
}
function strupper(str: PChar): PChar;

IMPLEMENTATION

function getPCharSize(str: PChar): integer; forward;

function strcat(destination, source: PChar): PChar;
begin
    destination := destination + source;
    strcat := destination;
end;

function strcomp(leftOperand, rightOperand: PChar): integer;
var result, leftIndex, rightIndex: integer;
begin
    leftIndex := 0;
    rightIndex := 0;

    while (leftOperand^[leftIndex] <> '\0') or (rightOperand^[rightIndex] <> '\0') do begin
        if leftOperand^[leftIndex] <> '\0' then begin
            result := result + 1;
            leftIndex := leftIndex + 1;
        end;

        if rightOperand^[rightIndex] <> '\0' then begin
            result := result - 1;
            rightIndex := rightIndex + 1;
        end;
    end;

    strcomp := result;
end;

function strupper(str: PChar): PChar;
var i, size: integer;
var res: PChar;
begin
    size := getPCharSize(str);
    res := stralloc(size);
    for i:=0 to size - 1 do begin
        if ord(str^[i]) > 96 and ord(str^[i]) < 123 then begin
            res^[i] := chr(ord(str^[i]) - 32);
        end else begin
            res^[i] := str^[i];
        end;
    end;
    strupper := res;
end;

function getPCharSize(str: PChar): integer;
var size: integer;
begin
    size := 0;
    while str^[size] <> '\0' do
        size := size + 1;

    getPCharSize := size + 1;
end;

END.
