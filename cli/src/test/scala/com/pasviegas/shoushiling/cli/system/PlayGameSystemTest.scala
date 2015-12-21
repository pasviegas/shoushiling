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
import com.pasviegas.shoushiling.cli.system.inputs.Play
import com.pasviegas.shoushiling.cli.system.messages.GameOverMessage
import com.pasviegas.shoushiling.cli.system.stages.GameOver
import com.pasviegas.shoushiling.core.GamePlay.{Match, Player, Throw}
import com.pasviegas.shoushiling.core.engine.Tie
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.{Failure, Try}

class PlayGameSystemTest extends FlatSpec with MustMatchers {

  "Players" should "be able to play the game and see the outcome" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Paper)),
      Player("2", Throw(Paper))
    )

    val defaultGame = GameState(
      `match` = Some(championshipFinale),
      game = Some(DefaultGame)
    )

    (PlayGameSystem request Play(defaultGame)).get.outcome mustBe defined
  }

  "If there is no match set, the game" should "not be played" in {
    import com.pasviegas.shoushiling.core._

    val stateWithNoMatch = GameState(game = Some(DefaultGame))

    (PlayGameSystem request Play(stateWithNoMatch)) must be(Failure(GameHasNoMatch))
  }

  "If there if no game rules are set, the game" should "not be played" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Paper)),
      Player("2", Throw(Paper))
    )

    val stateWithNoMatch = GameState(`match` = Some(championshipFinale))

    (PlayGameSystem request Play(stateWithNoMatch)) must be(Failure(GameHasNotBeenConfigured))
  }

  "If there if no game rules and no match are set, the game" should "not be played" in {
    (PlayGameSystem request Play(GameState())) must be(Failure(GameHasNotBeenConfigured))
  }

  "After the the match is played, the players" should "receive a feedback message" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Paper)),
      Player("2", Throw(Paper))
    )

    val defaultGame = GameState(
      `match` = Some(championshipFinale),
      game = Some(DefaultGame)
    )

    (PlayGameSystem request Play(defaultGame))
      .get.message must be(Some(GameOverMessage(Some(Tie(championshipFinale)))))
  }

  "After the the match is played, game" should "end" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Paper)),
      Player("2", Throw(Paper))
    )

    val defaultGame = GameState(
      `match` = Some(championshipFinale),
      game = Some(DefaultGame)
    )

    (PlayGameSystem request Play(defaultGame)).get.nextStage must be(GameOver())
  }

  "After the the match is played, the user" should "receive a feedback message" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Paper)),
      Player("2", Throw(Paper))
    )

    val defaultGame = GameState(
      `match` = Some(championshipFinale),
      game = Some(DefaultGame)
    )

    val gameOverState: Try[GameState] = PlayGameSystem request Play(defaultGame)
    gameOverState.get.message must be(Some(GameOverMessage(gameOverState.get.outcome)))
  }

}

