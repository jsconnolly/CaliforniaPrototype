//
//  AddLocationViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit
import MapKit
import CoreLocation

class AddLocationViewController: UIViewController {

    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var tableViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet var locationDetailView: UIView!
    @IBOutlet weak var locationDetailsLabel: UILabel!
    @IBOutlet var successfulLocationAddView: UIView!
    
    @IBOutlet weak var addLocationButton: RoundedRectButton!
    @IBOutlet weak var cancelAddLocationButton: RoundedRectButton!
//    @IBOutlet weak var addPersonToLocationButton: RoundedRectButton!
    @IBOutlet weak var addAnotherLocationButton: RoundedRectButton!
    
    
    fileprivate var searchResultsArray = [MKMapItem]()
    fileprivate var searchTimer: Timer?
    
    fileprivate var locationManager = CLLocationManager()
    fileprivate var currentLocation : CLLocation?
    fileprivate var postponedLocationAcceptance = false
 
    //MARK: - UIViewController delegate methods
    override func viewDidLoad() {
        super.viewDidLoad()

        let tableViewSearchCell = UINib(nibName: "SearchResultsTableViewCell", bundle: nil)
        self.tableView.register(tableViewSearchCell, forCellReuseIdentifier: "searchResultCell")
        
        self.setupLocationManager()
        if let userLocation = self.mapView.userLocation.location {
            self.centerMapOnLocation(userLocation)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(_:)), name: Notification.Name("UIKeyboardWillShowNotification"), object: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        NotificationCenter.default.removeObserver(self)
    }
    
    //MARK: - Keyboard notification methods
    func keyboardWillShow(_ notification: NSNotification) {
        let userInfo = notification.userInfo!
        var keyboardFrame : CGRect = (userInfo[UIKeyboardFrameEndUserInfoKey] as! NSValue).cgRectValue
        keyboardFrame = self.view.convert(keyboardFrame, from: nil)
        let duration = userInfo[UIKeyboardAnimationDurationUserInfoKey] as! TimeInterval
        
        self.tableViewBottomConstraint.constant = keyboardFrame.height - (self.tabBarController?.tabBar.frame.size.height)!
        
        UIView.animate(withDuration: duration, animations: { 
            self.view.layoutIfNeeded()
        }) { (completed) in
            
        }
    }
    
    //MARK: - View setup methods
    func setupLocationManager() {
        self.locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters
        self.locationManager.delegate = self
        self.locationManager.requestLocation()
    }
    
    func centerMapOnLocation(_ location: CLLocation) {
        let radius = CLLocationDistance.init(5000)
        let coordinateRegion = MKCoordinateRegionMakeWithDistance(location.coordinate, radius, radius)
        mapView.setRegion(coordinateRegion, animated: false)
    }
    
    //MARK: - Add Location Functions
    @IBAction func addLocationButtonTapped(_ sender: Any) {
        self.addLocationButton.alpha = 0.0
        self.cancelAddLocationButton.alpha = 0.0
        
//        self.addPersonToLocationButton.alpha = 1.0
        self.addAnotherLocationButton.alpha = 1.0
//        self.addPersonToLocationButton.isHidden = false
        self.addAnotherLocationButton.isHidden = false
        
        self.successfulLocationAddView.frame.origin = CGPoint(x: 0, y: 0)
        self.successfulLocationAddView.frame.size.width = self.view.frame.size.width
        self.locationDetailView.removeFromSuperview()
        self.view.addSubview(self.successfulLocationAddView)
        self.successfulLocationAddView.alpha = 1.0
        
        UIView.animate(withDuration: 0.35, animations: { 
            self.view.layoutIfNeeded()
        }) { (completed) in
                self.addLocationButton.isHidden = true
                self.cancelAddLocationButton.isHidden = true
            
        }
        
    }
    
    @IBAction func cancelAddLocationButtonTapped(_ sender: Any) {
        self.addLocationButton.alpha = 0.0
        self.cancelAddLocationButton.alpha = 0.0
        self.locationDetailView.alpha = 0.0
        self.tableViewBottomConstraint.constant = 0
        UIView.animate(withDuration: 0.3, animations: { 
            self.view.layoutIfNeeded()
        }) { (completed) in
            if completed {
                self.locationDetailView.removeFromSuperview()
                self.addLocationButton.isHidden = true
                self.cancelAddLocationButton.isHidden = true
            }
        }
    }
    
    @IBAction func addAnotherLocationButtonTapped(_ sender: Any) {
        self.addAnotherLocationButton.alpha = 0.0
//        self.addPersonToLocationButton.alpha = 0.0
        self.successfulLocationAddView.alpha = 0.0
        self.tableViewBottomConstraint.constant = 0
        UIView.animate(withDuration: 0.35, animations: { 
            self.view.layoutIfNeeded()
        }) { (completed) in
            if completed {
                self.successfulLocationAddView.removeFromSuperview()
//                self.addPersonToLocationButton.isHidden = true
                self.addAnotherLocationButton.isHidden = true
            }
        }
    }
    
