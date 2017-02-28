//
//  NetworkOperations.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/28/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation

class NetworkOperations {
    
    // Creates a configuration object with all of the settings set to defaults
    lazy var config: URLSessionConfiguration = URLSessionConfiguration.default
    
    // Creates a session with default configuration
    lazy var session: URLSession = URLSession(configuration: self.config)
    
    // Perform Web Request
    func performWebRequest(url: String, httpMethod: String, httpBody: [String: Any]?, httpHeaders: [String: String]?, completion: @escaping (Any?, Error?) -> Void) {
        
        // Convert String to URL
        guard let URL: NSURL = NSURL(string: url) else {
            completion(nil, NSError(domain: url, code: 1001, userInfo: ["Error": "NETWORK ERROR: Could not convert URLString to NSURL"]))
            return
        }
        
        // Initialize HTTP Request
        let request: NSMutableURLRequest = NSMutableURLRequest(url: URL as URL)
        
        // Set HTTP Method
        request.httpMethod = httpMethod
        
        // Set HTTP Headers
        if let headers = httpHeaders {
            request.allHTTPHeaderFields = headers
        }
        
        // Set HTTP Body
        if let body = httpBody {
            do {
                let jsonData = try JSONSerialization.data(withJSONObject: body, options: .prettyPrinted)
                request.httpBody = jsonData
            } catch {
                completion(nil, NSError(domain: url, code: 1001, userInfo: ["Error": "NETWORK ERROR: Could not serialize httpBody - 1001"]))
                print("Network Error - Code: 0")
            }
        }
        
        // Perform Web Request
        let dataTask = session.dataTask(with: request as URLRequest) {
            (data, response, error) in
            
            if error != nil {
                completion(nil, error) // If error exists return the error to the user
                return
            }
            
            guard let httpResponse = response as? HTTPURLResponse else {
                completion(nil, NSError(domain: url, code: 1001, userInfo: ["Error": "NETWORK ERROR: Failed to set httpResponse from server - 1002"]))
                return
            }
            
            if httpResponse.statusCode == 200 || httpResponse.statusCode == 201 {
                do {
                    if let d = data {
                        let jsonResponse = try JSONSerialization.jsonObject(with: d, options: [])
                        completion(jsonResponse, nil)
                    } else {
                        completion(nil, NSError(domain: url, code: 1001, userInfo: ["Error": "NETWORK ERROR: Failed to serialize httpResponse data - 1003"]))
                    }
                } catch {
                    completion(nil, NSError(domain: url, code: 1001, userInfo: ["Error": "NETWORK ERROR: Failed to serialize httpResponse data - 1004"]))
                }
            } else {
                completion(nil, NSError(domain: url, code: 1001, userInfo: ["Error": "NETWORK ERROR: httpResponse did not return with a 200 or 201 status code - 1005"]))
            }
        }
        
        dataTask.resume()
    }
}

