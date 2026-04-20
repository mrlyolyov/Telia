package com.optima.cms.adapter.magnolia;

import com.optima.cms.model.device.*;
import com.optima.cms.model.plan.FindAllRequest;
import com.optima.cms.port.DeviceCatalogPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Magnolia-backed implementation of {@link DeviceCatalogPort}: HTTP client + translation to API models.
 * Currently returns example catalog data matching the device contract.
 */
@Component
public class MagnoliaDeviceCatalogAdapter implements DeviceCatalogPort {

	@Override
	public List<Device> listDevices(FindAllRequest request) {
		return List.of(exampleIphone15Plus());
	}

	private static Device exampleIphone15Plus() {
		Device device = new Device();
		device.setExternalId("2000251652");
		device.setName("iPhone 15 Plus");
		device.setDescription("A16 Bionic chip. 48MP camera. USB-C.");

		DeviceAttachment front = new DeviceAttachment();
		front.setId("default-front");
		front.setName("iPhone 15 Plus - Front");
		front.setAttachmentType("picture");
		front.setMimeType("image/png");
		front.setFile("682a1f3c");

		DeviceAttachment specs = new DeviceAttachment();
		specs.setId("specs");
		specs.setName("Specifications");
		specs.setAttachmentType("other");
		specs.setContent("<table>...HTML specs...</table>");

		DeviceAttachment moreDetails = new DeviceAttachment();
		moreDetails.setId("more-details");
		moreDetails.setName("More Details");
		moreDetails.setAttachmentType("other");
		moreDetails.setContent("Summary text...");
		moreDetails.setExtension(List.of(
				kv("overview", "<div>...</div>"),
				kv("termsAndConditions", "<div>...</div>")));

		device.setAttachment(List.of(front, specs, moreDetails));

		device.setPrice(List.of(
				price(1, "oneTime", "USD", new BigDecimal("899")),
				price(2, "recurring", "USD", new BigDecimal("37.45"))));

		DeviceCharacteristics ch = new DeviceCharacteristics();
		ch.setColor(List.of(
				cv("Black"),
				cv("Pink"),
				cv("Yellow")));
		ch.setStorage(List.of(
				cv("128GB"),
				cv("256GB")));
		ch.setFeatures(List.of(
				feature("Display", "6.7 inches"),
				feature("Camera", "48MP + 12MP"),
				feature("Brand", "Apple"),
				feature("CPU", "Apple A16 Bionic"),
				feature("Battery", "4383 mAh"),
				feature("Band", "5G")));
		device.setCharacteristics(ch);

		DeviceVariant v1 = new DeviceVariant();
		v1.setVariantId(2000256260L);
		v1.setName("iPhone 15 Plus 128GB Black");
		DeviceCharacteristics v1ch = new DeviceCharacteristics();
		v1ch.setColor(List.of(cv("Black")));
		v1ch.setStorage(List.of(cv("128GB")));
		v1.setCharacteristics(v1ch);

		DeviceVariant v2 = new DeviceVariant();
		v2.setVariantId(2000256264L);
		v2.setName("iPhone 15 Plus 128GB Pink");
		DeviceAttachment pinkPic = new DeviceAttachment();
		pinkPic.setId("pink-front");
		pinkPic.setAttachmentType("picture");
		pinkPic.setMimeType("image/png");
		pinkPic.setFile("a92b3e7d");
		v2.setAttachment(List.of(pinkPic));
		DeviceCharacteristics v2ch = new DeviceCharacteristics();
		v2ch.setColor(List.of(cv("Pink")));
		v2ch.setStorage(List.of(cv("128GB")));
		v2.setCharacteristics(v2ch);

		device.setVariants(List.of(v1, v2));
		return device;
	}

	private static DeviceKeyValue kv(String key, String value) {
		DeviceKeyValue e = new DeviceKeyValue();
		e.setKey(key);
		e.setValue(value);
		return e;
	}

	private static DeviceCharacteristicValue cv(String value) {
		DeviceCharacteristicValue e = new DeviceCharacteristicValue();
		e.setValue(value);
		return e;
	}

	private static DeviceFeature feature(String key, String value) {
		DeviceFeature f = new DeviceFeature();
		f.setKey(key);
		f.setValue(value);
		return f;
	}

	private static DevicePrice price(int id, String type, String currency, BigDecimal value) {
		MoneyAmount amount = new MoneyAmount();
		amount.setCurrency(currency);
		amount.setValue(value);
		DevicePrice p = new DevicePrice();
		p.setId(id);
		p.setType(type);
		p.setAmount(amount);
		return p;
	}

}
