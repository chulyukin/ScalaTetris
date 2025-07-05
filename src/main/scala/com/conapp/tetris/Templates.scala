package com.conapp.tetris

/* Подсказки: "\u2550": ═; "\u2563": ═╣; "\u255A": ╚; "\u2554": ╔; "\u2560": ╠═; "\u2551": ║;
              "\u2557": ╗; "\u255D": ╝; :├  ⌛ ⏬ */
/** Псевдографические шаблоны и атрибуты окон */
object Templates {
  /** Ширина окна */
  final val W_WID: Int = 25 // Ширина окна (min 70)
  /** Высота окна */
  final val W_HIG: Int = 23 // Высота  окна (min 24)

  // ANSI-escape-коды для фона и текста
  val C_GRD: String = BACKGROUND     // Основной цвет фона окна
  val T_GRD: String = TWHITEFADE     // Основной цвет текста окна (рамки, текст рамки)
  val T_STT: String = TWHITEBRIGHT   // Статический текст

  // Цвет основного окна и текста
  val BTGRD: String = C_GRD+T_GRD

  /*--------------- Шаблон окна -----------------------------*/
  /**
   * Статическая верхняя часть окна (заголовок, меню, путь) (y=0)
   */
  val HEADER_STATIC: String = pos(1,1) +
    BTGRD +s"╔═"    +"═"*(W_WID-20)   +  "(Scala Tetris)═══╗\n"+
    BTGRD +s"║ ${T_STT}Score:$T_GRD  "+" "*(W_WID-11)   + "║\n"+
    BTGRD +s"╠══$T_GRD═" +"═"*(W_WID-6)                 +"═╣\n"+
    BTGRD +s"║"     +" "*(W_WID-2)                      + "║\n"

  /**
   * Шаблон одной строки окна
   */
  val WINDOW_ROW: String =
    BTGRD +s"║"                                    +" "*(W_WID-2)             +"║\n"
  /**
   * Основной шаблон окна (y=6)
   */
  val WINDOW_TEMPLATE: String = Range.inclusive(6, W_HIG).map(_=>
    WINDOW_ROW).mkString +
    BTGRD +s"╚═════"             +"═"*(W_WID-15)+"════════╝"

  /**
   * Печать 1 или нескольких строк шаблона WINDOW_ROW (с переданным текстом)
   *
   * Параметры:
   *  - x: Int Координата x
   *  - y: Int Координата y
   *  - str: String - строка для печати
   */
  val printToRow: (Int, Int, String) => Unit = (x: Int, y: Int, str: String) => {
    val rows = str.split("\n")
    println(pos(1,y) + WINDOW_ROW*rows.length)
    var Y = y
    rows.foreach(s => {println(pos(x,Y)+s);Y+=1})
  }
}