package com.example.usasurvivalapp.my_gas_feed.model;

public class Station {
	public String id;
	public String country;
	public String price;
	public String address;
	public String diesel;
	public String lat;
	public String lng;
	public String station;
	public String region;
	public String city;
	public String date;
	public String distance;

	public Station(String id, String country, String price, String address,
			String diesel, String lat, String lng, String station,
			String region, String city, String date, String distance) {
		super();
		this.id = id;
		this.country = country;
		this.price = price;
		this.address = address;
		this.diesel = diesel;
		this.lat = lat;
		this.lng = lng;
		this.station = station;
		this.region = region;
		this.city = city;
		this.date = date;
		this.distance = distance;
	}

	public String getId() {
		return id;
	}

	public String getCountry() {
		return country;
	}

	public String getPrice() {
		return price;
	}

	public String getAddress() {
		return address;
	}

	public String getDiesel() {
		return diesel;
	}

	public String getLat() {
		return lat;
	}

	public String getLng() {
		return lng;
	}

	public String getStation() {
		return station;
	}

	public String getRegion() {
		return region;
	}

	public String getCity() {
		return city;
	}

	public String getDate() {
		return date;
	}

	public String getDistance() {
		return distance;
	}

	public static class Detail {
		public String address;
		public long id;
		public String city;
		public String region;
		public String country;
		public double lat;
		public double lng;
		public String station_name;
		public String reg_price;
		public String reg_date;
		public String mid_price;
		public String mid_date;
		public String pre_price;
		public String pre_date;
		public int diesel;
	}
}
