UNIT string;

INTERFACE

{ 
	Allocates a new space on a heap for a string. Since in our implementation does not use
	null terminated strings, the size argument is not used and is left here only for backwards
	compatibility
}
function stralloc(size: integer): ^string;

{
    Concatenase two strings. The source argument is concatenated to the destinations argument
    which is then returned
}
function strcat(var destination, source: string): string;

{
    Compares two strings. Returns:
        0: if both strings are equal
        negative integer: if the left operand is shorter than the right operand
        positive integer: if the right operand is shorter than the left operand
}
function strcomp(leftOperand, rightOperand: string): integer;


IMPLEMENTATION

function stralloc(size: integer): ^string;
var str: ^string;
begin
    stralloc := new(str);
end;

function strcat(var destination, source: string): string;
begin
    destination = destination + source;
    strcat := destination;
end;

function strcomp(leftOperand, rightOperand: string): integer;
begin
    if leftOperand = rightOperand then
        strcomp := 0
    else if leftOperand < rightOperand then
        strcomp := -1
    else
        strcomp := 1;
end;

END;
