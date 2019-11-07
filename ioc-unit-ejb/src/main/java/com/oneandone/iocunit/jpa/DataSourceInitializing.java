package com.oneandone.iocunit.jpa;

import java.util.Collections;
import java.util.List;

/**
 * @author aschoerk
 */
public interface DataSourceInitializing {
    /**
     * if true, first removes all objects from the Db before first connection
     * @return true if db is to be cleared in the beginning.
     */
    public default boolean clearDb() {
        return true;
    }

    /**
     * Create Schema if necessary. Default is dbo.
     *
     * @return Name of a schema to be created before first connection. null,
     * if no schema needs to be created.
     */
    public default String initialSchemaName() {
        return null;
    }

    /**
     * Execute statements before first connection.
     *
     * @return statements to be executed before first connection,
     * after potential clearing and schema-creation.
     */
    public default List<String> initStatements() {
        return Collections.EMPTY_LIST;
    }

}
