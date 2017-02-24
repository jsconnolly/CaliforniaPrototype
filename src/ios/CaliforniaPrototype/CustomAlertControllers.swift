//
//  CustomAlertControllers.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/24/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import Foundation
import UIKit

struct CustomAlertControllers {
    
    static func controllerWith(message string: String) -> UIAlertController {
        let titleString = String.localizedStringWithFormat(NSLocalizedString("Action Required", comment: "alert window title"), [:])
        let alertController = UIAlertController(title: titleString, message: string, preferredStyle: .alert)
        
        let settingsButtonTitle = String.localizedStringWithFormat(NSLocalizedString("Settings", comment: "alert window action button"), [:])
        let settingsAction = UIAlertAction(title: settingsButtonTitle, style: .default) { (alertAction) in
            if let appSettings = NSURL(string: UIApplicationOpenSettingsURLString) {
                if #available(iOS 10, *) {
                    UIApplication.shared.open(appSettings as URL, options: [:], completionHandler: { (success) in
                    })
                } else {
                    UIApplication.shared.openURL(appSettings as URL)
                }
            }
        }
        alertController.addAction(settingsAction)
        
        let cancelButtonTitle = String.localizedStringWithFormat(NSLocalizedString("Cancel", comment: "alert window action button"), [:])
        let cancelAction = UIAlertAction(title: cancelButtonTitle, style: .cancel, handler: nil)
        alertController.addAction(cancelAction)
        
        return alertController
    }
}
