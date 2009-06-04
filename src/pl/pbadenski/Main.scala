package pl.pbadenski;

import model.customer._
import model.order._
import model.order.Order._;
import helpers.TransactionManager.get._

/**
 * Things to highlight:
 * 	- scalability,
 *  - expresivness,
 * 	- ability to build new language on the base of existing language,
 *  - extensible through the language syntax not magic tricks like bytecode manipulation, class transformation on the fly, proxies, AspectJ, etc.
 * 
 * Plan:
 *  - Create Customer class (java, scala)
 *  - Add accessors to Customer class (java, scala)
 *  - Add getFullName method to Customer class (java, scala)
 *  - Add Repository and Repository.objects (java, scala)
 *  - Main 1) create repository
 * 
 *  - Add query Customer.findByName (java, scala)
 *  - Main 2) User query
 * 
 *  - Just Scala from now on
 * 
 *  - ! Problem: We'd like to print business cards for clients
 *  - Create BusinessCard class
 *  - Main 3 a)
 * 
 * 	- Create factory method BusinessCard.create(c : Customer)
 * 	- Main 3 b)
 * 
 *  - Create Printer class with a methods Printer.print(textForPrinting : String)
 *  - Add a method BusinessCard.textForPrinting
 *  - Main 3 c)
 * 
 *  - Add an interface Printable with a method Printable.textForPrining
 * 	- Add BusinessCard extends Printable
 *  - Refactor Printer.print(textForPrinting) to Printer.print(forPrinting : Printable)
 *  - Main 3 d)
 * 
 *  - Change Printer to DefaultPrinter
 * 	- Add trait Printer
 *  - Change Printable to self-type
 *  - Main 3 e)
 * 
 * 	- Change Printable to type and show static duck typing
 *  - Main 3 e)
 * 
 *  - ! Problem: We'd like to find data of user's company, but it is not reachable via object graph traversal
 *  - Add Company class
 *  - Extend Repository.objects
 *  - Add a query Customer.fetchArchivedCompanies(c : Cutomer)
 *  - Add CustomerService and a method CustomerService.findFirstCompany(c : Customer)
 *  - Main 4 a)
 * 
 *  - Change CustomerService.findFirstCompany(c) to CustomerService.findFirstCompany with self type
 * 	- Main 4 b)
 * 
 *  - ! Problem: We'd like to send orders to addresses - static duck typing made useful
 *  - Create class Order
 *  - Reorganise packages
 *  - Add trait HasAddress and Customer extends HasAddress and also Order extends HasAddress
 *  - Add a method Order.send
 *  - Main 5
 * 
 *  - Change trait HasAddress to type object Order.HasAddress
 * 	- Main 5
 * 
 *  - ! Problem: We'd like to have declarative transaction management
 *  - ....
 *  - ! Problem: We'd like to create orders based on XML document
 *  - Implement a method Order.fromXML
 *  - Implement a method Item.fromXML
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

