# scala-tetris
Простой пример реализации консольного tetris на Scala с подсчетом очков. Cборщик: SBT Aassembly.  

## Overview
Это демонстрационный проект **Scala** для сборщика **SBT assembly** (**«fat» jar**),
с примером кода малой сложности. Пример предназначен для использования в образовательных целях
или как основа для других проектов **Scala**. Содержит необходимую структуру для сборки исполняемого приложения **java --jar**.

## Requirements
| Инструмент    |Версия|Комментарий|Ресурс|
|:--------------|:-|:-|:-|
| Java          |openjdk-17|Openjdk-17 - это (Open Java Development Kit) - это бесплатная реализация платформы Java Standard Edition (Java SE) с открытым исходным кодом |https://openjdk.org/|
| Scala         |2.13.11|Проверенная стабильная версия, хорошо совместима с OoenJDK-17 (в качестве альтернативы можно использовать 2.12.18 )|https://scala-lang.org/|
| SBT           |1.11.2| Scala build tool. В качестве альтернативы можно использовать не ниже 1.6.1|https://docs.scala-lang.org/overviews/scala-book/scala-build-tool-sbt.html|  

Про организацию рабочей среды, в которой собирается данный проект, можно прочитать здесь: https://github.com/chulyukin/howto-base-notes)

## Assembly and Launch
1) Загрузить проект в директорию проектов IntelliJ IDEA 2025.1.2 (Community Edition):
```console
cd ~/IdeaProjects
git clone https://github.com/chulyukin/acala_tetris.git
```
2) Открыть проект в среде IntelliJ IDEA 2025.1.2
3) Далее выполнить команду сборки
```console
assembly
```
