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
import com.pasviegas.shoushiling.cli.system.exceptions.{ConfigFileNotFound, ConfigFileNotInCorrectFormat}
import com.pasviegas.shoushiling.cli.system.inputs.StartGame
import com.pasviegas.shoushiling.cli.system.messages.WelcomeMessage
import com.pasviegas.shoushiling.cli.system.stages.ChooseGameMode
import com.pasviegas.shoushiling.cli.test.ShoushilingValues
import com.pasviegas.shoushiling.core.GamePlay.Move
import org.scalatest.{FlatSpec, MustMatchers}

import scala.util.Failure

class StartGameSystemTest extends FlatSpec with MustMatchers with ShoushilingValues {

  "A player" must "be able to start the game with the default rules" in {
    (StartGameSystem request StartGame(GameState())).get.game must be(RockPaperScissors)
  }

  "When the game starts, the player" should "receive a welcome message" in {
    (StartGameSystem request StartGame(GameState())).get.message must be(Some(WelcomeMessage))
  }

  "After the game starts, the player" must "choose the game mode" in {
    (StartGameSystem request StartGame(GameState())).get.nextStage must be(ChooseGameMode())
  }

  "A player" can "start a custom the game" in {
    (StartGameSystem request StartGame(GameState(), Some(correctGame)))
      .get.game.get.rules.find(_.winner == Move("Lizard")) mustBe defined
  }

  "A player" can "not start a game if the config file is not found" in {
    (StartGameSystem request StartGame(GameState(), Some("notFound.game"))) must be(Failure(ConfigFileNotFound))
  }

  "A player" can "not start a game if the config file is incorrectly configured" in {
    (StartGameSystem request StartGame(GameState(), Some(wrongGame))) must be(Failure(ConfigFileNotInCorrectFormat))
  }
}

