package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.init;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.VehicleType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.HouseholdId;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.RouteId;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.TripId;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.VehicleDataModel;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.VehicleTemplate;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.VehicleTemplateId;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.DistributionForVehicleTypeXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.HouseholdWithTheirVehiclesXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.NumberOfVehiclesForVehicleTypeXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.VehicleDataModelXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.VehicleTemplateIdXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.VehicleTemplateXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.VehicleTempleteForSpecificRouteIdXml;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer.VehicleTempleteForSpecificTripIdXml;
import cz.cvut.fel.aic.agentpolis.simulator.creator.initializator.InitModuleFactory;

/**
 * 
 * The factory/importer for loading vehicle data model from XML file via JAX-B into Java representation of
 * {@code VehicleDataModel}
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleDataModelFactory extends AbstractModule implements InitModuleFactory {

	private final File vehicleDataModelImXML;

	public VehicleDataModelFactory(File vehicleDataModelImXML) {
		super();
		this.vehicleDataModelImXML = vehicleDataModelImXML;
	}

	@Override
	protected void configure() {
	}

	@Singleton
	@Provides
	public VehicleDataModel provideVehicleDataModelModul() {

		VehicleDataModelXml vehicleDataModelXml = null;
		try {
			JAXBContext context = JAXBContext.newInstance(VehicleDataModelXml.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			vehicleDataModelXml = (VehicleDataModelXml) unmarshaller.unmarshal(vehicleDataModelImXML);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		Map<VehicleTemplateId, VehicleTemplate> vehicleTemplates = new HashMap<VehicleTemplateId, VehicleTemplate>();
		Map<VehicleType, Double> numberOfVehiclesForEachVehicleType = new HashMap<VehicleType, Double>();
		Map<HouseholdId, List<VehicleTemplateId>> householdsWithTheirVehicles = new HashMap<HouseholdId, List<VehicleTemplateId>>();
		Map<RouteId, VehicleTemplateId> specificVehicleTempleteForRouteId = new HashMap<RouteId, VehicleTemplateId>();
		Map<TripId, VehicleTemplateId> specificVehicleTempleteForTripId = new HashMap<TripId, VehicleTemplateId>();
		Map<VehicleType, TreeMap<Double, VehicleTemplateId>> distributionForEachVehicleType = new HashMap<VehicleType, TreeMap<Double, VehicleTemplateId>>();

		Map<String, VehicleTemplateId> assignedVehicleTempletedToVehicleId = new HashMap<String, VehicleTemplateId>();

		for (VehicleTemplateXml vehicleTemplate : vehicleDataModelXml
		        .getVehicleTemplet()) {
			VehicleTemplateId vehicleTemplateId = new VehicleTemplateId(vehicleTemplate.getVehicleTemplateId()
			        .getVehicleTemplateId());

			vehicleTemplates.put(
			        vehicleTemplateId,
			        new VehicleTemplate(vehicleTemplateId, vehicleTemplate.getPassengerCapacity(), vehicleTemplate
			                .getLengthInMeter(), vehicleTemplate.getTransportType(), vehicleTemplate.getVehicleType(),
			                vehicleTemplate.getProduceCO2InGramPerKm(), vehicleTemplate.getProduceCOInGramPerKm(),
			                vehicleTemplate.getProduceNOxInGramPerKm(), vehicleTemplate.getProduceSOxInGramPerKm(),
			                vehicleTemplate.getProducePM10InGramPerKm(), vehicleTemplate
			                        .getAverageVehicleSpeedInKmPerHour(), vehicleTemplate
			                        .getConsumeFuelInLitersPer100Km(), vehicleTemplate
			                        .getConsumeElectricityInkWhourPer100Km()));
		}

		for (NumberOfVehiclesForVehicleTypeXml numberOfVehiclesForVehicleType : vehicleDataModelXml
		        .getNumberOfVehiclesForEachVehicleType()) {
			numberOfVehiclesForEachVehicleType.put(numberOfVehiclesForVehicleType.vehicleTypeKey,
			        numberOfVehiclesForVehicleType.numberOfVehiclesValue.doubleValue());
		}

		for (HouseholdWithTheirVehiclesXml householdWithTheirVehicle : vehicleDataModelXml
		        .getHouseholdsWithTheirVehicles()) {

			List<VehicleTemplateId> vehicleTemplateIds = new ArrayList<VehicleTemplateId>();
			for (VehicleTemplateIdXml vehicleTemplateIdTmp : householdWithTheirVehicle
			        .getVehicleTemplateIdsValue()) {
				vehicleTemplateIds.add(new VehicleTemplateId(vehicleTemplateIdTmp.getVehicleTemplateId()));
			}

			householdsWithTheirVehicles.put(new HouseholdId(householdWithTheirVehicle.getHouseHoldIdKey()
			        .getHouseHoldId()), vehicleTemplateIds);
		}

		for (VehicleTempleteForSpecificRouteIdXml vehicleTempleteForSpecificRouteId : vehicleDataModelXml
		        .getSpecificVehicleTempleteForRouteId()) {
			specificVehicleTempleteForRouteId.put(new RouteId(vehicleTempleteForSpecificRouteId.getRouteId()
			        .getRouteId()), new VehicleTemplateId(vehicleTempleteForSpecificRouteId.getVehicleTemplateId()
			        .getVehicleTemplateId()));
		}

		for (VehicleTempleteForSpecificTripIdXml vehicleTempleteForSpecificTripId : vehicleDataModelXml
		        .getSpecificVehicleTempleteForTripId()) {
			specificVehicleTempleteForTripId.put(new TripId(vehicleTempleteForSpecificTripId.getTripId().getTripId()),
			        new VehicleTemplateId(vehicleTempleteForSpecificTripId.getVehicleTemplateId()
			                .getVehicleTemplateId()));
		}

		for (DistributionForVehicleTypeXml distributionForVehicleType : vehicleDataModelXml
		        .getDistributionForEachVehicleType()) {

			TreeMap<Double, VehicleTemplateId> vehicleTemplateIdsByCumulativeDist = distributionForEachVehicleType
			        .get(distributionForVehicleType.vehicleType);

			double prevComulative = 0.0;
			if (vehicleTemplateIdsByCumulativeDist == null) {
				vehicleTemplateIdsByCumulativeDist = new TreeMap<Double, VehicleTemplateId>();
			} else {
				prevComulative = vehicleTemplateIdsByCumulativeDist.lastKey();
			}

			prevComulative += distributionForVehicleType.vehicleTempletIdWithPercentage.percentageOfVehicleInVehicleType
			        .doubleValue();

			vehicleTemplateIdsByCumulativeDist
			        .put(prevComulative,
			                new VehicleTemplateId(
			                        distributionForVehicleType.vehicleTempletIdWithPercentage.vehicleTemplateId
			                                .getVehicleTemplateId()));
			distributionForEachVehicleType.put(distributionForVehicleType.vehicleType,
			        vehicleTemplateIdsByCumulativeDist);

		}

		for (VehicleType vehicleType : distributionForEachVehicleType.keySet()) {
			TreeMap<Double, VehicleTemplateId> vehicleTemplateIdsByCumulativeDist = distributionForEachVehicleType
			        .get(vehicleType);
			VehicleTemplateId vehicleTemplateId = vehicleTemplateIdsByCumulativeDist
			        .remove(vehicleTemplateIdsByCumulativeDist.lastKey());
			vehicleTemplateIdsByCumulativeDist.put(1.0, vehicleTemplateId);
		}

		return new VehicleDataModel(vehicleTemplates, numberOfVehiclesForEachVehicleType, householdsWithTheirVehicles,
		        specificVehicleTempleteForRouteId, specificVehicleTempleteForTripId, distributionForEachVehicleType,
		        assignedVehicleTempletedToVehicleId);
	}

	@Override
	public AbstractModule injectModule(Injector injector) {
		return this;
	}

}
