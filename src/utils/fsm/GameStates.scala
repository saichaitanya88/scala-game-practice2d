package utils.fsm

object GameStates extends Enumeration {
  val USER_TURN, ENEMY_TURN = Value
}
object GameStateActions extends Enumeration {
  val NEXT = Value
}
object GameStatesMap {
  def next(currentState: GameStates.Value, action: GameStateActions.Value): GameStates.Value = {
    currentState match {
      case GameStates.USER_TURN => {
        action match {
          case GameStateActions.NEXT => GameStates.ENEMY_TURN
        }
      }
      case GameStates.ENEMY_TURN => {
        action match { 
          case GameStateActions.NEXT => GameStates.USER_TURN
        }
      }
    }
  }
}