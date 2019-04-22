package dev.peproll.na

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger

import scala.util.Try

object Main extends App {

  val logger = Logger(BuildInfo.name)

  final case class Config(input: File = null, output: String = "result.csv", interval: Long = 3600)

  val parser = new scopt.OptionParser[Config](BuildInfo.name) {

    arg[File]("<file>")
      .required()
      .action((file, config) => config.copy(input = file))
      .validate(f => if (f.isFile) success else failure("File does not exist"))
      .text("Path to log file")

    opt[String]('o', "output")
      .action((output, config) => config.copy(output = output))
      .validate(o => Try(Paths.get(o)).toEither.fold(t => failure(t.getMessage), _ => success))
      .text("Path to result file")

    opt[Long]('i', "interval")
      .action((interval, config) => config.copy(interval = interval))
      .text("Period of time in seconds")
  }

  for {
    config <- parser.parse(args, Config())
  } yield {
    implicit val system = ActorSystem(BuildInfo.name)
    implicit val materializer = ActorMaterializer()
    import system.dispatcher

    logger.info("Start analysing ...")
    val result = LogAnalyser(
      config.input.toPath,
      Paths.get(config.output),
      config.interval
    )

    result.onComplete { _ =>
      logger.info("Finished!")
      system.terminate()
    }
  }
}
