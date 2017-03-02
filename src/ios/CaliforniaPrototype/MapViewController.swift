//
//  MapViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation

class MapViewController: UIViewController {

    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var addLocationOverlayView: UIView!
    
    fileprivate var locationManager = CLLocationManager()
    fileprivate var currentLocation : CLLocation?
    fileprivate var postponedLocationAcceptance = false
    fileprivate var user: User?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if UserDefaultManager.getAddLocationPopupViewed() {
            self.addLocationOverlayView.isHidden = true
        }
        
        if !UserDefaultManager.getLoggedInStatus() {
            DispatchQueue.main.async {
                let onboardingNavVC = UINavigationController(rootViewController: LandingViewController())
                self.tabBarController?.present(onboardingNavVC, animated: true, completion: nil)
            }
        } else {
            //self.getUser()
            self.setupLocationManager()
            if let userLocation = self.mapView.userLocation.location {
                self.centerMapOnLocation(userLocation)
            }
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.setupView()
        if UserDefaultManager.getLoggedInStatus() {
            self.getUser()
        }
    }
    
    func getUser() {
        APIManager.sharedInstance.getUserWithId(id: Keychain.get(key: "id") as! String, success: { (response) in
            self.user = response
            self.populateLocations()
        }) { (error) in
        }
    }
    
    func populateLocations(){
        guard let locations = self.user?.locations else { return }
        for location in locations {
            mapView.addAnnotation(location)
        }
        
        guard let alerts = self.user?.alerts else { return }
        for alert in alerts {
            mapView.addAnnotation(alert)
        }
        
    }
    
    func setupView() {
        let addLocationBtn = UIButton(type: .custom)
        addLocationBtn.setImage(UIImage.init(named: "addLocationIcon"), for: .normal)
        addLocationBtn.frame = CGRect(x: 0, y: 0, width: 30, height: 30)
        addLocationBtn.addTarget(self, action: #selector(pushToMapView), for: .touchUpInside)
        let btnItem = UIBarButtonItem(customView: addLocationBtn)
        self.navigationItem.setRightBarButton(btnItem, animated: true)
    }
    
    func setupLocationManager() {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters
        self.locationManager.delegate = self
        self.locationManager.requestLocation()
    }
    
    func pushToMapView() {
        self.navigationController?.pushViewController(AddLocationViewController(), animated: true)
    }
    
    @IBAction func addLocationButtonTapped(_ sender: Any) {
        UserDefaultManager.setAddLocationPopupViewed(true)
        pushToMapView()
    }

    @IBAction func skipButtonTapped(_ sender: Any) {
        UserDefaultManager.setAddLocationPopupViewed(true)
        UIView.animate(withDuration: 0.3, animations: { 
            self.addLocationOverlayView.alpha = 0.0
        }) { (completed) in
            if completed {
                self.addLocationOverlayView.isHidden = true
            }
        }
    }
    
    @IBAction func getLocationButtonTapped(_ sender: Any) {
        self.locationManager.requestLocation()
    }
    
    
}


extension MapViewController: CLLocationManagerDelegate {
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .authorizedWhenInUse:
            self.locationManager.requestLocation()
        case .authorizedAlways:
            self.locationManager.requestLocation()
        case .notDetermined:
            let alertController = CustomAlertControllers.controllerWith(title: "Error", message: "It seems you haven't accepted California Prototype to access your location. Would you like to do that now?")
            let retry = UIAlertAction(title: "Allow Access", style: .default) { (action:UIAlertAction) in
                self.locationManager.requestLocation()
            }
            alertController.addAction(retry)
            let cancel = UIAlertAction(title: "Not now", style: .cancel) { (action) in
                self.postponedLocationAcceptance = true
            }
            alertController.addAction(cancel)
            self.present(alertController, animated: true, completion: nil)
        case .denied:
            let deniedLocationString = String.localizedStringWithFormat(NSLocalizedString("You have previously denied location access. Please enable it for app to locate you.", comment: "denied location service"), [:])
            let alertController = CustomAlertControllers.controllerWith(message: deniedLocationString)
            self.present(alertController, animated: true, completion: nil)
        case .restricted:
            let restrictedLocationString = String.localizedStringWithFormat(NSLocalizedString("Your phone seems to be restricted and you cannot use location features. Please ensure that you enable Location Services in your Restriction settings.", comment: "restricted location service"), [:])
            let alertController = CustomAlertControllers.controllerWith(message: restrictedLocationString)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        if !self.postponedLocationAcceptance {
            let alertController = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error obtaining your location. \(error.localizedDescription)")
            let retry = UIAlertAction(title: "Try again", style: .default) { (action:UIAlertAction) in
                self.locationManager.requestLocation()
            }
            alertController.addAction(retry)
            let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
            alertController.addAction(cancel)
            self.present(alertController, animated: true, completion: nil)
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if self.currentLocation == nil {
            self.currentLocation = locations.first
            if let location = locations.first {
                self.centerMapOnLocation(location)
            }
        } else {
            if let current = self.currentLocation {
                self.centerMapOnLocation(current)
            }
            
        }
    }
    
    func centerMapOnLocation(_ location: CLLocation) {
        let radius = CLLocationDistance.init(16093)
        let coordinateRegion = MKCoordinateRegionMakeWithDistance(location.coordinate, radius, radius)
        mapView.setRegion(coordinateRegion, animated: true)
    }
    
    func showAlertWith(message messageString: String) {
        let alertController = CustomAlertControllers.controllerWith(message: messageString)
        self.present(alertController, animated: true, completion: nil)
    }
}

extension MapViewController: MKMapViewDelegate {
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if (annotation is MKUserLocation) {
            return nil
        }
        if let annotation = annotation as? UserLocation {
            let identifier = "userLoctionPin"
            var view: MKPinAnnotationView
            if let dequeuedView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier) as? MKPinAnnotationView {
                dequeuedView.annotation = annotation
                view = dequeuedView
            } else {
                view = MKPinAnnotationView(annotation: annotation, reuseIdentifier: identifier)
                view.pinTintColor = UIColor.blue
                view.canShowCallout = true
                view.rightCalloutAccessoryView = UIButton(type: .detailDisclosure) as UIView
            }
            return view
        }
        
        if let annotation = annotation as? Alert {
            let identifier = "alertLoctionPin"
            var view: MKPinAnnotationView
            if let dequeuedView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier) as? MKPinAnnotationView {
                dequeuedView.annotation = annotation
                view = dequeuedView
            } else {
                view = MKPinAnnotationView(annotation: annotation, reuseIdentifier: identifier)
                view.pinTintColor = UIColor.red
                view.canShowCallout = true
                view.rightCalloutAccessoryView = UIButton(type: .detailDisclosure) as UIView
            }
            return view
        }
        
        
        return nil
        
    }
    
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        let asdf = view.annotation as! UserLocation
        print(asdf)
    }
    
    
}






