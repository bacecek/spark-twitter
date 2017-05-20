import org.apache.hadoop.mapred.InvalidInputException
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import scala.Tuple2
import java.io.IOException

fun main(args: Array<String>) {

    // Получение имени файла, в котором хранятся твиты
    val inputFile = "output.txt"

    // Инициализация Spark
    val config = SparkConf()
            .setMaster("local")
            .setAppName("Spark")
    val sparkContext = JavaSparkContext(config)

    try {
        // Считывание текста из файла
        val text = sparkContext.textFile(inputFile)
        // Получение списка слов
        val words = text.flatMap { it -> it.replace("\\pP\\s+".toRegex(), "").toLowerCase().split(" ") }

        // Получение результата - списка слов и их количество в твитах
        val counts = words
                .mapToPair { x -> Tuple2(x, 1) }
                .reduceByKey { x, y -> x + y }
                .filter { x -> x._2() > 10 }
                .collect()
                .sortedByDescending { it._2() }

        // Вывод в консоль
        for (count in counts) {
            println("${count._1()} ${count._2()}")
        }
    } catch(e: InvalidInputException) {
        println("нет такого файла!")
    } catch(e: IOException) {
        println("проверьте правильность установки Hadoop и Spark на свой компьютер")
    }
}