package threading

import org.scalatest.FunSuite

class SynchronizedSpec extends FunSuite {
  test("Dead race scenario") {
    var num = 0
    def increment(): Int = {
      for (_ <- 1 to 100000) {
        num = num + 1
      }
      num
    }

    val t1 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t2 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t3 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t4 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t5 = new Thread{
      override def run(): Unit = {increment()}
    }

    val threads = List(t1, t2, t3, t4, t5)
    threads.map(_.start())
    threads.map(_.join())

    println(num)

    // OUTPUT:
    // 115789
    // 15885
    // 13383
  }

  test("Synchronization with Lock scenario") {
    var num = 0
    def increment(): Int = {
      this.synchronized{ // we changed only this line
        for (_ <- 1 to 100000) {
          num = num + 1
        }
        num
      }
    }

    val t1 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t2 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t3 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t4 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t5 = new Thread{
      override def run(): Unit = {increment()}
    }

    val threads = List(t1, t2, t3, t4, t5)
    threads.map(_.start())
    threads.map(_.join())

    println(num)

    // OUTPUT:
    // 500000
    // 500000
    // 500000
  }

  test("A Thread.sleep inside of a synchronized doesnt produce a change to another thread") {
    var num = 0
    def increment(): Int = {
      this.synchronized{ // we changed only this line
        println("this Thread id before waiting: " + Thread.currentThread.getId)
        Thread.sleep(4000)
        println("this Thread id after waiting: " + Thread.currentThread.getId)
        for (_ <- 1 to 100000) {
          num = num + 1
        }
        num
      }
    }

    val t1 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t2 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t3 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t4 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t5 = new Thread{
      override def run(): Unit = {increment()}
    }

    val threads = List(t1, t2, t3, t4, t5)
    threads.map(_.start())
    threads.map(_.join())

    println(num)

    // OUTPUT:
    // this Thread id before waiting: 155
    // this Thread id after waiting: 155
    // this Thread id before waiting: 159
    // this Thread id after waiting: 159
    // this Thread id before waiting: 158
    // this Thread id after waiting: 158
    // this Thread id before waiting: 157
    // this Thread id after waiting: 157
    // this Thread id before waiting: 156
    // this Thread id after waiting: 156
    // 500000

  }
}