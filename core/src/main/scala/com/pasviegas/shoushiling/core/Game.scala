// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or
// distribute this software, either in source code form or as a compiled
// binary, for any purpose, commercial or non-commercial, and by any
// means.
//
// In jurisdictions that recognize copyright laws, the author or authors
// of this software dedicate any and all copyright interest in the
// software to the public domain. We make this dedication for the benefit
// of the public at large and to the detriment of our heirs and
// successors. We intend this dedication to be an overt act of
// relinquishment in perpetuity of all present and future rights to this
// software under copyright law.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
//
// For more information, please refer to <http://unlicense.org/>
package com.pasviegas.shoushiling.core

import com.pasviegas.shoushiling.core.GameRules.GameRule

case class Game(rules: Set[GameRule] = Set()) {

  import scala.PartialFunction._

  def play(`match`: Match): GameOutcome =
    findIfHomeIsWinner(`match`)
      .orElse(findIfTie(`match`))
      .getOrElse(adversaryWon(`match`))

  private def adversaryWon(`match`: Match) =
    Win(`match`, Some(`match`.adversary))

  private def findIfHomeIsWinner(`match`: Match) =
    rules.find(winningRule(`match`))
      .map(rule => Win(`match`, Some(`match`.home)))

  private def winningRule(`match`: Match) =
    (rule: GameRule) =>
      (rule.winner == `match`.home.throws.move) &&
        (rule.loser == `match`.adversary.throws.move)

  private def findIfTie(`match`: Match) =
    condOpt(`match`.home.throws.move == `match`.adversary.throws.move) { case true => Tie(`match`) }

}
