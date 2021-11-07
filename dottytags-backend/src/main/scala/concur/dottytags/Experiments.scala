package concur.dottytags

import dottytags.*
import dottytags.predefs.all.*
import dottytags.syntax.given
import dottytags.units.*
import zio.ZIO
import scala.scalajs.js.annotation.*
import org.scalajs.dom

// Here we experiment with creating a non-ZIO scalatags implementation of
// widgets becoming other widgets
@JSExportTopLevel("Experiments")
object Experiments:

  @JSExport
  val testPage: Tag = html(
    head(
      script(src:="..."),
      script(
        "alert('Hello World')"
      )
    ),
    body(
      div(
        h1(id:="title", "This is a title"),
        p("This is a big paragraph of text")
      )
    )
  )
    
  def main(): Unit = {
      println("hello world")
  }

  def buttonEx(): Tag = button(
      onclick := ""

  )

  def textEx(txt: String): Tag = p(txt)


