package com.styng.playervitals.transaction;

/**
 * <p></p>
 */
public interface Action {

    /**
     * <p>Executes the action. If for instance you're updating values in a database, put that code here</p>
     *
     * @throws Exception The exception that occurred while trying to execute the action
     */
    void execute() throws Exception;

    /**
     * <p>Should roll back any changes made in {@link #execute()}</p>
     */
    void rollback() throws Exception;

}
