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

import com.pasviegas.shoushiling.cli.system.inputs._
import com.pasviegas.shoushiling.cli.{GameMode, GameState}
import com.pasviegas.shoushiling.core.GamePlay.{Move, Throw}

package object stages {

  trait Stage {
    def input(state: GameState, userInput: Option[String] = None): Option[GameInput]
  }

  case class GameStarted() extends Stage {
    def input(state: GameState, userInput: Option[String]): Option[GameInput] =
      Some(StartGame(state))
  }

  case class ChooseGameMode() extends Stage {
    def input(state: GameState, userInput: Option[String]): Option[GameInput] =
      Some(SelectPlayerMode(state, gameModeFromUserInput(userInput)))
  }

  case class HomePlayerChooseMoveToThrow() extends Stage {
    def input(state: GameState, userInput: Option[String]): Option[GameInput] =
      Some(SelectHomeMoveToThrow(state, throwFromUserInput(userInput)))
  }

  case class AdversaryPlayerChooseMoveToThrow() extends Stage {
    def input(state: GameState, userInput: Option[String]): Option[GameInput] =
      Some(SelectAdversaryMoveToThrow(state, throwFromUserInput(userInput)))
  }

  case class PlayTheGame() extends Stage {
    def input(state: GameState, userInput: Option[String]): Option[GameInput] =
      Some(Play(state))
  }

  case class GameOver() extends Stage {
    def input(state: GameState, userInput: Option[String]): Option[GameInput] = None
  }

  private def throwFromUserInput(userInput: Option[String]) =
    Throw(Move(Symbol(userInput.get)))

  private def gameModeFromUserInput(userInput: Option[String]) =
    GameMode(Symbol(userInput.get.toLowerCase()))

}
