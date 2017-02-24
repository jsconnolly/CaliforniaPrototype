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
    @IBOutlet weak var addLocationButton: UIButton!
    @IBOutlet weak var cancelAddLocationButton: UIButton!
    
    fileprivate var searchResultsArray = [MKMapItem]()
    fileprivate var searchTimer: Timer?
    
    fileprivate var locationManager = CLLocationManager()
    fileprivate var currentLocation : CLLocation?
 
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
    
    @IBAction func addLocationButtonTapped(_ sender: Any) {
        
    }
    
    @IBAction func cancelAddLocationButtonTapped(_ sender: Any) {
        self.addLocationButton.isHidden = true
        self.cancelAddLocationButton.isHidden = true
        self.locationDetailView.removeFromSuperview()
        self.tableViewBottomConstraint.constant = 0
        UIView.animate(withDuration: 0.3, animations: { 
            self.view.layoutIfNeeded()
        }) { (completed) in
            if completed {
                
            }
        }
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
//        if let coordinates = self.currentLocation?.coordinate {
//            request.region = MKCoordinateRegion(center: coordinates,
//                                                span: MKCoordinateSpan.init(latitudeDelta: 0.1, longitudeDelta: 0.1))
//        } else {
//            request.region = MKCoordinateRegion(center: CLLocationCoordinate2D.init(latitude: 0.0, longitude: 0.0),
//                                                span: MKCoordinateSpan.init(latitudeDelta: 0.1, longitudeDelta: 0.1))
//        }
        //33.632440, -117.733962
        request.region = MKCoordinateRegion(center: CLLocationCoordinate2D.init(latitude: 33.632440, longitude: -117.733962),
                                            span: MKCoordinateSpan.init(latitudeDelta: 0.1, longitudeDelta: 0.1))
        
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
            self.locationManager.requestWhenInUseAuthorization()
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
        //        let restrictedLocationString = String.localizedStringWithFormat(NSLocalizedString("We could not obtain your location. Error is \(error.localizedDescription)", comment: "can't obtain location"), [:])
        let alertController = UIAlertController(title: "Error", message: "There was an error obtaining your location. \(error.localizedDescription)", preferredStyle: .alert)
        let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(cancel)
        self.present(alertController, animated: true, completion: nil)
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
        
        self.addLocationButton.isHidden = false
        self.cancelAddLocationButton.isHidden = false
    }
    
    
}
