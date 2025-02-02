grammar Assembler;

program: (section)+ EOF;

section
    : '.code' startAt=numericLiteral EOL+ (statement EOL+)+ #codeSection
    | '.vars' startAt=numericLiteral? EOL+ (variableStatement EOL+)+ #variableSection
    | '.data' EOL+ (dataStatement EOL+)+ #dataSection
    ;

variableStatement
    : fieldDeclaration #variableDeclaration
    | pos=numericLiteral ':' fieldDeclaration #variableDeclarationWithFixedPosition
    ;

dataStatement
    : fieldDeclaration '=' value=numericExpression #simpleConstant
    | fieldDeclaration '=' '[' value=listOfNumbers ']' #arrayConstant
    ;

listOfNumbers
    : numericExpression ',' listOfNumbers #listWithContinuation
    | numericExpression #finalElementOfList
    ;

fieldDeclaration
    : SYMBOL '[' size=numericExpression ']' #fieldWithSize
//    | SYMBOL #simpleField
    ;

statement
    : label EOL* instruction #labelledInstruction
    | instruction       #normalInstruction
    ;

label: SYMBOL ':';
instruction
    : binaryAluInstruction
    | unaryAluInstruction
    | moveInstruction
    | stackInstruction
    | branchInstruction
    | simpleInstruction
    ;

binaryAluInstruction: operation=binaryAluOp source=binaryAluOpSrc;
binaryAluOp: 'and' | 'ior' | 'xor' | 'add' | 'sub';
binaryAluOpSrc
    : register8 #binaryAluOpRegisterSource
    | literalValue #binaryAluOpLiteralSource
    | addressingExpression # binaryAluOpAddressingSource
    ;

unaryAluInstruction: operation=unaryAluOp register8;
unaryAluOp: 'not' | 'shl' | 'rcl' | 'rl' | 'ushr' | 'ashr' | 'rrc' | 'rr' | 'inc' | 'dec' | 'comp' | 'swap';

moveInstruction
    : 'mov' to=register from=register   #movCopy
    | 'mov' to=register from=moveSource #movLoad
    | 'mov' to=moveTarget from=register #movStore
    ;
moveTarget: addressingExpression;
moveSource
    : literalValue #literalMoveSource
    | addressingExpression #addressedMoveSource
    ;

stackInstruction: operation=('push' | 'pop' | 'peek') register;
branchInstruction: operation=('bcc' | 'bcs' | 'bz' | 'bnz' | 'blz' | 'bgz' | 'blez' | 'bgez' | 'goto' | 'call') branchTarget;
simpleInstruction: operation=('return' | 'nop' | 'halt' | 'cclr' | 'cset' );

branchTarget: SYMBOL;
register: REGISTER_A | REGISTER_B | REGISTER_C | REGISTER_D | REGISTER_AB | REGISTER_CD;
register8: REGISTER_A | REGISTER_B | REGISTER_C | REGISTER_D;
literalValue: '#' numericExpression;
addressingExpression
    : '*' numericExpression #literalAdrExpr
    | '*' SYMBOL #symbolAdr
    | '*CD' #dynamicAdrExpr
    ;

numericExpression: numericLiteral;

numericLiteral: NUMBER;

REGISTER_A: 'A';
REGISTER_B: 'B';
REGISTER_C: 'C';
REGISTER_D: 'D';

REGISTER_AB: 'AB';
REGISTER_CD: 'CD';

SYMBOL: [a-zA-Z_]([a-zA-Z_0-9])*;
NUMBER
    : '0b'('_'?[01])+
    | '0x'('_'?[0-9a-fA-F])+
    | [0-9]('_'?[0-9])*
    ;
COMMENT: ';' ~[\r\n]* -> channel(HIDDEN);
EOL: '\r'? '\n';
WS: [ \t]+ -> skip;