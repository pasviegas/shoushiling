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

import com.pasviegas.shoushiling.cli.Inputs._
import com.pasviegas.shoushiling.cli.Messages.{MultiPlayerSelectedMessage, SinglePlayerSelectedMessage, WelcomeMessage}
import com.pasviegas.shoushiling.core.GamePlay.{Match, Move, Player, Throw}
import org.scalatest.{FlatSpec, MustMatchers}

class GameSystemTest extends FlatSpec with MustMatchers {

  "A player " must "be able to start the game" in {
    (GameSystem() request StartGame(GameState())).started must be(true)
  }

  "When the game starts, the player " should " receive a welcome message" in {
    (GameSystem() request StartGame(GameState())).message must be(Some(WelcomeMessage))
  }

  "A player " must "be able to choose to play alone" in {
    (GameSystem() request SinglePlayerMode(GameState())).mode must be(Some(SinglePlayer))
  }

  "When the player selects single player, he" should " receive a feedback message" in {
    (GameSystem() request SinglePlayerMode(GameState())).message must be(Some(SinglePlayerSelectedMessage))
  }

  "A player " must "be able to choose to play with another player" in {
    (GameSystem() request MultiPlayerMode(GameState())).mode must be(Some(MultiPlayer))
  }

  "When the player selects multi player, he" should " receive a feedback message" in {
    (GameSystem() request MultiPlayerMode(GameState())).message must be(Some(MultiPlayerSelectedMessage))
  }

  "The home player " must "be able to choose which move to throw" in {
    val championshipFinale = Match(Player("1", Throw(Move('Paper))), Player("2", Throw(Move('Paper))))
    val game = GameState(`match` = Some(championshipFinale))

    (GameSystem() request SelectHomeMoveToThrow(game, Throw(Move('Rock)))).`match`
      .get.home.throws must be(Throw(Move('Rock)))
  }

  "The adversary player " must "be able to choose which move to throw" in {
    val championshipFinale = Match(Player("1", Throw(Move('Paper))), Player("2", Throw(Move('Paper))))
    val game = GameState(`match` = Some(championshipFinale))

    (GameSystem() request SelectAdversaryMoveToThrow(game, Throw(Move('Rock)))).`match`
      .get.adversary.throws must be(Throw(Move('Rock)))
  }
}

