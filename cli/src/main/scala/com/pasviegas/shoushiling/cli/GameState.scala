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
package com.pasviegas.shoushiling.cli

import com.pasviegas.shoushiling.cli.system.messages.Message
import com.pasviegas.shoushiling.cli.system.stages.{GameStarted, Stage}
import com.pasviegas.shoushiling.core.GamePlay.{Match, Player, Throw}
import com.pasviegas.shoushiling.core.engine.{Game, GameOutcome}

case class GameMode(name: String)

case class PreMatch(homeThrow: Option[Throw] = None, adversaryThrow: Option[Throw] = None) {
  def asMatch: Option[Match] = for {
    player1Throw <- homeThrow
    player2Throw <- adversaryThrow
  } yield Match(Player("1", player1Throw), Player("2", player2Throw))
}

case class GameState(
  mode: Option[GameMode] = None,
  preMatch: PreMatch = PreMatch(),
  `match`: Option[Match] = None,
  message: Option[Message] = None,
  game: Option[Game] = None,
  outcome: Option[GameOutcome] = None,
  nextStage: Stage = GameStarted()
)
