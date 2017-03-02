//
//  Alert.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/2/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation
import MapKit

class Alert : NSObject {
    var name: String?
    var type: String?
    var date: String?
    var coordinates: [String: Double]?
    var location: String?
    
    init(name: String?, type: String?, date: String?, coordinates: [String: Double]?, location: String?) {
        self.name = name
        self.type = type
        self.date = date
        self.coordinates = coordinates
        self.location = location
    }
    
    convenience init(name: String) {
        self.init(name: "", type: "", date: "", coordinates: [:], location: "")
    }
    
//    public func alertArrayFromJsonArray(_ jsonAlertArray: [[String: Any]]?) -> [Alert]? {
//        guard let alerts = jsonAlertArray else { return nil }
//        var alertArray = [Alert]()
//        for jsonAlert in alerts {
//            let alert = Alert(name: jsonAlert["name"] as? String, type: jsonAlert["type"] as? String, date: jsonAlert["date"] as? String, coordinates: jsonAlert["loc"] as? [String: Double], location: jsonAlert["location"] as? String)
//            alertArray.append(alert)
//        }
//        return alertArray
//    }
    
    public func alertArrayFromJsonArray(_ jsonAlertArray: [[String: Any]]?) -> [Alert]? {
        guard let alerts = jsonAlertArray else { return nil }
        var alertArray = [Alert]()
        for jsonAlert in alerts {
            let alert = Alert(name: jsonAlert["name"] as? String, type: jsonAlert["type"] as? String, date: jsonAlert["date"] as? String, coordinates: fixCoordinates(jsonAlert["loc"] as! [Double]), location: jsonAlert["location"] as? String)
            alertArray.append(alert)
        }
        return alertArray
    }
    
    func fixCoordinates(_ coordinatesArray: [Double]) -> [String: Double] {
        let lat = coordinatesArray.last
        let lng = coordinatesArray.first
        return ["lat": lat!, "lng": lng!]
    }
    
}

extension Alert: MKAnnotation {
    public var coordinate: CLLocationCoordinate2D {
        get {
            let lat = coordinates?["lat"]
            let lng = coordinates?["lng"]
            if let lat = lat, let lng = lng {
                let location = CLLocationCoordinate2D(latitude: lat, longitude: lng)
                return location
            } else {
                return CLLocationCoordinate2D(latitude: 34.0522, longitude: 118.2437)
            }
        }
    }
    
    public var title: String? {
        get {
            return location
        }
    }
    public var subtitle: String? {
        get {
            return type
        }
    }
}
