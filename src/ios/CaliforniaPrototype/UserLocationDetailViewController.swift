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
    @IBOutlet weak var udpateButton: RoundedRectButton!
    
    var location: UserLocation?
    private var radiusArray = [Int]()
    private var radiousPickerView = UIPickerView()
    private var spinner = UIActivityIndicatorView()
    private var enableButton = false
    
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
        self.enableUpdateButton()
    }
    
    @IBAction func emailSwitchChanged(_ sender: Any) {
        self.enableUpdateButton()
    }
    
    func enableUpdateButton() {
       self.enableButton = true
    }
    
    @IBAction func updateButtonTapped(_ sender: Any) {
        if self.enableButton {
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
                guard let id = location?.id else { return }
                self.showAndStartSpinner()
                APIManager.sharedInstance.updateLocation(displayName: displayName, coordinates: coordinates, alertRadius: radiusText, enablePushNotifications: false, enableSMS: smsOn, enableEmail: emailOn, locationId: id, success: { (response) in
                    DispatchQueue.main.async {
                        self.stopAndRemoveSpinner()
                        self.notifyUser(title: "Success", message: "Profile updated successfully.", timeToDissapear: 1.5)
                    }
                    
                }, failure: { (error) in
                    DispatchQueue.main.async {
                        self.stopAndRemoveSpinner()
                        let alert = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error saving the location, please try saving again.")
                        let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                        alert.addAction(okAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                })
            }
        } else {
            self.notifyUser(title: "No Changes", message: "In order to update, you must change the location title or radius.", timeToDissapear: 3.0)
        }
    }
    
    //MARK: - UITextFieldDelegate methods
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        self.enableUpdateButton()
        return true
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
        self.enableUpdateButton()
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
    
    //MARK: - Alert method
    func notifyUser(title: String, message: String, timeToDissapear: Double) -> Void
    {
        let alert = UIAlertController(title: title,
                                      message: message,
                                      preferredStyle: .alert)
        
        let cancelAction = UIAlertAction(title: "OK",
                                         style: .cancel, handler: nil)
        
        alert.addAction(cancelAction)
        self.present(alert, animated: true, completion: nil)
        
        // Delay the dismissal by timeToDissapear seconds
        DispatchQueue.main.asyncAfter(deadline: .now() + timeToDissapear) {
            alert.dismiss(animated: true, completion: nil)
        }
    }
}
