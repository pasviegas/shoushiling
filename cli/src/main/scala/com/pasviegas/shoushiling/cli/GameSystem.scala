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

import com.pasviegas.shoushiling.cli.Exceptions.{GameHasNoMatch, GameHasNotBeenConfigured}
import com.pasviegas.shoushiling.cli.Inputs._
import com.pasviegas.shoushiling.cli.Messages._
import com.pasviegas.shoushiling.core.GamePlay.Throw

import scala.util.{Failure, Success, Try}

case class GameSystem() {

  import com.pasviegas.shoushiling.core._

  def request(input: GameInput): Try[GameState] = input match {
    case StartGame(state) => startGame(state)
    case SinglePlayerMode(state) => selectSinglePlayer(state)
    case MultiPlayerMode(state) => selectMultiPlayer(state)
    case SelectHomeMoveToThrow(state, thrown) => selectHomeMoveToThrow(state, thrown)
    case SelectAdversaryMoveToThrow(state, thrown) => selectAdversaryMoveToThrow(state, thrown)
    case Play(state) => play(state)
  }

  private def startGame(state: GameState) =
    Success(state.copy(
      started = true,
      message = Some(WelcomeMessage),
      game = Some(DefaultGame)
    ))

  private def selectSinglePlayer(state: GameState) =
    Success(state.copy(
      mode = Some(SinglePlayer),
      message = Some(SinglePlayerSelectedMessage)
    ))

  private def selectMultiPlayer(state: GameState) =
    Success(state.copy(
      mode = Some(MultiPlayer),
      message = Some(MultiPlayerSelectedMessage)
    ))

  private def selectHomeMoveToThrow(state: GameState, thrown: Throw) =
    Success(state.copy(
      `match` = state.`match`.map(players => players.copy(home = players.home.copy(throws = thrown))),
      message = Some(HomePlayerMoveSelectedMessage)
    ))

  private def selectAdversaryMoveToThrow(state: GameState, thrown: Throw) =
    Success(state.copy(
      `match` = state.`match`.map(players => players.copy(adversary = players.adversary.copy(throws = thrown))),
      message = Some(AdversaryPlayerMoveSelectedMessage)
    ))

  private def play(state: GameState) =
    (state.game, state.`match`) match {
      case (Some(game), Some(players)) => Success(state.copy(outcome = Some(game.play(players))))
      case (Some(game), None) => Failure(GameHasNoMatch)
      case (None, Some(players)) => Failure(GameHasNotBeenConfigured)
      case (None, None) => Failure(GameHasNotBeenConfigured)
    }

}
