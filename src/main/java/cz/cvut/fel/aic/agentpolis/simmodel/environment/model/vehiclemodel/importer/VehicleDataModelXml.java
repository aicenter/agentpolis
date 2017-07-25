package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * The XML wrapper of {@code VehicleDataModel} which is possible to
 * serialize/deserialize into/from XML file
 * 
 * 
 * 
 * @author Zbynek Moler
 * 
 */
@XmlType(name = "vehicleDataModel")
@XmlRootElement(name = "vehicleDataModel")
public class VehicleDataModelXml {

	@XmlElementWrapper(name = "distributionForEachVehicleTypeArray", required = true)
	private List<DistributionForVehicleTypeXml> distributionForEachVehicleType;

	@XmlElementWrapper(name = "numberOfVehiclesForEachVehicleTypeArray", required = true)
	private List<NumberOfVehiclesForVehicleTypeXml> numberOfVehiclesForEachVehicleType;

	@XmlElementWrapper(name = "vehicleTempletArray", required = true)
	private List<VehicleTemplateXml> vehicleTemplet;

	@XmlElementWrapper(name = "householdsWithTheirVehiclesArray")
	private List<HouseholdWithTheirVehiclesXml> householdsWithTheirVehicles;

	@XmlElementWrapper(name = "specificVehicleTempleteForRouteIdArray")
	private List<VehicleTempleteForSpecificRouteIdXml> specificVehicleTempleteForRouteId;

	@XmlElementWrapper(name = "specificVehicleTempleteForTripIdArray")
	private List<VehicleTempleteForSpecificTripIdXml> specificVehicleTempleteForTripId;

	@SuppressWarnings("unused")
    private VehicleDataModelXml() {
		super();
	}

	public VehicleDataModelXml(List<DistributionForVehicleTypeXml> distributionForEachVehicleType,
			List<NumberOfVehiclesForVehicleTypeXml> numberOfVehiclesForEachVehicleType,
			List<VehicleTemplateXml> vehicleTemplet, List<HouseholdWithTheirVehiclesXml> householdsWithTheirVehicles,
			List<VehicleTempleteForSpecificRouteIdXml> specificVehicleTempleteForRouteId,
			List<VehicleTempleteForSpecificTripIdXml> specificVehicleTempleteForTripId) {
		super();
		this.distributionForEachVehicleType = distributionForEachVehicleType;
		this.numberOfVehiclesForEachVehicleType = numberOfVehiclesForEachVehicleType;
		this.vehicleTemplet = vehicleTemplet;
		this.householdsWithTheirVehicles = householdsWithTheirVehicles;
		this.specificVehicleTempleteForRouteId = specificVehicleTempleteForRouteId;
		this.specificVehicleTempleteForTripId = specificVehicleTempleteForTripId;
	}

	public List<DistributionForVehicleTypeXml> getDistributionForEachVehicleType() {
		return distributionForEachVehicleType;
	}

	public List<NumberOfVehiclesForVehicleTypeXml> getNumberOfVehiclesForEachVehicleType() {
		return numberOfVehiclesForEachVehicleType;
	}

	public List<VehicleTemplateXml> getVehicleTemplet() {
		return vehicleTemplet;
	}

	public List<HouseholdWithTheirVehiclesXml> getHouseholdsWithTheirVehicles() {
		return householdsWithTheirVehicles;
	}

	public List<VehicleTempleteForSpecificRouteIdXml> getSpecificVehicleTempleteForRouteId() {
		return specificVehicleTempleteForRouteId;
	}

	public List<VehicleTempleteForSpecificTripIdXml> getSpecificVehicleTempleteForTripId() {
		return specificVehicleTempleteForTripId;
	}

}
