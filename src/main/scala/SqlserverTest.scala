import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import slick.jdbc.SQLServerProfile.api._
import slick.lifted.ProvenShape
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

//CREATE TABLE user_profiles (
//  id         INT IDENTITY (1, 1) PRIMARY KEY,
//  first_name VARCHAR(100) NOT NULL,
//  last_name  VARCHAR(100) NOT NULL
//)

case class UserProfile(id: Int, firstName: String, lastName: String)

class UserProfiles(tag: Tag) extends Table[UserProfile](tag, "user_profiles") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def firstName: Rep[String] = column[String]("first_name")
  def lastName: Rep[String] = column[String]("last_name")
  def * : ProvenShape[UserProfile] = (id, firstName, lastName) <>(UserProfile.tupled, UserProfile.unapply) // scalastyle:ignore
}

class UserProfileRepository(db: Database) {
  val userProfileQuery: TableQuery[UserProfiles] = TableQuery[UserProfiles]

  def insert(user: UserProfile): Future[Int] =
    db.run(userProfileQuery += user)

  def get(firstName: String): Future[Option[UserProfile]] =
    db.run(
      userProfileQuery
        .filter(_.firstName === firstName)
        .take(1)
        .result
        .headOption)

  def update(id: Int, firstName: String): Future[Int] =
    db.run(
      userProfileQuery
        .filter(_.id === id)
        .map(_.firstName)
        .update(firstName))

  def delete(id: Int): Future[Int] =
    db.run(userProfileQuery.filter(_.id === id).delete)
}

object SqlserverTest extends App {

  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("sqlserver")
  val db: JdbcProfile#Backend#Database = dbConfig.db

  val repo = new UserProfileRepository(db)

  val f = Insert
  //val f = Get
  //val f = Update
  //val f = Delete
  Await.ready(f, Duration.Inf)


  def Insert = {
    repo.insert(UserProfile(0, "a", "b"))
  }

  def Get = {
    repo.get("a").map( result => {
      result match {
        case Some(item) => println(item.toString)
        case None    => {}
      }
    })
  }

  def Update = {
    repo.update(3, "updated")
  }

  def Delete = {
    repo.delete(4)
  }
}