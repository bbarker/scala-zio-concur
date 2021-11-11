package concur.dottytags

import org.scalajs.dom
import org.scalajs.dom.raw.Node
import scala.scalajs.js

object mount:

  // We try to simplify the approach in monadic-html by avoiding recursive mounting
  // This will be one by directly writing the tag's html into the DOM, including a
  // <script> tag where applicable. However, these event handlers, and possibly other
  // external handlers, may have references to the node.
  //
  // > In modern browsers, if a DOM Element is removed, its listeners are also
  // > removed from memory in javascript. Note that this will happen ONLY if the
  // > element is reference-free. Or in other words, it doesn't have any reference and
  // > can be garbage collected.
  // See also https://stackoverflow.com/a/12528067/3096687
  //
  // With that in mind, we'll need a way to either update the listeners, or cancel them
  // If that doesn't work out, we may have to fall back to the monadic-html approach
  // Some possibilities are discussed at https://stackoverflow.com/q/4386300/3096687

  implicit private class DomNodeExtra(node: Node):
    //TODO: ? setEventListener
    //TODO: ? setMetadata
    //TODO: ?

    // Creates and inserts two empty text nodes into the DOM, which delimitate
    // a mounting region between them point. Because the DOM API only exposes
    // `.insertBefore` things are reversed: at the position of the `}`
    // character in our binding example, we insert the start point, and at `{`
    // goes the end.
    def createMountSection(): (Node, Node) =
      val start = dom.document.createTextNode("")
      val end   = dom.document.createTextNode("")
      node.appendChild(end)
      node.appendChild(start)
      (start, end)

    // Elements are then "inserted before" the start point, such that
    // inserting List(a, b) looks as follows: `}` → `a}` → `ab}`. Note that a
    // reference to the start point is sufficient here. */
    def mountHere(child: Node, start: Option[Node]): Unit =
      start.fold(node.appendChild(child))(point => node.insertBefore(child, point))

    // Cleaning stuff is equally simple, `cleanMountSection` takes a references
    // to start and end point, and (tail recursively) deletes nodes at the
    // left of the start point until it reaches end of the mounting section. */
    def cleanMountSection(start: Node, end: Node): Unit =
      val next = start.previousSibling
      if (next != end)
        node.removeChild(next)
        cleanMountSection(start, end)
