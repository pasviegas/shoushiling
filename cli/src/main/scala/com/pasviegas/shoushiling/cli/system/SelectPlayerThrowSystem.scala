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

import com.pasviegas.shoushiling.cli.system.exceptions._
import com.pasviegas.shoushiling.cli.system.inputs.{GameInput, SelectAdversaryMoveToThrow, SelectHomeMoveToThrow}
import com.pasviegas.shoushiling.cli.system.messages._
import com.pasviegas.shoushiling.cli.system.stages.{AdversaryPlayerChooseMoveToThrow, PlayTheGame}
import com.pasviegas.shoushiling.cli.system.support.ComputerPlayer
import com.pasviegas.shoushiling.cli.{GameMode, GameState}
import com.pasviegas.shoushiling.core.GamePlay.{Move, Throw}
import com.pasviegas.shoushiling.core.engine.Game

import scala.util.{Failure, Random, Success, Try}

case class SelectPlayerThrowSystem(seed: Random) extends AGameSystem {

  def request: PartialFunction[GameInput, Try[GameState]] = {
    case SelectHomeMoveToThrow(state, Throw(Move(move))) if checkUnknown(move, state.game) =>
      Failure(UnknownMoveSelected)
    case SelectAdversaryMoveToThrow(state, Throw(Move(move))) if checkUnknown(move, state.game) =>
      Failure(UnknownMoveSelected)
    case SelectHomeMoveToThrow(state, thrown) => state.mode match {
      case Some(GameMode("single")) => selectSinglePlayerHome(state, thrown)
      case Some(GameMode("multi")) => selectMultiPlayerHome(state, thrown)
      case Some(_) => Failure(UnknownGameModeSelected)
      case None => Failure(NoGameModeSelected)
    }
    case SelectAdversaryMoveToThrow(state, thrown) =>
      selectAdversaryMoveToThrow(state, thrown)
  }

  private def checkUnknown(move: String, game: Option[Game]) =
    !game.get.rules.exists(_.winner.name == move)

  private def selectSinglePlayerHome(state: GameState, thrown: Throw) =
    Success(state.copy(
      `match` = state.preMatch.copy(homeThrow = Some(thrown), adversaryThrow = Some(randomThrow(state))).asMatch,
      message = Some(SinglePlayerMoveSelectedMessage),
      nextStage = PlayTheGame()
    ))

  private def randomThrow(state: GameState): Throw =
    ComputerPlayer(seed).throws(state.game.get)

  private def selectMultiPlayerHome(state: GameState, thrown: Throw) =
    Success(state.copy(
      preMatch = state.preMatch.copy(homeThrow = Some(thrown)),
      message = Some(HomePlayerMoveSelectedMessage(state)),
      nextStage = AdversaryPlayerChooseMoveToThrow()
    ))

  private def selectAdversaryMoveToThrow(state: GameState, thrown: Throw) =
    Success(state.copy(
      `match` = state.preMatch.copy(adversaryThrow = Some(thrown)).asMatch,
      message = Some(AdversaryPlayerMoveSelectedMessage),
      nextStage = PlayTheGame()
    ))
}
