package pl.pbadenski.model.order;

import scala.xml._
import customer._

object Order {
	type HasAddress = {
	  def address : Address
	}
 
	def fromXML(node  : Node) = node match {
	  case <order>{items @ _* }</order> => items map { Item.fromXML(_) }
	} 
}

class Order(
  recipient : Order.HasAddress, 
  items : Seq[Item]
) {
	def send() {
		println("Sending to address: " + recipient.address);
	}
 
	override def toString = "Order(" + items + ")"
 
 	case class OrderProcessResult()
 	case class NotProcessed() extends OrderProcessResult
 	case class Processed() extends OrderProcessResult
 	case class PartiallyProcessed(processedItems : Iterable[Item], notProcessedItems : Iterable[Item]) extends OrderProcessResult
   
 	case class ItemProcessResult(item : Item, result : Boolean)
 	case class Failure(override val item : Item) extends ItemProcessResult(item, false)
 	case class Success(override val item : Item) extends ItemProcessResult(item, true)
    
	def process() : OrderProcessResult = {
	  items
	  	.map { item => (item, item.process) }
	  	.partition { case (k, v) => v == true } match {
	      case (Nil, _) => NotProcessed()
	      case (_, Nil) => Processed()
	      case (processedItems, notProcessedItems) => {
	        PartiallyProcessed(
	          processedItems	.map { case (item, _) => item }, 
	          notProcessedItems .map { case (item, _) => item })
	      }
	  }
	}
}
