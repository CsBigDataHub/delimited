package net.tixxit.csv

/**
 * A single row in a CSV file.
 */
final class CsvRow(val cells: Vector[CsvCell]) extends AnyVal {
  def text(format: CsvFormat): Vector[String] = cells.map(_ render format)

  def render(format: CsvFormat): String =
    cells.iterator map (_ render format) mkString format.separator

  override def toString: String =
    cells.mkString("CsvRow(", ", ", ")")
}

object CsvRow extends (Vector[CsvCell] => CsvRow) {
  def apply(cells: Vector[CsvCell]): CsvRow = new CsvRow(cells)
  def apply(cells: CsvCell*): CsvRow = new CsvRow(Vector(cells: _*))

  def data(cells: String*): CsvRow =
    new CsvRow(cells.map(CsvCell.Data(_))(collection.breakOut))
}
