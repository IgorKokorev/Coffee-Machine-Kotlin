package machine

// coffee data: how much water, milk and beans needed for 1 cup and the price of 1 cup
data class Coffee( val water: Int, val milk: Int, val bean: Int, val price: Int)

val espresso: Coffee = Coffee(250, 0, 16, 4)
val latte: Coffee = Coffee(350, 75, 20, 7)
val cappuccino: Coffee = Coffee(200, 100, 12, 6)

// menu options to select a coffee to prepare
val cmSelect: Map< String, Coffee> = mapOf("1" to espresso, "2" to latte, "3" to cappuccino)

// data of a cm resources
data class CMData(var water: Int, var milk: Int, var bean: Int,
                  var money: Int, var cups: Int)

class CMException(s: String) :  Exception(s)

class CoffeeMachine ( val resource: CMData, var staus: Int = 0) {

    // Coffee Machine status
    val CMS_IDLE = 0
    val CMS_BUY = 1
    val CMS_FILL_WATER = 2
    val CMS_FILL_MILK = 3
    val CMS_FILL_BEANS = 4
    val CMS_FILL_CUPS = 5

    // perform the operation in input depending on the CM status
    fun doCM( inp: String) {
        when (this.staus) {
            CMS_IDLE -> doCMIdle(inp)
            CMS_BUY -> try{
                this.doCMBuy(inp)
            } catch (e: CMException) {
                println("Sorry, not enough ${e.message}!")
            }
            CMS_FILL_WATER -> this.doCMFill(inp)
            CMS_FILL_MILK -> this.doCMFill(inp)
            CMS_FILL_BEANS -> this.doCMFill(inp)
            CMS_FILL_CUPS -> this.doCMFill(inp)
        }
    }

    private fun doCMFill(inp: String) {

        when (this.staus ) {
            CMS_FILL_WATER -> {
                this.resource.water += inp.toInt()
                println("Write how many ml of milk you want to add:")
                this.staus = CMS_FILL_MILK
            }

            CMS_FILL_MILK -> {
                this.resource.milk += inp.toInt()
                println("Write how many ml of coffee beans you want to add:")
                this.staus = CMS_FILL_BEANS
            }

            CMS_FILL_BEANS -> {
                this.resource.bean += inp.toInt()
                println("Write how many disposable cups you want to add:")
                this.staus = CMS_FILL_CUPS
            }

            CMS_FILL_CUPS -> {
                this.resource.cups += inp.toInt()
                this.staus = CMS_IDLE
            }
        }
    }

    private fun doCMBuy(inp: String) {

        this.staus = CMS_IDLE
        if (inp.equals("back")) return

        if ( !Regex("[123]").matches(inp) ) throw CMException("Incorrect operation")

        if(this.resource.water < cmSelect[inp]!!.water ) throw CMException("water")
        if(this.resource.milk < cmSelect[inp]!!.milk ) throw CMException("milk")
        if(this.resource.bean < cmSelect[inp]!!.bean ) throw CMException("coffee beans")
        if(this.resource.cups < 1 ) throw CMException("cups")

        this.resource.water -= cmSelect[inp]!!.water
        this.resource.milk -= cmSelect[inp]!!.milk
        this.resource.bean -= cmSelect[inp]!!.bean
        this.resource.cups--
        this.resource.money += cmSelect[inp]!!.price

        println("I have enough resources, making you a coffee!")
    }

    private fun doCMIdle(inp: String) {
        when (inp) {
            "buy" -> {
                println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
                this.staus = CMS_BUY
                }
            "fill" -> {
                println("Write how many ml of water you want to add:")
                this.staus = CMS_FILL_WATER
            }
            "take" -> this.take()
            "remaining" -> this.printCMStatus()
        }
    }

    private fun take() {
        println("I gave you \$${this.resource.money}")
        this.resource.money = 0
    }

    fun printCMStatus() {
        println("\nThe coffee machine has:")
        println("${this.resource.water} ml of water")
        println("${this.resource.milk} ml of milk")
        println("${this.resource.bean} g of coffee beans")
        println("${this.resource.cups} disposable cups")
        println("\$${this.resource.money} of money")
    }
}


fun main() {
    // creating a coffee machine with supplies and IDLE status
    val cm = CoffeeMachine( CMData(400, 540, 120, 550, 9) )

    do {
        // print the welcome message only if CM is idle
        if (cm.staus == cm.CMS_IDLE ) println("\nWrite action (buy, fill, take, remaining, exit):")
        // reading input anyway
        val inp = readln()
        // if user wants to quit - buy
        if (inp.equals("exit")) break
        // if not, CM will do what is asked in input string
        cm.doCM(inp)
    } while (true)
    println("buy")
}
