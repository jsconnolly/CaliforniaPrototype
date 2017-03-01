//
//  NotificationsPermissionViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
import UserNotifications

class NotificationsPermissionViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
    }

    @IBAction func allowButtonTapped(_ sender: Any) {
        self.allowNotifications()
        self.navigationController?.setViewControllers([TabBarViewController()], animated: true)
    }
    
    @IBAction func denyButtonTapped(_ sender: Any) {
        self.navigationController?.setViewControllers([TabBarViewController()], animated: true)
    }
    
    func allowNotifications() {
        if #available(iOS 10.0, *) {
            let center = UNUserNotificationCenter.current()
            center.requestAuthorization(options: [.alert, .badge, .sound]) { (granted, error) in
                if error == nil {
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
        } else {
            let settings: UIUserNotificationSettings = UIUserNotificationSettings.init(types: [.alert, .badge], categories: nil)
            UIApplication.shared.registerUserNotificationSettings(settings)
            UIApplication.shared.registerForRemoteNotifications()
        }
    }
    
}
