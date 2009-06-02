package pl.pbadenski.helpers


trait TransactionManager {
  implicit var transaction : Transaction = _
  
  def transactional[T](body : => T)(implicit transaction : Transaction) : T;
}

class DefaultTransactionManager(sessionFactory : String) extends TransactionManager {
	def transactional[T](body : => T)(implicit transaction : Transaction) = {
		if (transaction == null) { error("no transaction present") }
		println("Begin transaciton")
		val result = body;
		println("End transaciton")
		result
	} 
}

object TransactionManager {
	val get = new DefaultTransactionManager("great")
}

class Transaction
