//
//  Keychain.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/28/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation
import Security

public class Keychain {
    
    public class func set(key: String, value: String) -> Bool {
        if let data: NSData = value.data(using: String.Encoding.utf8) as NSData? {
            return set(key: key, value: data)
        }
        
        return false
    }
    
    public class func set(key: String, value: NSData) -> Bool {
        let query = [
            (kSecClass as String)       : kSecClassGenericPassword,
            (kSecAttrAccount as String) : key,
            (kSecValueData as String)   : value
            ] as [String : Any]
        
        SecItemDelete(query as CFDictionary)
        
        return SecItemAdd(query as CFDictionary, nil) == noErr
    }
    
    public class func get(key: String) -> NSString? {
        if let data = getData(key: key) {
            return NSString(data: data as Data, encoding: String.Encoding.utf8.rawValue)
        }
        
        return nil
    }
    
    public class func getData(key: String) -> NSData? {
        let query = [
            (kSecClass as String)       : kSecClassGenericPassword,
            (kSecAttrAccount as String) : key,
            (kSecReturnData as String)  : kCFBooleanTrue,
            (kSecMatchLimit as String)  : kSecMatchLimitOne
            ] as [String : Any]
        
        var retrievedData: NSData?
        var extractedData: AnyObject?
        let status = SecItemCopyMatching(query as CFDictionary, &extractedData)
        
        if (status == errSecSuccess) {
            retrievedData = extractedData as? NSData
        }
        
        if status == noErr && retrievedData != nil {
            return retrievedData
        }
        
        return nil
    }
    
    public class func delete(key: String) -> Bool {
        let query = [
            (kSecClass as String)       : kSecClassGenericPassword,
            (kSecAttrAccount as String) : key
            ] as [String : Any]
        
        return SecItemDelete(query as CFDictionary) == noErr
    }
    
    public class func clear() -> Bool {
        let query = [
            (kSecClass as String): kSecClassGenericPassword
        ]
        
        return SecItemDelete(query as CFDictionary) == noErr
    }
}
