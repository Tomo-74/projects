import java.util.{Date, GregorianCalendar}

/**
 * A task to complete by a certain deadline. Tasks are immutable once created.
 *
 * @param _name the task name
 * @param _deadline the task deadline in the form: MM/DD/YYYY
 */
class Task(_name: String, _deadline: String) {
  private val name: String = _name
  private val deadlineAsString = _deadline // for simple string representation of deadline
  private val deadlineComponents: Array[String] = _deadline.split("/") // split deadline by '/'
  val deadline: Date = GregorianCalendar(deadlineComponents(2).toInt, deadlineComponents(0).toInt, deadlineComponents(1).toInt).getTime // Create new Date object for sorting

  // Getters
  def getName: String = name
  def getDeadline: String = deadline.toString

  /**
   * Returns a string representation of a Task object.
   */
  override def toString: String = {
    name + "\n" + deadlineAsString
  }
}