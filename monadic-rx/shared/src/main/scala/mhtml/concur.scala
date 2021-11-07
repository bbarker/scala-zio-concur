package mhtml

import dottytags.*
import dottytags.predefs.all.*
import dottytags.syntax.given
import dottytags.units.*
import zio.ZIO

/*
We need a HKT that can be combined monoidally and monadically
ZIO fulfills monadically, but what about monoidally? This probably depends on
the Document type (e.g HTML in PureScript);
an example might be the ScalaTags "Tags" type; but we also need to
carry around the actual value type being computed by a Widget. While there are
probably many ways  to encode this, a natural way to think of it is that we have
a function from Tag => A

Do we want to look into type lambas for this?

JavaScript?
we can have an HTML element become a new type of HTML element using the DOM,
which should be supported in the DOM-enabled variant of ScalaTags

Concur typically uses something like React under the hood for event handling
*/
object concur:
  type Widget[R, E, A] = ZIO[R, E, A] // TODO
  val x: Int = 5



// Here we experiment with creating a non-ZIO scalatags implementation of
// widgets becoming other widgets
object Main:

  def buttonEx(): Tag = button(
      onclick := ""

  )
  def textEx(txt: String): Tag = p(txt)