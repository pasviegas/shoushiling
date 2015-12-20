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

import com.pasviegas.shoushiling.cli.Inputs._
import com.pasviegas.shoushiling.cli.Messages._
import com.pasviegas.shoushiling.cli.Stages._
import com.pasviegas.shoushiling.cli.system.Exceptions.NoGameModeSelected
import com.pasviegas.shoushiling.cli.{GameState, MultiPlayer, PreMatch, SinglePlayer}
import com.pasviegas.shoushiling.core.GamePlay.{Move, Throw}
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.{Failure, Random}

class SelectPlayerThrowSystemTest extends FlatSpec with MustMatchers {

  "The home player" must "be able to choose which move to throw" in {
    import com.pasviegas.shoushiling.core.DefaultGame

    val game = GameState(mode = Some(SinglePlayer), game = Some(DefaultGame))

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.`match`.get.home.throws must be(Throw(Move('Rock)))
  }

  "When the home player selects its throw, he" should "receive a feedback message" in {
    import com.pasviegas.shoushiling.core.DefaultGame

    val game = GameState(mode = Some(SinglePlayer), game = Some(DefaultGame))

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.message must be(Some(HomePlayerMoveSelectedMessage))
  }

  "If there if no game mode set, the game" should "not be played" in {
    val moveToThrow: SelectHomeMoveToThrow = SelectHomeMoveToThrow(GameState(), Throw(Move('Rock)))

    (SelectPlayerThrowSystem(new Random) request moveToThrow) must be(Failure(NoGameModeSelected))
  }

  "After the home player chooses its throw and game mode is single player, it" should "play the game" in {
    import com.pasviegas.shoushiling.core.DefaultGame

    val game = GameState(mode = Some(SinglePlayer), game = Some(DefaultGame))

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.nextStage must be(PlayTheGame())
  }

  "After the home player chooses its throw and game mode is multi player, it" should "wait for the next player" in {
    val game = GameState(mode = Some(MultiPlayer))

    (SelectPlayerThrowSystem(new Random) request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.nextStage must be(AdversaryPlayerChooseMoveToThrow())
  }

  "The adversary player " must "be able to choose which move to throw" in {
    val game = GameState(preMatch = PreMatch(homeThrow = Some(Throw(Move('Paper)))))

    (SelectPlayerThrowSystem(new Random) request SelectAdversaryMoveToThrow(game, Throw(Move('Rock))))
      .get.`match`.get.adversary.throws must be(Throw(Move('Rock)))
  }

  "When the adversary player selects its throw, he" should "receive a feedback message" in {
    val game = GameState(preMatch = PreMatch(homeThrow = Some(Throw(Move('Paper)))))

    (SelectPlayerThrowSystem(new Random) request SelectAdversaryMoveToThrow(game, Throw(Move('Rock))))
      .get.message must be(Some(AdversaryPlayerMoveSelectedMessage))
  }

  "After the adversary player chooses its throw, players " should "play the game" in {
    val game = GameState(preMatch = PreMatch(homeThrow = Some(Throw(Move('Paper)))))

    (SelectPlayerThrowSystem(new Random) request SelectAdversaryMoveToThrow(game, Throw(Move('Rock))))
      .get.nextStage must be(PlayTheGame())
  }

}

