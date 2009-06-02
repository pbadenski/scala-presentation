package pl.pbadenski;

import model.customer._
import model.order._
import model.order.Order._
/**
 * Plan:
 *  - Stworzyć klasę Customer (java, scala)
 *  - Dodać akcesory do klasy Customer (java, scala)
 *  - Dodać metodę getFullName do Customer (java, scala)
 *  - Dodać Repository i Repository.objects (java, scala)
 *  - Main 1) stworzyć repository
 *  - Dodać kwerendę Customer.findByName (java, scala)
 *  - Main 2) Wykorzystać kwerendę
 *  - Od tego momentu już tylko Scala
 *  - ! Problem: Chcemy drukować wizytówki klientów
 *  - Stworzyć klasę BusinessCard
 *  - Main 3 a)
 * 	- Stworzyć faktorkę BusinessCard.create(c : Customer)
 * 	- Main 3 b)
 *  - Stworzyć klasę Printer z metodą Printer.print(textForPrinting : String)
 *  - dodać metodę BusinessCard.textForPrinting
 *  - Main 3 c)
 *  - Dodać interfejs Printable z metodą Printable.textForPrining
 * 	- Dodać BusinessCard extends Printable
 *  - Zrefaktoryzować Printer.print(textForPrinting) => Printer.print(forPrinting : Printable)
 *  - Main 3 d)
 *  - Zmienić Printer na DefaultPrinter
 * 	- Dodać trait Printer
 *  - Zmienić Printable na self-type
 *  - Main 3 e)
 * 	- Zmienić Printable na type i pokazać static duck typing
 *  - Main 3 e) 
 *  - ! Problem: Chcemy znaleźć dane firmy użytkownika, są dostępne poza object graph traversal
 *  - Dodać klasę Company
 *  - Rozbudować Repository.objects
 *  - Dodać kwerendę Customer.fetchArchivedCompanies(c : Cutomer)
 *  - Dodać CustomerService i metodę CustomerService.findFirstCompany(c : Customer)
 *  - Main 4 a)
 *  - Zmienić CustomerService.findFirstCompany(c) na CustomerService.findFirstCompany z self typem
 * 	- Main 4 b)
 *  - ! Problem: Chcemy wysyłać zamówienia pod adresy - kiedy static duck typing sie przydaje
 *  - Stworzyć klasę Order
 *  - Zreorganizować paczki
 *  - Dodać trait HasAddress i Customer extends HasAddress oraz Order extends HasAddress
 *  - Dodać metodę Order.send
 *  - Main 5
 *  - Zmienić trait HasAddress na type object Order.HasAddress
 * 	- Main 5
 *  - ! Problem: Chcemy transakcyjność deklaratywną
 *  - ....
 *  - ! Problem: Chcem tworzyć zamówienia na podstawie dokumentu XML 
 *  - Zaimplementować klasę Order.fromXML
 *  - Zaimplementować klasę Item.fromXML
 *  - Main 7
 */
object Main {
	def main(args : Array[String]) : Unit = {
			// 1 first class citizens - objects (singletons)
			val repository = dataaccess.Repository

			// 2 a) easy implementation of queries and collection transformations
			val jan = repository.findByName("Jan").get 
			println(jan);
			// 2 b)
			// try {
			// 	val jan = repository.findByName("Jan").get
			// } catch {
			// 	case ex : Exception => error ("object not found")
			// }
			// 2 c)
			// val jan = repository.findByName("Jan").getOrElse { error("object not found") }
			
			// 3 a) again easy collection transformations
			// val cards = repository.customers.map { new BusinessCard(c.getFullName, c.company.name)}
			// 3 b) 
				// 1st version
			val cards = repository.customers.map { BusinessCard.create(_) }
				// 2nd version
			val cards2 = for (c <- repository.customers) yield BusinessCard.create(c)
			// 3 c)
			// new Printer.print(cards(0).textForPrinting)
			// 3 d)
			// new Printer.print(cards(0))     
			// 3 e abstracting over self-type
			cards foreach { _.print }

   			// 4 a)
			// println(new CustomerService().findFirstCompany(jan))
			// 4 b) traits, abstracting over self-type, killing procedural code
			println(jan.findFirstCompany)

			// 5 anonymous types aka static duck typing
			List[HasAddress](jan, jan.findFirstCompany).foreach { eachRecipient =>
			new Order(eachRecipient, 
					List(
							new Item("bicycle", "sports"), 
							new Item("spadle", "gardening"))
			).send()
			}
   
   			// 6 a) call-by-name, extending expressiveness through libraries and not through language
   			//	new Transaction(TransactionManager.get) {
   			//  	def work {
   			//   		println("in transaction")
   			//   	}
   			//	}.commit
			// 6 b)
			//	transactional(TransactionManager.get) { _ =>
			// 		println("in transaction")
			//	}
   			// 6 c)
   			//	transactional {
   			//		println("within transaction")
   			//	}

			// 7 XML manipulation
			val msg = scala.xml.XML.loadString("""<?xml version="1.0" encoding="UTF-8"?>
			<order>
				<item>
					<name>bicycle</name>
					<category>sports</category>
				</item>
				<item>
					<name>spadle</name>
					<category>gardening</category>
				</item>
			</order>""".lines.map { _.trim }.mkString);
			
			val order = Order.fromXML(msg)
			println(order)
			
			// TBD 8 pattern matching

	}
}

class TransactionManager(sessionFactory : String) {
	implicit var transaction : Transaction = _

	def transactional(body : => Unit)(implicit transaction : Transaction) = {
	if (transaction == null) { error("no transaction present") }
	println("Begin transaciton")
	body;
	println("End transaciton")
} 
}

object TransactionManager {
	def get = new TransactionManager("great")
}

class Transaction
