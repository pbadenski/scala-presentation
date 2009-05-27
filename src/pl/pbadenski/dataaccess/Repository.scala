package pl.pbadenski.dataaccess;

import model.customer._
import model.order._

object Repository {
	private val POWER_MEDIA = 
		new Company(
				"Power Media", 
				new Address("Kiełbaśnicza 27", "Wrocław", "53-123"))

	private val RADIO_ESKA = 
		new Company(
				"Radio Eska", 
				new Address("Esiasta 27", "Wrocław", "52-123"))

	private val JAN_KOWALSKI = new Customer(1, 
					"Jan", 
					"Kowalski", 
					new Address(
							"Wrocławska 122", 
							"Wałbrzych", 
							"53-321"),
					POWER_MEDIA) with CustomerService;
	private val objects = List(
			JAN_KOWALSKI,
			new Customer(2, 
					"Maria", 
					"Słowik", 
					new Address(
							"Kazimierza wielkiego 15", 
							"Wrocław", 
							"53-304"),
					POWER_MEDIA) with CustomerService
	)
 
	def customers = objects
 
	def companyArchive = Map[Customer, List[Company]](JAN_KOWALSKI -> List(RADIO_ESKA))
 
	def findByName(name : String) = objects find { _.firstName == name };
}
