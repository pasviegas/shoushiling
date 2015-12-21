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

import com.pasviegas.shoushiling.cli.system.exceptions.UnknownGameModeSelected
import com.pasviegas.shoushiling.cli.system.inputs.{GameInput, SelectPlayerMode}
import com.pasviegas.shoushiling.cli.system.messages.{MultiPlayerSelectedMessage, SinglePlayerSelectedMessage}
import com.pasviegas.shoushiling.cli.system.stages.HomePlayerChooseMoveToThrow
import com.pasviegas.shoushiling.cli.{GameMode, GameState}

import scala.util.{Failure, Success, Try}

case object SelectPlayerModeSystem extends AGameSystem {

  def request: PartialFunction[GameInput, Try[GameState]] = {
    case SelectPlayerMode(state, GameMode("single")) => selectSinglePlayer(state)
    case SelectPlayerMode(state, GameMode("multi")) => selectMultiPlayer(state)
    case SelectPlayerMode(state, GameMode(_)) => Failure(UnknownGameModeSelected)
  }

  private def selectSinglePlayer(state: GameState) =
    Success(state.copy(
      mode = Some(GameMode("single")),
      message = Some(SinglePlayerSelectedMessage(state)),
      nextStage = HomePlayerChooseMoveToThrow()
    ))

  private def selectMultiPlayer(state: GameState) =
    Success(state.copy(
      mode = Some(GameMode("multi")),
      message = Some(MultiPlayerSelectedMessage(state)),
      nextStage = HomePlayerChooseMoveToThrow()
    ))
}
