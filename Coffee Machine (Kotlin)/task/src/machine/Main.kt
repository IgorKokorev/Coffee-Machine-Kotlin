package machine

data class Coffee( val water: Int, val milk: Int, val bean: Int, val price: Int)

val espresso: Coffee = Coffee(250, 0, 16, 4)
val latte: Coffee = Coffee(350, 75, 20, 7)
val cappuccino: Coffee = Coffee(200, 100, 12, 6)

val cmResource: Map< String, Coffee> = mapOf("1" to espresso, "2" to latte,
    "3" to cappuccino)

data class CoffeeMachine( var water: Int, var milk: Int, var bean: Int,
                          var money: Int, var cups: Int)

val cm = CoffeeMachine(400, 540, 120, 550, 9)

class CMException(s: String) :  Exception(s)

fun main() {

//    printCMStatus()
    do {
        println("\nWrite action (buy, fill, take, remaining, exit):")
        when (readln()) {
            "buy" -> try { buy() }
                     catch (e: CMException) {
                        println("Sorry, not enough ${e.message}!") }
            "fill" -> fill()
            "take" -> take()
            "remaining" -> printCMStatus()
            "exit" -> break
        }
    } while (true)
}

fun printCMStatus() {
    println("\nThe coffee machine has:")
    println("${cm.water} ml of water")
    println("${cm.milk} ml of milk")
    println("${cm.bean} g of coffee beans")
    println("${cm.cups} disposable cups")
    println("\$${cm.money} of money")
}

fun buy() {
    println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
    val inp = readln()
    if ( !Regex("[123]").matches(inp) ) throw CMException("Incorrect operation")
    if(cm.water < cmResource[inp]!!.water ) throw CMException("water")
    if(cm.milk < cmResource[inp]!!.milk ) throw CMException("milk")
    if(cm.bean < cmResource[inp]!!.bean ) throw CMException("coffee beans")
    if(cm.cups < 1 ) throw CMException("cups")

    cm.water -= cmResource[inp]!!.water
    cm.milk -= cmResource[inp]!!.milk
    cm.bean -= cmResource[inp]!!.bean
    cm.cups--
    cm.money += cmResource[inp]!!.price

    println("I have enough resources, making you a coffee!")
}

fun fill() {
    println("Write how many ml of water you want to add:")
    cm.water += readln().toInt()
    println("Write how many ml of milk you want to add:")
    cm.milk += readln().toInt()
    println("Write how many grams of coffee beans you want to add:")
    cm.bean += readln().toInt()
    println("Write how many disposable cups you want to add:")
    cm.cups += readln().toInt()
}

fun take() {
    println("I gave you \$${cm.money}")
    cm.money = 0
}