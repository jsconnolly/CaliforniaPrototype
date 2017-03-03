//
//  UserDefaultManager.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation

class UserDefaultManager {
    
    class func setAddLocationPopupViewed(_ status: Bool) {
        set(status, "addLocationPopupViewed")
    }
    
    class func getAddLocationPopupViewed () -> Bool {
        let defaults = UserDefaults.standard
        return defaults.bool(forKey: "addLocationPopupViewed")
    }
    
    class func setLoggedInStatus(_ status: Bool) {
        set(status, "addLocationPopupViewed")
    }
    
    class func getLoggedInStatus() -> Bool {
        let defaults = UserDefaults.standard
        return defaults.bool(forKey: "addLocationPopupViewed")
    }
    
    private class func set(_ boolValue: Bool,_ key: String) {
        let defaults = UserDefaults.standard
        defaults.set(boolValue, forKey: key)
        defaults.synchronize()
    }
}
