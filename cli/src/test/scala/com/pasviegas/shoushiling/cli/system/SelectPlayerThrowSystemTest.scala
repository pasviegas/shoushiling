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

import com.pasviegas.shoushiling.cli._
import com.pasviegas.shoushiling.cli.system.exceptions._
import com.pasviegas.shoushiling.cli.system.inputs.{SelectAdversaryMoveToThrow, SelectHomeMoveToThrow}
import com.pasviegas.shoushiling.cli.system.messages._
import com.pasviegas.shoushiling.cli.system.stages.{AdversaryPlayerChooseMoveToThrow, PlayTheGame}
import com.pasviegas.shoushiling.cli.test.ShoushilingValues
import com.pasviegas.shoushiling.core.GamePlay.{Move, Throw}
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.{Failure, Random}

class SelectPlayerThrowSystemTest extends FlatSpec with MustMatchers with ShoushilingValues {

  "The home player" must "be able to choose which move to throw" in {
    val game = GameState(mode = Some(SinglePlayer), game = RockPaperScissors)

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Rock)))
      .get.`match`.get.home.throws must be(Throw(Rock))
  }

  "When the home player selects its throw, he" should "receive a feedback message" in {
    val game = GameState(mode = Some(SinglePlayer), game = RockPaperScissors)

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Rock)))
      .get.message must be(Some(SinglePlayerMoveSelectedMessage))
  }

  "If the home player chooses an unknown move, the game" should "not be played" in {
    val game = GameState(mode = Some(SinglePlayer), game = RockPaperScissors)
    val moveToThrow = SelectHomeMoveToThrow(game, Throw(Move("Brock")))

    (SelectPlayerThrowSystem(new Random) request moveToThrow) must be(Failure(UnknownMoveSelected))
  }

  "When the game is multi player the home player" should "receive a different feedback message" in {
    val game = GameState(mode = Some(MultiPlayer), game = RockPaperScissors)

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Rock)))
      .get.message must be(Some(HomePlayerMoveSelectedMessage(game)))
  }

  "If there if no game mode set, the game" should "not be played" in {
    val game = GameState(game = RockPaperScissors)
    val moveToThrow = SelectHomeMoveToThrow(game, Throw(Rock))

    (SelectPlayerThrowSystem(new Random) request moveToThrow) must be(Failure(NoGameModeSelected))
  }

  "After the home player chooses its throw and game mode is single player, it" should "play the game" in {
    val game = GameState(mode = Some(SinglePlayer), game = RockPaperScissors)

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Rock)))
      .get.nextStage must be(PlayTheGame())
  }

  "After the home player chooses its throw and game mode is multi player, it" should "wait for the next player" in {
    val game = GameState(mode = Some(MultiPlayer), game = RockPaperScissors)

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Rock)))
      .get.nextStage must be(AdversaryPlayerChooseMoveToThrow())
  }

  "The adversary player " must "be able to choose which move to throw" in {
    val game = GameState(
      game = RockPaperScissors,
      preMatch = PreMatch(homeThrow = Some(Throw(Paper)))
    )

    (SelectPlayerThrowSystem(new Random) request SelectAdversaryMoveToThrow(game, Throw(Rock)))
      .get.`match`.get.adversary.throws must be(Throw(Rock))
  }

  "When the adversary player selects its throw, he" should "receive a feedback message" in {
    val game = GameState(
      game = RockPaperScissors,
      preMatch = PreMatch(homeThrow = Some(Throw(Paper)))
    )

    (SelectPlayerThrowSystem(new Random) request SelectAdversaryMoveToThrow(game, Throw(Rock)))
      .get.message must be(Some(AdversaryPlayerMoveSelectedMessage))
  }

  "If the home adversary chooses an unknown move, the game" should "not be played" in {
    val game = GameState(
      game = RockPaperScissors,
      preMatch = PreMatch(homeThrow = Some(Throw(Paper)))
    )

    val toThrow = SelectAdversaryMoveToThrow(game, Throw(Move("Brock")))
    (SelectPlayerThrowSystem(new Random) request toThrow) must be(Failure(UnknownMoveSelected))
  }

  "After the adversary player chooses its throw, players " should "play the game" in {
    val game = GameState(
      game = RockPaperScissors,
      preMatch = PreMatch(homeThrow = Some(Throw(Paper)))
    )

    (SelectPlayerThrowSystem(new Random) request SelectAdversaryMoveToThrow(game, Throw(Rock)))
      .get.nextStage must be(PlayTheGame())
  }

  "When the user selects an unknown game mode, it" should "not be able to play it" in {
    val game = GameState(
      game = RockPaperScissors,
      preMatch = PreMatch(homeThrow = Some(Throw(Paper))),
      mode = Some(GameMode("MMORPG"))
    )
    val moveToThrow = SelectHomeMoveToThrow(game, Throw(Rock))

    (SelectPlayerThrowSystem(new Random) request moveToThrow) must be(Failure(UnknownGameModeSelected))
  }
}

