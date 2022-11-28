package esa.mo.nmf.cmt.services.mc;

import esa.mo.mc.impl.consumer.ParameterConsumerServiceImpl;
import esa.mo.nmf.cmt.ConstellationManagementTool;
import esa.mo.nmf.groundmoadapter.GroundMOAdapterImpl;
import org.ccsds.moims.mo.mal.MALDecoder;
import org.ccsds.moims.mo.mal.MALEncoder;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.structures.*;
import org.ccsds.moims.mo.mc.parameter.structures.*;
import org.ccsds.moims.mo.mc.structures.ParameterExpression;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static esa.mo.helpertools.helpers.HelperAttributes.javaType2Attribute;

public class ParameterGround {
    public static ParameterDefinitionDetails makeNewParameterDefinition(int rawType,
                                                                 String rawUnit, String description, boolean generationEnabled, float interval,
                                                                 ParameterExpression validityExpression, ParameterConversion conversion) {
        ParameterDefinitionDetails PDef = new ParameterDefinitionDetails();
        PDef.setDescription(description);
        PDef.setRawType((byte) rawType);
        PDef.setRawUnit(rawUnit);
        PDef.setDescription(description);
        PDef.setGenerationEnabled(generationEnabled);  // shall not matter, because when we add it it will be false!
        PDef.setReportInterval(new Duration(interval));
        PDef.setValidityExpression(validityExpression);
        PDef.setConversion(conversion);

        return PDef;
    }

    public static void setValue(GroundMOAdapterImpl groundMOAdapter) {

        final ParameterConsumerServiceImpl serviceMCParameter = groundMOAdapter.getMCServices().getParameterService();

        ParameterRawValue rawValue = new ParameterRawValue();

        Union attribute = (Union) javaType2Attribute("thisIsAnAttribute");

        rawValue.setParamInstId(4l);
        rawValue.setRawValue(attribute);

        ParameterRawValueList rawValueList = new ParameterRawValueList();

        rawValueList.add(rawValue);

        try {
            serviceMCParameter.getParameterStub().setValue(rawValueList);
        } catch (MALInteractionException | MALException ex) {
            Logger.getLogger(ConstellationManagementTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void addParameter(GroundMOAdapterImpl groundMOAdapter, ParameterRawValue value) {

        final ParameterConsumerServiceImpl serviceMCParameter = groundMOAdapter.getMCServices().getParameterService();

        //  1 = blob
        //  2 = boolean
        //  3 = Duration
        //  4 = Float
        //  5 = Double
        //  6 = Identifier
        //  7 = Octet
        //  8 = UOctet
        //  9 = Short
        // 10 = UShort
        // 11 = Integer
        // 12 = UInteger
        // 13 = Long
        // 14 = ULong
        // 15 = String
        // 16 = Time
        // 17 = FineTime
        // 18 = URI

        int rawType = 15;
        String rawUnit = "GPS position CSV";
        String description = "GeoFence data for testApp";
        boolean generationEnabled = false;
        float interval = 10f;
        ParameterExpression parameterExpression = null;
        ParameterConversion parameterConversion = null;

        ParameterDefinitionDetails parDef;
        parDef = makeNewParameterDefinition(
                rawType,
                rawUnit,
                description,
                generationEnabled,
                interval,
                parameterExpression,
                parameterConversion);

        ParameterCreationRequest request = new ParameterCreationRequest();
        request.setName(new Identifier("GeoFence.testApp"));
        request.setParamDefDetails(parDef);

        ParameterCreationRequestList requestList = new ParameterCreationRequestList();
        requestList.add(request);

        try {
            serviceMCParameter.getParameterStub().addParameter(requestList);
            setValue(groundMOAdapter);
        } catch (MALInteractionException | MALException ex) {
            Logger.getLogger(ConstellationManagementTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
