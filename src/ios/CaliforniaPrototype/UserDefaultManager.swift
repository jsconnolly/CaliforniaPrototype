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
        let defaults = UserDefaults.standard
        defaults.set(status, forKey: "addLocationPopupViewed")
        defaults.synchronize()
    }
    
    class func getAddLocationPopupViewed () -> Bool {
        let defaults = UserDefaults.standard
        return defaults.bool(forKey: "addLocationPopupViewed")
    }
}
