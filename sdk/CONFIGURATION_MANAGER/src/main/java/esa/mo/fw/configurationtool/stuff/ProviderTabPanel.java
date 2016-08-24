/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.fw.configurationtool.stuff;

import esa.mo.fw.configurationtool.services.com.ArchiveConsumerManagerPanel;
import esa.mo.fw.configurationtool.services.com.EventConsumerPanel;
import esa.mo.fw.configurationtool.services.common.ConfigurationConsumerPanel;
import esa.mo.fw.configurationtool.services.mc.ActionConsumerPanel;
import esa.mo.fw.configurationtool.services.mc.AggregationConsumerPanel;
import esa.mo.fw.configurationtool.services.mc.AlertConsumerPanel;
import esa.mo.fw.configurationtool.services.mc.CheckConsumerPanel;
import esa.mo.fw.configurationtool.services.mc.ParameterConsumerPanel;
import esa.mo.fw.configurationtool.services.mc.ParameterPublishedValues;
import esa.mo.fw.configurationtool.services.mc.StatisticConsumerPanel;
import esa.mo.fw.configurationtool.services.sm.AppsLauncherConsumerPanel;
import esa.mo.fw.configurationtool.services.sm.PackageManagementConsumerPanel;
import esa.mo.nanosatmoframework.groundmoadapter.consumer.GroundMOAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.common.directory.structures.ProviderSummary;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;

/**
 *
 * @author Cesar Coelho
 */
public class ProviderTabPanel extends javax.swing.JPanel {

    private final GroundMOAdapter services;

    /**
     * Creates new form ObjectsDisplay
     *
     * @param provider
     */
    public ProviderTabPanel(final ProviderSummary provider) {
        services = new GroundMOAdapter(provider);

        this.insertTabs(); // Insert all the tabs
    }

    public GroundMOAdapter getServices() {
        return this.services;
    }

    public final void insertTabs() {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                initComponents();

                try {
                    // Software Management
                    if (services.getSMServices() != null) {

                        if (services.getSMServices().getAppsLauncherService() != null) {
                            serviceTabs.insertTab("Apps Launcher service", null, new AppsLauncherConsumerPanel(services.getSMServices().getAppsLauncherService()), "Apps Launcher Tab", serviceTabs.getTabCount());
                        }

                        if (services.getSMServices().getPackageManagementService() != null) {
                            serviceTabs.insertTab("Package Management service", null, new PackageManagementConsumerPanel(services.getSMServices().getPackageManagementService()), "Package Management Tab", serviceTabs.getTabCount());
                        }

                    }

                    // COM
                    if (services.getCOMServices() != null) {

                        if (services.getCOMServices().getArchiveService() != null) {
                            serviceTabs.insertTab("Archive Manager", null, new ArchiveConsumerManagerPanel(services.getCOMServices().getArchiveService()), "Archive Tab", serviceTabs.getTabCount());
                        }

                        if (services.getCOMServices().getEventService() != null) {
                            serviceTabs.insertTab("Event service", null, new EventConsumerPanel(services.getCOMServices().getEventService(), services.getCOMServices().getArchiveService()), "Event Tab", serviceTabs.getTabCount());
                        }

                    }

                    // MC
                    if (services.getMCServices() != null) {

                        if (services.getMCServices().getActionService() != null) {
                            serviceTabs.insertTab("Action service", null, new ActionConsumerPanel(services.getMCServices().getActionService()), "Action Tab", serviceTabs.getTabCount());
                        }

                        if (services.getMCServices().getParameterService() != null) {
                            serviceTabs.insertTab("Parameter service", null, new ParameterConsumerPanel(services.getMCServices().getParameterService()), "Parameter Tab", serviceTabs.getTabCount());
                            serviceTabs.insertTab("Published Parameter Values", null, new ParameterPublishedValues(services.getMCServices().getParameterService()), "Published Parameters Tab", serviceTabs.getTabCount());
                        }

                        if (services.getMCServices().getAggregationService() != null) {
                            serviceTabs.insertTab("Aggregation service", null, new AggregationConsumerPanel(services.getMCServices().getAggregationService()), "Aggregation Tab", serviceTabs.getTabCount());
                        }

                        if (services.getMCServices().getAlertService() != null) {
                            serviceTabs.insertTab("Alert service", null, new AlertConsumerPanel(services.getMCServices().getAlertService()), "Alert Tab", serviceTabs.getTabCount());
                        }

                        if (services.getMCServices().getCheckService() != null) {
                            serviceTabs.insertTab("Check service", null, new CheckConsumerPanel(services.getMCServices().getCheckService()), "Check Tab", serviceTabs.getTabCount());
                        }

                        if (services.getMCServices().getStatisticService() != null) {
                            serviceTabs.insertTab("Statistic service", null, new StatisticConsumerPanel(services.getMCServices().getStatisticService(), services.getMCServices().getParameterService()), "Statistic Tab", serviceTabs.getTabCount());
                        }

                    }

                    // Common
                    if (services.getCommonServices() != null) {

                        if (services.getCommonServices().getConfigurationService() != null) {
                            serviceTabs.insertTab("Configuration service", null, new ConfigurationConsumerPanel(services.getCommonServices().getConfigurationService()), "Configuration Tab", serviceTabs.getTabCount());
                        }

                    }

                } catch (MALInteractionException ex) {
                    Logger.getLogger(ProviderTabPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MALException ex) {
                    Logger.getLogger(ProviderTabPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        };

        t1.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        serviceTabs = new javax.swing.JTabbedPane();

        setLayout(new java.awt.BorderLayout());

        serviceTabs.setToolTipText("");
        serviceTabs.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        serviceTabs.setMaximumSize(new java.awt.Dimension(800, 600));
        serviceTabs.setMinimumSize(new java.awt.Dimension(800, 600));
        serviceTabs.setPreferredSize(new java.awt.Dimension(800, 600));
        serviceTabs.setRequestFocusEnabled(false);
        add(serviceTabs, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane serviceTabs;
    // End of variables declaration//GEN-END:variables
}
