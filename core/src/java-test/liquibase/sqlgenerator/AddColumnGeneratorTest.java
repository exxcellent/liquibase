package liquibase.sqlgenerator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import liquibase.database.CacheDatabase;
import liquibase.database.DB2Database;
import liquibase.database.DerbyDatabase;
import liquibase.database.H2Database;
import liquibase.database.MySQLDatabase;
import liquibase.database.OracleDatabase;
import liquibase.database.SQLiteDatabase;
import liquibase.statement.AddColumnStatement;
import liquibase.statement.AutoIncrementConstraint;
import liquibase.statement.PrimaryKeyConstraint;

public class AddColumnGeneratorTest extends AbstractSqlGeneratorTest<AddColumnStatement> {

    public AddColumnGeneratorTest() throws Exception {
        this(new AddColumnGenerator());
    } 

    public AddColumnGeneratorTest(SqlGenerator<AddColumnStatement> generatorUnderTest) throws Exception {
        super(generatorUnderTest);
    }

	@Override
	protected AddColumnStatement createSampleSqlStatement() {
		return new AddColumnStatement(null, "table_name", "column_name", "column_type", null);
	}


	@Override
    public void isValid() throws Exception {
        super.isValid();
        AddColumnStatement addPKColumn = new AddColumnStatement(null, "table_name", "column_name", "column_type", null, new PrimaryKeyConstraint("pk_name"));

        assertFalse(generatorUnderTest.validate(addPKColumn, new OracleDatabase()).hasErrors());
        assertTrue(generatorUnderTest.validate(addPKColumn, new CacheDatabase()).getErrorMessages().contains("Cannot add a primary key column"));
        assertTrue(generatorUnderTest.validate(addPKColumn, new H2Database()).getErrorMessages().contains("Cannot add a primary key column"));
        assertTrue(generatorUnderTest.validate(addPKColumn, new DB2Database()).getErrorMessages().contains("Cannot add a primary key column"));
        assertTrue(generatorUnderTest.validate(addPKColumn, new DerbyDatabase()).getErrorMessages().contains("Cannot add a primary key column"));
        assertTrue(generatorUnderTest.validate(addPKColumn, new SQLiteDatabase()).getErrorMessages().contains("Cannot add a primary key column"));

        assertTrue(generatorUnderTest.validate(new AddColumnStatement(null, null, null, null, null, new AutoIncrementConstraint()), new MySQLDatabase()).getErrorMessages().contains("Cannot add a non-primary key identity column"));

        assertTrue(generatorUnderTest.validate(new AddColumnStatement(null, null, null, null, null, new AutoIncrementConstraint()), new MySQLDatabase()).getErrorMessages().contains("Cannot add a non-primary key identity column"));
    }
}