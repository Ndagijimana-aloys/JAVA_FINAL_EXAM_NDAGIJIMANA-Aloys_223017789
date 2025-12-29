// fpms/panels/RefreshablePanel.java
package fpms.panels;

/**
 * Interface used by MainDashboard to refresh data in all tabs.
 * Every panel (AccountHolderPanel, AccountPanel, TransactionPanel, etc.)
 * must implement this interface and provide a refresh() method
 * that reloads data from the database and updates the JTable.
 */
public interface RefreshablePanel {

    /**
     * Refreshes the panel's data.
     * This method is called when:
     * - User switches to the tab
     * - User clicks "Refresh All Tabs" in the menu
     * - After performing CRUD operations
     */
    void refresh();
}