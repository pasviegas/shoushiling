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
import com.pasviegas.shoushiling.cli.system.stages.ChooseGameMode
import com.pasviegas.shoushiling.cli.test.ShoushilingValues
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.Random

class GameLoopTest extends FlatSpec with MustMatchers with ShoushilingValues {

  "A single player Game" should "loop through its stages until reaching the end" in {
    val gameLoop: Option[GameLoop] = for {
      started <- GameLoop(GameSystem(new Random), GameState()).next()
      chooseMode <- started.next(Some("single"))
      chooseThrow <- chooseMode.next(Some("Rock"))
      play <- chooseThrow.next()
      endGame <- play.next()
    } yield endGame

    gameLoop must be(None)
  }

  "A multi player Game" should "loop through its stages until reaching the end" in {
    val gameLoop: Option[GameLoop] = for {
      started <- GameLoop(GameSystem(new Random), GameState()).next()
      chooseMode <- started.next(Some("multi"))
      player1Throw <- chooseMode.next(Some("Rock"))
      player2Throw <- player1Throw.next(Some("Rock"))
      play <- player2Throw.next()
      endGame <- play.next()
    } yield endGame

    gameLoop must be(None)
  }

  "The a Game" should "retry if the user tried something wrong" in {
    val retriedLoop: Option[GameLoop] = for {
      started <- GameLoop(GameSystem(new Random), GameState()).next()
      chooseMMORPGMode <- started.next(Some("MMORPG"))
    } yield chooseMMORPGMode

    retriedLoop.get.state.nextStage must be(ChooseGameMode())
  }

  "A Game" should "start and go through all stages until reaching the end" in {
    var inputs = List("single", "Rock")

    GameLoop.start((lopp) => {}, () => inputs match {
      case head :: tail =>
        inputs = tail
        Some(head)
      case Nil => None
    })

    inputs must be(Nil)
  }

  "A custom Game" should "start and go through all stages until reaching the end" in {
    var inputs = List("single", "Lizard")

    val args: Array[String] = Array(correctGame)
    GameLoop.start(args, (lopp) => {}, () => inputs match {
      case head :: tail =>
        inputs = tail
        Some(head)
      case Nil => None
    })

    inputs must be(Nil)
  }
}
