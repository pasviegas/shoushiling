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

import com.pasviegas.shoushiling.cli.system.GameSystem

import scala.util.control.Breaks._
import scala.util.{Failure, Random, Success}

case class GameLoop(system: GameSystem, state: GameState) {

  def next(userInput: Option[String] = None): Option[GameLoop] =
    state.nextStage.input(state, userInput).map(system(_)).flatMap {
      case Success(nextState) => Some(GameLoop(system, nextState))
      case Failure(exception) => Some(GameLoop(system, state))
    }.orElse(None)
}

object GameLoop {

  def start(printState: (GameLoop) => Unit, readInput: () => Option[String]): Unit =
    iterate(gameStart, printState, readInput)

  private def gameStart: Option[GameLoop] =
    GameLoop(GameSystem(new Random), GameState()).next()

  private def iterate(gameStart: Option[GameLoop], printState: (GameLoop) => Unit, readInput: () => Option[String]) =
    breakable {
      Stream.iterate(gameStart)(_.flatMap(_.next(readInput()))).foreach {
        case Some(game) =>
          printState(game)
          game
        case None => break()
      }
    }
}
