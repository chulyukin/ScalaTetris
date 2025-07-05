package com.conapp.tetris
import scala.util.Random
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.jline.terminal.Terminal
import com.conapp.tetris.Templates._

object ConsoleTetris extends App {
  // Незапланированный выброс из приложения
  sys.addShutdownHook {
    print("\u001b[?25h") // Сделать курсор видимым
    System.out.flush() }
  // Создать окно по шаблону
  private val createTemplateWindow: () => Unit = () => {
    // Верхняя рамка
    print(HEADER_STATIC) // высота 2
    // Само окно высота W_HIGHT-4
    print(WINDOW_TEMPLATE)
    printToRow (2, W_HIG-4,
      T_STT + "Controls:\n" + TWHITE   +
              "Left: a; Right: d\n"    +
              "Drop: <Enter> or ' '\n" +
              "Exit: q" + TWHITE)
  }
  // Текущее время
  private val now = System.currentTimeMillis()
  private val terminal: Terminal = prepareTerminal(width = W_WID, height = W_HIG)
  // Создать окно по шаблону
  createTemplateWindow()
  //  Размер поля и символ заполнитель
  private final val Width = W_WID - 8
  private final val Height = W_HIG - 7
  private final val Empty = '.' // empty Symbol
  private final val pSm  = '▮'   // piece Symbol
  private final val speedMs = 350 // Скорость падения (каждые X мили.сек)

  type Field = Array[Array[Char]]
  // Tetromino
  val pieces = Array(
    Array(Array(pSm, pSm, pSm, pSm)),         // '▮▮▮▮'
    Array(Array(pSm, pSm), Array(pSm, pSm)),  // O
    Array(Array(' ', pSm, ' '), Array(pSm, pSm, pSm)), // T
    Array(Array(pSm, ' '), Array(pSm, pSm), Array(' ', pSm)), // S
    Array(Array(' ', pSm), Array(pSm, pSm), Array(pSm, ' '))  // Z
  )
  // Заполнение Empty символом поля
  def emptyField(): Field = Array.fill(Height, Width)(Empty)

  // Отрисовка фигуры
  def draw (field: Field, piece: Array[Array[Char]], px: Int, py: Int): Unit = {
    setPos(2,5)
    val screen = field.map(_.clone)
    for {
      y <- piece.indices
      x <- piece(y).indices
      if piece(y)(x) == pSm
      fy = py + y
      fx = px + x
      if fy >= 0 && fy < Height && fx >= 0 && fx < Width
    } screen(fy)(fx) = pSm
    screen.zipWithIndex.foreach{case(row,i) => println(pos(3+((W_WID-Width)/2-2),i+3) + row.mkString)}
  }
  // Возвращает true если фигру можно положить
  def canPlace(field: Field, piece: Array[Array[Char]], px: Int, py: Int): Boolean = {
    piece.indices.forall { y =>
      piece(y).indices.forall { x =>
        piece(y)(x) != pSm ||
          ((py + y) < Height && (py + y) >= 0 &&
            (px + x) < Width && (px + x) >= 0 &&
            field(py + y)(px + x) == Empty)
      }
    }
  }
  // Расчет координаты фигуры на поле
  def place(field: Field, piece: Array[Array[Char]], px: Int, py: Int): Unit = {
    for {
      y <- piece.indices
      x <- piece(y).indices
      if piece(y)(x) == pSm
      fy = py + y
      fx = px + x
      if fy >= 0 && fy < Height && fx >= 0 && fx < Width
    } field(fy)(fx) = pSm
  }

  /**
   * Вращение фигуры
   * @param piece текущая фигура
   * @return текущая фигура, повернутая на 90 градусов
   */
  def rotate(piece: Array[Array[Char]]): Array[Array[Char]] = piece.transpose.map(_.reverse)

  def dropDown (field: Field, piece: Array[Array[Char]], px: Int, py: Int): Int = {
    var new_py = py
    while (canPlace(field, piece, px, new_py +1)) new_py+=1
    new_py
  }
  // Удаление заполненной линии
  def clearLines(field: Field): Int = {
    val full = field.indexWhere(row => row.forall(_ == pSm))
    if (full == -1) 0
    else {
      for (i <- full until 1 by -1) field(i) = field(i-1).clone()
      field(0) = Array.fill(Width)(Empty)
      1 + clearLines(field)
    }
  }

  var field = emptyField()
  var gameOver = false
  var score = 0
  // Новая фигура
  def nextPiece(): Array[Array[Char]] = pieces(Random.nextInt(pieces.length))
  // Цикл - обработчик клавиш
  def inputLoop(): Future[Unit] = Future {
    while (!gameOver) {
      val key= terminal.input().read()
      key match {
        case 'a' => moveLeft = true
        case 'd' => moveRight = true
        case KEY_ENTER | ' ' => moveDown = true
        case 'w' => rotatePiece = true
        case 'q'=> gameOver = true
        case _ =>
      }
    }
  }

  // Управление
  @volatile var moveLeft, moveRight, moveDown, rotatePiece = false

  inputLoop()

  var currPiece = nextPiece()
  var px = Width / 2 - currPiece(0).length / 2
  var py = 0
  // gameOver = true
  while (!gameOver) {
    draw(field, currPiece, px, py)
    print(pos(10,2)+s"$score")
    Thread.sleep(speedMs)

    // Управление
    if (moveLeft && canPlace(field, currPiece, px - 1, py)) px -= 1
    if (moveRight && canPlace(field, currPiece, px + 1, py)) px += 1
    if (moveDown) {
      py = dropDown(field, currPiece, px, py)
    }
    if (rotatePiece) {
      val rotated = rotate(currPiece)
      if (canPlace(field,rotated,px,py)) currPiece = rotated
    }
    moveLeft = false; moveRight = false; moveDown = false; rotatePiece = false
    //
    if (canPlace(field, currPiece, px, py + 1)) {
      py += 1
    } else {
      place(field, currPiece, px, py)
      score += clearLines(field)
      currPiece = nextPiece()
      px = Width / 2 - currPiece(0).length / 2
      py = 0
      if (!canPlace(field, currPiece, px, py)) {
        draw(field, currPiece, px, py)
        println(pos(4,3) + s"${T_STT}Game Over!")
        gameOver = true
      }
    }
  }
  println(pos(3,W_HIG/2)+s"${T_STT}Final score: $score" + pos(3,W_HIG))
  clearScrean()
  println(s"Your ${TWHITE}final score: $score")
  println(s"Game playing spent time: " +
          s"${((System.currentTimeMillis()- now)/1000.0/60*100).toInt/100.0}" +
          s" min")
}