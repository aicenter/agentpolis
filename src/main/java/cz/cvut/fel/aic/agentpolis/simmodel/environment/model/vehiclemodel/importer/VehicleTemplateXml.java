package cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.importer;

import javax.xml.bind.annotation.XmlElement;

import cz.cvut.fel.aic.agentpolis.simmodel.entity.vehicle.VehicleType;
import cz.cvut.fel.aic.agentpolis.simmodel.environment.model.vehiclemodel.TransportType;

/**
 * The template describes a particular vehicle through its attributes. The
 * implementation of this class servers for serialization/deserialization
 * into/from XML file.
 * 
 * It should not be used in a simulation data model
 * 
 * @author Zbynek Moler
 * 
 */
public class VehicleTemplateXml {

	@XmlElement(required = true)
	private VehicleTemplateIdXml vehicleTemplateId;
	@XmlElement(required = true)
	private int passengerCapacity;
	@XmlElement(required = true)
	private double lengthInMeter;

	@XmlElement(required = true)
	private TransportType transportType;
	@XmlElement(required = true)
	private VehicleType vehicleType;

	@XmlElement(required = true)
	private double produceCO2InGramPerKm;
	@XmlElement(required = true)
	private double produceCOInGramPerKm;
	@XmlElement(required = true)
	private double produceNOxInGramPerKm;
	@XmlElement(required = true)
	private double produceSOxInGramPerKm;
	@XmlElement(required = true)
	private double producePM10InGramPerKm;

	@XmlElement(required = true)
	private double averageVehicleSpeedInKmPerHour;

	@XmlElement(required = true)
	private double consumeFuelInLitersPer100Km;
	@XmlElement(required = true)
	private double consumeElectricityInkWhourPer100Km;

	@SuppressWarnings("unused")
    private VehicleTemplateXml() {
	}

	public VehicleTemplateXml(VehicleTemplateIdXml vehicleTemplateId, int passengerCapacity, double lengthInMeter,
							  TransportType transportType, VehicleType vehicleType, double produceCO2InGramPerKm,
							  double produceCOInGramPerKm, double produceNOxInGramPerKm, double produceSOxInGramPerKm,
							  double producePM10InGramPerKm, double averageVehicleSpeedInKmPerHour, double consumeFuelInLitersPer100Km,
							  double consumeElectricityInkWhourPer100Km) {
		super();
		this.vehicleTemplateId = vehicleTemplateId;
		this.passengerCapacity = passengerCapacity;
		this.lengthInMeter = lengthInMeter;
		this.transportType = transportType;
		this.vehicleType = vehicleType;
		this.produceCO2InGramPerKm = produceCO2InGramPerKm;
		this.produceCOInGramPerKm = produceCOInGramPerKm;
		this.produceNOxInGramPerKm = produceNOxInGramPerKm;
		this.produceSOxInGramPerKm = produceSOxInGramPerKm;
		this.producePM10InGramPerKm = producePM10InGramPerKm;
		this.averageVehicleSpeedInKmPerHour = averageVehicleSpeedInKmPerHour;
		this.consumeFuelInLitersPer100Km = consumeFuelInLitersPer100Km;
		this.consumeElectricityInkWhourPer100Km = consumeElectricityInkWhourPer100Km;
	}

	public VehicleTemplateIdXml getVehicleTemplateId() {
		return vehicleTemplateId;
	}

	public int getPassengerCapacity() {
		return passengerCapacity;
	}

	public double getLengthInMeter() {
		return lengthInMeter;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public double getProduceCO2InGramPerKm() {
		return produceCO2InGramPerKm;
	}

	public double getProduceCOInGramPerKm() {
		return produceCOInGramPerKm;
	}

	public double getProduceNOxInGramPerKm() {
		return produceNOxInGramPerKm;
	}

	public double getProduceSOxInGramPerKm() {
		return produceSOxInGramPerKm;
	}

	public double getProducePM10InGramPerKm() {
		return producePM10InGramPerKm;
	}

	public double getAverageVehicleSpeedInKmPerHour() {
		return averageVehicleSpeedInKmPerHour;
	}

	public double getConsumeFuelInLitersPer100Km() {
		return consumeFuelInLitersPer100Km;
	}

	public double getConsumeElectricityInkWhourPer100Km() {
		return consumeElectricityInkWhourPer100Km;
	}

}
