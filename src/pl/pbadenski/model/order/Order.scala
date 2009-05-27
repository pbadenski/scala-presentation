package pl.pbadenski.model.order;

import scala.xml._
import customer._

import Order._
class Order(
  recipient : HasAddress, 
  items : Seq[Item]
) {
	def send() {
		println("Sending to address: " + recipient.address);
	}
 
	override def toString = "Order(" + items + ")"
}

object Order {
	type HasAddress = {
	  def address : Address
	}
 
	def fromXML(node  : Node) = node match {
	  case <order>{items @ _* }</order> => items map { Item.fromXML(_) }
	} 
}