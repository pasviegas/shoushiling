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
import com.pasviegas.shoushiling.cli.system.inputs._
import com.pasviegas.shoushiling.cli.system.messages.WelcomeMessage
import com.pasviegas.shoushiling.cli.test.ShoushilingValues
import com.pasviegas.shoushiling.core.GamePlay.{Player, Throw}
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.Try

class GameSystemTest extends FlatSpec with MustMatchers with ShoushilingValues {

  "Game system" should "respond to StartGame input" in {
    (TestGameSystem isDefinedAt StartGame(GameState())) must be(true)
  }

  "Game system" should "respond to SinglePlayerMode input" in {
    (TestGameSystem isDefinedAt SelectPlayerMode(GameState(), SinglePlayer)) must be(true)
  }

  "Game system" should "respond to MultiPlayerMode input" in {
    (TestGameSystem isDefinedAt SelectPlayerMode(GameState(), MultiPlayer)) must be(true)
  }

  "Game system" should "respond to SelectHomeMoveToThrow input" in {
    val game = GameState(game = RockPaperScissors)

    (TestGameSystem isDefinedAt SelectHomeMoveToThrow(game, Throw(Rock))) must be(true)
  }

  "Game system" should "respond to SelectAdversaryMoveToThrow input" in {
    val game = GameState(game = RockPaperScissors)
    val moveToThrow: SelectAdversaryMoveToThrow = SelectAdversaryMoveToThrow(game, Throw(Rock))

    (TestGameSystem isDefinedAt moveToThrow) must be(true)
  }

  "Game system" should "respond to Play input" in {
    (TestGameSystem isDefinedAt Play(GameState())) must be(true)
  }

  "Game system" should "behave as a partial function" in {
    (TestGameSystem apply StartGame(GameState())).get.message must be(Some(WelcomeMessage))
  }

  "A player" should "be able to play a full multi player game" in {
    val finalGame: Try[GameState] = for {
      gameStarted <- TestGameSystem request StartGame(GameState())
      modeChosen <- TestGameSystem request SelectPlayerMode(gameStarted, MultiPlayer)
      homeThrowChosen <- TestGameSystem request SelectHomeMoveToThrow(modeChosen, Throw(Rock))
      throwsChosen <- TestGameSystem request SelectAdversaryMoveToThrow(homeThrowChosen, Throw(Scissors))
      gamePlayed <- TestGameSystem request Play(throwsChosen)
    } yield gamePlayed

    finalGame.get.outcome.get.winner.get must be(Player("1", Throw(Rock)))
  }

  "A player" should "be able to play a full single player game" in {
    val finalGame: Try[GameState] = for {
      gameStarted <- TestGameSystem request StartGame(GameState())
      modeChosen <- TestGameSystem request SelectPlayerMode(gameStarted, SinglePlayer)
      throwsChosen <- TestGameSystem request SelectHomeMoveToThrow(modeChosen, Throw(Rock))
      gamePlayed <- TestGameSystem request Play(throwsChosen)
    } yield gamePlayed

    finalGame.get.outcome mustBe defined
  }

}

