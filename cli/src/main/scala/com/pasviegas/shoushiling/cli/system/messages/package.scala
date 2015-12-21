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
package com.pasviegas.shoushiling.cli.system

import com.pasviegas.shoushiling.cli.GameState
import com.pasviegas.shoushiling.core.engine.GameOutcome

package object messages {

  private def moves(state: GameState) =
    state.game.get.rules.map(_.winner.name.name).mkString(", ")

  trait Message

  case class SinglePlayerSelectedMessage(state: GameState) extends Message {
    override def toString: String =
      s"You have selected single player, what will be your move? (${moves(state)})"
  }

  case class MultiPlayerSelectedMessage(state: GameState) extends Message {
    override def toString: String =
      s"You have selected multi player, what will be the first player's move? (${moves(state)})"
  }

  case class HomePlayerMoveSelectedMessage(state: GameState) extends Message {
    override def toString: String =
      s"What will be the adversary's move? (${moves(state)})"
  }

  case class GameOverMessage(outcome: Option[GameOutcome]) extends Message {
    override def toString: String =
      s"${outcome.get}! Good game! Press enter to exit!"
  }

  case object WelcomeMessage extends Message {
    override def toString: String =
      "Welcome to Shoushiling, please type the game mode: (Single, Multi)"
  }

  case object SinglePlayerMoveSelectedMessage extends Message {
    override def toString: String =
      "Good choice! Press enter to see the match result!"
  }

  case object AdversaryPlayerMoveSelectedMessage extends Message {
    override def toString: String =
      "Good choice! Press enter to see the match result!"
  }

}
