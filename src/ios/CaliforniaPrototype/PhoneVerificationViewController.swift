//
//  PhoneVerificationViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/22/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class PhoneVerificationViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var phoneNumberLabel: UILabel!
    
    @IBOutlet weak var firstCodeTextField: OutlinedTextField!
    @IBOutlet weak var secondCodeTextField: OutlinedTextField!
    @IBOutlet weak var thirdCodeTextField: OutlinedTextField!
    @IBOutlet weak var fourthCodeTextField: OutlinedTextField!
    @IBOutlet weak var fifthCodeTextField: OutlinedTextField!
    @IBOutlet weak var sixthCodeTextField: OutlinedTextField!
    private var textFieldArray = [OutlinedTextField]()
    @IBOutlet weak var invalidPhoneNumberLabel: UILabel!
    
    var phoneNumber = String()
    private var spinner = UIActivityIndicatorView()
    private var validCode = String()
    var fromSignIn = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.navigationController?.isNavigationBarHidden = true
        self.textFieldArray = [firstCodeTextField, secondCodeTextField, thirdCodeTextField, fourthCodeTextField, fifthCodeTextField, sixthCodeTextField]
        
        self.phoneNumberLabel.text = phoneNumber
    }

    
    @IBAction func editNumberButtonTapped(_ sender: Any) {
       _ = self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func verifyButtonTapped(_ sender: Any) {
        let containsNumbers = "^[0-9]+$"
        let codeTest = NSPredicate(format: "SELF MATCHES %@", containsNumbers)
        var valid = false
        for textField in textFieldArray {
            let result = codeTest.evaluate(with: textField.text)
            if result {
                valid = true
                self.validCode.append(textField.text!)
            } else {
                self.validCode.removeAll()
                UIView.animate(withDuration: 0.2, animations: {
                    self.invalidPhoneNumberLabel.text = "Only numbers 0-9 are allowed as input."
                    self.invalidPhoneNumberLabel.alpha = 1.0
                })
            }
        }
        if valid == true {
            var number = String()
            if fromSignIn {
                number = "1" + self.phoneNumber
            } else {
                number = self.phoneNumber
            }
            self.showAndStartSpinner()
            APIManager.sharedInstance.signInWithPhone(number: number, password: self.validCode, success: { (response: [String : Any?]) in
                guard let token = response["token"] else { return }
                guard let id = response["id"] else { return }
                UserManager.loginAndSave(userId: id as! String, token: token as! String)
                DispatchQueue.main.async {
                    self.showAndStartSpinner()
                    self.navigationController?.setViewControllers([TabBarViewController()], animated: true)
                }
            }, failure: { (error) in
                DispatchQueue.main.async {
                    self.stopAndRemoveSpinner()
                    let alert = CustomAlertControllers.controllerWith(title: "Error", message: "An error occurred with your request. Please try again.")
                    let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alert.addAction(okAction)
                    self.present(alert, animated: true, completion: nil)
                }
            })
        }
        
        
    }
    
    @IBAction func resendCodeButtonTapped(_ sender: Any) {
        var number = String()
        if fromSignIn {
            number = "1" + self.phoneNumber
        } else {
            number = self.phoneNumber
        }
        APIManager.sharedInstance.phoneVerification(number, success: { (response: [String : Any?]) in
            self.spinner.stopAnimating()
            DispatchQueue.main.async {
                let alert = CustomAlertControllers.controllerWith(title: "Sent", message: "Your request was sent.")
                let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                alert.addAction(okAction)
                self.present(alert, animated: true, completion: nil)
            }
        }, failure: { (error) in
            self.spinner.stopAnimating()
            if error?.code == 404 {
                DispatchQueue.main.async {
                    
                }
            } else {
                DispatchQueue.main.async {
                    let alert = CustomAlertControllers.controllerWith(title: "Error", message: "An error occurred with your request. Please try again.")
                    let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
                    alert.addAction(okAction)
                    self.present(alert, animated: true, completion: nil)
                }
            }
        })
    }
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if self.invalidPhoneNumberLabel.alpha == 1.0 {
            UIView.animate(withDuration: 0.2, animations: { 
                self.invalidPhoneNumberLabel.alpha = 0.0
            })
        }
        guard let count = textField.text?.characters.count else { return false }
        let newLength = count + string.characters.count - range.length
        
        if newLength < 1 {
            return true
        } else if newLength == 1 {
            textField.text = string
            switch textField {
            case self.firstCodeTextField:
                self.secondCodeTextField.becomeFirstResponder()
            case self.secondCodeTextField:
                self.thirdCodeTextField.becomeFirstResponder()
            case self.thirdCodeTextField:
                self.fourthCodeTextField.becomeFirstResponder()
            case self.fourthCodeTextField:
                self.fifthCodeTextField.becomeFirstResponder()
            case self.fifthCodeTextField:
                self.sixthCodeTextField.becomeFirstResponder()
            default:
                return textField.resignFirstResponder()
            }
            return false
        } else {
            return false
        }
        
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.black.cgColor
    }
    func textFieldDidEndEditing(_ textField: UITextField) {
        textField.layer.borderColor = UIColor.textFieldBorderGray().cgColor
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
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
    
}
