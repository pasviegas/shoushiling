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

import java.nio.file.{Files, Paths}

import com.pasviegas.shoushiling.cli.GameState
import com.pasviegas.shoushiling.cli.system.exceptions.{ConfigFileNotFound, ConfigFileNotInCorrectFormat}
import com.pasviegas.shoushiling.cli.system.inputs.{GameInput, StartGame}
import com.pasviegas.shoushiling.cli.system.messages.WelcomeMessage
import com.pasviegas.shoushiling.cli.system.stages.ChooseGameMode
import com.pasviegas.shoushiling.core.GamePlay.Move
import com.pasviegas.shoushiling.core._
import com.pasviegas.shoushiling.core.engine.{GameRule, BalancedGame}

import scala.util.{Failure, Success, Try}

case object StartGameSystem extends AGameSystem {

  def request: PartialFunction[GameInput, Try[GameState]] = {
    case StartGame(state, Some(configFile)) =>
      configFile match {
        case file: String if notExists(file) => Failure(ConfigFileNotFound)
        case file: String if notParsable(file) => Failure(ConfigFileNotInCorrectFormat)
        case file: String => startCustomGame(state, file)
      }
    case StartGame(state, None) => startDefaultGame(state)
  }

  private def notExists(file: String) =
    !Files.exists(Paths.get(file))

  private def notParsable(file: String) =
    !unParsedRulesFromFile(file).forall(_.length == 3)

  private def startDefaultGame(state: GameState) =
    Success(state.copy(
      message = Some(WelcomeMessage),
      game = Some(DefaultGame),
      nextStage = ChooseGameMode()
    ))

  private def startCustomGame(state: GameState, configFile: String) =
    Success(state.copy(
      message = Some(WelcomeMessage),
      game = Some(gameFromFile(configFile)),
      nextStage = ChooseGameMode()
    ))

  private def gameFromFile(configFile: String) =
    BalancedGame(configToRuleSet(configFile))

  private def configToRuleSet(configFile: String) =
    unParsedRulesFromFile(configFile)
      .foldLeft(Set[GameRule]())(toRuleSet)

  private def toRuleSet: (Set[GameRule], Array[String]) => Set[GameRule] =
    (ruleSet, ruleParts) =>
      ruleSet + GameRule(Move(ruleParts(0)) -> ruleParts(1) -> Move(ruleParts(2)))

  private def unParsedRulesFromFile(configFile: String) =
    io.Source.fromFile(configFile)
      .getLines()
      .map(_.trim.split(" "))
}
