UNIT graph;
uses graphcorejava;

INTERFACE

{
Draws a recrangle with topleft corner at (x1, y1) and bottom right corner at (x2, y2)
}
procedure Bar(x1, y1, x2, y2: integer);

{
Draws a 3d bar with topleft corner at (x1, y1) and bottom right corner at (x2, y2). Depth specifies the number of pixels
used to show the depth of the bar. If Top is true; then a 3-dimensional top is drawn.
}
procedure Bar3D(x1, y1, x2, y2, depth: integer; top: boolean);

IMPLEMENTATION

var currentColor: integer;
var currentXPosition: integer;
var currentYPosition: integer;

procedure Bar(x1, y1, x2, y2: integer);
var x,y: integer;
begin
    currentColor := 123458;
    for x := x1 to x2 do begin
        putPixel(x, y1, currentColor);
        putPixel(x, y2, currentColor);
    end;

    for y := y1 to y2 do begin
        putPixel(x1, y, currentColor);
        putPixel(x2, y, currentColor);
    end;
end;

procedure Bar3D(x1, y1, x2, y2, depth: integer; top: boolean);
var x, y, z:integer;
begin
    Bar(x1, y1, x2, y2);
    for z := 0 to depth do begin
        putPixel(x1 + z, y1 - z, currentColor);
        putPixel(x2 + z, y1 - z, currentColor);
        putPixel(x2 + z, y2 - z, currentColor);
    end;

    for x:=x1 + depth to x2 + depth do begin
        putPixel(x, y1 - depth, currentColor);
    end;

    for y:=y1 - depth to y2 - depth do begin
        putPixel(x2 + depth, y, currentColor);
    end;
end;

END.
