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

listOfNumbers: numericExpression (',' listOfNumbers)?;

fieldDeclaration
    : SYMBOL '[' size=numericExpression ']' #fieldWithSize
    ;

statement
    : label EOL* instruction #labelledInstruction
    | instruction       #normalInstruction
    ;

label: SYMBOL ':';
instruction
    : binaryAluInstruction
    | unaryAluInstruction
    | swapInstruction
    | shiftInstruction
    | moveInstruction
    | branchInstruction
    | simpleInstruction
    | stackManipulationInstruction
    ;

binaryAluInstruction: operation=binaryAluOp source=binaryAluOpSrc;
binaryAluOp: 'and' | 'ior' | 'xor' | 'adc' | 'sbc' | 'cmp';
binaryAluOpSrc
    : register8 #binaryAluOpRegisterSource
    | literalValue #binaryAluOpLiteralSource
    | addressingExpression # binaryAluOpAddressingSource
    ;
unaryAluInstruction: operation=unaryAluOp register8;
unaryAluOp: 'not' | 'inc' | 'dec' | 'neg';

shiftInstruction: 'rlc' | 'rld' | 'rra' | 'rrc' | 'rrd';

swapInstruction: 'swp' (register8NotA | addressingExpression);

stackManipulationInstruction : op=('spa'|'spf') size=numericExpression;

moveInstruction
    : 'mov' to=register8 from=register8 #movCopy8
    | 'mov' to=register16 from=register16 #movCopy16
    | 'mov' to=register from=moveSource #movLoad
    | 'mov' to=moveTarget from=register #movStore
    ;
moveTarget: addressingExpression;
moveSource
    : literalValue #literalMoveSource
    | addressingExpression #addressedMoveSource
    ;

branchInstruction: operation=('bcc' | 'bcs' | 'bez' | 'bnz' | 'blz' | 'bgz' | 'bnp' | 'bnn' | 'jmp' | 'jsr') branchTarget;
simpleInstruction: operation=('ret' | 'nop' | 'hlt' | 'cfc' | 'cfs' );

branchTarget: SYMBOL;
register8NotA: REGISTER_B | REGISTER_C | REGISTER_D;
register: REGISTER_A | REGISTER_B | REGISTER_C | REGISTER_D | REGISTER_AB | REGISTER_CD;
register8: REGISTER_A | REGISTER_B | REGISTER_C | REGISTER_D;
register16: REGISTER_AB | REGISTER_CD;
literalValue: '#' numericExpression;
addressingExpression
    : '*' numericExpression #literalAdrExpr
    | '*' SYMBOL #symbolAdr
    | '*SP' # stackAdrExpr
    | '*CD' #dynamicAdrExpr
    | '*(' complexAddressingExpression ')' #complexAddressing
    ;

complexAddressingExpression
    : 'CD' '+' numericExpression #registerOffsetAddressing
    | 'SP' '+' numericExpression #stackOffsetAddressing
    | SYMBOL '+' numericExpression #symbolOffsetAddressing
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