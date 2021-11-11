package concur

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


JavaScript?
we can have an HTML element become a new type of HTML element using the DOM,
which should be supported in the DOM-enabled variant of ScalaTags (or DottyTags)
However, this requires additional effects to be executed during each flatMap,
so we somehow need to wrap or extend ZIO as a new type, and implement the relevant operations.

In order to mount and replace nodes, we may want to make use of template tags:
https://stackoverflow.com/questions/494143/creating-a-new-dom-element-from-an-html-string-using-built-in-dom-methods-or-pro
https://developer.mozilla.org/en-US/docs/Web/HTML/Element/template

The templates have the added benefit that we may be able to use them in a way similar to react
props: we can pass in the updated props to a templating function. This may reduce resource
usage as we can keep the templates stored in the Widget.

Ideas for actually mounting and replacing the node with a new node can be found here:
https://stackoverflow.com/questions/843680/how-to-replace-dom-element-in-place-using-javascript

Concur typically uses something like React under the hood for event handling, but hopefully we
can avoid the need and just use some built-in logic and a tags library.
 */
object concur:
  type Widget[R, E, A] = ZIO[R, E, A] // TODO
  val x: Int = 5
