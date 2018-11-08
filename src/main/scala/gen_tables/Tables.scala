package gen_tables
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.SQLServerProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = UserProfiles.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table UserProfiles
   *  @param id Database column id SqlType(int identity), PrimaryKey
   *  @param firstName Database column first_name SqlType(varchar), Length(100,true)
   *  @param lastName Database column last_name SqlType(varchar), Length(100,true) */
  case class UserProfile(id: Int, firstName: String, lastName: String)
  /** GetResult implicit for fetching UserProfile objects using plain SQL queries */
  implicit def GetResultUserProfile(implicit e0: GR[Int], e1: GR[String]): GR[UserProfile] = GR{
    prs => import prs._
    UserProfile.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table user_profiles. Objects of this class serve as prototypes for rows in queries. */
  class UserProfiles(_tableTag: Tag) extends profile.api.Table[UserProfile](_tableTag, Some("dbo"), "user_profiles") {
    def * = (id, firstName, lastName) <> (UserProfile.tupled, UserProfile.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(firstName), Rep.Some(lastName)).shaped.<>({r=>import r._; _1.map(_=> UserProfile.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int identity), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    /** Database column first_name SqlType(varchar), Length(100,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(100,varying=true))
    /** Database column last_name SqlType(varchar), Length(100,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(100,varying=true))
  }
  /** Collection-like TableQuery object for table UserProfiles */
  lazy val UserProfiles = new TableQuery(tag => new UserProfiles(tag))
}
