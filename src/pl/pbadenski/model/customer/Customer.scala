package pl.pbadenski.model.customer;

import scala.reflect._ 

object Customer //DAO
{
  val repository = dataaccess.Repository
  
  def findByName(name : String) = {
    repository.customers filter { _.firstName == name }
  }

  def findById(id : Long) = {
	  repository.customers find { _.id == id } getOrElse { 
	    error("Customer with specified id not found, id = " + id)
	  }
  }
  
  def fetchArchivedCompanies(c : Customer) = {
    repository.companyArchive(c)
  }
}

trait CustomerService { self : Customer =>
  private val customerDao = Customer
  
  def findFirstCompany = {
    customerDao.fetchArchivedCompanies(self).first
  }
}

class Customer(
		private val id : Long,
		@BeanProperty val firstName : String, 
		@BeanProperty val lastName : String, 
		val address : Address,
		val company : Company) {
  def fullName = firstName + " " + lastName
}
