package ui

import javax.swing.JPanel
import ui.elements.Grid
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Color
import java.awt.Dimension
import java.awt.Toolkit
import listeners.GridMouseListener
import java.awt.event.MouseEvent
import models.Entity
import models.Ship
import models.enums.ShipType
import models.enums.EntityType
import java.awt.Font
import java.awt.Point
import java.awt.Rectangle
import java.awt.geom.Rectangle2D
import java.awt.Cursor
import utils.fsm.GameStateMachine
import utils.fsm.SelectionStates
import utils.fsm.GameStates
import utils.fsm.SelectionStatesMap
import utils.fsm.SelectionStateActions
import scala.actors.Futures.future
import ai.EnemyAI
import ai.AiAction

class BasicBoard extends JPanel {
  def pWidth: Int = 1280
  def pHeight: Int = 720
  var grid: Grid = null
  var entities: List[Entity] = List[Entity]()
  var selectedEntity: Option[Entity] = None: Option[Entity]
  var cursors: List[(String, Cursor)] = List[(String, Cursor)]()
  var stateMachine: GameStateMachine = new GameStateMachine()
  initBoard()
  def initCursors() {
    cursors = ("CROSSHAIR_CURSOR", new Cursor(Cursor.CROSSHAIR_CURSOR)) :: cursors
    cursors = ("MOVE_CURSOR", new Cursor(Cursor.MOVE_CURSOR)) :: cursors
    cursors = ("HAND_CURSOR", new Cursor(Cursor.HAND_CURSOR)) :: cursors
    cursors = ("WAIT_CURSOR", new Cursor(Cursor.WAIT_CURSOR)) :: cursors
    setCursor(cursors.find(c => c._1 == "HAND_CURSOR").get._2)
  }
  def initEntities() {
    val initEnemyShips: Int = 3
    val initPlayerShips: Int = 1
    List.range(0, 3).foreach(r => {
      val p = Utils.getEmptyLocation(grid.gridCount, this)
      entities = Ship(p.x, p.y, ShipType.FIGHTER, EntityType.ENEMY_SHIP) :: entities
    })
    List.range(0, initPlayerShips).foreach(r => {
      val p = Utils.getEmptyLocation(grid.gridCount, this)
      val ps = Ship(p.x, p.y, ShipType.HEAVY_CRUISER, EntityType.PLAYER_SHIP)
      entities = ps :: entities
    })
    entities.foreach(e => e.printInfo)
  }

