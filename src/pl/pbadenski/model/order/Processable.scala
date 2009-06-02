package pl.pbadenski.model.order

trait Processable {
	type T
	
	def process : T
}
