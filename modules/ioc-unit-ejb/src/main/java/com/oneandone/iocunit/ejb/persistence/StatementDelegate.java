package com.oneandone.iocunit.ejb.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;


public class StatementDelegate implements Statement {
    private final Statement delegate;
    private final JdbcSqlConverter jdbcSqlConverter;

    private String convert(final String sql) {
        if(jdbcSqlConverter != null) {
            return jdbcSqlConverter.convert(sql);
        }
        else {
            return sql;
        }
    }

    public StatementDelegate(final Statement statement, JdbcSqlConverter jdbcSqlConverter) {
        this.delegate = statement;
        this.jdbcSqlConverter = jdbcSqlConverter;
    }


    @Override
    public ResultSet executeQuery(final String sql) throws SQLException {
        return delegate.executeQuery(convert(sql));
    }


    @Override
    public int executeUpdate(final String sql) throws SQLException {
        return delegate.executeUpdate(convert(sql));
    }


    @Override
    public void close() throws SQLException {
        delegate.close();
    }


    @Override
    public int getMaxFieldSize() throws SQLException {
        return delegate.getMaxFieldSize();
    }


    @Override
    public void setMaxFieldSize(final int max) throws SQLException {
        delegate.setMaxFieldSize(max);
    }


    @Override
    public int getMaxRows() throws SQLException {
        return delegate.getMaxRows();
    }


    @Override
    public void setMaxRows(final int max) throws SQLException {
        delegate.setMaxRows(max);
    }


    @Override
    public void setEscapeProcessing(final boolean enable) throws SQLException {
        delegate.setEscapeProcessing(enable);
    }


    @Override
    public int getQueryTimeout() throws SQLException {
        return delegate.getQueryTimeout();
    }


    @Override
    public void setQueryTimeout(final int seconds) throws SQLException {
        delegate.setQueryTimeout(seconds);
    }


    @Override
    public void cancel() throws SQLException {
        delegate.cancel();
    }


    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }


    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }


    @Override
    public void setCursorName(final String name) throws SQLException {
        delegate.setCursorName(name);
    }


    @Override
    public boolean execute(final String sql) throws SQLException {
        return delegate.execute(convert(sql));
    }


    @Override
    public ResultSet getResultSet() throws SQLException {
        return delegate.getResultSet();
    }


    @Override
    public int getUpdateCount() throws SQLException {
        return delegate.getUpdateCount();
    }


    @Override
    public boolean getMoreResults() throws SQLException {
        return delegate.getMoreResults();
    }


    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }


    @Override
    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }


    @Override
    public void setFetchSize(final int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }


    @Override
    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }


    @Override
    public int getResultSetConcurrency() throws SQLException {
        return delegate.getResultSetConcurrency();
    }


    @Override
    public int getResultSetType() throws SQLException {
        return delegate.getResultSetType();
    }


    @Override
    public void addBatch(final String sql) throws SQLException {
        delegate.addBatch(convert(sql));
    }


    @Override
    public void clearBatch() throws SQLException {
        delegate.clearBatch();
    }


    @Override
    public int[] executeBatch() throws SQLException {
        return delegate.executeBatch();
    }


    @Override
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }


    @Override
    public boolean getMoreResults(final int current) throws SQLException {
        return delegate.getMoreResults(current);
    }


    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return delegate.getGeneratedKeys();
    }


    @Override
    public int executeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        return delegate.executeUpdate(convert(sql), autoGeneratedKeys);
    }


    @Override
    public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        return delegate.executeUpdate(sql, columnIndexes);
    }


    @Override
    public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
        return delegate.executeUpdate(convert(sql), columnNames);
    }


    @Override
    public boolean execute(final String sql, final int autoGeneratedKeys) throws SQLException {
        return delegate.execute(convert(sql), autoGeneratedKeys);
    }


    @Override
    public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
        return delegate.execute(convert(sql), columnIndexes);
    }


    @Override
    public boolean execute(final String sql, final String[] columnNames) throws SQLException {
        return delegate.execute(convert(sql), columnNames);
    }


    @Override
    public int getResultSetHoldability() throws SQLException {
        return delegate.getResultSetHoldability();
    }


    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }


    @Override
    public void setPoolable(final boolean poolable) throws SQLException {
        delegate.setPoolable(poolable);
    }


    @Override
    public boolean isPoolable() throws SQLException {
        return delegate.isPoolable();
    }


    @Override
    public void closeOnCompletion() throws SQLException {
        delegate.closeOnCompletion();
    }


    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return delegate.isCloseOnCompletion();
    }


    @Override
    public long getLargeUpdateCount() throws SQLException {
        return delegate.getLargeUpdateCount();
    }


    @Override
    public void setLargeMaxRows(final long max) throws SQLException {
        delegate.setLargeMaxRows(max);
    }


    @Override
    public long getLargeMaxRows() throws SQLException {
        return delegate.getLargeMaxRows();
    }


    @Override
    public long[] executeLargeBatch() throws SQLException {
        return delegate.executeLargeBatch();
    }


    @Override
    public long executeLargeUpdate(final String sql) throws SQLException {
        return delegate.executeLargeUpdate(sql);
    }


    @Override
    public long executeLargeUpdate(final String sql, final int autoGeneratedKeys) throws SQLException {
        return delegate.executeLargeUpdate(sql, autoGeneratedKeys);
    }


    @Override
    public long executeLargeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
        return delegate.executeLargeUpdate(sql, columnIndexes);
    }


    @Override
    public long executeLargeUpdate(final String sql, final String[] columnNames) throws SQLException {
        return delegate.executeLargeUpdate(sql, columnNames);
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }


    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }
}
