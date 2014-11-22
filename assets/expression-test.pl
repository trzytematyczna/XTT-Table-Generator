xtype [name: numeric,
       base: numeric,
       length: 10,
       desc: 'Numeric value',
       domain: [-2147483647 to 2147483647]
      ].

xtype [name: case,
       base: symbolic,
       desc: 'Symbolic value',
       domain: [add, subtract, multiply, divide, brackets, precedence, associavity, wacko]
      ].

%%%%%%%%%%%%%%%%%%%%%%%%% ATTRIBUTES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%

xattr [name: x,
       abbrev: x,
       class: simple,
       type: numeric
      ].

xattr [name: y,
       abbrev: y,
       class: simple,
       type: numeric
      ].

xattr [name: z,
       abbrev: z,
       class: simple,
       type: numeric
      ].

xattr [name: result,
       abbrev: r,
       class: simple,
       type: numeric
      ].


xattr [name: switch,
       abbrev: s,
       class: simple,
       type: case
      ].


%%%%%%%%%%%%%%%%%%%%%%%% TABLE SCHEMAS DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%

xschm 'Test': [s] ==> [r].

%%%%%%%%%%%%%%%%%%%%%%%%%%%% RULES DEFINITIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%

xrule 'Test'/add: 
    [s eq add] ==> [r set x + y].
xrule 'Test'/subtract:
    [s eq subtract] ==> [r set x - y].
xrule 'Test'/multiply:
    [s eq multiply] ==> [r set x * y].
xrule 'Test'/divide:
    [s eq divide] ==> [r set x / y].
xrule 'Test'/brackets:
    [s eq brackets] ==> [r set (x + y) * (x + z)].
xrule 'Test'/precedence:
    [s eq precedence] ==> [r set x + y * y - z / z].
xrule 'Test'/associavity:
    [s eq associavity] ==> [r set x - y - z + x + y + z].
xrule 'Test'/wacko:
    [s eq wacko] ==> [r set (x + (x + y * z)) * ((x - z) + y)].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%