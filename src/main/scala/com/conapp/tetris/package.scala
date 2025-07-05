package com.conapp

import java.nio.charset.StandardCharsets
import org.jline.terminal.Attributes.LocalFlag
import org.jline.terminal.{Attributes, Terminal, TerminalBuilder}

/**
 *  Общие методы, переменные, и др. </br>
 *
 *  <h3>Константы: </h3>
 *  - цвета фона BG<цвет>;
 *  - цвета текста T<цвет>;
 *  - коды клавишей KEY_<код> ;
 *  - типы значений поля TYPE_<значение>.
 */
package object tetris {
  // Цвета фона
  final val BACKGROUND  = "\u001b[0m\u001b[48;5;64"  // Background green
  // Цвета текста
  final val TWHITEFADE   = "\u001b[38;5;66m"              // Text faded white
  final val TWHITE       = "\u001b[38;5;254m\u001b[0m"             // Text simple white
  final val TWHITEBRIGHT = "\u001b[38;5;250m\u001b[01m"   // Text bright white
  // Клавиши
  final val KEY_ENTER     = 10

  def prepareTerminal (width: Int, height: Int): Terminal = {
    val tm = TerminalBuilder.builder().system(true).encoding(StandardCharsets.UTF_8).build()
    // Атрибуты терминала
    // ECHO - отображение символов при вводе (отключить)
    // ICANON - канонический (вводимые символы ожидают ENTER) режим работы (отключить)
    val newattrs = new Attributes(tm.getAttributes)
    newattrs.setLocalFlag(LocalFlag.ICANON,false)
    newattrs.setLocalFlag(LocalFlag.ECHO,false)
    tm.setAttributes(newattrs)
    //Проверка настройки терминала + установка размера
    if (tm.getWidth < width | tm.getHeight< height ) print(s"\u001b[8;${height+1};${width+1}t")
    // Возвращаемое значение - Terminal
    tm}
  /**
   * Строковая позиция курсора (для подстановки в print). Перемещает курсор на позицию координат.
   * Используется в строке print
   *
   * Принимает
   * x: Int - позиция в строке
   * y: Int - позиция в колонке
   */
  val pos: (Int, Int) => String = (x: Int, y: Int) => {s"\u001b[$y;${x}H"} // в строке

  /**
   * Устанавливает курсор в позицию координат. Делает курсор видимым при необходимости
   * @param x позиция в строке экрана
   * @param y позиция в колонке экрана
   * @param hide скрыть/показать курсор (true - скрыть)
   */
  def setPos(x: Int, y: Int, hide: Boolean = true ): Unit = { // установить позицию курсора
    print(pos(x,y) + (if (hide) "\u001b[?25l" else "\u001b[?25h"))
    print(s"\u001b[3 q") // мигающий курсор "_"
  }

  /** Очистка экрана */
  def clearScrean(): Unit = print("\u001b[2J\u001b[H")

}