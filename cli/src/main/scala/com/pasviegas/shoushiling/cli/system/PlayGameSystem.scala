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
import com.pasviegas.shoushiling.cli.system.exceptions.{GameHasNoMatch, GameHasNotBeenConfigured}
import com.pasviegas.shoushiling.cli.system.inputs.{GameInput, Play}
import com.pasviegas.shoushiling.cli.system.messages.GameOverMessage
import com.pasviegas.shoushiling.cli.system.stages.GameOver
import com.pasviegas.shoushiling.core.GamePlay.Match
import com.pasviegas.shoushiling.core.engine.Game

import scala.util.{Failure, Success, Try}

case object PlayGameSystem extends AGameSystem {

  def request: PartialFunction[GameInput, Try[GameState]] = {
    case Play(state) => (state.game, state.`match`) match {
      case (Some(game), Some(players)) => play(state, game, players)
      case (Some(game), None) => Failure(GameHasNoMatch)
      case (None, Some(players)) => Failure(GameHasNotBeenConfigured)
      case (None, None) => Failure(GameHasNotBeenConfigured)
    }
  }

  private def play(state: GameState, game: Game, players: Match) =
    Success(state.copy(
      outcome = Some(game.play(players)),
      message = Some(GameOverMessage),
      nextStage = GameOver()
    ))
}
