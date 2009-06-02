import pl.pbadenski.model.order._
import org.junit._

class OrderProcessingTest {
	trait ProcessAlwaysYieldsSuccess extends Item { override val process = true  }
	trait ProcessAlwaysYieldsFailure extends Item { override val process = false }
 
	@Test
	def should_be_processed_if_all_items_has_been_successfully_processed() {
	  // given order contains items that will be successfully processed 
	  val order = new Order(
	    null, 
	    (1 to 5).map { i => new Item("any", "any") with ProcessAlwaysYieldsSuccess })

	  // when order has been processed
	  val result = order.process

	  // then result states it has been processed
	  Assert.fail("not implemented")
	}
 
 	@Test
 	def should_be_not_processed_if_all_items_has_failed_during_processing() {
	  // given order contains items that will be failed during processing
	  val order = new Order(
	    null, 
	    (1 to 5).map { i => new Item("any", "any") with ProcessAlwaysYieldsFailure })

	  // when order has been processed
	  val result = order.process

	  // then result states it has been failed
	  Assert.fail("not implemented")
	}
  
  	@Test
 	def should_be_partially_processed_if_some_items_has_been_processed_and_some_failed() {
	  // given order contains items that will be successfully processed 
      val items = (
        (1 to 5).map { i => new Item("any", "any") with ProcessAlwaysYieldsFailure }
	  	++
	  	(1 to 5).map { i => new Item("any", "any") with ProcessAlwaysYieldsSuccess })
      
	  val order = new Order(null, items)
      
	  // when order has been processed
	  val result = order.process
	  
	  // then result states it has been failed 
	  Assert.fail("not implemented")
   
	  // then it is possible to tell failed from processed
	  Assert.fail("not implemented")
	}
}
