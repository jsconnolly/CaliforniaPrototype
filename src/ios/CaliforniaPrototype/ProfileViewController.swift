//
//  ProfileViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/23/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var nameTextField: OutlinedTextField!
    @IBOutlet weak var numberTextField: OutlinedTextField!
    @IBOutlet weak var emailTextField: OutlinedTextField!
    @IBOutlet weak var emailValidationLabel: UILabel!
    @IBOutlet weak var numberValidationLabel: UILabel!
    @IBOutlet weak var emailSwitch: UISwitch!
    @IBOutlet weak var smsSwitch: UISwitch!
    
    private var user : User?
    private var spinner = UIActivityIndicatorView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if UserDefaultManager.getLoggedInStatus() {
            self.getUser()
        }
    }
    //MARK: - Obtain user and set text fields
    func getUser() {
        self.showAndStartSpinner()
        APIManager.sharedInstance.getUserWithId(id: UserManager.retrieveUserId(), success: { (response) in
            self.user = response
            DispatchQueue.main.async {
                self.stopAndRemoveSpinner()
                self.setTextFields()
            }
        }) { (error) in
            DispatchQueue.main.async {
                self.stopAndRemoveSpinner()
            }
        }
    }
    
    func setTextFields() {
        self.nameTextField.text = self.user?.name
        self.emailTextField.text = self.user?.email
        if let phoneNumber = self.user?.phone {
            var countryCodelessString = phoneNumber
            self.numberTextField.text = String(countryCodelessString.characters.dropFirst())
        } else {
            self.numberTextField.text?.removeAll()
        }
        
        if let hasLocations = self.user?.locations {
            if hasLocations.count != 0 {
                if let emailOn = hasLocations[0].enableEmail {
                    self.emailSwitch.setOn(emailOn, animated: true)
                }
                if let smsOn = hasLocations[0].enableSMS {
                    self.smsSwitch.setOn(smsOn, animated: true)
                }
            }
        }
        
        
    }
    
    //MARK: - Set and change notification switches
    @IBAction func emailSwitchChanged(_ sender: Any) {
        if self.emailSwitch.isOn == false {
            let alert = CustomAlertControllers.controllerWith(title: "Turn off Email Notifications", message: "If you turn this off, you will not be alerted of potential hazards. Are you sure you would like to turn off all email notifications?")
            let cancelAction = UIAlertAction(title: "Cancel", style: .default) { (action) in
                self.emailSwitch.setOn(true, animated: true)
            }
            let okAction = UIAlertAction(title: "Turn off", style: .default) { (action) in
                self.updateNotificationSwitchSettings()
            }
            alert.addAction(cancelAction)
            alert.addAction(okAction)
            self.present(alert, animated: true, completion: nil)
        } else {
            self.updateNotificationSwitchSettings()
        }
    }
    
    @IBAction func smsSwitchChanged(_ sender: Any) {
        if self.smsSwitch.isOn == false {
            let alert = CustomAlertControllers.controllerWith(title: "Turn off SMS Notifications", message: "If you turn this off, you will not be alerted of potential hazards on your phone. Are you sure you would like to turn off all SMS notifications?")
            let cancelAction = UIAlertAction(title: "Cancel", style: .default) { (action) in
                self.smsSwitch.setOn(true, animated: true)
            }
            let okAction = UIAlertAction(title: "Turn off", style: .default) { (action) in
                self.updateNotificationSwitchSettings()
            }
            alert.addAction(cancelAction)
            alert.addAction(okAction)
            self.present(alert, animated: true, completion: nil)
        } else {
            self.updateNotificationSwitchSettings()
        }
    }
    
    func updateNotificationSwitchSettings() {
        guard let locations = self.user?.locations else { return }
        let smsNotifications = self.smsSwitch.isOn
        let emailNotifications = self.emailSwitch.isOn
        
        for location in locations {
            self.showAndStartSpinner()
            APIManager.sharedInstance.updateLocation(displayName: location.displayName, coordinates: location.coordinates!, alertRadius: location.alertRadius, enablePushNotifications: false, enableSMS: smsNotifications, enableEmail: emailNotifications, locationId: location.id, success: { (response) in
                self.stopAndRemoveSpinner()
                print("update location response is \(response)")
            }, failure: { (error) in
                self.stopAndRemoveSpinner()
                print("update location error response is \(error)")
            })
        }
    }

    //MARK: - Save Profile
    @IBAction func saveButtonTapped(_ sender: Any) {
        let emailText = self.emailTextField.text ?? ""
        if !emailText.isEmpty || emailText != "" {
            if !ValidationMethods().isValidEmail(emailText) {
                self.emailValidationLabel.isHidden = false
                return
            } else {
                if self.emailValidationLabel.isHidden == false {
                    self.emailValidationLabel.isHidden = true
                }
            }
        }
        
        let phoneText = self.numberTextField.text ?? ""
        var phoneResult = false
        if !phoneText.isEmpty || phoneText != "" {
            let containsNumbers = "^[0-9]+$"
            let codeTest = NSPredicate(format: "SELF MATCHES %@", containsNumbers)
            phoneResult = codeTest.evaluate(with: phoneText)
            if !phoneResult {
                self.numberValidationLabel.isHidden = false
                return
            } else {
                if self.numberValidationLabel.isHidden == false {
                    self.numberValidationLabel.isHidden = true
                }
            }
        }
        
        if ValidationMethods().isValidEmail(emailText) || phoneResult {
            self.showAndStartSpinner()
            APIManager.sharedInstance.updateUser(name: self.nameTextField.text ?? nil, email: emailText, phone: phoneText, success: { (response) in
                print(response)
                DispatchQueue.main.async {
                    self.stopAndRemoveSpinner()
                    self.notifyUser(title: "Success", message: "Profile updated successfully.", timeToDissapear: 2)
                }
            }, failure: { (error) in
                print(error)
                DispatchQueue.main.async {
                    self.stopAndRemoveSpinner()
                    let alert = CustomAlertControllers.controllerWith(title: "Error", message: "There was an error saving your profile, please try saving again.")
                    let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alert.addAction(okAction)
                    self.present(alert, animated: true, completion: nil)
                }
            })
        } else {
            let alert = CustomAlertControllers.controllerWith(title: "Error", message: "Please make sure you have either an email address or phone number.")
            let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okAction)
            self.present(alert, animated: true, completion: nil)
        }
        
    }
    
    //MARK: - UITextFieldDelegate methods
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == self.numberTextField {
            guard let count = textField.text?.characters.count else { return false }
            let newLength = count + string.characters.count - range.length
            
            if newLength <= 10 {
                return true
            } else {
                return false
            }
        } else {
            return true
        }
    }
    
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == self.emailTextField {
            let emailText = textField.text ?? ""
            if !emailText.isEmpty || emailText != "" {
                if !ValidationMethods().isValidEmail(emailText) {
                    self.emailValidationLabel.isHidden = false
                    return
                } else {
                    if self.emailValidationLabel.isHidden == false {
                        self.emailValidationLabel.isHidden = true
                    }
                }
            }
        }
        
        if textField == self.numberTextField {
            let phoneText = textField.text ?? ""
            var phoneResult = false
            if !phoneText.isEmpty || phoneText != "" {
                let containsNumbers = "^[0-9]+$"
                let codeTest = NSPredicate(format: "SELF MATCHES %@", containsNumbers)
                phoneResult = codeTest.evaluate(with: phoneText)
                if !phoneResult {
                    self.numberValidationLabel.isHidden = false
                } else {
                    if self.numberValidationLabel.isHidden == false {
                        self.numberValidationLabel.isHidden = true
                    }
                }
            }
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
    }
    
    //MARK: - Logout

    @IBAction func logoutButtonTapped(_ sender: Any) {
        UserManager.logoutAndCleanUserInfo()
        let mapNavVC = self.tabBarController?.viewControllers?[0] as! UINavigationController
        //let mapVC = mapNavVC.topViewController as! MapViewController()
        self.tabBarController?.selectedIndex = 0
        
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
    
    //MARK: - Alert method
    func notifyUser(title: String, message: String, timeToDissapear: Int) -> Void
    {
        let alert = UIAlertController(title: title,
                                  message: message,
                                  preferredStyle: .alert)
        
        let cancelAction = UIAlertAction(title: "OK",
                                         style: .cancel, handler: nil)
        
        alert.addAction(cancelAction)
        self.present(alert, animated: true, completion: nil)
        
        // Delay the dismissal by timeToDissapear seconds
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            alert.dismiss(animated: true, completion: nil)
        }
    }
    
}
