//
//  LocationPermissionsViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
import CoreLocation

class LocationPermissionsViewController: UIViewController, CLLocationManagerDelegate {

    let locationManager = CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.isNavigationBarHidden = true
        self.locationManager.delegate = self
    }

    @IBAction func denyButtonTapped(_ sender: Any) {
        
    }
    
    @IBAction func allowButtonTapped(_ sender: Any) {
        locationManager.requestAlwaysAuthorization()
    }
    
    //MARK: - CLLocationManagerDelegate methods
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedAlways {
            //self.navigationController?.pushViewController(NotificationsPermissionViewController(), animated: true)
        } else {
            
        }
    }

}


