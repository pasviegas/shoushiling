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

import com.pasviegas.shoushiling.cli.Exceptions.{NoGameModeSelected, GameHasNoMatch, GameHasNotBeenConfigured}
import com.pasviegas.shoushiling.cli.Inputs._
import com.pasviegas.shoushiling.cli.Messages._
import com.pasviegas.shoushiling.cli.Stages.{AdversaryPlayerChooseMoveToThrow, ChooseGameMode, HomePlayerChooseMoveToThrow, PlayTheGame}
import com.pasviegas.shoushiling.core.GamePlay.{Match, Move, Player, Throw}
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.Failure

class GameSystemTest extends FlatSpec with MustMatchers {

  "A player " must "be able to start the game" in {
    (GameSystem() request StartGame(GameState())).get.started must be(true)
  }

  "A player " must "be able to start the game with the default rules" in {
    import com.pasviegas.shoushiling.core._

    (GameSystem() request StartGame(GameState())).get.game must be(Some(DefaultGame))
  }

  "When the game starts, the player " should " receive a welcome message" in {
    (GameSystem() request StartGame(GameState())).get.message must be(Some(WelcomeMessage))
  }

  "After the game starts, the player " must " choose the game mode" in {
    (GameSystem() request StartGame(GameState())).get.nextStage must be(ChooseGameMode())
  }

  "A player " must "be able to choose to play alone" in {
    (GameSystem() request SinglePlayerMode(GameState())).get.mode must be(Some(SinglePlayer))
  }

  "When the player selects single player, he" should " receive a feedback message" in {
    (GameSystem() request SinglePlayerMode(GameState())).get.message must be(Some(SinglePlayerSelectedMessage))
  }

  "After the player chooses single player " must " choose its move" in {
    (GameSystem() request SinglePlayerMode(GameState())).get.nextStage must be(HomePlayerChooseMoveToThrow())
  }

  "A player " must "be able to choose to play with another player" in {
    (GameSystem() request MultiPlayerMode(GameState())).get.mode must be(Some(MultiPlayer))
  }

  "When the player selects multi player, he" should " receive a feedback message" in {
    (GameSystem() request MultiPlayerMode(GameState())).get.message must be(Some(MultiPlayerSelectedMessage))
  }

  "After the player chooses multi player " must " choose its move" in {
    (GameSystem() request MultiPlayerMode(GameState())).get.nextStage must be(HomePlayerChooseMoveToThrow())
  }

  "The home player " must "be able to choose which move to throw" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(
      `match` = Some(championshipFinale),
      mode = Some(SinglePlayer)
    )

    (GameSystem() request SelectHomeMoveToThrow(game, Throw(Move('Rock)))).get.`match`
      .get.home.throws must be(Throw(Move('Rock)))
  }

  "When the home player selects its throw, he" should " receive a feedback message" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(
      `match` = Some(championshipFinale),
      mode = Some(SinglePlayer)
    )

    (GameSystem() request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.message must be(Some(HomePlayerMoveSelectedMessage))
  }

  "If there if no game mode set, the game" should "not be played" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(`match` = Some(championshipFinale))

    (GameSystem() request SelectHomeMoveToThrow(game, Throw(Move('Rock)))) must be(Failure(NoGameModeSelected))
  }

  "After the home player chooses its throw and game mode is single player, it " should " play the game" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(
      `match` = Some(championshipFinale),
      mode = Some(SinglePlayer)
    )

    (GameSystem() request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.nextStage must be(PlayTheGame())
  }

  "After the home player chooses its throw and game mode is multi player, it " should " wait for the next player" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(
      `match` = Some(championshipFinale),
      mode = Some(MultiPlayer)
    )

    (GameSystem() request SelectHomeMoveToThrow(game, Throw(Move('Rock))))
      .get.nextStage must be(AdversaryPlayerChooseMoveToThrow())
  }

  "The adversary player " must "be able to choose which move to throw" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(`match` = Some(championshipFinale))

    (GameSystem() request SelectAdversaryMoveToThrow(game, Throw(Move('Rock)))).get.`match`
      .get.adversary.throws must be(Throw(Move('Rock)))
  }

  "When the adversary player selects its throw, he" should " receive a feedback message" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(`match` = Some(championshipFinale))

    (GameSystem() request SelectAdversaryMoveToThrow(game, Throw(Move('Rock))))
      .get.message must be(Some(AdversaryPlayerMoveSelectedMessage))
  }

  "After the adversary player chooses its throw, players " should " play the game" in {
    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val game = GameState(`match` = Some(championshipFinale))

    (GameSystem() request SelectAdversaryMoveToThrow(game, Throw(Move('Rock))))
      .get.nextStage must be(PlayTheGame())
  }

  "Players" should " be able to play the game and see the outcome" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val defaultGame = GameState(
      `match` = Some(championshipFinale),
      game = Some(DefaultGame)
    )

    (GameSystem() request Play(defaultGame)).get.outcome mustBe defined
  }

  "If there is no match set, the game" should "not be played" in {
    import com.pasviegas.shoushiling.core._

    val stateWithNoMatch = GameState(game = Some(DefaultGame))

    (GameSystem() request Play(stateWithNoMatch)) must be(Failure(GameHasNoMatch))
  }

  "If there if no game rules are set, the game" should "not be played" in {

    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val stateWithNoMatch = GameState(`match` = Some(championshipFinale))

    (GameSystem() request Play(stateWithNoMatch)) must be(Failure(GameHasNotBeenConfigured))
  }

  "If there if no game rules and no match are set, the game" should "not be played" in {
    (GameSystem() request Play(GameState())) must be(Failure(GameHasNotBeenConfigured))
  }

  "After the the match is played, the players" should " receive a feedback message" in {
    import com.pasviegas.shoushiling.core._

    val championshipFinale = Match(
      Player("1", Throw(Move('Paper))),
      Player("2", Throw(Move('Paper)))
    )

    val defaultGame = GameState(
      `match` = Some(championshipFinale),
      game = Some(DefaultGame)
    )

    (GameSystem() request Play(defaultGame)).get.message must be(Some(EndGameMessage))
  }

}

