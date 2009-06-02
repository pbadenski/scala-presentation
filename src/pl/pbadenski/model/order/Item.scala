package pl.pbadenski.model.order;

import scala.xml._

object Item {
	def fromXML(node : Node) = node match {
	case <item><name>{Text(name)}</name><category>{Text(category)}</category></item> =>
	  new Item(name, category)
	}
}

class Item(
		name : String,
		category : String
) extends Processable {
  type T = Boolean
  
  def process() = {
    println("processing")
    true
  }
  
  override def toString = List(name, category).map(_.mkString("'", "", "'")).mkString("Item(", ", ", ")")
}
