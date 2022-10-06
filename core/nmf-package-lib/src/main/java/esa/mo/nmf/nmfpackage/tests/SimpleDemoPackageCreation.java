/* ----------------------------------------------------------------------------
 * Copyright (C) 2021      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under European Space Agency Public License (ESA-PL) Weak Copyleft – v2.4
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
package esa.mo.nmf.nmfpackage.tests;

import esa.mo.helpertools.helpers.HelperTime;
import esa.mo.nmf.nmfpackage.HelperNMFPackage;
import esa.mo.nmf.nmfpackage.NMFPackageCreator;
import esa.mo.nmf.nmfpackage.descriptor.NMFPackageDescriptor;
import esa.mo.nmf.nmfpackage.descriptor.NMFPackageDetails;
import esa.mo.nmf.nmfpackage.descriptor.NMFPackageMetadata;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.ccsds.moims.mo.mal.structures.Time;

/**
 * A simple demo code to test the generation of NMF Packages.
 *
 * @author Cesar Coelho
 */
public class SimpleDemoPackageCreation {

    /**
     * Main command line entry point.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        SimpleDemoPackageCreation.createPackage();
    }

    public static void createPackage() {
        ArrayList<String> files = new ArrayList<>();
        ArrayList<String> newLocations = new ArrayList<>();

        String myAppFilename = "myApp.filetype";
        String dummyFolder = "myInstalledApp";
        files.add(myAppFilename);
        newLocations.add("apps" + File.separator + dummyFolder + File.separator + myAppFilename);

        final Time time = new Time(System.currentTimeMillis());
        final String timestamp = HelperTime.time2readableString(time);

        Logger.getLogger(NMFPackageCreator.class.getName()).log(Level.INFO,
                "\n------------- Package 1 Generation -------------\n");

        // Package 1
        NMFPackageDetails details = new NMFPackageDetails("TestPackage", "1.0",
                timestamp, "noclass", "", "96m");

        NMFPackageCreator.nmfPackageCreator(details, files, newLocations);

        Logger.getLogger(NMFPackageCreator.class.getName()).log(Level.INFO,
                "\n------------- Package 2 Generation -------------\n");

        // Package 2
        NMFPackageDetails details2 = new NMFPackageDetails("TestPackage", "2.0",
                timestamp, "noclass", "", "96m");
        NMFPackageCreator.nmfPackageCreator(details2, files, newLocations);

        Logger.getLogger(NMFPackageCreator.class.getName()).log(Level.INFO,
                "\n------------- Package 3 Generation -------------\n");

        // Package 3
        NMFPackageDetails details3 = new NMFPackageDetails("TestPackage", "3.0",
                timestamp, "noclass", "", "96m");
        String location = NMFPackageCreator.nmfPackageCreator(details3, files, newLocations);

        try {
            // Test if the created file can be parsed
            ZipFile writtenFile = new ZipFile(location);
            NMFPackageDescriptor theDescriptor = NMFPackageDescriptor.parseZipFile(writtenFile);
            theDescriptor = null;


            ZipEntry entry = writtenFile.getEntry(NMFPackageMetadata.FILENAME);

            // Try to open the the receipt file inside the Zip file
            // and parse it into a NMFPackageDescriptor object
            try (InputStream stream = writtenFile.getInputStream(entry)) {
                NMFPackageMetadata metadata = NMFPackageMetadata.load(stream);
                metadata = null;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SimpleDemoPackageCreation.class.getName()).log(
                    Level.SEVERE, "The file could not be processed!", ex);
        }
    }

}
