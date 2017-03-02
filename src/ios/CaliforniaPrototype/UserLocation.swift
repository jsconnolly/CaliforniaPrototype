//
//  UserLocation.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/1/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation
import MapKit

class UserLocation : NSObject {
    var enableSMS: Bool?
    var enablePushNotifications: Bool?
    var enableEmail: Bool?
    var displayName: String?
    var alertRadius: String?
    var id: String?
    var coordinates: [String: Double]?
    
    init(enableSMS: Bool?, enablePushNotifications: Bool?, enableEmail: Bool?, displayName: String?, alertRadius: String?, id: String?, coordinates: [String: Double]?) {
        self.enableSMS = enableSMS
        self.enablePushNotifications = enablePushNotifications
        self.enableEmail = enableEmail
        self.displayName = displayName
        self.alertRadius = alertRadius
        self.id = id
        self.coordinates = coordinates
    }
    
    convenience init(enableSMS: Bool) {
        self.init(enableSMS: false, enablePushNotifications:false, enableEmail: false, displayName: "", alertRadius: "", id: "", coordinates: [:])
    }
    
    public func locationFromDictionaryArray(_ dictionaryArray: [[String: Any]]) -> [UserLocation] {
        var userLocationArray = [UserLocation]()
        for dictionary in dictionaryArray {
            let location = UserLocation(enableSMS: dictionary["enableSMS"] as? Bool, enablePushNotifications: dictionary["enablePushNotifications"] as? Bool, enableEmail: dictionary["enableEmail"] as? Bool, displayName: dictionary["displayName"] as? String, alertRadius: dictionary["alertRadius"] as? String, id: dictionary["id"] as? String, coordinates: dictionary["coordinates"] as? [String: Double])
            userLocationArray.append(location)
        }
        return userLocationArray
    }
}

extension UserLocation: MKAnnotation {
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
            return displayName
        }
    }
    public var subtitle: String? {
        get {
            return displayName
        }
    }
    
}