  def initBoard() {
    setBackground(Color.BLACK)
    setDoubleBuffered(true)
    println(s"pw: ${pWidth} , ph: ${pHeight}")
    val hw = Math.min(pWidth, pHeight)
    grid = new Grid(hw, hw)
    val gml = new GridMouseListener()
    attachListenerFunctions(gml)
    addMouseListener(gml)
    initEntities()
    initCursors()
  }
  def highLightGridLoc(e: MouseEvent) {
    if (stateMachine.gameState != GameStates.USER_TURN) return;
    val p = e.getPoint
    if (p.x > grid.maxLineSize || p.y > grid.maxLineSize) {
      grid.highlightedGridLoc = null
    } else {
      grid.setSelectedGridLoc(p)
    }
    repaint()
  }
  def selectShip(e: MouseEvent) {
    if (stateMachine.gameState != GameStates.USER_TURN) return;
    val movablePositions = (if (selectedEntity.isDefined) Utils.getMovablePositions(selectedEntity.get.asInstanceOf[Ship], this)
    else List[Point]())
    val p = e.getPoint
    val x = (p.x - (p.x % grid.gridSize)) / grid.gridSize
    val y = (p.y - (p.y % grid.gridSize)) / grid.gridSize
    val previousEntity = selectedEntity
    selectedEntity = entities.find(e => e.x == x && e.y == y)
    if (selectedEntity.isDefined) {
      setCursor(cursors.find(c => c._1 == "CROSSHAIR_CURSOR").get._2)
      selectedEntity.get.entityType match {
        case EntityType.ENEMY_SHIP => {
          if (previousEntity.isDefined){
            if (previousEntity.get.entityType == EntityType.PLAYER_SHIP){
              stateMachine.selectionState = SelectionStates.SELECTED_ATTACK_LOCATION //SelectionStatesMap.next(stateMachine.selectionState, SelectionStateActions.SELECTED_ATTACK_ENEMY)
            }
          }
          else if (movablePositions.size == 0)
            stateMachine.selectionState = SelectionStatesMap.next(stateMachine.selectionState, SelectionStateActions.SELECTED_ENEMY) 
        }
        case EntityType.PLAYER_SHIP => {
          stateMachine.selectionState = SelectionStatesMap.next(stateMachine.selectionState, SelectionStateActions.SELECTED_PLAYER)
        }
      }
      if (stateMachine.selectionState == SelectionStates.NONE) {
        selectedEntity = None: Option[Entity]
        grid.highlightedGridLoc = null
      }
    } 
    else if (!selectedEntity.isDefined) {
      println("selectedEntity.isNotDefined")
      val isPreviousEntityPlayer = if (previousEntity.isDefined) previousEntity.get.entityType == EntityType.PLAYER_SHIP else false
      if (movablePositions.find(mp => mp.x == x && mp.y == y).isDefined && isPreviousEntityPlayer) {
        stateMachine.selectionState = SelectionStates.SELECTED_MOVE_LOCATION
      }
      else{
        stateMachine.selectionState = SelectionStatesMap.next(stateMachine.selectionState, SelectionStateActions.SELECTED_EMPTY_SPOT)
      }
    } else {
      setCursor(cursors.find(c => c._1 == "HAND_CURSOR").get._2)
      stateMachine.gameState = GameStates.USER_TURN
      stateMachine.selectionState = SelectionStates.NONE
    }

    // ATTACK/MOVE HERE
    if (stateMachine.selectionState == SelectionStates.SELECTED_ATTACK_LOCATION) {
      val es = selectedEntity.get.asInstanceOf[Ship]
      val ps = previousEntity.get.asInstanceOf[Ship]
      es.takeDamage(ps, Utils.distance(es, ps))
      if (es.getHp() <= 0){
        entities = entities.filterNot(e => e == es)
      }
      if (getShips(EntityType.ENEMY_SHIP).length == 0) {
        println("YOU WIN!")
        System.exit(0)
      }
      stateMachine.selectionState = SelectionStates.NONE
      selectedEntity = None: Option[Entity]
      grid.highlightedGridLoc = null
      stateMachine.gameState = GameStates.ENEMY_TURN
    } 
    else if (stateMachine.selectionState == SelectionStates.SELECTED_MOVE_LOCATION) {
      println(s"previousEntity is ${previousEntity}")
      previousEntity.get.x = x
      previousEntity.get.y = y
      stateMachine.selectionState = SelectionStates.NONE
      selectedEntity = None: Option[Entity]
      grid.highlightedGridLoc = null
      stateMachine.gameState = GameStates.ENEMY_TURN
    }
    debugPrintCurrentState()
    repaint()
    future { 
      if (stateMachine.gameState == GameStates.ENEMY_TURN)
        executeEnemyTurn()
    }
  }
  def attachListenerFunctions(gml: GridMouseListener) {
    gml.addMouseClickEvent("highlightGridLoc", highLightGridLoc)
    gml.addMouseClickEvent("selectShip", selectShip)
  }
  def executeEnemyTurn() =  {
    setCursor(cursors.find(c => c._1 == "WAIT_CURSOR").get._2)
    println("delay 2 seconds")
    Thread.sleep(2000)
    val enemyAction = EnemyAI.getNextEnemyAction(this)
    val es = enemyAction.enemyShip
    val ps = enemyAction.playerShip
    enemyAction.aiAction match {
      case AiAction.ATTACK => {
        println(s"enemy ${es.shipType} : shoots player")
        ps.takeDamage(es, Utils.distance(es, ps))
      } 
      case AiAction.MOVE => {
        es.moveTo(EnemyAI.getNextEnemyPosition(es, ps, this))
      }
      case _ => {
        
      }
    }
    if (ps.getHp() <= 0){
      entities = entities.filterNot(e => e == ps)
    }
    if (getShips(EntityType.PLAYER_SHIP).length == 0){
      println("YOU LOSE!")
      System.exit(0)
    }
    stateMachine.gameState = GameStates.USER_TURN
    setCursor(cursors.find(c => c._1 == "HAND_CURSOR").get._2)
  }
  def debugPrintCurrentState() {
    println(s"gameState: ${stateMachine.gameState} & selectionState: ${stateMachine.selectionState}")
  }
  def getShips(entityType: EntityType.Value) : List[Entity] = {
    entities.filter(p => List(classOf[Ship]).contains(p.getClass)).filter(p => p.entityType == entityType)
  }
  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    grid.draw(g.asInstanceOf[Graphics2D])
    /* draw entities on the board */
    g.setColor(Color.WHITE)
    val f = new Font("Ariel", 1, 12)
    g.setFont(f)
    entities.foreach(e => {
      val x = (e.x * grid.gridSize) + grid.gridSize / 4 - 5
      val y = (e.y * grid.gridSize) + grid.gridSize / 4
      if (selectedEntity.isDefined && e == selectedEntity.get) g.setColor(Color.BLACK)
      e.draw(g.asInstanceOf[Graphics2D], new Point(x, y))
      g.setColor(Color.WHITE)
    })

    /* Selected entity info */
    if (selectedEntity.isDefined) {
      val lines = selectedEntity.get.toString.split("\n")
      lines.zipWithIndex.foreach(f => {
        g.drawString(f._1, grid.width, 15 * (f._2 + 1))
      })
      /*Draw movable area*/
      val movablePoints = Utils.getMovablePositions(selectedEntity.get.asInstanceOf[Ship], this)
      movablePoints.foreach(p => {
        g.setColor(Color.decode("#AA6600"))
        g.drawRect(p.x * grid.gridSize, p.y * grid.gridSize, grid.gridSize, grid.gridSize)
        g.setColor(Color.decode("#e68a00"))
        g.asInstanceOf[Graphics2D].fill(new Rectangle2D.Double(p.x * grid.gridSize, p.y * grid.gridSize, grid.gridSize, grid.gridSize))
      })
    }

    /* Draw random stuff*/
    g.setColor(Color.orange)
    g.drawString("Color test string", grid.width, 100)
    g.setColor(Color.cyan)
    g.drawString("Color test string", grid.width, 120)
    g.setColor(Color.yellow)
    g.drawString("Color test string", grid.width, 140)
    g.setColor(Color.lightGray)
    g.drawString("Color test string", grid.width, 160)
    g.setColor(Color.decode("#AA6600"))
    g.drawString("Color test string", grid.width, 180)

    g.drawString("with crosshair, click orange to move, click enemy to attack", grid.width, 200)
    g.drawString("with crosshair, show percentage of attack plus possible damage to each enemy", grid.width, 220)
    Toolkit.getDefaultToolkit().sync()
  }
}