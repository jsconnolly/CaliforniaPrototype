//
//  PhoneLoginViewController.swift
//  CaliforniaPrototype
//
//  Created by Luis Garcia on 2/22/17.
//  Copyright © 2017 HOTB. All rights reserved.
//

import UIKit

class PhoneLoginViewController: UIViewController, UITextFieldDelegate {

    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var phoneTextField: OutlinedTextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.stackView.arrangedSubviews[1].isHidden = true
    }

    
    @IBAction func continueButtonTapped(_ sender: Any) {
        self.phoneTextField.resignFirstResponder()
        guard let phoneString = self.phoneTextField.text else { return }
        if !ValidationMethods().isValidPhoneNumber(phoneString) {
            self.phoneTextField.layer.borderColor = UIColor.red.cgColor
            if self.stackView.arrangedSubviews[1].isHidden == true {
                self.animateStackSubview(1, to: false)
            }
        } else {
            self.phoneTextField.layer.borderColor = UIColor.black.cgColor
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
            //If phone number has not been registered, it will create a temporary user then allow a password to be created with the verification code provided
            let fullPhoneString = "1" + phoneString
            APIManager.sharedInstance.phoneVerification(fullPhoneString, success: { (response: [String : Any?]) in
                DispatchQueue.main.async {
                    let phoneVerificationVC = PhoneVerificationViewController()
                    phoneVerificationVC.phoneNumber = phoneString
                    phoneVerificationVC.phoneNumberLabel.text = phoneString
                    self.navigationController?.pushViewController(phoneVerificationVC, animated: true)
                }
            }, failure: { (error) in
                if error?.code == 404 {
                    DispatchQueue.main.async {
                        let phoneVerificationVC = PhoneVerificationViewController()
                        phoneVerificationVC.phoneNumber = phoneString
                        phoneVerificationVC.phoneNumberLabel.text = phoneString
                        self.navigationController?.pushViewController(phoneVerificationVC, animated: true)
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
    }
    
    func animateStackSubview(_ viewNumber: Int,to bool: Bool) {
        self.stackView.arrangedSubviews[viewNumber].isHidden = bool
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
    
    @IBAction func emailLoginButtonTapped(_ sender: Any) {
        
    }

    @IBAction func noAccountButtonTapped(_ sender: Any) {
        
    }
    
//MARK: - UITextField delegate methods
    func textFieldDidBeginEditing(_ textField: UITextField) {
        self.setTextFieldBorderActive(textField)
        if textField == self.phoneTextField {
            if self.stackView.arrangedSubviews[1].isHidden == false {
                self.animateStackSubview(1, to: true)
            }
        }
    }
    
    
    func setTextFieldBorderActive(_ textField: UITextField) {
        if textField == self.phoneTextField {
            textField.layer.borderColor = UIColor.black.cgColor
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.window?.endEditing(true)
        self.phoneTextField.layer.borderColor = UIColor.textFieldBorderGray().cgColor
    }
}