    @IBAction func addPersonToLocationButtonTapped(_ sender: Any) {
    
    }
    
}


extension AddLocationViewController: UISearchBarDelegate {
    //MARK: - UISearchBarDelegate
    func searchBarShouldBeginEditing(_ searchBar: UISearchBar) -> Bool {
        self.searchBar.setShowsCancelButton(true, animated: true)
        return true
    }
    
    func searchBarShouldEndEditing(_ searchBar: UISearchBar) -> Bool {
        self.searchBar.setShowsCancelButton(false, animated: true)
        return true
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        if (searchBar.text?.characters.count)! > 1 {
            if let searchTimer = searchTimer {
                searchTimer.invalidate()
            }
            searchTimer = Timer.scheduledTimer(timeInterval: 0.6,
                                               target: self,
                                               selector: #selector(sendSearchString),
                                               userInfo: nil,
                                               repeats: false)
        } else {
            self.searchResultsArray.removeAll()
            self.tableView.reloadData()
        }
    }
    
    func sendSearchString() {
        self.localSearch(searchString: self.searchBar.text!)
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.searchResultsArray.removeAll()
        self.tableView.reloadData()
        self.searchBar.text?.removeAll()
        self.searchBar.resignFirstResponder()
        
        self.tableViewBottomConstraint.constant = 0
        UIView.animate(withDuration: 0.3, animations: { 
            self.view.layoutIfNeeded()
        }) { (completed) in
            
        }
    }
    
    //MARK: - MKLocalSearchRequest methods
    func localSearch(searchString: String) {
        self.searchResultsArray.removeAll()
        self.tableView.reloadData()
        let request = MKLocalSearchRequest()
        request.naturalLanguageQuery = searchString
        if let coordinates = self.currentLocation?.coordinate {
            request.region = MKCoordinateRegion(center: coordinates,
                                                span: MKCoordinateSpan.init(latitudeDelta: 0.1, longitudeDelta: 0.1))
        } else {
            request.region = MKCoordinateRegion(center: CLLocationCoordinate2D.init(latitude: 33.632440, longitude: -117.733962),
                                                span: MKCoordinateSpan.init(latitudeDelta: 0.1, longitudeDelta: 0.1))
        }
        
        let localSearcher = MKLocalSearch(request: request)
        localSearcher.start { (response: MKLocalSearchResponse?, error: Error?) in
            if let response = response {
                self.searchResultsArray.append(contentsOf: response.mapItems)
                self.tableView.reloadData()
            } else {

            }
        }
    }
    
}


extension AddLocationViewController: CLLocationManagerDelegate {
    
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
        }
    }
    
    
    func showAlertWith(message messageString: String) {
        let alertController = CustomAlertControllers.controllerWith(message: messageString)
        self.present(alertController, animated: true, completion: nil)
    }
    
    
    
}


extension AddLocationViewController: UITableViewDelegate, UITableViewDataSource {
    
    //MARK: - UITableViewDataSource methods
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.searchResultsArray.count
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "searchResultCell", for: indexPath) as! SearchResultsTableViewCell
        let result = self.searchResultsArray[indexPath.row]
        if let locationName = result.name, let address = result.placemark.title {
            return cell.configureCell(place: locationName, address: address)
        }
        
        return cell
    }
    
    
    //MARK: - UITableViewDelegate methods
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.showDetailsAndOptionsAndCenterMapAt(self.searchResultsArray[indexPath.row])
        self.searchResultsArray.removeAll()
        self.tableView.reloadData()
        
        self.searchBar.resignFirstResponder()
        self.tableViewBottomConstraint.constant = self.view.frame.size.height
        UIView.animate(withDuration: 0.35, animations: {
            self.view.layoutIfNeeded()
        }) { (completed) in
            
        }
    }
    
    func showDetailsAndOptionsAndCenterMapAt(_ place: MKMapItem) {
        let point = MKPointAnnotation()
        point.coordinate = place.placemark.coordinate
        point.title = place.name
        self.mapView.addAnnotation(point)
        self.locationDetailsLabel.text = place.placemark.title
        self.mapView.setCenter(point.coordinate, animated: true)
        
        self.locationDetailView.frame.origin = CGPoint(x: 0, y: 0)
        self.locationDetailView.frame.size.width = self.view.frame.size.width
        self.view.addSubview(self.locationDetailView)
        
        self.addLocationButton.alpha = 1.0
        self.cancelAddLocationButton.alpha = 1.0
        self.addLocationButton.isHidden = false
        self.cancelAddLocationButton.isHidden = false
    }
    
    
}
