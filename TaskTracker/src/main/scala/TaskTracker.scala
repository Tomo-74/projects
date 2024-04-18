// GUI libraries
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ScrollPane, TextField}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.*
import javafx.event.{ActionEvent, EventHandler}
import scalafx.scene.layout.{HBox, VBox}
import scala.io.Source

// Utility libraries
import java.util.{Date, Comparator, Locale}
import scala.collection.mutable.PriorityQueue
import java.io.{File, PrintWriter, FileNotFoundException, IOException}
import java.text.SimpleDateFormat

/**
 * Task Tracker application.
 */
object TaskTracker extends JFXApp3 {
  private var taskComparator: Ordering[Task] = Ordering.by[Task, Date](_.deadline) // Ordering for Tasks based on their deadline
  private var tasks: PriorityQueue[Task] = PriorityQueue.empty[Task](taskComparator) // Priority queue that will store tasks, sorted by deadline

  /**
   * Utility method to print all tasks in the queue.
   */
  private def printTasks: Unit = {
    tasks.foreach(task => println(task.toString() + "\n"))
  }

  /**
   * Program GUI.
   */
  override def start(): Unit = {
    // Declare at start to avoid forward references
    var taskList: VBox = null
    var homeScene: VBox = null
    var taskScrollPane: ScrollPane = null


    ////////////////////
    // NEW TASK ENTRY //
    ////////////////////
    val nameEntry = new TextField {
      promptText = "Name"
      style = "-fx-background-color: #22272b; -fx-text-fill: white;"
    }

    val deadlineEntry = new TextField {
      promptText = "MM/DD/YYYY"
      style = "-fx-background-color: #22272b; -fx-text-fill: white;"
    }

    // Button for cancelling new task submission
    val cancelBtn = new Button {
      text = "Cancel"
      prefWidth = 75
      prefHeight = 30
      style = "-fx-background-color: grey; -fx-text-fill: white;"

      // On click: transition back to home scene
      onAction = (event: ActionEvent) => {
        stage.scene = new Scene {
          fill = rgb(49, 51, 56)
          content = new VBox {
            spacing = 10
            children = Seq(homeScene)
          }
        }
      }
    }

    // Button for submitting a new task
    val submitBtn = new Button {
      text = "Submit"
      prefWidth = 75
      prefHeight = 30
      style = "-fx-background-color: grey; -fx-text-fill: white;"

      // On click: add task to backend and transition back to home scene
      onAction = (event: ActionEvent) => {
        try {
          // Collect user input
          val name: String = nameEntry.text.value
          val deadline: String = deadlineEntry.text.value

          // Validate input
          val validFmt = """^\d{2}/\d{2}/\d{4}$""" // MM/DD/YYYY
          if(!deadline.matches(validFmt)) throw new Exception("Invalid date format: use MM/DD/YYYY") // Do not halt program

          // Create new task
          val newTask = new Task(name, deadline)
          tasks.enqueue(newTask)

          // Clear all tasks buttons and then re-add them, this time inserting the new task where it should go according to its priority (displays tasks in order of descending priority by default)
          val tasksClone = tasks.clone
          taskList.children.clear
          while(tasksClone.nonEmpty) {
            val curTask = tasksClone.dequeue // get highest priority task

            val curTaskBtn = new Button { // create a button to display it
              text = curTask.toString
              onAction = (event: ActionEvent) => {  // On click: remove this task from GUI and backend
                val tasksClone = tasks.clone
                tasks.clear
                for (task <- tasksClone) { // Add back all tasks except for the one that was clicked
                  if (!task.toString.equals(curTask.toString)) {
                    tasks.enqueue(task)
                  }
                }
                taskList.children.remove(this) // Remove from GUI
              }
            }

            taskList.children.add(curTaskBtn)
          }

          stage.scene = new Scene {
            fill = rgb(49, 51, 56)
            content = new VBox {
              spacing = 10
              children = homeScene
            }
          }
        }
        catch { case e: Exception => println(e.toString) }
      }
    }

    // HBox containing submit and cancel buttons
    val submitCancelBox = new HBox {
      spacing = 10
      children = Seq(submitBtn, cancelBtn)
    }

    // VBox representing the new task entry scene
    val newTaskEntry = new VBox {
      spacing = 5
      children = Seq(nameEntry, deadlineEntry, submitCancelBox)
      fillWidth = true
      style = "-fx-background-color: #22272b; -fx-text-fill: white;"
    }


    ////////////////
    // HOME SCENE //
    ////////////////
    // Button for adding new task
    val addTaskBtn = new Button {
      text = "Add Task"
      prefWidth = 100
      prefHeight = 40
      style = "-fx-background-color: grey; -fx-text-fill: white;"
      onAction = (event: ActionEvent) => {
        stage.scene = new Scene {
          fill = rgb(49, 51, 56)
          content = newTaskEntry
        }
      }
    }

    // Button for filtering how the tasks are displayed
    val filterBtn = new Button {
      text = "Filter: ▲" // starts in Descending mode
      prefWidth = 150
      prefHeight = 20
      style = "-fx-background-color: grey; -fx-text-fill: white;"

      // On click: toggle between ascending and descending order
      onAction = (event: ActionEvent) => {
        val tasksClone = tasks.clone
        taskList.children.clear

        if(text.value.equals("Filter: ▲")) { // Switch from descending to ascending order: remove all buttons from the screen then re-add them in ascending order
          text = "Filter: ▼"

          while(tasksClone.nonEmpty) {
            val curTask = tasksClone.dequeue // get highest priority task
            val curTaskBtn = new Button { // create a new button to display it
              text = curTask.toString
              onAction = (event: ActionEvent) => { // On click: remove this task from GUI and backend
                val tasksClone = tasks.clone
                tasks.clear
                for (task <- tasksClone) { // Add back all tasks except for the one that was clicked
                  if (!task.toString.equals(this.text)) {
                    tasks.enqueue(task)
                  }
                }
                taskList.children.remove(this) // Remove from GUI
              }
            }
            taskList.children.add(0, curTaskBtn) // add each task to top of the taskList (results in ascending order)
          }

        } else { // Switch from ascending to descending order: remove all buttons from the screen then re-add them in descending order
          text = "Filter: ▲"

          while (tasksClone.nonEmpty) {
            val curTask = tasksClone.dequeue // get highest priority task
            val curTaskBtn = new Button { // create a button to display it
              text = curTask.toString
              onAction = (event: ActionEvent) => { // On click: remove this task from GUI and backend
                val tasksClone = tasks.clone
                tasks.clear
                for (task <- tasksClone) { // Add back all tasks except for the one that was clicked
                  if (!task.toString.equals(curTask.toString)) {
                    tasks.enqueue(task)
                  }
                }
                taskList.children.remove(this) // Remove from GUI
              }
            }
            taskList.children.add(curTaskBtn) // add each task to end of the taskList (results in descending order)
          }
        }

        // Load home scene
        stage.scene = new Scene {
          fill = rgb(49, 51, 56)
          content = new VBox {
            spacing = 10
            children = homeScene
          }
        }

      } // end ActionEvent
    } // end filterBtn

    // VBox for displaying tasks
    taskList = new VBox {
      spacing = 10
    }

    // The main home scene containing the filter button, tasks, and add task button
    homeScene = new VBox {
      spacing = 20
      maxWidth = 200
      maxHeight = 400
      style = "-fx-background-color: #101204; -fx-text-fill: white;"
      children = Seq(filterBtn, taskList, addTaskBtn)
    }


    ///////////
    // STAGE //
    ///////////
    stage = new JFXApp3.PrimaryStage {
      width = 600
      height = 600
      scene = new Scene {
        fill = rgb(49, 51, 56)
        content = homeScene
      }
    } // end of stage
  } // end of start()
} // end of TaskTracker class