//
//  UserLocationDetailViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 3/2/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class UserLocationDetailViewController: UIViewController, UITextFieldDelegate, UIPickerViewDelegate, UIPickerViewDataSource {

    @IBOutlet weak var locationNameTextField: OutlinedTextField!
    @IBOutlet weak var radiusTextField: OutlinedTextField!
    
    @IBOutlet weak var smsSwitch: UISwitch!
    @IBOutlet weak var emailSwitch: UISwitch!
    
    var location: UserLocation?
    private var radiusArray = [Int]()
    private var radiousPickerView = UIPickerView()
    private var spinner = UIActivityIndicatorView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.locationNameTextField.text = location?.displayName
        self.radiusTextField.text = location?.alertRadius
        if let emailOn = self.location?.enableEmail {
            self.emailSwitch.setOn(emailOn, animated: true)
        }
        if let smsOn = self.location?.enableSMS {
            self.smsSwitch.setOn(smsOn, animated: true)
        }
        
        self.radiusArray += 1...50
        self.radiusTextField.inputView = radiousPickerView
        self.radiusTextField.delegate = self
        self.radiousPickerView.delegate = self
        self.radiousPickerView.dataSource = self
    }
    
    @IBAction func smsSwitchChanged(_ sender: Any) {
        
    }
    
    @IBAction func emailSwitchChanged(_ sender: Any) {
        
    }
    
    @IBAction func updateButtonTapped(_ sender: Any) {
        
        guard let displayName = locationNameTextField.text, let radiusText = radiusTextField.text else { return }
        if displayName.isEmpty || displayName == " " || radiusText.isEmpty || radiusText == " " {
            let alert = CustomAlertControllers.controllerWith(title: "Error", message: "You must enter a location name and radius for this location if you wish to update it.")
            let okAlert = UIAlertAction(title: "Ok", style: .default, handler: nil)
            alert.addAction(okAlert)
            self.present(alert, animated: true, completion: nil)
        } else {
            let smsOn = self.smsSwitch.isOn
            let emailOn = self.emailSwitch.isOn
            guard let coordinates = location?.coordinates else { return }
            self.showAndStartSpinner()
            APIManager.sharedInstance.updateLocation(displayName: displayName, coordinates: coordinates, alertRadius: radiusText, enablePushNotifications: false, enableSMS: smsOn, enableEmail: emailOn, locationId: self.location?.id, success: { (response) in
                self.stopAndRemoveSpinner()
                
            }, failure: { (error) in
                self.stopAndRemoveSpinner()
                
            })
        }
        
        
    }
    
    //MARK: - UIPickerView Data soure and delegate methods
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return self.radiusArray.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return "\(self.radiusArray[row])" + ".0"
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        self.radiusTextField.text = "\(self.radiusArray[row])" + ".0"
    }
    
    //MARK: - Activity Indicator Methods
    func showAndStartSpinner() {
        self.spinner = UIActivityIndicatorView(activityIndicatorStyle: .gray)
        self.spinner.center = self.view.center
        self.spinner.hidesWhenStopped = true
        self.spinner.startAnimating()
        self.view.addSubview(self.spinner)
    }
    
    func stopAndRemoveSpinner() {
        self.spinner.stopAnimating()
        self.spinner.removeFromSuperview()
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
    }
}
