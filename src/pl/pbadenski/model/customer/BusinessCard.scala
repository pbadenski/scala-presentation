package pl.pbadenski.model.customer;
object Printer {
  type printable = {
	  def textForPrinting : String    
  }
}

trait Printer { self : Printer.printable =>
  def print
}

trait DefaultPrinter extends Printer { self : Printer.printable =>
  def print = println(textForPrinting)
}
  
object BusinessCard {
  def create(customer : Customer) = 
    new BusinessCard(customer.fullName, customer.company.name) with DefaultPrinter
}

class BusinessCard(
  fullName : String, 
  company : String) {
	def textForPrinting = toString   
}
