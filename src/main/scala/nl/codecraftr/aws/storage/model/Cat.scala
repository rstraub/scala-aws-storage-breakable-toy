package nl.codecraftr.aws.storage.model

final case class Cat(name: String, age: Int, color: String)

object Cat {
  val garfield: Cat = Cat("Garfield", 38, "ginger and black")
}
