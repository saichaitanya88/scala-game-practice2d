package ai

import models.Ship
import models.enums.EntityType
import models.enums.WeaponType
import java.awt.Point
import models.enums.ShipType
import models.enums.EngineType
import scala.util.control.Breaks._
import ui.BasicBoard
import ui.Utils

object EnemyAI {
  /** Decide which enemy should be used for the next turn */
  def getNextEnemyAction(board: BasicBoard) : EnemyAction = {
    var enemyAction : EnemyAction = null
    val enemies = board.getShips(EntityType.ENEMY_SHIP)
    val ps = board.getShips(EntityType.PLAYER_SHIP)(0).asInstanceOf[Ship]
    val enemyDistances = enemies.map(e => e.asInstanceOf[Ship]).map(e => (e, Utils.distance(ps, e))).sortWith((a,b) => a._2 < b._2)
    val enemyPossibleDamage = enemies.map(e => e.asInstanceOf[Ship])
                                      .map(e => {
                                          val dissipatedDamage = WeaponType.getRange(e.weapon).dissipatedDamage(Utils.distance(ps, e))
                                          (e, WeaponType.firePower(e.weapon) * (1 - dissipatedDamage), dissipatedDamage)
                                        })
                                      .sortWith((a,b) => a._2 > b._2)
    println(enemyDistances.toString)
    println(enemyPossibleDamage.toString)
    println(s"comparing ${enemyPossibleDamage.head._3} with 0.5")
    if (enemyPossibleDamage.head._3 < 0.5){
      println("dissipated damage is acceptable, go ahead and shoot player")
      enemyAction = new EnemyAction(enemyPossibleDamage.head._1, AiAction.ATTACK, ps)
    }
    else {
      println("decide which ship to move for best damage possibility, move is not high priority because the player is not likely to be stationary")
      enemyAction = new EnemyAction(enemyPossibleDamage.head._1, AiAction.MOVE, ps)
    }
    enemyAction
  }
  def getNextEnemyPosition(enemy: Ship, player: Ship, board: BasicBoard): Point = {
    println("EnemyAI.getNextEnemyPosition")
    //need to move the ship to an optimal position that gives best damage
    val totalMovementCapacity = ShipType.baseMoveDistance(enemy.shipType) + EngineType.moveDistanceBonus(enemy.engineType)

    /**
     * Starting from the player's position as the target position, move the ship to where the ship's weapons can do 100% damage (dissipatedDamage == 0)
     */
    var targetPos = new Point(player.x, player.y)
    var enemyPos = new Point(enemy.x, enemy.y)
    var currentPos = new Point(enemy.x, enemy.y)
    var moveX: Boolean = true
    def debugInfo = {
      println(s"totalMovementCapacity: ${totalMovementCapacity}")
      println(s"Weapon.getRange: ${WeaponType.getRange(enemy.weapon)}")
      println(s"DissipatedDamage: ${WeaponType.getRange(enemy.weapon).dissipatedDamage(Utils.distance(enemy, player))}")
      println(s"Distance: ${Utils.distance(targetPos, currentPos)}")
      println(s"targetPos: ${targetPos} ;; enemyPos: ${enemyPos} ;; moveX: ${moveX} ;; currentPos: ${currentPos}")
    }
    debugInfo
    val movablePoints = getMovablePositions(enemy, board)
    val movablePointsDistToTarget = movablePoints.map(p => {
              val dist = Utils.distance(p, targetPos)
              (p,dist, WeaponType.getRange(enemy.weapon).dissipatedDamage(dist))
          })
    val leastDamageDissipation = movablePointsDistToTarget.sortWith((a,b) => a._3 < b._3).take(1)
//    println(movablePointsDistToTarget)
//    println(s"leastDamageDissipation: ${leastDamageDissipation}")
    return leastDamageDissipation.take(1)(0)._1 
  }
  def getMovablePositions(es: Ship, board: BasicBoard): List[Point] = {
    var points = List[Point]()
    val totalMovementCapacity = ShipType.baseMoveDistance(es.shipType) + EngineType.moveDistanceBonus(es.engineType)
    List.range(0, board.grid.gridCount).foreach(y => {
      List.range(0, board.grid.gridCount).foreach(x => {
        val point = new Point(x,y)
        var dist = Utils.distance(point, new Point(es.x, es.y))
        val pointOccupied = board.entities.find { p => p.x == x && p.y == y }.isDefined
        if (dist <= totalMovementCapacity && !pointOccupied) points = point :: points
      })
    })
    points
  }
  def printMovablePositions(es: Ship, board: BasicBoard){
    println("EnemyAI.printMovablePositions")
    val totalMovementCapacity = ShipType.baseMoveDistance(es.shipType) + EngineType.moveDistanceBonus(es.engineType)
    List.range(0, board.grid.gridCount).foreach(y => {
      List.range(0, board.grid.gridCount).foreach(x => {
        var dist = Utils.distance(new Point(x,y), new Point(es.x, es.y))
        val formattedDist = if (dist > totalMovementCapacity) "N/A" else format2d(dist)
        print(s"(${x},${y}: ${formattedDist} ), ")
      })
      println("")
    })
  }
  def format2d(d: Double) : String = {
    "%.2f".format(d)
  }
}

object AiAction extends Enumeration {
  val MOVE, ATTACK = Value
}

class EnemyAction(val enemyShip: Ship, val aiAction: AiAction.Value, val playerShip: Ship){
  
}
/*while (WeaponType.getRange(enemy.weapon).dissipatedDamage(BasicBoard.distance(enemy, player)) < 1 &&  
        BasicBoard.distance(currentPos, enemyPos) <= totalMovementCapacity && 
        (currentPos.x >= 0 && currentPos.y >= 0 && currentPos.x < board.gridSize && currentPos.y < board.gridSize)){
      debugInfo
      currentPos = new Point(currentPos.x + (if (moveX) ( if (currentPos.x > targetPos.x) 1 else -1 ) else 0), 
                              currentPos.y + (if (!moveX) ( if (currentPos.y > targetPos.y) 1 else -1 ) else 0))
      moveX = !moveX
    }*/