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
package com.pasviegas.shoushiling.core.engine

import com.pasviegas.shoushiling.core.GamePlay.{Match, Player}

sealed trait GameOutcome {
  def `match`: Match

  def winner: Option[Player]
}

final case class Tie(`match`: Match) extends GameOutcome {
  def winner: Option[Player] = None

  override def toString: String = "Tie"
}

final case class Win(`match`: Match, winner: Some[Player]) extends GameOutcome {
  override def toString: String =
    winner.map(player => s"Player ${player.name} wins with ${player.throws.move.name}").get

}
