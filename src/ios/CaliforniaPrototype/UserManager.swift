//
//  UserManager.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/2/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation

class UserManager {
    
    class func loginAndSave(userId id: String, token: String) {
        _ = Keychain.set(key: "userId", value: id)
        _ = Keychain.set(key: "token", value: token)
        UserDefaultManager.setLoggedInStatus(true)
    }
    
    class func logoutAndCleanUserInfo() {
        UserDefaultManager.setLoggedInStatus(false)
        _ = Keychain.delete(key: "userId")
        _ = Keychain.delete(key: "token")
    }
    
    class func retrieveUserToken() -> String {
        return Keychain.get(key: "token") as! String
    }
    
    class func retrieveUserId() -> String {
        return Keychain.get(key: "userId") as! String
    }
    
}
