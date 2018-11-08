import slick.basic.DatabaseConfig
import slick.codegen.SourceCodeGenerator
import slick.jdbc.{JdbcProfile, SQLServerProfile}
import slick.jdbc.SQLServerProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}


//Important: add O.AutoInc to
//val id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

object CodeGenTest extends App {

//  slick.codegen.SourceCodeGenerator.main(
//    Array("slick.jdbc.SQLServerProfile",
//        "slick.jdbc.SQLServerProfile$",
//        "jdbc:sqlserver://127.0.0.1:1433;databaseName=slick_test",
//        "src/main/scala",
//        "gen_tables",
//        "sa",
//        "1234")
//  )

  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("sqlserver")
  val db: JdbcProfile#Backend#Database = dbConfig.db

  val modelAction = SQLServerProfile.createModel(Some(SQLServerProfile.defaultTables)) // you can filter specific tables here
  val modelFuture = db.run(modelAction)
  // customize code generator
  val codegenFuture = modelFuture.map(model => new SourceCodeGenerator(model) {
    // override mapped table and class name
    override def entityName =
      dbTableName => dbTableName.dropRight(1).toLowerCase.toCamelCase
    override def tableName =
      dbTableName => dbTableName.toLowerCase.toCamelCase

    // add some custom import
    //override def code = "import foo.{MyCustomType,MyCustomTypeMapper}" + "\n" + super.code

    // override table generator
    override def Table = new Table(_){

      //override def autoIncLastAsOption = true

      // disable entity class generation and mapping
      override def EntityType = new EntityType{
        override def classEnabled = true
      }

      // override contained column generator
      override def Column = new Column(_){
        // use the data model member of this column to change the Scala type,
        // e.g. to a custom enum or anything else
        override def rawType =
          if(model.name == "SOME_SPECIAL_COLUMN_NAME") "MyCustomType" else super.rawType
      }
    }
  })

  val resultFuture = codegenFuture.map( result => {
     result.writeToFile(
        "slick.jdbc.SQLServerProfile",
        "src/main/scala/",
        "gen_tables",
        "Tables",
        "Tables.scala"
   )})

  Await.ready(resultFuture, Duration.Inf)
}