//
//  MapViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
import MapKit

class MapViewController: UIViewController {

    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var addLocationOverlayView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if UserDefaultManager.getAddLocationPopupViewed() {
            self.addLocationOverlayView.isHidden = true
        }
        
        self.setupView()
        
    }
    
        
    func setupView() {
        let addLocationNavButton = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.add, target: self, action: #selector(pushToMapView))
        navigationItem.rightBarButtonItem = addLocationNavButton
    }
    
    func pushToMapView() {
        navigationController?.pushViewController(AddLocationViewController(), animated: true)
    }
    
    @IBAction func addLocationButtonTapped(_ sender: Any) {
        UserDefaultManager.setAddLocationPopupViewed(true)
        pushToMapView()
    }

    @IBAction func skipButtonTapped(_ sender: Any) {
        UIView.animate(withDuration: 0.3, animations: { 
            self.addLocationOverlayView.alpha = 0.0
        }) { (completed) in
            if completed {
                self.addLocationOverlayView.isHidden = true
            }
        }
    }
    
    
}
