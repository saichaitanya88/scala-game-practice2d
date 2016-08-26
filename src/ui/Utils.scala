package ui

import java.awt.Point
import models.enums.EntityType
import models.Ship
import models.enums.ShipType
import models.enums.EngineType

object Utils {
  def getEmptyLocation(gridSize: Int, board: BasicBoard): Point = {
    var p = new Point(scala.util.Random.nextInt(gridSize),scala.util.Random.nextInt(gridSize))
    while(Utils.getEntityAtLocation(p.x,p.y, board) != "."){
      p = new Point(scala.util.Random.nextInt(gridSize),scala.util.Random.nextInt(gridSize))
    }
    p
  }
  def getEntityAtLocation(x: Int,y: Int, board: BasicBoard): String = {
    val entity = board.entities.find(e => e.x == x && e.y == y)
    if (entity.isDefined) EntityType.code(entity.get.entityType) else "."
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
  def distance(o: Ship, t: Ship) : Double = {
    scala.math.sqrt(scala.math.pow(t.x-o.x ,2)+scala.math.pow(t.y-o.y ,2))
  }
  def distance(o: Point, t: Point) : Double = {
    scala.math.sqrt(scala.math.pow(t.x-o.x ,2)+scala.math.pow(t.y-o.y ,2))
  }
}