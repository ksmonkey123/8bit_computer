package ch.awae.custom8bit.assembler.parser

import org.antlr.v4.runtime.*

class ParsingException(rule: ParserRuleContext) : RuntimeException(
    "parse error at ${rule.start.line}:${rule.start.charPositionInLine}: ${rule.text}"
)