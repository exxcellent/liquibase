package liquibase.change.core;

import liquibase.database.core.DB2Database;
import liquibase.database.Database;
import liquibase.statement.RenameTableStatement;
import liquibase.statement.ReorganizeTableStatement;
import liquibase.statement.SqlStatement;
import liquibase.util.StringUtils;
import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.Change;

import java.util.ArrayList;
import java.util.List;

/**
 * Renames an existing table.
 */
public class RenameTableChange extends AbstractChange {

    private String schemaName;
    private String oldTableName;

    private String newTableName;

    public RenameTableChange() {
        super("renameTable", "Rename Table", ChangeMetaData.PRIORITY_DEFAULT);
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = StringUtils.trimToNull(schemaName);
    }

    public String getOldTableName() {
        return oldTableName;
    }

    public void setOldTableName(String oldTableName) {
        this.oldTableName = oldTableName;
    }

    public String getNewTableName() {
        return newTableName;
    }

    public void setNewTableName(String newTableName) {
        this.newTableName = newTableName;
    }

    public SqlStatement[] generateStatements(Database database) {
        List<SqlStatement> statements = new ArrayList<SqlStatement>();
        String schemaName = getSchemaName() == null?database.getDefaultSchemaName():getSchemaName();
        statements.add(new RenameTableStatement(schemaName, getOldTableName(), getNewTableName()));
        if (database instanceof DB2Database) {
            statements.add(new ReorganizeTableStatement(schemaName, getNewTableName()));
        }

        return statements.toArray(new SqlStatement[statements.size()]);
    }

    @Override
    protected Change[] createInverses() {
        RenameTableChange inverse = new RenameTableChange();
        inverse.setSchemaName(getSchemaName());
        inverse.setOldTableName(getNewTableName());
        inverse.setNewTableName(getOldTableName());

        return new Change[]{
                inverse
        };
    }

    public String getConfirmationMessage() {
        return "Table " + oldTableName + " renamed to " + newTableName;
    }
}