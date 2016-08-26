package utils.fsm


object SelectionStates extends Enumeration {
  val NONE, SELECTED_PLAYER, SELECTED_ENEMY, SELECTED_MOVE_LOCATION, SELECTED_ATTACK_LOCATION = Value
}
object SelectionStateActions extends Enumeration {
  val SELECTED_ENEMY, SELECTED_EMPTY_SPOT, SELECTED_PLAYER, SELECTED_OTHER_PLAYER, SELECTED_SAME_PLAYER = Value
}
object SelectionStatesMap {
  def next(currentState: SelectionStates.Value, action: SelectionStateActions.Value): SelectionStates.Value = {
    currentState match {
      case SelectionStates.NONE => {
        action match {
          case SelectionStateActions.SELECTED_EMPTY_SPOT => SelectionStates.NONE
          case SelectionStateActions.SELECTED_ENEMY => SelectionStates.SELECTED_ENEMY
          case SelectionStateActions.SELECTED_PLAYER => SelectionStates.SELECTED_PLAYER
          case SelectionStateActions.SELECTED_OTHER_PLAYER => SelectionStates.SELECTED_PLAYER
          case SelectionStateActions.SELECTED_SAME_PLAYER => SelectionStates.SELECTED_PLAYER
        }
      }
      case SelectionStates.SELECTED_PLAYER => {
        action match {
          case SelectionStateActions.SELECTED_EMPTY_SPOT => SelectionStates.SELECTED_MOVE_LOCATION
          case SelectionStateActions.SELECTED_ENEMY => SelectionStates.SELECTED_ATTACK_LOCATION
          case SelectionStateActions.SELECTED_PLAYER => SelectionStates.NONE
          case SelectionStateActions.SELECTED_SAME_PLAYER => SelectionStates.NONE
          case SelectionStateActions.SELECTED_OTHER_PLAYER => SelectionStates.SELECTED_PLAYER
        }
      }
      case SelectionStates.SELECTED_ENEMY => {
        action match {
          case SelectionStateActions.SELECTED_EMPTY_SPOT => SelectionStates.NONE
          case SelectionStateActions.SELECTED_ENEMY => SelectionStates.NONE
          case SelectionStateActions.SELECTED_PLAYER => SelectionStates.SELECTED_PLAYER
          case SelectionStateActions.SELECTED_SAME_PLAYER => SelectionStates.SELECTED_PLAYER
          case SelectionStateActions.SELECTED_OTHER_PLAYER => SelectionStates.SELECTED_PLAYER
        }
      }
      case SelectionStates.SELECTED_MOVE_LOCATION => SelectionStates.NONE
      case SelectionStates.SELECTED_ATTACK_LOCATION => SelectionStates.NONE
    }
  }
}