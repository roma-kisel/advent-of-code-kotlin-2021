import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Reads the given input txt file as text
 */
fun readInputAsText(name: String) = File("src", "$name.txt").readText()

/**
 * Reads lines from the given input txt file and transform them to the given type
 */
fun <T> readInput(name: String, transform: (s: String) -> T) = readInput(name).map(transform)

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
