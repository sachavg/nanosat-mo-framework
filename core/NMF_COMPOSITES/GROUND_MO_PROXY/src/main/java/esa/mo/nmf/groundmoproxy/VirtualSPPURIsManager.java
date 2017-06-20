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
package esa.mo.nmf.groundmoproxy;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class VirtualSPPURIsManager {

    private final static String PROTOCOL_SPP = "malspp";
    private final static String APID_QUALIFIER = "247";
    private final static int APID_RANGE_START = 2;
    private final static int APID_RANGE_END = 2;
    private final static int SOURDEID_RANGE_START = 0; // Set by SPP
    private final static int SOURDEID_RANGE_END = 255;
    private final HashMap<String, String> virtualAPIDsMap = new HashMap<String, String>();
    private final HashMap<String, String> reverseMap = new HashMap<String, String>();
    private final AtomicInteger uniqueAPID;
    private final AtomicInteger uniqueSourceId;
    private final Object MUTEX = new Object();

    public VirtualSPPURIsManager() {
        Random random = new Random();
        int apid = random.nextInt((APID_RANGE_END - APID_RANGE_START) + 1) + APID_RANGE_START;
        int sourceId = random.nextInt((SOURDEID_RANGE_END - SOURDEID_RANGE_START) + 1) + SOURDEID_RANGE_START;

        uniqueAPID = new AtomicInteger(apid);
        uniqueSourceId = new AtomicInteger(sourceId);
    }

    public String getURI(String virtualSPPURI) {
        String reverse = null;

        synchronized (MUTEX) {
            reverse = reverseMap.get(virtualSPPURI);

            if (reverse == null) {
                System.out.println("The reverse APID for virtualSPPURI: " + virtualSPPURI + " could not be found!");
            }
        }

        return reverse;
    }

    public String getVirtualSPPURI(String uriFrom) {
        String virtualAPID = null;

        synchronized (MUTEX) {
            virtualAPID = virtualAPIDsMap.get(uriFrom);

            if (virtualAPID == null) { // If it does not exist...
                virtualAPID = this.generateNewSPPURI();
                virtualAPIDsMap.put(uriFrom, virtualAPID);
                reverseMap.put(virtualAPID, uriFrom);
            }

            System.out.println("The virtualAPID is: " + virtualAPID);
        }

        return virtualAPID;
    }

    private String generateNewSPPURI() {
        int sourceId = uniqueSourceId.getAndIncrement();
        int apid = uniqueAPID.get();

        if (sourceId > SOURDEID_RANGE_END) {
            uniqueSourceId.set(SOURDEID_RANGE_START);
            sourceId = SOURDEID_RANGE_START;
            apid = uniqueAPID.incrementAndGet();

            if (apid > APID_RANGE_END) {
                uniqueAPID.set(APID_RANGE_START);
                apid = APID_RANGE_START;
            }
        }

        return PROTOCOL_SPP + ":" + APID_QUALIFIER + "/" + apid + "/" + sourceId;
    }

}