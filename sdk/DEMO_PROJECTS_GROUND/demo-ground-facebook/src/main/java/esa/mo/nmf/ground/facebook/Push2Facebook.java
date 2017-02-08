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
package esa.mo.nmf.ground.facebook;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import esa.mo.helpertools.connections.ConnectionConsumer;
import esa.mo.helpertools.helpers.HelperMisc;
import esa.mo.nmf.groundmoadapter.GroundMOAdapter;
import esa.mo.nmf.groundmoadapter.SimpleDataReceivedListener;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ground consumer: Demo Facebook
 *
 */
public class Push2Facebook {

    private static final String TOKEN_FILENAME = "token.properties";
    private GroundMOAdapter moGroundAdapter;
    private String ACCESS_TOKEN;

    public Push2Facebook() {

        ConnectionConsumer connection = new ConnectionConsumer();

        try {
            connection.loadURIs();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Push2Facebook.class.getName()).log(Level.SEVERE, null, ex);
        }

        moGroundAdapter = new GroundMOAdapter(connection);
        moGroundAdapter.addDataReceivedListener(new DataReceivedAdapter());

        final java.util.Properties sysProps = System.getProperties();

        // Load the properties out of the file
        File file = new File(System.getProperty(TOKEN_FILENAME, TOKEN_FILENAME));
        if (file.exists()) {
            try {
                sysProps.putAll(HelperMisc.loadProperties(file.toURI().toURL(), TOKEN_FILENAME));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Push2Facebook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ACCESS_TOKEN = System.getProperty("access_token", "null");

    }

    /**
     * Main command line entry point.
     *
     * @param args the command line arguments
     */
    public static void main(final String args[]) throws Exception {
        Push2Facebook demo = new Push2Facebook();
    }

    public class DataReceivedAdapter extends SimpleDataReceivedListener {

        @Override
        public void onDataReceived(String parameterName, Serializable data) {

            Logger.getLogger(Push2Facebook.class.getName()).log(Level.INFO, "\nPosting on facebook...\nParameter name: {0}" + "\n" + "Data content:\n{1}", new Object[]{parameterName, data.toString()});

            // Get the Token here: https://developers.facebook.com/tools/explorer/
            FacebookClient facebookClient = new DefaultFacebookClient(ACCESS_TOKEN, Version.VERSION_2_4);

            if (facebookClient == null) {
                Logger.getLogger(Push2Facebook.class.getName()).log(Level.INFO, "The facebookClient is null! The access token might be incorrect...\n");
            } else {
                Logger.getLogger(Push2Facebook.class.getName()).log(Level.INFO, "The facebookClient is connected!\n");
            }

            FacebookType publishMessageResponse = facebookClient.publish(
                    "me/feed",
                    FacebookType.class,
                    Parameter.with("message", data.toString())
            );

            String str = "";

            if (publishMessageResponse.getId() != null) {
                str += publishMessageResponse.getId() + "\n";
            }

            if (publishMessageResponse.getMetadata() != null) {
                str += publishMessageResponse.getMetadata().toString() + "\n";
            }

            if (publishMessageResponse.getType() != null) {
                str += publishMessageResponse.getType() + "\n";
            }

            Logger.getLogger(Push2Facebook.class.getName()).log(Level.INFO, str);
        }

    }

}