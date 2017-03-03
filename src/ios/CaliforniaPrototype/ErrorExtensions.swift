//
//  ErrorExtensions.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/28/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation

extension Error {
    var code: Int { return (self as NSError).code }
    var domain: String { return (self as NSError).domain }
}
