package net.tixxit.delimited

/**
 * The row delimiter used to separate rows in a delimited file. The primary
 * `value` specified is always used as the row delimtier when rendering. The
 * `alternate` value is an optional 2nd delimiter that may be used only when
 * parsing a delimited file.
 *
 * In general, you'll want to use `RowDelim.Both` as your row delimiter. This
 * will use \n to delimit rows, but will also accept \r\n as a row delimiter
 * since that happens often.
 *
 * @param value     the row delimiter to use when parsing/rendering
 * @param alternate an optional alternative that may be accepted during parsing
 */
case class RowDelim(value: String, alternate: Option[String] = None) {
  override def toString: String = this match {
    case RowDelim.Unix => "Unix"
    case RowDelim.Windows => "Windows"
    case RowDelim.Both => "Unix+Windows"
    case RowDelim(value, None) => s"""RowDelim("$value")"""
    case RowDelim(value, Some(alternate)) => s"""RowDelim("$value", Some("$alternate"))"""
  }
}

object RowDelim {
  implicit val RowDelimOrdering: Ordering[RowDelim] =
    Ordering.by { case RowDelim(value, alt) => (value, alt) }

  /** Use \n as a row delimiter. */
  val Unix: RowDelim = RowDelim("\n")

  /** Use \r\n as a row delimiter. */
  val Windows: RowDelim = RowDelim("\r\n")

  /** Use \n as a row delimiter, but also accepts \r\n during parsing. */
  val Both: RowDelim = RowDelim("\n", Some("\r\n"))
}
